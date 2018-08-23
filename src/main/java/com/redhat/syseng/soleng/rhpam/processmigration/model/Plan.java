package com.redhat.syseng.soleng.rhpam.processmigration.model;

import java.util.HashMap;
import java.util.Map;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "plans", schemaVersion = "1.0")
public class Plan implements Identifiable {

    @Id
    private Long id;
    private String name;
    private String description;
    private ProcessDefinition source;
    private ProcessDefinition target;
    private Map<String, String> mappings = new HashMap<>();

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
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

    public void setDescription(String Description) {
	this.description = Description;
    }

    public ProcessDefinition getSource() {
	return source;
    }

    public void setSource(ProcessDefinition source) {
	this.source = source;
    }

    public ProcessDefinition getTarget() {
	return target;
    }

    public void setTarget(ProcessDefinition target) {
	this.target = target;
    }

    public Map<String, String> getMappings() {
	return mappings;
    }

    public void setMappings(Map<String, String> mappings) {
	this.mappings = mappings;
    }

}
