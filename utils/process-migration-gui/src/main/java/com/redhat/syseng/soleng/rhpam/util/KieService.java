/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.util;
import com.redhat.syseng.soleng.rhpam.model.containers.Response;
import com.redhat.syseng.soleng.rhpam.model.instance.ProcessInstanceList;
import com.redhat.syseng.soleng.rhpam.model.process.ProcessDefinitions;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public interface KieService {
    @GET
    @Path("/services/rest/server/containers")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    Response listContainers();
    //String listContainers();


    @GET
    @Path("/services/rest/server/containers/{containerId}/processes")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    ProcessDefinitions getProcessDefinitions(@PathParam("containerId") String containerId);
    //String getProcessDefinitions1(@PathParam("containerId") String containerId);

    @GET
    @Path("/services/rest/server/containers/{containerId}/processes/instances")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    ProcessInstanceList getRunningInstances(@PathParam("containerId") String containerId);

            
    
}
