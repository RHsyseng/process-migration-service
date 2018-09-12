/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.io.Serializable;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@NamedQueries({
               @NamedQuery(name = "Plan.findAll", query = "SELECT p FROM Plan p"),
               @NamedQuery(name = "Plan.findById", query = "SELECT p FROM Plan p WHERE p.id = :id")
})

public class Plan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name, description;
    
    @JsonProperty("container_id")
    @Column(name = "container_id")
    private String containerId;

    @JsonProperty("target_container_id")
    @Column(name = "target_container_id")
    private String targetContainerId;

    @JsonProperty("target_process_id")
    @Column(name = "target_process_id")
    private String targetProcessId;

    @JsonProperty("node_mappings")
    @Column(name = "node_mappings")
    private HashMap<String, String> nodeMappings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTargetContainerId() {
        return targetContainerId;
    }

    public void setTargetContainerId(String targetContainerId) {
        this.targetContainerId = targetContainerId;
    }

    public String getTargetProcessId() {
        return targetProcessId;
    }

    public void setTargetProcessId(String targetProcessId) {
        this.targetProcessId = targetProcessId;
    }

    public HashMap<String, String> getNodeMappings() {
        return nodeMappings;
    }

    public void setNodeMappings(HashMap<String, String> nodeMappings) {
        this.nodeMappings = nodeMappings;
    }

}
