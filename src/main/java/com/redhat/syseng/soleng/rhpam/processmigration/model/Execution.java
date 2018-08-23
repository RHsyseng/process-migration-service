/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.net.URI;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author czhu
 */
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

    @JsonProperty("start_at")
    private Date startAt;

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

    public Date getStartAt() {
	return startAt;
    }

    public void setStartAt(Date startAt) {
	this.startAt = startAt;
    }

}
