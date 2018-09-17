package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.security.auth.login.CredentialNotFoundException;
import javax.transaction.Transactional;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.arjuna.ats.jta.UserTransaction;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Credentials;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationReport;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.model.exceptions.PlanNotFoundException;
import com.redhat.syseng.soleng.rhpam.processmigration.service.CredentialsProviderFactory;
import com.redhat.syseng.soleng.rhpam.processmigration.service.CredentialsService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.KieService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.MigrationService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.SchedulerService;
import org.jboss.logging.Logger;
import org.kie.server.api.model.admin.MigrationReportInstance;

@ApplicationScoped
public class MigrationServiceImpl implements MigrationService {

    private static final Logger logger = Logger.getLogger(MigrationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PlanService planService;

    @Inject
    private KieService kieService;

    @Inject
    private SchedulerService schedulerService;

    @Inject
    private CredentialsService credentialsService;

    @Override
    public Migration get(Long id) {
        TypedQuery<Migration> query = em.createNamedQuery("Migration.findById", Migration.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<MigrationReport> getResults(Long id) {
        TypedQuery<MigrationReport> query = em.createNamedQuery("MigrationReport.findByMigrationId", MigrationReport.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Migration> findAll() {
        return em.createNamedQuery("Migration.findAll", Migration.class).getResultList();
    }

    @Override
    public Migration submit(MigrationDefinition definition, Credentials credentials) {
        Plan plan = planService.get(definition.getPlanId());
        if (plan == null) {
            throw new PlanNotFoundException(definition.getPlanId());
        }
        Migration migration = new Migration(definition);
        beginTx();
        em.persist(migration);
        if (ExecutionType.SYNC.equals(definition.getExecution().getType())) {
            credentialsService.save(credentials.setMigrationId(migration.getId()));
            migrate(migration, plan, credentials);
            commitTx();
        } else {
            if (definition.getExecution().getScheduledStartTime() == null) {
                commitTx();
                CompletableFuture.runAsync(() -> {
                    beginTx();
                    migrate(migration, plan, credentials);
                    commitTx();
                });
            } else {
                schedulerService.scheduleMigration(migration, credentials);
                commitTx();
            }
        }
        return migration;
    }

    @Override
    @Transactional
    public Migration delete(Long id) {
        Migration migration = get(id);
        if (migration == null) {
            return null;
        }
        em.remove(migration);
        return migration;
    }

    @Override
    @Transactional
    public Migration update(Long id, MigrationDefinition definition) {
        Migration migration = get(id);
        if (migration != null) {
            em.persist(migration);
        }
        return migration;
    }

    @Override
    @Transactional
    public Migration migrate(Migration migration) {
        Plan plan = planService.get(migration.getDefinition().getPlanId());
        if (plan == null) {
            throw new PlanNotFoundException(migration.getDefinition().getPlanId());
        }
        Credentials credentials = credentialsService.get(migration.getId());
        return migrate(migration, plan, credentials);
    }

    private Migration migrate(Migration migration, Plan plan, Credentials credentials) {
        try {
            migration = em.find(Migration.class, migration.getId());
            em.persist(migration.start());
            if (credentials == null) {
                throw new CredentialNotFoundException();
            }
            AtomicBoolean hasErrors = new AtomicBoolean(false);
            //each instance id will spawn its own request to KIE server for migration.
            List<Long> instancesIdList = migration.getDefinition().getProcessInstanceIds();
            for (Long instanceId : instancesIdList) {
                MigrationReportInstance reportInstance = kieService
                                                                   .createProcessAdminServicesClient(CredentialsProviderFactory.getProvider(credentials))
                                                                   .migrateProcessInstance(
                                                                                           plan.getSourceContainerId(),
                                                                                           instanceId,
                                                                                           plan.getTargetContainerId(),
                                                                                           plan.getTargetProcessId(),
                                                                                           plan.getMappings());
                if (!hasErrors.get() && !reportInstance.isSuccessful()) {
                    hasErrors.set(Boolean.TRUE);
                }
                em.persist(new MigrationReport(migration.getId(), reportInstance));
            }
            migration.complete(hasErrors.get());
        } catch (Exception e) {
            logger.error("Migration failed", e);
            migration.fail(e);
        } finally {
            credentialsService.delete(migration.getId());
            em.persist(migration);
            if (ExecutionType.ASYNC.equals(migration.getDefinition().getExecution().getType()) &&
                migration.getDefinition().getExecution().getCallbackUrl() != null) {
                doCallback(migration);
            }
        }
        return migration;
    }

    private void doCallback(Migration migration) {
        URI callbackURI = null;
        try {
            callbackURI = migration.getDefinition().getExecution().getCallbackUrl();
            Response response = ClientBuilder.newClient()
                                             .target(callbackURI)
                                             .request(MediaType.APPLICATION_JSON)
                                             .buildPost(Entity.json(migration))
                                             .invoke();
            if (Status.OK.getStatusCode() == response.getStatus()) {
                logger.debugf("Migration [%s] - Callback to %s replied successfully", migration.getId(), callbackURI);
            } else {
                logger.warnf("Migration [%s] - Callback to %s replied with %s", migration.getId(), callbackURI, response.getStatus());
            }
        } catch (Exception e) {
            logger.errorf("Migration [%s] - Callback to %s failed.", migration.getId(), callbackURI, e);
        }
    }

    private void beginTx() {
        try {
            if (UserTransaction.userTransaction().getStatus() == javax.transaction.Status.STATUS_NO_TRANSACTION) {
                UserTransaction.userTransaction().begin();
            }
        } catch (Exception e) {
            logger.error("Unable to begin transaction.", e);
        }
    }

    private void commitTx() {
        try {
            if (UserTransaction.userTransaction().getStatus() == javax.transaction.Status.STATUS_ACTIVE) {
                UserTransaction.userTransaction().commit();
            }
        } catch (Exception e) {
            logger.error("Unable to commit transaction.", e);
        }
    }
}
