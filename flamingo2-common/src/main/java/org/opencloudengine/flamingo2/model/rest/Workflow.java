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

/**
 * Workflow Domain Object.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class Workflow implements Serializable {

    private long id;

    private String workflowId;

    private String workflowName;

    private String description;

    private String variable;

    private String workflowXml;

    private String designerXml;

    private Timestamp createDate;

    private String username;

    private WorkflowStatusType status;

    private long workflowTreeId;

    public Workflow() {
    }

    public Workflow(long id) {
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

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkflowXml() {
        return workflowXml;
    }

    public void setWorkflowXml(String workflowXml) {
        this.workflowXml = workflowXml;
    }

    public WorkflowStatusType getStatus() {
        return status;
    }

    public void setStatus(WorkflowStatusType status) {
        this.status = status;
    }

    public long getWorkflowTreeId() {
        return workflowTreeId;
    }

    public void setWorkflowTreeId(long workflowTreeId) {
        this.workflowTreeId = workflowTreeId;
    }

    public String getDesignerXml() {
        return designerXml;
    }

    public void setDesignerXml(String designerXml) {
        this.designerXml = designerXml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workflow workflow = (Workflow) o;

        if (id != workflow.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Workflow{" +
                "id=" + id +
                ", workflowId='" + workflowId + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", description='" + description + '\'' +
                ", variable='" + variable + '\'' +
                ", workflowXml='" + workflowXml + '\'' +
                ", designerXml='" + designerXml + '\'' +
                ", createDate=" + createDate +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", workflowTreeId=" + workflowTreeId +
                '}';
    }
}