package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionStatus;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "migrations", schemaVersion = "1.0")
public class Migration implements Identifiable {

    @Id
    private Long id;
    private MigrationDefinition definition;

    // TODO: Use LocalDateTime when possible. Currently JsonDB causes problems due
    // to the lack of default constructor
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("started_at")
    private Date startedAt;

    @JsonProperty("finished_at")
    private Date finishedAt;

    @JsonProperty("cancelled_at")
    private Date cancelledAt;

    private ExecutionStatus status;

    @JsonProperty("error_message")
    private String errorMessage;

    public Migration() {
    }

    public Migration(MigrationDefinition definition) {
	this.definition = definition;
	Date now = new Date();
	this.createdAt = now;
	if (ExecutionType.ASYNC.equals(definition.getExecution().getType())
		&& now.before(definition.getExecution().getStartAt())) {
	    this.status = ExecutionStatus.SCHEDULED;
	} else {
	    this.status = ExecutionStatus.CREATED;
	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public MigrationDefinition getDefinition() {
	return definition;
    }

    public void setDefinition(MigrationDefinition definition) {
	this.definition = definition;
    }

    public Date getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    public Date getStartedAt() {
	return startedAt;
    }

    public void setStartedAt(Date startedAt) {
	this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
	return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
	this.finishedAt = finishedAt;
    }

    public Date getCancelledAt() {
	return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt) {
	this.cancelledAt = cancelledAt;
    }

    public ExecutionStatus getStatus() {
	return status;
    }

    public void setStatus(ExecutionStatus status) {
	this.status = status;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public Migration start() {
	this.startedAt = new Date();
	this.status = ExecutionStatus.STARTED;
	return this;
    }

    public Migration complete(Boolean hasErrors) {
	this.finishedAt = new Date();
	if (Boolean.TRUE.equals(hasErrors)) {
	    this.status = ExecutionStatus.COMPLETED_WITH_ERRORS;
	} else {
	    this.status = ExecutionStatus.COMPLETED;
	}
	return this;
    }

    public Migration cancel() {
	this.cancelledAt = new Date();
	this.status = ExecutionStatus.CANCELLED;
	return this;
    }

    public Migration fail(Exception e) {
	this.finishedAt = new Date();
	this.status = ExecutionStatus.COMPLETED_WITH_ERRORS;
	this.errorMessage = e.getMessage();
	return this;
    }

}
