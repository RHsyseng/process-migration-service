/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam;

import org.kie.server.controller.api.model.spec.ServerTemplateList;
import org.kie.server.controller.client.KieServerControllerClient;
import org.kie.server.controller.client.KieServerControllerClientFactory;

/**
 *
 * @author czhu
 */
public class ControllerClient {

    private static final String URL = "http://localhost:8180/kie-server-controller/rest/controller";
    private static final String WS_URL = "ws://localhost:8180/kie-server-controller/rest/controller";
    private static final String USER = "executionUser";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
//REST CLIENT
KieServerControllerClient client = KieServerControllerClientFactory.newRestClient(URL,USER,PASSWORD);

//WS client
        //KieServerControllerClient client = KieServerControllerClientFactory.newWebSocketClient(WS_URL,USER,PASSWORD);

        final ServerTemplateList serverTemplateList = client.listServerTemplates();
        System.out.println(String.format("Found %s server template(s) at controller url: %s",
                serverTemplateList.getServerTemplates().length,
                URL));
    }
}
