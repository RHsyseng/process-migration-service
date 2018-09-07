package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionStatus;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.model.ProcessInstanceMigration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.exceptions.PlanNotFoundException;
import com.redhat.syseng.soleng.rhpam.processmigration.service.KieService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.MigrationService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PersistenceService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;
import org.jboss.logging.Logger;
import org.kie.server.api.model.admin.MigrationReportInstance;

@ApplicationScoped
public class MigrationServiceImpl implements MigrationService {

    private static final Logger logger = Logger.getLogger(MigrationServiceImpl.class);

    @Inject
    private PlanService planService;
    @Inject
    private PersistenceService<Migration> migrationPersistenceService;
    @Inject
    private PersistenceService<ProcessInstanceMigration> piMigrationPersistenceService;
    @Inject
    private KieService kieService;

    private static final String RESULTS_QUERY = "";

    @PostConstruct
    public void initKieConnection() {

    }

    @Override
    public Migration get(Long id) {
        return migrationPersistenceService.get(id);
    }

    @Override
    public List<ProcessInstanceMigration> getResults(Long id) {
        return piMigrationPersistenceService.findByQuery(RESULTS_QUERY);
    }

    @Override
    public List<Migration> findAll() {
        return migrationPersistenceService.findAll();
    }

    @Override
    public Migration submit(MigrationDefinition definition) {
        Plan plan = planService.get(definition.getPlanId());
        if (plan == null) {
            throw new PlanNotFoundException(definition.getPlanId());
        }
        Migration migration = new Migration(definition);
        migrationPersistenceService.save(migration);
        if (ExecutionType.ASYNC.equals(definition.getExecution().getType())) {
            scheduleMigration(migration, plan);
            return migration;
        } else {
            return migrate(migration, plan);
        }
    }

    @Override
    public Migration cancel(Long id) {
        Migration migration = migrationPersistenceService.get(id);
        if (migration == null) {
            return null;
        }
        if (ExecutionStatus.SCHEDULED.equals(migration.getStatus())) {
            migrationPersistenceService.save(migration.cancel());
        }
        return migration;
    }

    @Override
    public Migration update(MigrationDefinition migration) {
        // TODO Implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Migration migrate(Migration migration, Plan plan) {
        migrationPersistenceService.save(migration.start());
        try {
            AtomicBoolean hasErrors = new AtomicBoolean(false);
            List<ProcessInstanceMigration> piMigrations = new ArrayList<>();
            List<MigrationReportInstance> reports = kieService.getProcessAdminServicesClient().migrateProcessInstances(
                                                                                                                       plan.getSource().getContainerId(), migration.getDefinition().getProcessInstancesId(),
                                                                                                                       plan.getTarget().getContainerId(), plan.getTarget().getId(), plan.getMappings());
            reports.stream().forEach(i -> {
                if (!hasErrors.get() && !i.isSuccessful()) {
                    hasErrors.set(Boolean.TRUE);
                }
                ProcessInstanceMigration piMigration = new ProcessInstanceMigration(migration.getId(), i);
                piMigrationPersistenceService.save(piMigration);
                piMigrations.add(piMigration);
            });
            return migrationPersistenceService.save(migration.complete(hasErrors.get()));
        } catch (Exception e) {
            logger.error("Migration failed", e);
            return migrationPersistenceService.save(migration.fail(e));
        }
    }

    private void scheduleMigration(Migration migration, Plan plan) {
        long delay = new Date().getTime() - migration.getDefinition().getExecution().getStartAt().getTime();

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
