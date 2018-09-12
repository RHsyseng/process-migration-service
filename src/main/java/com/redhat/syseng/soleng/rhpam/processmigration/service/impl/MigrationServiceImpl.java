package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationReport;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.exceptions.PlanNotFoundException;
import com.redhat.syseng.soleng.rhpam.processmigration.service.KieService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.MigrationService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;
import org.jboss.logging.Logger;
import org.kie.server.api.model.admin.MigrationReportInstance;

@ApplicationScoped
public class MigrationServiceImpl implements MigrationService {

    private static final Logger logger = Logger.getLogger(MigrationServiceImpl.class);

    @PersistenceContext(unitName = "migration-unit")
    EntityManager em;

    @Inject
    private PlanService planService;

    @Inject
    private KieService kieService;

    //Used by Junit test where em can't be injected
    public void setEntityManagerAndPlanService(EntityManager em, PlanService planService) {
        this.em = em;
        this.planService = planService;
    }

    @PostConstruct
    public void initKieConnection() {

    }

    @Override
    public Migration get(Long id) {
        //Migration result = null;
        Query query = em.createNamedQuery("Migration.findById", Migration.class);
        query.setParameter("id", id);
        Migration result = (Migration) query.getSingleResult();

        return result;
    }

    @Override
    public List<MigrationReport> getResults(Long id) {

        Query query = em.createNamedQuery("MigrationReport.findById", MigrationReport.class);
        query.setParameter("id", id);
        List<MigrationReport> result = query.getResultList();

        return result;
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
        System.out.println("!!!!!!!!!!!!!!!!" + migration.getCallbackUrl());
        if (null != migration) {
            migration.getPlan().setId(definition.getPlanId());
            migration.setProcessInstancesIds(definition.getProcessInstancesId().toString());
            migration.setExecutionType(definition.getExecution().getType().toString());
            if (definition.getExecution().getType().equals(Execution.ExecutionType.ASYNC)) {
                //these 2 fields only make sense when the type is "ASYNC", otherwise just ignore
                migration.setCallbackUrl(definition.getExecution().getCallbackUrl().toString());
                migration.setScheduleStartTime(definition.getExecution().getScheduledStartTime());
            }
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
            List<Long> instancesIdList = parseProcessInstancesIds(migration.getProcessInstancesIds());
            for (Long instanceId : instancesIdList) {
                MigrationReportInstance report = kieService.getProcessAdminServicesClient().migrateProcessInstance(
                                                                                                                   plan.getContainerId(), instanceId,
                                                                                                                   plan.getTargetContainerId(), plan.getTargetProcessId(), plan.getNodeMappings());
                if (!hasErrors.get() && !report.isSuccessful()) {
                    hasErrors.set(Boolean.TRUE);
                }
                MigrationReport tmpReport = new MigrationReport();
                tmpReport.setMigrationId(migration.getId());
                tmpReport.setMigrationReport(report.toString());
                em.persist(tmpReport);
            }

            em.persist(migration.complete(hasErrors.get()));

        } catch (Exception e) {
            logger.error("Migration failed", e);
            em.persist(migration.fail(e));
        }
        return migration;
    }

    //This method parses instancesId string like this "[1, 2, 3 ,4]" to List<long>
    private List<Long> parseProcessInstancesIds(String tmpIds) {
        List<Long> result = new ArrayList<Long>();
        //need to remove the [ ] and space
        tmpIds = tmpIds.replaceAll("[\\[\\] ]", "");
        Scanner sc = new Scanner(tmpIds).useDelimiter(",");
        while (sc.hasNextLong()) {
            result.add(sc.nextLong());
        }
        return result;
    }

    private void scheduleMigration(Migration migration, Plan plan) {

        long delay = new Date().getTime() - migration.getScheduleStartTime().getTime();

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
