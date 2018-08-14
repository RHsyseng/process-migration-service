package com.redhat.syseng.soleng.rhpam.util;

import com.redhat.syseng.soleng.rhpam.model.BpmNode;
import com.redhat.syseng.soleng.rhpam.model.MigrationPlan;
import com.redhat.syseng.soleng.rhpam.model.ProcessInfo;

import com.redhat.syseng.soleng.rhpam.model.containers.Response;
import com.redhat.syseng.soleng.rhpam.model.instance.ProcessInstanceList;
import com.redhat.syseng.soleng.rhpam.model.process.ProcessDefinitions;
import javax.servlet.http.HttpServletRequest;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.io.StringWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.drools.compiler.kie.builder.impl.KieServicesImpl;
import org.eclipse.aether.artifact.Artifact;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.kie.api.KieServices;
import org.kie.api.management.GAV;
import org.kie.scanner.KieMavenRepository;

public class MigrationUtils {

    private static Logger logger = Logger.getLogger(MigrationUtils.class.getName());

    //String kieHost = System.getenv("KIE_HOST");        
    //int kiePort = Integer.valueOf(System.getenv("KIE_PORT"));        
//    public static String kieHost = "myapp-kieserver-calvin-test.rhdp.ocp.cloud.lab.eng.bos.redhat.com";
//    public static int kiePort = 80;
//    public static String kieContextRoot = "";
    private static String kieHost;
    private static String kiePort;
    private static String kieContextRoot;
    private static String kieUsername;
    private static String kiePassword;
    public static String protocol = "http";

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
            //in OCP, this won't be defined, so set to empty string. 
            if (kieContextRoot == null) {
                kieContextRoot = "";
            }
        }
        return kieContextRoot;
    }

    private static void getSystemEnvForKie() {
        kieUsername = System.getenv("KIE_SERVER_USER");
        kiePassword = System.getenv("KIE_SERVER_PWD");

        //because in OCP template's environment variable is in this format ${MYAPP}_KIESERVER_SERVICE_HOST
        //so need to loop through all and find the matching one
        Map<String, String> envs = System.getenv();
        for (String envName : envs.keySet()) {
//                System.out.format("%s=%s%n", envName, envs.get(envName));
            if (envName.contains("KIESERVER_SERVICE_HOST")) {
                kieHost = envs.get(envName);
                System.out.println("!!!!!!!!!!!!!!!!!!!!! kieHost " + kieHost);
            } else if (envName.contains("KIESERVER_SERVICE_PORT")) {
                kiePort = envs.get(envName);
                System.out.println("!!!!!!!!!!!!!!!!!!!!! kiePort " + kiePort);
            }
        }

    }

    private static ResteasyClient createRestClientWithCerts(boolean useOcpCertificate) {
        ResteasyClient client;

        if (useOcpCertificate) {
            //use the OCP certificate which exist here in every pod: /var/run/secrets/kubernetes.io/serviceaccount/ca.crt
            try (FileInputStream in = new FileInputStream("/var/run/secrets/kubernetes.io/serviceaccount/ca.crt")) {

                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate cert = cf.generateCertificate(in);
                //logger.info("createRestClientWithCerts, created Certificate from /var/run/secrets/kubernetes.io/serviceaccount/ca.crt");

                // load the keystore that includes self-signed cert as a "trusted" entry
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                keyStore.setCertificateEntry("ocp-cert", cert);
                tmf.init(keyStore);
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, tmf.getTrustManagers(), null);
                //logger.info("createRestClientWithCerts, created SSLContext");

                //For proper HTTPS authentication
                ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
                clientBuilder.sslContext(ctx);
                client = clientBuilder.build();
            } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException ex) {
                logger.log(Level.SEVERE, null, ex);
                throw new IllegalStateException(ex);
            }

            //use filter to add http header
            //RestClientRequestFilter filter = new RestClientRequestFilter();
            //client.register(filter);
        } else {
            client = new ResteasyClientBuilder().build();
        }
        return client;
    }

    private static URIBuilder getUriBuilder(Object... path) {

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(protocol);

        uriBuilder.setHost(getKieHost());
        uriBuilder.setPort(Integer.parseInt(getKiePort()));

        StringWriter stringWriter = new StringWriter();
        for (Object part : path) {
            stringWriter.append('/').append(String.valueOf(part));
        }
        uriBuilder.setPath(stringWriter.toString());
        return uriBuilder;
    }

    public static KieService getKieService() throws URISyntaxException {
        boolean useOcpCertificate = false;
        ResteasyClient client = createRestClientWithCerts(useOcpCertificate);

        URIBuilder uriBuilder = getUriBuilder(getKieContextRoot());
        String url = uriBuilder.build().toString();
        //System.out.println("kieService URL: " + url);
        ResteasyWebTarget webTarget = client.target(url);
        webTarget.register(new BasicAuthentication(getKieUsername(), getKiePassword()));
        return webTarget.proxy(KieService.class);
    }

    public static Response getContainersFromKieServer() throws URISyntaxException {
        Response response = getKieService().listContainers();
        return response;

        /*
        String response = getKieService().listContainers();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! response: " + response);
        return null;
         */
    }

    public static ProcessDefinitions getProcessDefinitionsFromKieServer(String containerId) throws URISyntaxException {
        ProcessDefinitions response = getKieService().getProcessDefinitions(containerId);
        return response;

        /*
        String response = getKieService().getProcessDefinitions(containerId);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!getProcessDefinitionsFromKieServer response: " + response);
        return null;
         */
    }

    public static ProcessInstanceList getRunningInstancesFromKieServer(String containerId) throws URISyntaxException {
        ProcessInstanceList response = getKieService().getRunningInstances(containerId);
        return response;
    }

    public static MigrationPlan getMigrationPlanFromRequest(HttpServletRequest request) {
        MigrationPlan plan = new MigrationPlan();
        plan.setContainerId(request.getParameter("containerId"));
        plan.setTargetContainerId(request.getParameter("targetContainerId"));
        plan.setTargetProcessId(request.getParameter("targetProcessId"));
        //plan.setUseRest(request.getParameter("useRest"));

        //running instance id list
        String[] instanceIdArray = request.getParameterValues("instanceIds");
        if (null != instanceIdArray) {
            ArrayList<Long> instanceIds = new ArrayList<>();
            for (String str : instanceIdArray) {
                instanceIds.add(Long.valueOf(str));
            }
            plan.setProcessInstancesId(instanceIds);
        } else {
            System.out.println("!!!!!!!!!!!!!! have to choose a running instance for migration!");
        }

        //node mapping section
        HashMap<String, String> map = new HashMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            int i = name.indexOf("-");
            if (name.contains("targetNodeId-")) {
                //this parameter is for v2node mapping

                String sourceNodeId = name.substring(i + 1);
                String targetNodeId = request.getParameter(name);
                if (null != targetNodeId && !targetNodeId.equals("")) {
                    map.put(sourceNodeId, targetNodeId);
                }

            }
            plan.setNodeMapping(map);

        }

        /*

        String checkBoxValue2 = request.getParameter("checkBox2");
        String checkBoxValue3 = request.getParameter("checkBox3");

        System.out.println("checkBox2 " + checkBoxValue2);
        System.out.println("checkBox3 " + checkBoxValue3);
         */
        return plan;
    }

    public static MigrationPlan getLoginInfo(HttpServletRequest request) {
        MigrationPlan customer = new MigrationPlan();
        return customer;
    }

    public ArrayList<BpmNode> parseBpmnFileInsideWar(String xmlFilePath) {
        InputStream inputStream = getClass().getResourceAsStream(xmlFilePath);
        return parseBpmnFile(inputStream);

    }

    public static ArrayList<BpmNode> parseBpmnFile(String xmlFilePath) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(new File(xmlFilePath));
        return parseBpmnFile(inputStream);
    }

    public static ArrayList<BpmNode> parseBpmnFile(InputStream inputStream) {

        SAXBuilder saxBuilder = new SAXBuilder();
        ArrayList<BpmNode> result = new ArrayList<>();
        try {

            Document document = (Document) saxBuilder.build(inputStream);
            //root element is bpmn2:definitions
            Element rootElement = document.getRootElement();

            /*
            ArrayList<Element> listElement1 = rootElement.getChildren();
            for (Element v2node : listElement1) {
                System.out.println("v2node.getName() :" + v2node.getName());
                System.out.println("v2node.getAttributeValue(\"id\") :" + v2node.getAttributeValue("id"));
                System.out.println("v2node.toString() :" + v2node.toString());
                System.out.println("");
                System.out.println("");

                if (v2node.getName().equals("process")) {
                    System.out.println("!!!!!!!!!here22");
                    
                    Element processNode = rootElement.getChild("process",rootElement.getNamespace());
                    System.out.println("processNode.getName222() :" + processNode.getName());
                }

            }
             */
            Element processNode = rootElement.getChild("process", rootElement.getNamespace());

            List<Element> listElement = processNode.getChildren();

            for (Element node : listElement) {

                if (!node.getName().equals("sequenceFlow") && !node.getName().equals("startEvent") && !node.getName().equals("endEvent")) {
                    BpmNode bpmNode = new BpmNode();
                    bpmNode.setType(node.getName());
                    bpmNode.setName(node.getAttributeValue("name"));
                    bpmNode.setId(node.getAttributeValue("id"));
                    result.add(bpmNode);
                    /*
                    System.out.println(" " + bpmNode.toString());
                    System.out.println("");
                    System.out.println("");
                     */
                }
            }

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
        return result;
    }

    public static ArrayList<String> validateMigrationPlan(MigrationPlan plan, ProcessInfo processInfo) {
        ArrayList<String> errors = new ArrayList();

        //validate running instance id, need to have at least 1 running instance id
        List<Long> processIds = plan.getProcessInstancesId();
        if (processIds == null || processIds.isEmpty()) {
            errors.add("Need to choose at least one running instance id for migration!");
        }

        //validate the v2node mapping: 
        //1) target id must be a valid id from existing V2 v2node ids 
        //2) mapping must between same type of v2node
        Map<String, String> map = plan.getNodeMapping();
        if (!map.isEmpty()) {
            Set<String> keys = map.keySet();
            ArrayList<BpmNode> v1Nodes = processInfo.getNodeV1();

            for (String sourceNodeId : keys) {
                String targetNodeId = map.get(sourceNodeId);
                ArrayList<BpmNode> v2Nodes = processInfo.getNodeV2();
                boolean found = false;
                for (BpmNode v2node : v2Nodes) {
                    String id = v2node.getId();
                    if (id.equals(targetNodeId)) {
                        found = true;

                        //now compare if source and target node are the same type or not. 
                        String targetNodeType = v2node.getType();

                        for (BpmNode v1Node : v1Nodes) {
                            String v1NodeId = v1Node.getId();
                            if (sourceNodeId.equals(v1NodeId)) {
                                String sourceNodeType = v1Node.getType();
                                if (!sourceNodeType.equals(targetNodeType)) {
                                    errors.add("Node Mapping can only happen in the same type of node, for node id: " + sourceNodeId + " of sourceNodeType: " + sourceNodeType + " is not the same as node id: " + targetNodeId + " of targetNodeType: " + targetNodeType);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                if (!found) {
                    errors.add("For targetNodeId, please choose from newer version node id, right now this is not a valid one: " + targetNodeId);
                }
            }
        }

        return errors;
    }

    /*
    public static ArrayList<BpmNode> retriveFilesFromKjar(String groupId, String artifactId, String version) throws IOException {
        //System.out.println("!######################################retriveFilesFromKjar started ");
        ArrayList<BpmNode> bpmNodes = null;
        KieServices ks = (KieServicesImpl) KieServices.Factory.get();

//        String groupId = "com.myteam";
        //      String artifactId = "testProject1";
        //    String version = "1.0";
        GAV gav = new GAV(groupId, artifactId, version);

        KieMavenRepository repository1 = KieMavenRepository.getKieMavenRepository();

        Artifact artifact = repository1.resolveArtifact(gav);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact.getVersion() " + artifact.getVersion());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact.getArtifactId() " + artifact.getArtifactId());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact.getGroupId() " + artifact.getGroupId());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact " + artifact);
        File kjarFile = artifact.getFile();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!kjarFile.getName() " + kjarFile.getName());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!kjarFile.getPath() " + kjarFile.getPath());

        JarFile jarFile = new JarFile(kjarFile);
        Enumeration enumEntries = jarFile.entries();
        while (enumEntries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumEntries.nextElement();
            //System.out.println("jarEntry.getName(): " + jarEntry.getName());

            //File f = new File(destDir + java.io.File.separator + file.getName());
            File f = new File(jarEntry.getName());
            if (jarEntry.getName().contains("bpmn2")) {
                //System.out.println("!!!!!!!!!!!!!! found bpmn2 file");
                //System.out.println("jarEntry.getName(): " + jarEntry.getName());

                // get the input stream
                InputStream is = jarFile.getInputStream(jarEntry);

                bpmNodes = parseBpmnFile(is);
                is.close();

            } else if (jarEntry.getName().contains("svg")) {
                //This is the process diagram file
                //System.out.println("!!!!!!!!!!!!!! found svg file");
                //System.out.println("jarEntry.getName(): " + jarEntry.getName());
            }

        }
        jarFile.close();
        //System.out.println("!######################################retriveFilesFromKjar ended ");

        return bpmNodes;
    }
     */
    public static void retriveProcessInfoFromKjar(String groupId, String artifactId, String version, ProcessInfo info, boolean isV1) throws IOException {
        //System.out.println("######################################retriveProcessInfoFromKjar started, isV1 " + isV1);
        KieServices ks = (KieServicesImpl) KieServices.Factory.get();

        GAV gav = new GAV(groupId, artifactId, version);

        KieMavenRepository repository = KieMavenRepository.getKieMavenRepository();

        Artifact artifact = repository.resolveArtifact(gav);
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact.getVersion() " + artifact.getVersion());
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact.getArtifactId() " + artifact.getArtifactId());
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact.getGroupId() " + artifact.getGroupId());
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!artifact " + artifact);
        File kjarFile = artifact.getFile();
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!kjarFile.getName() " + kjarFile.getName());
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!kjarFile.getPath() " + kjarFile.getPath());

        JarFile jarFile = new JarFile(kjarFile);
        Enumeration enumEntries = jarFile.entries();
        while (enumEntries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumEntries.nextElement();
            //System.out.println("------------jarEntry.getName(): " + jarEntry.getName());

            if (jarEntry.getName().contains("bpmn")) {
                //System.out.println("!!!!!!!!!!!!!! found bpmn file");
                //System.out.println("jarEntry.getName(): " + jarEntry.getName());

                // get the input stream
                InputStream inputStream = jarFile.getInputStream(jarEntry);

                if (isV1) {
                    info.setNodeV1(parseBpmnFile(inputStream));
                } else {
                    info.setNodeV2(parseBpmnFile(inputStream));
                }

                inputStream.close();

            } else if (jarEntry.getName().contains("svg")) {
                //This is the process diagram file

                //System.out.println("!!!!!!!!!!!!!! found svg file");
                //System.out.println("jarEntry.getName(): " + jarEntry.getName());
                InputStream inputStream = jarFile.getInputStream(jarEntry);

                byte[] bytes = IOUtils.toByteArray(inputStream);
                String base64Image = Base64.getEncoder().encodeToString(bytes);

                //BufferedImage image = ImageUtil.base64ImageStringToBufferedImage(base64Image);
                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>...ImageUtil.base64ImageStringToBufferedImage(base64Image) is done: " + image);
                //String base64Image2 = ImageUtil.bufferedImageToBase64ImageString(image, "svg");
                //There might be several svg file in the jar, espeically in the newer version
                //so only get the svg file name match the process id
                if (isV1) {
                    String tmpStrV1 = info.getProcessId();

                    String tmpNameV1 = tmpStrV1;
                    int i = tmpStrV1.indexOf(".");
                    if (i > -1){
                        tmpNameV1 = tmpStrV1.substring(tmpStrV1.indexOf("."));
                    }
                    if (jarEntry.getName().contains(tmpNameV1)) {
                        info.setBase64ImageV1(base64Image);
                    }
                } else {
                    String tmpStrV2 = info.getTargetProcessId();
                    String tmpNameV2 = tmpStrV2;
                    int i = tmpStrV2.indexOf(".");
                    if (i > -1){
                        tmpNameV2 = tmpStrV2.substring(tmpStrV2.indexOf("."));
                    }
                    if (jarEntry.getName().contains(tmpNameV2)) {
                        info.setBase64ImageV2(base64Image);
                    }

                }

                inputStream.close();
            }
        }
        jarFile.close();
        //System.out.println("######################################retriveProcessInfoFromKjar ended ");

        //return info;
    }

    public void loadProperties(String fileName) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = getClass().getResourceAsStream(fileName);
            //input = new FileInputStream(fileName);

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            for (Entry<Object, Object> e : prop.entrySet()) {
                System.out.println(">>>>>>>>>>>> prop entry: " + e);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    
    public void loadPropertiesFromWar2(String fileName, ServletContext servletContext) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = servletContext.getResourceAsStream(fileName);

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            for (Entry<Object, Object> e : prop.entrySet()) {
                System.out.println(">>>>>>>>>>>> prop entry: " + e);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
