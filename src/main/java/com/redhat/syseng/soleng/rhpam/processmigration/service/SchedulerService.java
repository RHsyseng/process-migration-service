package com.redhat.syseng.soleng.rhpam.processmigration.service;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Credentials;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;

public interface SchedulerService {

    void scheduleMigration(Migration migration, Credentials credentials);

}
