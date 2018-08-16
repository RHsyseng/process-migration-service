package com.redhat.syseng.soleng.rhpam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redhat.syseng.soleng.rhpam.model.Container;
import com.redhat.syseng.soleng.rhpam.model.MigrationPlan;
import com.redhat.syseng.soleng.rhpam.model.ProcessInfo;
import com.redhat.syseng.soleng.rhpam.model.containers.Response;
import com.redhat.syseng.soleng.rhpam.model.containers.Response.KieContainers.KieContainer;
import com.redhat.syseng.soleng.rhpam.model.instance.ProcessInstanceList;
import com.redhat.syseng.soleng.rhpam.model.instance.ProcessInstanceList.ProcessInstance;
import com.redhat.syseng.soleng.rhpam.model.process.ProcessDefinitions;
import com.redhat.syseng.soleng.rhpam.util.HttpErrorException;
import com.redhat.syseng.soleng.rhpam.util.MigrationUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kie.server.api.model.admin.MigrationReportInstance;
import org.kie.server.api.model.definition.ProcessDefinition;

public class RestClient {


    public static void getProcessDefinitionsFromRest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws URISyntaxException, ServletException, IOException {
        System.out.println("-----------------------------getProcessDefinitionsFromRest started ");


        Response response = MigrationUtils.getContainersFromKieServer();

        List<KieContainer> kieContainers = response.getKieContainers().getKieContainer();

        ArrayList<Container> containers = new ArrayList();

        for (KieContainer kieContainer : kieContainers) {

            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!kieContainer.getReleaseId() : " + kieContainer.getReleaseId());

            String containerId = kieContainer.getContainerId();
            ProcessDefinitions processDefinitions = MigrationUtils.getProcessDefinitionsFromKieServer(containerId);

            Container container = new Container();
            container.setArtifiactId(kieContainer.getReleaseId().getArtifactId());
            container.setContainerAlias(kieContainer.getContainerAlias());
            container.setContainerId(containerId);
            container.setGroupId(kieContainer.getReleaseId().getGroupId());
            container.setProcessId(processDefinitions.getProcesses().getProcessId());
            container.setVersion(kieContainer.getReleaseId().getVersion());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!container: " + container);
            containers.add(container);
        }
        httpRequest.getSession().setAttribute("kieContainers", containers);
        //httpRequest.setAttribute("kieContainers", containers);

        //System.getProperties().list(System.out);
        System.out.println("-----------------------------all envs ---------------------------");
        Map<String, String> envs = System.getenv();
        for (String envName : envs.keySet()) {
            System.out.format("%s=%s%n", envName, envs.get(envName));
        }
        //httpRequest.setAttribute("envs", envs);
        System.out.println("-----------------------------all envs ---------------------------");
                 
        //System.out.println("-----------------------------load properties from inside war---------------------------");
        //new MigrationUtils().loadProperties("/team.properties");
        //System.out.println("-----------------------------load properties from inside war end ---------------------------");
        
        //Not using this, because it doesn't work for symbolic link situtaion 
        //System.out.println("-----------------------------loadPropertiesFromOutsideOfWar 1---------------------------");
        //ServletContext servletContext = httpRequest.getServletContext();
        //new MigrationUtils().loadPropertiesFromWar2("/WEB-INF/classes/team2.properties", servletContext);
        //System.out.println("-----------------------------loadPropertiesFromOutsideOfWar end ---------------------------");
        
       
        
        RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/chooseContainer.jsp");
        requestDispatcher.forward(httpRequest, httpResponse);        

        System.out.println("-----------------------------getProcessDefinitionsFromRest ended ");
    }
    
    public static void getProcessDefinitionsFromApi(HttpServletRequest request) throws URISyntaxException, NamingException, ServletException, IOException {
        //Note: for now I can't use this get everything from API
        //reason: The biggest problem is the version in process defintion is not the same version as from release id. 
        //e.g: process defintion verson: 1.0,   kieContainer.getReleaseId().getVersion() is : 1.0.0
        //and only the 1.0.0 can be used to retrive information from KJar in MigrationUtils.retriveProcessInfoFromKjar
        List<ProcessDefinition> processDefinitionList = KieApiClient.listProcessesDefintionsFromQueryServicesClient();
        /*
        for (ProcessDefinition processDefinition : processDefinitionList) {
            Map<String, String[]> mp = processDefinition.getAssociatedEntities();
System.out.println("-----------------------------getProcessDefinitionsFromApi mp "  + mp);
            Iterator it = mp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
         */
        
        

        //System.out.println("-----------------------------getContainerAndProcessInfo ended ");
    }
    

    public static void getRunningInstances(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws URISyntaxException, IOException, ServletException {
        System.out.println("-----------------------------getRunningInstances started ");

        ProcessInfo processInfo = new ProcessInfo();
        String containerId = httpRequest.getParameter("containerId");
        processInfo.setContainerId(containerId);
        String targetContainerId = httpRequest.getParameter("targetContainerId");
        processInfo.setTargetContainerId(targetContainerId);

        ArrayList<Container> containers = (ArrayList<Container>) httpRequest.getSession().getAttribute("kieContainers");
        //ArrayList<Container> containers = (ArrayList<Container>) httpRequest.getAttribute("kieContainers");
        for (Container container : containers) {
            if (container.getContainerId().equals(containerId)) {
                String processId = container.getProcessId();

                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! processId " + processId);
                processInfo.setProcessId(processId);
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! container.getVersion() " + container.getVersion());
                MigrationUtils.retriveProcessInfoFromKjar(container.getGroupId(), container.getArtifiactId(), container.getVersion(), processInfo, true);

            }
            if (container.getContainerId().equals(targetContainerId)) {
                String targetProcessId = container.getProcessId();
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! targetProcessId " + targetProcessId);
                processInfo.setTargetProcessId(targetProcessId);

                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! container.getVersion() " + container.getVersion());
                MigrationUtils.retriveProcessInfoFromKjar(container.getGroupId(), container.getArtifiactId(), container.getVersion(), processInfo, false);

            }
        }

        //processInfo.setTargetProcessId(request.getParameter("targetProcessId"));
        ProcessInstanceList processInstanceList = MigrationUtils.getRunningInstancesFromKieServer(processInfo.getContainerId());
        if (null != processInstanceList) {
            List<ProcessInstance> processInstances = processInstanceList.getProcessInstance();
            if (!processInstances.isEmpty()) {
                ArrayList<Integer> runningInstance = new ArrayList();
                for (ProcessInstance eachInstance : processInstances) {
                    byte processInstanceId = eachInstance.getProcessInstanceId();
                    runningInstance.add(new Integer(processInstanceId));
                }
                processInfo.setRunningInstance(runningInstance);
            }
        }


        /*
        String xmlFilePath = "/HW1_v1.bpmn2";
        MigrationUtils util = new MigrationUtils();
        processInfo.setNodeV1(util.parseBpmnFileInsideWar(xmlFilePath));
        xmlFilePath = "/HW1_v2.bpmn2";
        processInfo.setNodeV2(util.parseBpmnFileInsideWar(xmlFilePath));
         */
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!get nodes from BPMN file inside kjar ");

        /*
        boolean isV1 = true;
        MigrationUtils.retriveProcessInfoFromKjar("com.myteam", "testProject1", "1.0", processInfo, isV1);
        isV1 = false;
        MigrationUtils.retriveProcessInfoFromKjar("com.myteam", "testProject1", "2.0", processInfo, isV1);

         */
        //request.setAttribute("processInfo", processInfo);
        //need to use session attribe here because between chooseContainer and migration jsp, the request is different, so processInfo is lost 
        //if just stored in request attribue
        System.out.println("!!!!!!!!!getRunningInstances before dispatch to migration.jsp ");
        httpRequest.getSession().setAttribute("processInfo", processInfo);
        RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/migration.jsp");
        requestDispatcher.forward(httpRequest, httpResponse);        
        System.out.println("-----------------------------getRunningInstances ended ");

    }

    /*
    public ProcessInfo getProcessInfo(HttpServletRequest httpRequest) throws FileNotFoundException {
        ProcessInfo processInfo = new ProcessInfo();


        //processInfo.setRunningInstance(getRunningInstances());
        processInfo.setContainerId("testProject1_1.0");
        processInfo.setTargetContainerId("testProject1_2.0");
        processInfo.setTargetProcessId("src.HW2");
        httpRequest.setAttribute("processInfo", processInfo);

        return processInfo;
    }
     */
    public static void generatePlan(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws HttpErrorException, NamingException, ServletException, IOException {
        System.out.println("-----------------------------generatePlan started ");
        MigrationPlan plan = MigrationUtils.getMigrationPlanFromRequest(httpRequest);
        ProcessInfo processInfo = (ProcessInfo) httpRequest.getSession().getAttribute("processInfo");
        System.out.println("!!!!!!!!!!!!!!generatePlan, processInfo" + processInfo);
        ArrayList<String> validationError = MigrationUtils.validateMigrationPlan(plan, processInfo);

        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonInString = gson.toJson(plan);
        //Entity<Customer> entity = Entity.entity(customer, MediaType.APPLICATION_JSON);
        logInfo("MigrationPlan is generated:" + plan);
        logInfo("MigrationPlan in json string :" + jsonInString);
        
        //now generate compact json string to be used in the migration service. 
        gson = new GsonBuilder().create();
        String jsonInString2 = gson.toJson(plan);
        logInfo("MigrationPlan in compact json string :" + jsonInString2);        

        //Response response = webTarget.httpRequest(MediaType.APPLICATION_JSON).post(entity);
        //new KieApiClient().migrateInstance(plan);
        httpRequest.getSession().setAttribute("migrationPlan", plan);
        httpRequest.setAttribute("migrationPlanInJson", jsonInString);

        if (!validationError.isEmpty()) {
            String validationErrorJsonString = gson.toJson(validationError);
            httpRequest.setAttribute("validationError", validationErrorJsonString);

        }
        RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/migrationPlan.jsp");
        requestDispatcher.forward(httpRequest, httpResponse);        
        
        System.out.println("-----------------------------generatePlan ended ");

    }

    public static void executePlan(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws HttpErrorException, NamingException, ServletException, IOException {
        System.out.println("-----------------------------executePlan started ");
        MigrationPlan plan = (MigrationPlan) httpRequest.getSession().getAttribute("migrationPlan");
        logInfo("!!!!!!!!!!!!!!!!!!!Executing MigrationPlan now:" + plan);
        List<MigrationReportInstance> reports = KieApiClient.migrateInstance(plan);
        System.out.println("!!!!!!!!!!!!!!!!!!!Executing MigrationPlan result: " + reports.toString());

        httpRequest.getSession().removeAttribute("migrationPlan");
        httpRequest.getSession().removeAttribute("kieContainers");
        httpRequest.getSession().removeAttribute("processInfo");
        httpRequest.setAttribute("migrationReports", reports);
        RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/migrationReport.jsp");
        requestDispatcher.forward(httpRequest, httpResponse);        
        System.out.println("-----------------------------executePlan ended ");
    }

   
    public static void cleanSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException  {
        System.out.println("-----------------------------cleanSession started ");

        httpRequest.getSession().removeAttribute("migrationPlan");
        httpRequest.getSession().removeAttribute("kieContainers");
        httpRequest.getSession().removeAttribute("processInfo");
        RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/index.jsp");
        requestDispatcher.forward(httpRequest, httpResponse);        
        System.out.println("-----------------------------cleanSession ended ");
    }    
    
    private static void logInfo(String message) {
        Logger.getLogger(RestClient.class.getName()).log(Level.INFO, message);
    }

}
