/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MigrationDefinition {


    @JsonProperty("plan_id")
    private Long planId;
    // TODO: private ProcessInstanceQuery query;


    @JsonProperty("process_instances_id")
    private ArrayList<Long> processInstancesId = new ArrayList<>();
    private Execution execution;

    public Long getPlanId() {
	return planId;
    }

    public void setPlanId(Long planId) {
	this.planId = planId;
    }

    public List<Long> getProcessInstancesId() {
	return processInstancesId;
    }

    public void setProcessInstancesId(List<Long> processInstancesId) {
	if (processInstancesId != null) {
	    this.processInstancesId = new ArrayList<>(processInstancesId);
	} else {
	    this.processInstancesId = null;
	}
    }

    public Execution getExecution() {
	return execution;
    }

    public void setExecution(Execution execution) {
	this.execution = execution;
    }

}
