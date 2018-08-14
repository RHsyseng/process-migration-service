/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.model;

import java.util.ArrayList;

/**
 *
 * @author czhu
 */
public class ProcessInfo {

    private ArrayList<BpmNode> nodeV1;
    private ArrayList<BpmNode> nodeV2;
    private ArrayList<Integer> runningInstance;
    private String containerId;
    private String processId;    
    private String targetContainerId;
    private String targetProcessId;

    private String base64ImageV1;
    private String base64ImageV2;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBase64ImageV1() {
        return base64ImageV1;
    }

    public void setBase64ImageV1(String base64ImageV1) {
        this.base64ImageV1 = base64ImageV1;
    }

    public String getBase64ImageV2() {
        return base64ImageV2;
    }

    public void setBase64ImageV2(String base64ImageV2) {
        this.base64ImageV2 = base64ImageV2;
    }

    public ArrayList<BpmNode> getNodeV1() {
        return nodeV1;
    }

    public void setNodeV1(ArrayList<BpmNode> nodeV1) {
        this.nodeV1 = nodeV1;
    }

    public ArrayList<BpmNode> getNodeV2() {
        return nodeV2;
    }

    public void setNodeV2(ArrayList<BpmNode> nodeV2) {
        this.nodeV2 = nodeV2;
    }

    public ArrayList<Integer> getRunningInstance() {
        return runningInstance;
    }

    public void setRunningInstance(ArrayList<Integer> runningInstance) {
        this.runningInstance = runningInstance;
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

    @Override
    public String toString() {
        return "ProcessInfo [nodeV1=" + nodeV1 + ", nodeV2=" + nodeV2
                + ", containerId=" + containerId + ", targetContainerId=" + targetContainerId 
                + ", processId=" + processId + ", targetProcessId=" + targetProcessId
                + ", runningInstance=" + runningInstance + "]";
    }

}
