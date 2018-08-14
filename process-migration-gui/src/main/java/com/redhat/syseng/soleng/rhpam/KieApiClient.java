/*
 * Copyright 2018 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.syseng.soleng.rhpam;

import com.redhat.syseng.soleng.rhpam.model.MigrationPlan;
import com.redhat.syseng.soleng.rhpam.util.MigrationUtils;
import static com.redhat.syseng.soleng.rhpam.util.MigrationUtils.getKieUsername;
import java.io.ByteArrayInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.maven.project.MavenProject;
import org.appformer.maven.integration.MavenRepository;
import org.appformer.maven.integration.embedder.MavenProjectLoader;

import org.kie.server.api.model.admin.MigrationReportInstance;
import org.kie.server.api.model.definition.AssociatedEntitiesDefinition;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.admin.impl.ProcessAdminServicesClientImpl;
import org.kie.server.client.impl.KieServicesClientImpl;
import org.kie.server.client.impl.KieServicesConfigurationImpl;
import org.kie.server.client.impl.ProcessServicesClientImpl;
import org.kie.server.client.impl.QueryServicesClientImpl;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.scanner.KieMavenRepository;
import org.kie.server.api.model.ReleaseId;

/**
 *
 * @author czhu
 */
public class KieApiClient {

    private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String JMS_QUEUE_REQUEST = "jms/queue/KIE.SERVER.REQUEST";
    private static final String JMS_QUEUE_RESPONSE = "jms/queue/KIE.SERVER.RESPONSE";
    private static String kieServiceUrl = MigrationUtils.protocol + "://" + MigrationUtils.getKieHost() + ":" + MigrationUtils.getKiePort() + "/" + MigrationUtils.getKieContextRoot() + "services/rest/server";

    public static ProcessAdminServicesClientImpl setupProcessAdminServicesClient(MigrationPlan unit, String url, String username, String password) throws NamingException {

        String provider_url = System.getenv("KIE_JMS_PROVIDER_URL");

        KieServicesConfigurationImpl config = null;
        boolean async = false;
        if (async) {
            //REST config
            config = new KieServicesConfigurationImpl(url, username, password);
        } else {
            //JMS config

            java.util.Properties env = new java.util.Properties();
            env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            env.put(javax.naming.Context.PROVIDER_URL, provider_url);
            env.put(javax.naming.Context.SECURITY_PRINCIPAL, username);
            env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);
            InitialContext ctx = new InitialContext(env);

            ConnectionFactory conn = (ConnectionFactory) ctx.lookup(JMS_CONNECTION_FACTORY);
            Queue respQueue = (Queue) ctx.lookup(JMS_QUEUE_RESPONSE);
            Queue reqQueue = (Queue) ctx.lookup(JMS_QUEUE_REQUEST);

            config = new KieServicesConfigurationImpl(conn, reqQueue, respQueue, username, password);

        }

        ProcessAdminServicesClientImpl client = new ProcessAdminServicesClientImpl(config);
        KieServicesClientImpl kieServicesClientImpl = new KieServicesClientImpl(config);
        client.setOwner(kieServicesClientImpl);

        return client;

    }

    public static QueryServicesClientImpl setupQueryServicesClient() throws NamingException {

        KieServicesConfigurationImpl config = new KieServicesConfigurationImpl(kieServiceUrl, MigrationUtils.getKieUsername(), MigrationUtils.getKiePassword());

        QueryServicesClientImpl client = new QueryServicesClientImpl(config);
        KieServicesClientImpl kieServicesClientImpl = new KieServicesClientImpl(config);
        client.setOwner(kieServicesClientImpl);

        return client;

    }

    public static ProcessServicesClientImpl setupProcessServicesClient() throws NamingException {

        KieServicesConfigurationImpl config = new KieServicesConfigurationImpl(kieServiceUrl, MigrationUtils.getKieUsername(), MigrationUtils.getKiePassword());

        ProcessServicesClientImpl client = new ProcessServicesClientImpl(config);
        KieServicesClientImpl kieServicesClientImpl = new KieServicesClientImpl(config);
        client.setOwner(kieServicesClientImpl);

        return client;

    }

    public KieServicesClient getKieServicesClient() {

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(kieServiceUrl, MigrationUtils.getKieUsername(), MigrationUtils.getKiePassword());
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        return client;
    }

    public static MavenRepository getRepository() {
        KieMavenRepository repository = null;

        if (repository == null) {
            // Initialize repository with minimal pom file.
            KieServices ks = KieServices.Factory.get();
            ReleaseId initReleaseId = new ReleaseId("org.kie.server.initial", "init-maven-repo", "42");
            KieFileSystem kfs = ks.newKieFileSystem().generateAndWritePomXML(initReleaseId);
            byte[] pom = kfs.read("pom.xml");

            MavenProject minimalMavenProject = MavenProjectLoader.parseMavenPom(new ByteArrayInputStream(pom));
            repository = KieMavenRepository.getKieMavenRepository(minimalMavenProject);
        }
        return repository;
    }

    public static List<MigrationReportInstance> migrateInstance(MigrationPlan plan) throws NamingException {

        ProcessAdminServicesClientImpl client = setupProcessAdminServicesClient(plan, kieServiceUrl, MigrationUtils.getKieUsername(), MigrationUtils.getKiePassword());
        List<MigrationReportInstance> reports = client.migrateProcessInstances(plan.getContainerId(), plan.getProcessInstancesId(), plan.getTargetContainerId(), plan.getTargetProcessId(), plan.getNodeMapping());
        return reports;
    }

    //This node id is not really the node id needed for migration's node mapping, weird. Keep it for now
    /*
    public void getNodeLists(ProcessAdminServicesClientImpl client, MigrationPlan unit) {
        List<ProcessNode> list = client.getProcessNodes(unit.getContainerId(), unit.getProcessInstanceId());
        for (ProcessNode node : list) {
            System.out.println("!!!!!!!!!!!!!!getNodeName " + node.getNodeName());
            System.out.println("!!!!!!!!!!!!!!getNodeType " + node.getNodeType());
            System.out.println("!!!!!!!!!!!!!!getNodeId " + node.getNodeId());
            System.out.println("!!!!!!!!!!!!!!getProcessId " + node.getProcessId());
            System.out.println("  ");
            System.out.println("  ");

        }

    }
     */
    public static void getProcessDefinitionFromProcessServicesClient(ProcessServicesClientImpl client, String containerId, String processId) {

        ProcessDefinition processDefinition = client.getProcessDefinition(containerId, processId);
        System.out.println("!!!!!!!!!!!!!!!!!!!processDefinition.getPackageName(): " + processDefinition.getPackageName());
        System.out.println("!!!!!!!!!!!!!!!!!!!processDefinition: " + processDefinition);

        /* below is the code to check associatedEntitiesDefinition, which doesn't help much
        it shows me user node and the user can access it. 

        AssociatedEntitiesDefinition associatedEntitiesDefinition = client.getAssociatedEntityDefinitions(containerId, processId);
        System.out.println("!!!!!!!!!!!!!!!!!!!associatedEntitiesDefinition: " + associatedEntitiesDefinition);
        System.out.println("!!!!!!!!!!!!!!!!!!!associatedEntitiesDefinition.getAssociatedEntities(): " + associatedEntitiesDefinition.getAssociatedEntities());
        Map<String, String[]> mp = associatedEntitiesDefinition.getAssociatedEntities();
        System.out.println("-----------------------------getProcessDefinitionsFromApi mp " + mp);
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            String[] sts = (String[]) pair.getValue();
            for (int i = 0; i < sts.length; i++) {
                String name = sts[i];
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> " + name);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        */
        ProcessInstance processInstance = client.getProcessInstance(containerId, new Long("1"));
        System.out.println("!!!!!!!!!!!!!!!!!!!processInstance: " + processInstance);

    }

    public static List<ProcessDefinition> listProcessesDefintionsFromQueryServicesClient() throws NamingException {
        QueryServicesClientImpl queryClient = setupQueryServicesClient();
        //which page to display, starting from 0
        int pageNumber = 0;
        //how many records to display in one page.
        int pageSize = 1000;
        List<ProcessDefinition> processDefinitionList = queryClient.findProcesses(pageNumber, pageSize);

        //List<ProcessDefinition> processDefinitionList = queryClient.findProcessesByContainerId(containerId, 0, 1000);
        
        for (ProcessDefinition processDefinition : processDefinitionList) {
            //System.out.println(processDefinition.getName() + " - " + processDefinition.getId() + " v" + processDefinition.getVersion());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!! processDefinition " + processDefinition);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!! processDefinition.toString() " + processDefinition.toString());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!! processDefinition.getPackageName() " + processDefinition.getPackageName());
            System.out.println("");
        }
         
        return processDefinitionList;
    }

    /* 
        try {
            List<Product> products;
            String query = request.getParameter("query");
            if (query == null || query.isEmpty()) {
                products = getFeaturedProducts();
            } else {
                products = searchProducts(query);
            }
            request.setAttribute("products", products);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to retrieve products: " + e.getMessage());
        }        
     */
    public static void main(String[] args) throws NamingException, FileNotFoundException, IOException, URISyntaxException {
        MigrationPlan unit = new MigrationPlan();
        //String containerId = System.getenv("CONTAINER_ID");
        //Long processInstanceId = new Long(System.getenv("PROCESS_INSTANCE_ID"));
        //String targetContainerId = System.getenv("TARGET_CONTAINER_ID");
        //String targetProcessId = System.getenv("TARGET_PROCESS_ID");

        //String sourceNodeId = System.getenv("SOURCE_NODE_ID");
        //String targetNodeId = System.getenv("TARGET_NODE_ID");
        Boolean testREST = Boolean.valueOf(System.getenv("TEST_REST"));

        //test migration with node mapping
        /*
        unit.setContainerId(containerId);
        unit.setTargetContainerId(targetContainerId);
        unit.setProcessInstanceId(processInstanceId);
        unit.setTargetProcessId(targetProcessId);
        unit.setTestRest(testREST.toString());
        unit.setSourceNodeId(sourceNodeId);
        unit.setTargetNodeId(targetNodeId);

        KieApiClient migration = new KieApiClient();
        ProcessAdminServicesClientImpl client = migration.setupProcessAdminServicesClient(unit);
        migration.migrateInstance(client, unit);
         */
        //ProcessInfo p = MigrationUtils.getProcessInfoForTesting();
        //System.out.println("processInfo: " + p);
        //test get process definition
        //String processId = System.getenv("PROCESS_ID");
        String containerId = "testProject1_2.0.0";
        String processId = "testProject1.HW2";
        //System.out.println("== get process definition from ProcessServicesClient==");
        //getProcessDefinitionFromProcessServicesClient(setupProcessServicesClient(), containerId, processId);
        //test queryService by listing all process
        System.out.println("== Listing Business Processes definition ==");
        listProcessesDefintionsFromQueryServicesClient();

        //MigrationUtils.getContainersFromKieServer();
        //MigrationUtils.retriveFilesFromKjar("com.myteam","testProject1","1.0");
    }

}
