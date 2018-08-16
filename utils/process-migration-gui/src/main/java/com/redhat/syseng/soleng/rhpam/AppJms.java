package com.redhat.syseng.soleng.rhpam;

import java.util.List;
import java.util.Properties;
import javax.jms.Queue;
import java.util.stream.Collectors;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;

public class AppJms {

    public KieServicesClient getKieServerClient() throws NamingException {

        String provider_url = System.getenv("KIE_JMS_PROVIDER_URL");
        String username = System.getenv("KIE_USER");
        String password = System.getenv("KIE_PASSWORD");
        System.out.println("JMS provider_url: " + provider_url);

        String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
        //String INITIAL_CONTEXT_FACTORY = new String("org.wildfly.naming.client.WildFlyInitialContextFactory");

        String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
        String REQUEST_QUEUE_JNDI = "jms/queue/KIE.SERVER.REQUEST";
        String RESPONSE_QUEUE_JNDI = "jms/queue/KIE.SERVER.RESPONSE";

        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, provider_url);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        InitialContext context = new InitialContext(env);

        Queue requestQueue = (Queue) context.lookup(REQUEST_QUEUE_JNDI);
        Queue responseQueue = (Queue) context.lookup(RESPONSE_QUEUE_JNDI);
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY);
        KieServicesConfiguration conf = KieServicesFactory.newJMSConfiguration(connectionFactory, requestQueue, responseQueue, username, password);

        //conf.setResponseHandler(new FireAndForgetResponseHandler());
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

        return kieServicesClient;

    }

    public void startProcess(KieServicesClient kieServicesClient) {
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);

        String containerId = System.getenv("CONTAINER_ID");
        String processDefinitionId = System.getenv("PROCESS_DEFINITION_ID");
//        String containerId = "testProject1_1.0";
//        String processDefinitionId = "testProject1.HW1";

        long processInstanceId = processClient.startProcess(containerId, processDefinitionId);
        System.out.println("JMS, started process instance #" + processInstanceId);
        List<ProcessInstance> processInstances = processClient.findProcessInstances(containerId, 0, 100);
        System.out.println("JMS, found processes IDs: " + processInstances.stream().map(ProcessInstance::getId).collect(Collectors.toList()));

    }

    public static void main(String[] args) throws NamingException, InterruptedException {
        AppJms app = new AppJms();

        KieServicesClient kieServicesClient = app.getKieServerClient();
        //app.startProcess(kieServicesClient);
        long sleepTimeInSeconds = Long.parseLong(System.getenv("SLEEP_IN_SECONDS"));

        System.out.println("!!!!!!!!!!!!!!!!!!!!before sent message, sleep for " + sleepTimeInSeconds + " seconds");
        Thread.sleep(sleepTimeInSeconds * 1000);

        int loopCount = Integer.parseInt(System.getenv("MESSAGE_COUNT"));
        for (int i = 0; i < loopCount; i++) {
            try {
                System.out.println("loop number: " + i);
                //kieServicesClient.getServerInfo();
                app.startProcess(kieServicesClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
        KieServerInfo serverInfo = kieServicesClient.getServerInfo().getResult();

        System.out.print("Server capabilities (fire and forgot):");
        for (String capability : serverInfo.getCapabilities()) {
            System.out.print(" " + capability);
        }
         */
        System.out.println();
        System.out.println("!!!!!!!!!!!!!!!!!!!!sent " + loopCount + " messages");

    }
}
