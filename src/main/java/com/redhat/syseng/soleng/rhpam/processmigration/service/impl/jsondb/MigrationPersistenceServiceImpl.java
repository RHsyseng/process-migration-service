package com.redhat.syseng.soleng.rhpam.processmigration.service.impl.jsondb;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;

@ApplicationScoped
public class MigrationPersistenceServiceImpl extends PersistenceServiceImpl<Migration> {

    public MigrationPersistenceServiceImpl() {
        super(Migration.class);
    }

}
