package com.redhat.syseng.soleng.rhpam.processmigration.service;

import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.admin.ProcessAdminServicesClient;

public interface KieService {

    ProcessAdminServicesClient getProcessAdminServicesClient();

    KieServicesClient getKieServicesClient();

    ServiceResponse<KieServerStateInfo> getServerState();
}
