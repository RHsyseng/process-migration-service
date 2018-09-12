package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionStatus;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Execution.ExecutionType;

@Entity
@NamedQueries({
               @NamedQuery(name = "Migration.findAll", query = "SELECT p FROM Migration p"),
               @NamedQuery(name = "Migration.findById", query = "SELECT p FROM Migration p WHERE p.id = :id")
})

public class Migration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "started_at")
    private Date startedAt;

    @Column(name = "finished_at")
    private Date finishedAt;

    @Column(name = "cancelled_at")
    private Date cancelledAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "process_instance_ids")
    private String processInstancesIds;

    //Execution
    @Column(name = "execution_type")
    private String executionType;

    @Column(name = "execution_status")
    private String executionStatus;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "sechdule_start_time")
    private Date scheduleStartTime;

    @ManyToOne
    @JoinColumn()
    private Plan plan;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "migrationId")
    private List<MigrationReport> reports;

    public Migration() {}

    public Migration(MigrationDefinition definition, Plan plan) {
        this.plan = plan;

        processInstancesIds = definition.getProcessInstancesId().toString();
        executionType = definition.getExecution().getType().toString();
        if (definition.getExecution().getType().equals(Execution.ExecutionType.ASYNC)) {//these 2 fields only make sense when the type is "ASYNC", otherwise just ignore
            callbackUrl = definition.getExecution().getCallbackUrl().toString();
            scheduleStartTime = definition.getExecution().getScheduledStartTime();
        }
        Date now = new Date();
        createdAt = now;
        if (ExecutionType.ASYNC.equals(definition.getExecution().getType()) && now.before(definition.getExecution().getScheduledStartTime())) {
            executionStatus = Execution.ExecutionStatus.SCHEDULED.toString();
        } else {
            executionStatus = Execution.ExecutionStatus.CREATED.toString();
        }
    }

    public List<MigrationReport> getReports() {
        return reports;
    }

    public void setReports(List<MigrationReport> reports) {
        this.reports = reports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getProcessInstancesIds() {
        return processInstancesIds;
    }

    public void setProcessInstancesIds(String processInstancesIds) {
        this.processInstancesIds = processInstancesIds;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Date getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(Date scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Migration start() {
        startedAt = new Date();
        executionStatus = ExecutionStatus.STARTED.toString();
        return this;
    }

    public Migration complete(Boolean hasErrors) {
        finishedAt = new Date();
        if (Boolean.TRUE.equals(hasErrors)) {
            executionStatus = ExecutionStatus.COMPLETED_WITH_ERRORS.toString();
        } else {
            executionStatus = ExecutionStatus.COMPLETED.toString();
        }
        return this;
    }

    public Migration cancel() {
        cancelledAt = new Date();
        executionStatus = ExecutionStatus.CANCELLED.toString();
        return this;
    }

    public Migration fail(Exception e) {
        finishedAt = new Date();
        executionStatus = ExecutionStatus.COMPLETED_WITH_ERRORS.toString();
        errorMessage = e.getMessage();
        return this;
    }

}
