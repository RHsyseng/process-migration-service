package com.redhat.syseng.soleng.rhpam.processmigration.service;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;

public interface SchedulerService {

    void scheduleMigration(Migration migration, Plan plan);
}
