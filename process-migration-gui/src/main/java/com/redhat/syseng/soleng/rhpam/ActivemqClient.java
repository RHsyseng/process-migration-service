/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
// import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * This is a generic client to EAP 7 activeMQ.
 */
public class ActivemqClient {

    public static void main(final String[] args) throws Exception {

        
        String queueName = System.getenv("JMS_QUEUE_NAME");
        String username = System.getenv("KIE_USER");
        String password = System.getenv("KIE_PASSWORD");
        String provider_url = System.getenv("KIE_JMS_PROVIDER_URL");
        System.out.println("!!!!!!!!!!!!!JMS_QUEUE_NAME: " + queueName);

        
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        p.put(Context.PROVIDER_URL, provider_url);
        InitialContext ic = new InitialContext(p);

        Destination destination = (Destination) ic.lookup(queueName);
        ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/RemoteConnectionFactory");

        Connection c = cf.createConnection(username, password);
        c.start();

        Session session = c.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(destination);
        Message m = session.createTextMessage("something");
        producer.send(m);

        c.close();        
        
 
    }
}
