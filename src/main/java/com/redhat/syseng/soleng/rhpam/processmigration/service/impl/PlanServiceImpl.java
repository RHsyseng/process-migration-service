package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PersistenceService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;

@ApplicationScoped
public class PlanServiceImpl implements PlanService {

    @Inject
    private PersistenceService<Plan> planPersistenceService;

    @Override
    public Plan get(Long id) {
	return planPersistenceService.get(id);
    }

    @Override
    public List<Plan> findAll() {
	return planPersistenceService.findAll();
    }

    @Override
    public Plan delete(Long id) {
	return planPersistenceService.delete(id);
    }

    @Override
    public Plan save(Plan plan) {
	return planPersistenceService.save(plan);
    }

}
