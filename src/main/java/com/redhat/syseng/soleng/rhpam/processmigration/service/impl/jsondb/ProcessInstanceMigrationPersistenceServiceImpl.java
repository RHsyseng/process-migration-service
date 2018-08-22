package com.redhat.syseng.soleng.rhpam.processmigration.service.impl.jsondb;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.syseng.soleng.rhpam.processmigration.model.ProcessInstanceMigration;

@ApplicationScoped
public class ProcessInstanceMigrationPersistenceServiceImpl extends PersistenceServiceImpl<ProcessInstanceMigration> {

    public ProcessInstanceMigrationPersistenceServiceImpl() {
	super(ProcessInstanceMigration.class);
    }

}
