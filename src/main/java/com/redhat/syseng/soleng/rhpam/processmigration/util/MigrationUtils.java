package com.redhat.syseng.soleng.rhpam.processmigration.util;

import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.kie.server.client.admin.impl.ProcessAdminServicesClientImpl;
import org.kie.server.client.impl.KieServicesClientImpl;
import org.kie.server.client.impl.KieServicesConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;

@Deprecated
public class MigrationUtils {

    private static final Logger logger = LoggerFactory.getLogger(MigrationUtils.class);

    private static String kieHost;
    private static String kiePort;
    private static String kieContextRoot;
    private static String kieUsername;
    private static String kiePassword;
    public static String kieProtocol;
    public static String jmsProtocol = "http-remoting";

    private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String JMS_QUEUE_REQUEST = "jms/queue/KIE.SERVER.REQUEST";
    private static final String JMS_QUEUE_RESPONSE = "jms/queue/KIE.SERVER.RESPONSE";
    private static final String KIE_JMS_SERVICE_URL = jmsProtocol + "://" + getKieHost() + ":" + getKiePort();

    public static String getKieHost() {
	if (kieHost == null) {
	    getSystemEnvForKie();
	}
	return kieHost;
    }

    public static String getKiePort() {
	if (kiePort == null) {
	    getSystemEnvForKie();
	}
	return kiePort;
    }

    public static String getKieProtocol() {
	if (kieProtocol == null) {
	    getSystemEnvForKie();
	}
	return kieProtocol;
    }

    public static String getKieUsername() {
	if (kieUsername == null) {
	    getSystemEnvForKie();
	}
	return kieUsername;
    }

    public static String getKiePassword() {
	if (kiePassword == null) {
	    getSystemEnvForKie();
	}
	return kiePassword;
    }

    public static String getKieContextRoot() {
	if (kieContextRoot == null) {
	    kieContextRoot = System.getenv("KIE_CONTEXT_ROOT");
	    // in OCP, this won't be defined, so set to empty string.
	    if (kieContextRoot == null) {
		kieContextRoot = "";
	    }
	}
	return kieContextRoot;
    }

    private static void getSystemEnvForKie() {
	kieUsername = System.getenv("KIE_SERVER_USER");
	kiePassword = System.getenv("KIE_SERVER_PWD");

	// because in OCP template's environment variable is in this format
	// ${MYAPP}_KIESERVER_SERVICE_HOST
	// so need to loop through all and find the matching one
	Map<String, String> envs = System.getenv();
	for (String envName : envs.keySet()) {
	    // System.out.format("%s=%s%n", envName, envs.get(envName));
	    if (envName.contains("KIESERVER_SERVICE_HOST")) {
		kieHost = envs.get(envName);
		// System.out.println("!!!!!!!!!!!!!!!!!!!!! kieHost " + kieHost);
	    } else if (envName.contains("KIESERVER_SERVICE_PORT")) {
		kiePort = envs.get(envName);
		// System.out.println("!!!!!!!!!!!!!!!!!!!!! kiePort " + kiePort);
	    } else if (envName.contains("KIESERVER_SERVICE_PROTOCOL")) {
		kieProtocol = envs.get(envName);
	    }
	}

    }

    public static ProcessAdminServicesClientImpl setupProcessAdminServicesClient(Plan plan, String url, String username,
	    String password, boolean isRest) throws NamingException {

	KieServicesConfigurationImpl config = null;
	if (isRest) {
	    // REST config for sync mode to KIE server
	    logger.info("URL {} | username {} | password {}", url, username, password);
	    config = new KieServicesConfigurationImpl(url, username, password);

	} else {
	    // JMS config for aysnc mode to KIE server
	    logger.info("!!!!!!!!!!!!!! Async mode using JMS client");

	    logger.info(" kieJmsServiceUrl: " + KIE_JMS_SERVICE_URL);
	    logger.info(" username: " + username);
	    logger.info(" password: " + password);

	    java.util.Properties env = new java.util.Properties();
	    env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
		    "org.jboss.naming.remote.client.InitialContextFactory");
	    env.put(javax.naming.Context.PROVIDER_URL, KIE_JMS_SERVICE_URL);
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

}
