package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import org.kie.server.api.model.admin.MigrationReportInstance;

@Document(collection = "process-instance-migrations", schemaVersion = "1.0")
public class ProcessInstanceMigration implements Identifiable {

    @Id
    private Long id;
    private Long migrationId;
    private Long processInstanceId;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<String> logs;

    public ProcessInstanceMigration() {}

    public ProcessInstanceMigration(Long migrationId, MigrationReportInstance report) {
        this.migrationId = migrationId;
        this.processInstanceId = report.getProcessInstanceId();
        if (report.getStartDate() != null) {
            this.startedAt = LocalDateTime.ofInstant(report.getStartDate().toInstant(), ZoneId.systemDefault());
        }
        if (report.getEndDate() != null) {
            this.finishedAt = LocalDateTime.ofInstant(report.getEndDate().toInstant(), ZoneId.systemDefault());
        }
        this.logs = report.getLogs();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Long migrationId) {
        this.migrationId = migrationId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

}
