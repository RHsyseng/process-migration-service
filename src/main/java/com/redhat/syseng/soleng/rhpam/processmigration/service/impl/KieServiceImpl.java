package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.admin.ProcessAdminServicesClient;
import org.kie.server.client.admin.impl.ProcessAdminServicesClientImpl;
import org.kie.server.client.impl.KieServicesClientImpl;
import org.kie.server.client.impl.KieServicesConfigurationImpl;

import com.redhat.syseng.soleng.rhpam.processmigration.model.KieServerConfig;
import com.redhat.syseng.soleng.rhpam.processmigration.service.KieService;

@ApplicationScoped
public class KieServiceImpl implements KieService {

    private static final Logger logger = Logger.getLogger(KieServiceImpl.class);

    @Inject
    private KieServerConfig config;
    private KieServicesConfiguration kieServicesConfig;
    private ProcessAdminServicesClient processAdminServicesClient;
    private KieServicesClient kieServicesClient;

    @PostConstruct
    public void initKieServersConfig() {
	logger.infov("Configuring Kie Services with: {0}", config.toString());
	kieServicesConfig = new KieServicesConfigurationImpl(config.getUrl(), config.getUsername(),
		config.getPassword());
	this.processAdminServicesClient = new ProcessAdminServicesClientImpl(kieServicesConfig);
	this.kieServicesClient = new KieServicesClientImpl(kieServicesConfig);
	((ProcessAdminServicesClientImpl) processAdminServicesClient)
		.setOwner((KieServicesClientImpl) kieServicesClient);
    }

    public ProcessAdminServicesClient getProcessAdminServicesClient() {
	return processAdminServicesClient;
    }

    public KieServicesClient getKieServicesClient() {
	return kieServicesClient;
    }

    public ServiceResponse<KieServerStateInfo> getServerState() {
	try {
	    return kieServicesClient.getServerState();
	} catch (Exception e) {
	    ServiceResponse<KieServerStateInfo> response = new ServiceResponse<>();
	    response.setType(ResponseType.FAILURE);
	    response.setMsg(e.getMessage());
	    return response;
	}
    }

}
