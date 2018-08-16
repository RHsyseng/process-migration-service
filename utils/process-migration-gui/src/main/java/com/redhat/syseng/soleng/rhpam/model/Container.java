/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.model;

/**
 *
 * @author czhu
 */
public class Container {
    private String containerAlias;
    private String containerId;
    private String artifiactId;
    private String groupId;
    private String version;
    private String processId;    

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getContainerAlias() {
        return containerAlias;
    }

    public void setContainerAlias(String containerAlias) {
        this.containerAlias = containerAlias;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getArtifiactId() {
        return artifiactId;
    }

    public void setArtifiactId(String artifiactId) {
        this.artifiactId = artifiactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return "Containers [containerId=" + containerId + ", containerAlias=" + containerAlias
                + ", artifiactId=" + artifiactId + ", groupId=" + groupId + ", processId=" + processId 
                + ", version=" + version+ "]";
    }    
}
