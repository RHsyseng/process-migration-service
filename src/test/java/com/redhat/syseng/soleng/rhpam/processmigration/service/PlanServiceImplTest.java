package com.redhat.syseng.soleng.rhpam.processmigration.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.service.impl.PlanServiceImpl;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableWeld
public class PlanServiceImplTest extends PersistenceTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator
                                             .from(PlanServiceImpl.class)
                                             .activate(ApplicationScoped.class)
                                             .setPersistenceContextFactory(getPCFactory())
                                             .build();
    @Inject
    private PlanService planService;

    @Test
    public void testSaveAndFindAll() {
        assertNotNull(planService);

        Plan plan = new Plan();
        plan.setSourceContainerId("containerId");
        plan.setName("name");
        plan.setTargetContainerId("targetContainerId");
        plan.setDescription("description");

        List<Plan> plans = planService.findAll();

        assertNotNull(plans);
        assertEquals(1, plans.size());
        
    }

}
