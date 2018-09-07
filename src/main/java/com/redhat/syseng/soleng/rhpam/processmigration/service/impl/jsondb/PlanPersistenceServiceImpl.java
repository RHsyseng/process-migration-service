package com.redhat.syseng.soleng.rhpam.processmigration.service.impl.jsondb;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;

@ApplicationScoped
public class PlanPersistenceServiceImpl extends PersistenceServiceImpl<Plan> {

    public PlanPersistenceServiceImpl() {
        super(Plan.class);
    }

}
