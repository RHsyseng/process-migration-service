package com.redhat.syseng.soleng.rhpam.processmigration.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.service.impl.MigrationServiceImpl;
import com.redhat.syseng.soleng.rhpam.processmigration.service.impl.PlanServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//follow example of https://memorynotfound.com/unit-test-jpa-junit-in-memory-h2-database/
public class PersistenceServiceImplTest {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;
    protected static PlanServiceImpl planService;
    protected static MigrationServiceImpl migrationService;

    @BeforeClass
    public static void init() {
        emf = Persistence.createEntityManagerFactory("migration-test");
        em = emf.createEntityManager();
        planService = new PlanServiceImpl();
        planService.setEntityManager(em);
        migrationService = new MigrationServiceImpl();
        migrationService.setEntityManagerAndPlanService(em, planService);

    }

    @Test
    public void testSaveAndReadMigration() throws URISyntaxException {
        //test save and read plan
        Plan plan = new Plan();
        plan.setContainerId("containerId");
        plan.setName("name");
        plan.setTargetContainerId("targetContainerId");
        plan.setDescription("description");
        em.getTransaction().begin();
        em.persist(plan);
        em.getTransaction().commit();
        List<Plan> plans = planService.findAll();
        assertNotNull(plans);
        assertEquals(1, plans.size());

        //test save and read migration
        //Note: Migration failed with NullPointerException is normal because there is no backend.
        MigrationDefinition md = new MigrationDefinition();
        md.setPlanId(plan.getId());
        List<Long> ids = new ArrayList();
        ids.add(new Long(1));
        md.setProcessInstancesId(ids);
        Execution execution = new Execution();
        execution.setType(Execution.ExecutionType.SYNC);
        md.setExecution(execution);

        em.getTransaction().begin();
        migrationService.submit(md);
        em.getTransaction().commit();

        List<Migration> migration = migrationService.findAll();
        assertNotNull(migration);
        assertEquals(1, migration.size());

    }

}
