/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Workflow Execution History Domain Object.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class WorkflowHistory implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private long id;

    private String workflowId;

    private long jobId;

    private String jobStringId;

    private String workflowName;

    private String workflowXml;

    private String currentAction;

    private String jobName;

    private String variable;

    private Date startDate;

    private String startDateSimple;

    private Date endDate;

    private String endDateSimple;

    private long elapsed;

    private String cause;

    private int currentStep;

    private int totalStep;

    private String exception;

    private State status;

    private String username;

    private String jobType;

    private String logPath;

    private String sf_parentIdentifier;

    private String sf_rootIdentifier;

    private int sf_depth;

    private String sf_taskId;

    public WorkflowHistory() {
    }

    public WorkflowHistory(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getJobStringId() {
        return jobStringId;
    }

    public void setJobStringId(String jobStringId) {
        this.jobStringId = jobStringId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowXml() {
        return workflowXml;
    }

    public void setWorkflowXml(String workflowXml) {
        this.workflowXml = workflowXml;
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartDateSimple() {
        return startDateSimple;
    }

    public void setStartDateSimple(String startDateSimple) {
        this.startDateSimple = startDateSimple;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndDateSimple() {
        return endDateSimple;
    }

    public void setEndDateSimple(String endDateSimple) {
        this.endDateSimple = endDateSimple;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public int getTotalStep() {
        return totalStep;
    }

    public void setTotalStep(int totalStep) {
        this.totalStep = totalStep;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getSf_parentIdentifier() {
        return sf_parentIdentifier;
    }

    public void setSf_parentIdentifier(String sf_parentIdentifier) {
        this.sf_parentIdentifier = sf_parentIdentifier;
    }

    public String getSf_rootIdentifier() {
        return sf_rootIdentifier;
    }

    public void setSf_rootIdentifier(String sf_rootIdentifier) {
        this.sf_rootIdentifier = sf_rootIdentifier;
    }

    public int getSf_depth() {
        return sf_depth;
    }

    public void setSf_depth(int sf_depth) {
        this.sf_depth = sf_depth;
    }

    public String getSf_taskId() {
        return sf_taskId;
    }

    public void setSf_taskId(String sf_taskId) {
        this.sf_taskId = sf_taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowHistory that = (WorkflowHistory) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "WorkflowHistory{" +
                "id=" + id +
                ", workflowId='" + workflowId + '\'' +
                ", jobId=" + jobId +
                ", jobStringId='" + jobStringId + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", workflowXml='" + workflowXml + '\'' +
                ", currentAction='" + currentAction + '\'' +
                ", jobName='" + jobName + '\'' +
                ", variable='" + variable + '\'' +
                ", startDate=" + startDate +
                ", startDateSimple='" + startDateSimple + '\'' +
                ", endDate=" + endDate +
                ", endDateSimple='" + endDateSimple + '\'' +
                ", elapsed=" + elapsed +
                ", cause='" + cause + '\'' +
                ", currentStep=" + currentStep +
                ", totalStep=" + totalStep +
                ", exception='" + exception + '\'' +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", jobType='" + jobType + '\'' +
                ", logPath='" + logPath + '\'' +
                ", sf_parentIdentifier='" + sf_parentIdentifier + '\'' +
                ", sf_rootIdentifier='" + sf_rootIdentifier + '\'' +
                ", sf_depth=" + sf_depth +
                ", sf_taskId='" + sf_taskId + '\'' +
                '}';
    }
}