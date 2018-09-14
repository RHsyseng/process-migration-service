package com.redhat.syseng.soleng.rhpam.processmigration.service;

import java.util.List;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Credentials;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationReport;

public interface MigrationService {

    Migration get(Long id);

    List<MigrationReport> getResults(Long id);

    List<Migration> findAll();

    Migration submit(MigrationDefinition definition, Credentials credentials);

    Migration update(Long id, MigrationDefinition migration);

    Migration delete(Long id);
    
    Migration migrate(Migration migration);

}
