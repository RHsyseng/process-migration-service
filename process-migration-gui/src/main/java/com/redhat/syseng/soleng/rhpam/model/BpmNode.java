/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.model;

/**
 *
 * @author czhu
 */
public class BpmNode {

    private String name;
    private String id;
    private String type;

    //note this targetId is only used in nodeMapping, request from JSP to backend. No value from parse bpmn2 file. 
    //private String targetId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    @Override
    public String toString() {
        return "BpmNode [name=" + name + ", type=" + type 
                + ", id=" + id + "]";
    }

}
