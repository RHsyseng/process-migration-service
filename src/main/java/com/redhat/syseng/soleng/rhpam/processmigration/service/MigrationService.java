package com.redhat.syseng.soleng.rhpam.processmigration.service;

import java.util.List;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.ProcessInstanceMigration;

public interface MigrationService {

    Migration get(Long id);

    List<ProcessInstanceMigration> getResults(Long id);

    List<Migration> findAll();

    Migration submit(MigrationDefinition migration);

    Migration update(MigrationDefinition migration);

    Migration cancel(Long id);

}
