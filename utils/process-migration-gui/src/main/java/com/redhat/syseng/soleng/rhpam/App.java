package com.redhat.syseng.soleng.rhpam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;

public class App {

    public static void main(String[] args) {
//        String serverUrl = "http://localhost:8180/kie-execution-server/services/rest/server";
        //below is to be used for kie server in OCP. 
        //String serverUrl = "http://myapp-kieserver-calvin-test.rhdp.ocp.cloud.lab.eng.bos.redhat.com/services/rest/server";

        String serverUrl = System.getenv("KIE_REST_URL");
        System.out.println("KIE_REST_URL: " + serverUrl);

        String username = System.getenv("KIE_USER");
        String password = System.getenv("KIE_PASSWORD");
        String containerId = System.getenv("CONTAINER_ID");
        String processDefinitionId = System.getenv("PROCESS_DEFINITION_ID");

        KieServicesClient client = KieServicesFactory.newKieServicesRestClient(serverUrl, username, password);
        System.out.println(client.getClass().getName());
        for (KieContainerResource kieContainerResource : client.listContainers().getResult().getContainers()) {
            System.out.println("Got kie container " + kieContainerResource.getContainerId());
        }

        Map<String, Object> variables = new HashMap<String, Object>();

        int loopCount = Integer.parseInt(System.getenv("MESSAGE_COUNT"));
        for (int i = 0; i < loopCount; i++) {
            try {
                System.out.println("loop number: " + i);
                ProcessServicesClient processClient = client.getServicesClient(ProcessServicesClient.class);
                long processInstanceId = processClient.startProcess(containerId, processDefinitionId, variables);
                System.out.println("Started process instance #" + processInstanceId);
            } catch (Exception e) {
                System.out.println(" exception: " + e.getMessage());

            }
            //List<ProcessInstance> processInstances = processClient.findProcessInstances(containerId, 0, 100);
            //System.out.println("Found processes IDs: " + processInstances.stream().map(ProcessInstance::getId).collect(Collectors.toList()));

        }

    }
}
