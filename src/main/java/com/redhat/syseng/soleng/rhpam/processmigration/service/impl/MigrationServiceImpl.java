package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationReport;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.model.exceptions.PlanNotFoundException;
import com.redhat.syseng.soleng.rhpam.processmigration.service.KieService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.MigrationService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;
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
    @Transactional
    public Migration submit(MigrationDefinition definition) {

        Plan plan = planService.get(definition.getPlanId());
        if (plan == null) {
            throw new PlanNotFoundException(definition.getPlanId());
        }
        Migration migration = new Migration(definition, plan);
        em.persist(migration);

        if (ExecutionType.ASYNC.equals(definition.getExecution().getType())) {
            scheduleMigration(migration, plan);
            return migration;
        } else {
            return migrate(migration, plan);
        }
    }

    /*
    @Override
    @Transactional
    public Migration cancel(Long id) {
    
        Migration migration = get(id);
        if (migration == null) {
            return null;
        }
        if (ExecutionStatus.SCHEDULED.equals(migration.getExecutionStatus()) || ExecutionStatus.CREATED.equals(migration.getExecutionStatus())) {
            em.persist(migration.cancel());
        }
        return migration;
    
    }
    
     */
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

    @Transactional
    private Migration migrate(Migration migration, Plan plan) {
        em.persist(migration.start());
        try {
            AtomicBoolean hasErrors = new AtomicBoolean(false);
            //each instance id will spawn its own request to KIE server for migration.
            List<Long> instancesIdList = migration.getDefinition().getProcessInstanceIds();
            for (Long instanceId : instancesIdList) {
                MigrationReportInstance reportInstance = kieService
                                                                   .getProcessAdminServicesClient()
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
            em.persist(migration.complete(hasErrors.get()));

        } catch (Exception e) {
            logger.error("Migration failed", e);
            em.persist(migration.fail(e));
        }
        return migration;
    }

    private void scheduleMigration(Migration migration, Plan plan) {

        long delay = new Date().getTime() - migration.getDefinition().getExecution().getScheduledStartTime().getTime();

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.schedule(new Runnable() {

            @Override
            public void run() {
                Migration result = migrate(migration, plan);

                // TODO: Refactor
                // ResteasyClient client = new ResteasyClientBuilder().build();
                // ResteasyWebTarget target =
                // client.target(migration.getDefinition().getExecution().getCallbackUrl());
                // target.request().post(Entity.text(result));
            }
        }, delay, TimeUnit.MILLISECONDS); // run in "delay" milliseconds
    }

}
