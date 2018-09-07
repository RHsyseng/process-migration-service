package com.redhat.syseng.soleng.rhpam.processmigration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.syseng.soleng.rhpam.processmigration.model.KieServerConfig;

public class KieServiceImplTest {

  @Inject
  private KieServerConfig config;

  @Test
  @Ignore
  public void testConfiguration() {
	 assertNotNull(config);
	 assertEquals("localhost", config.getHost());
	 assertEquals(Integer.valueOf(8080), config.getPort());
  }
}
