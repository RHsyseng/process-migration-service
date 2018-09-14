package com.redhat.syseng.soleng.rhpam.processmigration.service;

import javax.inject.Inject;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Credentials;
import com.redhat.syseng.soleng.rhpam.processmigration.service.impl.CredentialsServiceImpl;
import com.redhat.syseng.soleng.rhpam.processmigration.service.util.ConfigurationValueProducer;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(WeldJunit5Extension.class)
public class CredentialsServiceImplTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConfigurationValueProducer.class, CredentialsServiceImpl.class).build();

    @Inject
    private CredentialsService service;

    @Test
    public void testSaveGet() {
        assertNotNull(service);
        Credentials cred = new Credentials().setMigrationId(1L).setUsername("kermit").setPassword("theFrog");
        Credentials saved = service.save(cred);
        assertNotNull(cred.getMigrationId());
        assertEquals(saved, cred);

        Credentials loaded = service.get(cred.getMigrationId());
        assertNotNull(loaded);
        assertEquals(cred.getMigrationId(), loaded.getMigrationId());
        assertEquals(cred.getUsername(), loaded.getUsername());
        assertEquals(cred.getPassword(), loaded.getPassword());
        assertNull(loaded.getToken());
    }

    @Test
    public void testSaveDelete() {
        assertNotNull(service);
        Credentials cred = new Credentials().setMigrationId(2L).setUsername("kermit").setPassword("theFrog");
        Credentials saved = service.save(cred);
        assertNotNull(cred.getMigrationId());
        assertEquals(saved, cred);

        Credentials loaded = service.delete(cred.getMigrationId());
        assertNotNull(loaded);
        assertEquals(cred.getMigrationId(), loaded.getMigrationId());
        assertEquals(cred.getUsername(), loaded.getUsername());
        assertEquals(cred.getPassword(), loaded.getPassword());
        assertNull(loaded.getToken());
        assertNull(service.get(cred.getMigrationId()));
    }

    @Test
    public void testSaveGetToken() {
        assertNotNull(service);
        Credentials cred = new Credentials().setMigrationId(3L).setToken("ZRi:K1BC&[-S:*c6;°§2]22PP]NR}Y/r^}8J,:O,B1({>Iz%3{h+P<.??w29U-P");
        Credentials saved = service.save(cred);
        assertNotNull(cred.getMigrationId());
        assertEquals(saved, cred);

        Credentials loaded = service.delete(cred.getMigrationId());
        assertNotNull(loaded);
        assertEquals(cred.getMigrationId(), loaded.getMigrationId());
        assertNull(loaded.getUsername());
        assertNull(loaded.getPassword());
        assertEquals(cred.getToken(), loaded.getToken());

        assertNull(service.get(cred.getMigrationId()));
    }
}
