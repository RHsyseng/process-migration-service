package com.redhat.syseng.soleng.rhpam.processmigration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessDefinition {

    private String id;

    @JsonProperty("container_id")
    private String containerId;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getContainerId() {
	return containerId;
    }

    public void setContainerId(String containerId) {
	this.containerId = containerId;
    }

}
