package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
               @NamedQuery(name = "MigrationReport.findById", query = "SELECT p FROM MigrationReport p WHERE p.migrationId = :id")
})
@Table(indexes = {
                  @Index(columnList = "migrationId")})
public class MigrationReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long migrationId;

    @Column(name = "migration_report")
    private String migrationReport;

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

    public String getMigrationReport() {
        return migrationReport;
    }

    public void setMigrationReport(String migrationReport) {
        this.migrationReport = migrationReport;
    }

}
