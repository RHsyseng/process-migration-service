package com.redhat.syseng.soleng.rhpam.processmigration.service;

import java.util.List;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;

public interface PlanService {

    Plan get(Long id);

    List<Plan> findAll();

    Plan delete(Long id);

    Plan save(Plan plan);

}
