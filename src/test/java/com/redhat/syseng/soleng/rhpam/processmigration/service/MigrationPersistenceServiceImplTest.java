package com.redhat.syseng.soleng.rhpam.processmigration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.service.impl.jsondb.MigrationPersistenceServiceImpl;
import com.redhat.syseng.soleng.rhpam.processmigration.service.impl.jsondb.PersistenceServiceImpl;

public class MigrationPersistenceServiceImplTest {

    private static final PersistenceService<Migration> service = new MigrationPersistenceServiceImpl();

    @BeforeClass
    public static void init() {
	PersistenceServiceImpl<Migration> serviceImpl = (PersistenceServiceImpl<Migration>) service;
	serviceImpl.setDbFilesLocation("target/db/migration-db");
	serviceImpl.initialize();
    }

    @Test
    public void testSave() {
	MigrationDefinition definition = new MigrationDefinition();
	definition.setPlanId(1L);
	Execution execution = new Execution();
	execution.setType(ExecutionType.SYNC);
	definition.setExecution(execution);
	definition.setProcessInstancesId(Arrays.asList(1L, 2L));
	Migration migration = new Migration(definition);
	migration.setCreatedAt(new Date());

	Migration result = service.save(migration);

	assertNotNull(result.getId());
	assertEquals(migration.getCreatedAt(), result.getCreatedAt());

	Migration savedResult = service.get(result.getId());
	assertEquals(migration.getCreatedAt(), savedResult.getCreatedAt());
	assertEquals(migration.getDefinition().getProcessInstancesId().size(),
		savedResult.getDefinition().getProcessInstancesId().size());
    }

}
