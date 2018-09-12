/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.net.URI;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Execution {

    public enum ExecutionType {
	ASYNC, SYNC
    }

    public enum ExecutionStatus {
	SCHEDULED, STARTED, COMPLETED, COMPLETED_WITH_ERRORS, CANCELLED, CREATED
    }

    private ExecutionType type;

    @JsonProperty("callback_url")
    private URI callbackUrl;

    @JsonProperty("scheduled_start_time")
    private Date scheduledStartTime;

    public Date getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(Date scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public ExecutionType getType() {
	return type;
    }

    public void setType(ExecutionType type) {
	this.type = type;
    }

    public URI getCallbackUrl() {
	return callbackUrl;
    }

    public void setCallbackUrl(URI callbackUrl) {
	this.callbackUrl = callbackUrl;
    }



}
