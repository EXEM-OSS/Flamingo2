/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.designer.activiti;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.designer.activiti.task.Transformer;
import org.opencloudengine.flamingo2.engine.tree.TreeService;
import org.opencloudengine.flamingo2.model.opengraph.Opengraph;
import org.opencloudengine.flamingo2.model.rest.*;
import org.opencloudengine.flamingo2.util.JaxbUtils;
import org.opencloudengine.flamingo2.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.opencloudengine.flamingo2.util.DateUtils.getCurrentDateTime;
import static org.opencloudengine.flamingo2.util.JVMIDUtils.generateUUID;
import static org.opencloudengine.flamingo2.util.JsonUtils.format;
import static org.opencloudengine.flamingo2.util.StringUtils.escape;
import static org.opencloudengine.flamingo2.util.StringUtils.unescape;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private TreeService treeService;

    @Autowired
    private Transformer transformer;

    public Map<String, Object> save(WorkflowStatusType status, String processId, String workflowName,
                                    String designerXml, String workflowXml, Map<String, Object> variable,
                                    long treeId, long steps, String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("workflowId", processId);
        params.put("workflowName", workflowName);
        params.put("variable", escape(format(variable)));
        params.put("workflowXml", workflowXml);
        params.put("designerXml", designerXml);
        params.put("create", new Timestamp(new Date().getTime()));
        params.put("treeId", treeId);
        params.put("username", username);

        if (status == WorkflowStatusType.CREATED) {
            params.put("status", WorkflowStatusType.REGISTERED);
            workflowRepository.insert(params);
        } else if (status == WorkflowStatusType.REGISTERED) {
            params.put("status", WorkflowStatusType.REGISTERED);
            workflowRepository.update(params);
        } else if (status == WorkflowStatusType.COPIED) {
            params.put("status", WorkflowStatusType.REGISTERED);
            workflowRepository.insert(params);
        } else {
            logger.info("CREATED, REGISTERED, COPIED state did not come to save the workflow.");
        }
        return params;
    }

    @Override
    public Map<String, Object> saveAsNew(String parentTreeId, String xml, String username) {
        try {
            Map<String, Object> variable = new HashMap<>();
            Map<String, Map<String, Object>> localVariables = transformer.getLocalVariables(xml);
            Map<String, Object> globalVariables = transformer.getGlobalVariables(xml);
            List parallelVectors = transformer.getParallelVectors(xml);
            variable.put("local", localVariables);
            variable.put("global", globalVariables);
            variable.put("parallelVectors", parallelVectors);

            // 신규 Process ID를 생성한다.
            String newProcessId = getCurrentDateTime() + "_" + generateUUID();

            // BPMN 모델을 생성한다.
            BpmnModel bpmnModel = transformer.unmarshall(xml, newProcessId);

            // 트리 노드를 생성한다.
            Tree parent;
            if ("/".equals(parentTreeId)) {
                parent = treeService.getRoot(TreeType.WORKFLOW, username);
            } else {
                parent = treeService.get(Long.parseLong(parentTreeId));
            }

            Tree tree = new Tree(bpmnModel.getMainProcess().getName());
            tree.setTreeType(TreeType.WORKFLOW);
            tree.setNodeType(NodeType.ITEM);
            tree.setUsername(username);

            Tree child = treeService.create(parent, tree, NodeType.ITEM);
            String designerXml = getDesignerXml(child.getId(), newProcessId, xml, WorkflowStatusType.REGISTERED.toString());
            String workflowXml = transformer.convertUengineBpmnXml(transformer.createBpmnXML(bpmnModel));

            Map<String, Object> saved = save(WorkflowStatusType.CREATED,
                    newProcessId, bpmnModel.getMainProcess().getName(),
                    designerXml, workflowXml, variable, child.getId(),
                    getSteps(bpmnModel.getMainProcess().getFlowElements()), username);

            logger.info("The process has been saved : {}", saved);

            return saved;
        } catch (Exception ex) {
            throw new ServiceException("You can not save a new workflow", ex);
        }
    }

    @Override
    public Map<String, Object> saveAsUpdate(String treeId, String processId, String xml, String username) {
        try {
            Map<String, Object> variable = new HashMap<>();
            Map<String, Map<String, Object>> localVariables = transformer.getLocalVariables(xml);
            Map<String, Object> globalVariables = transformer.getGlobalVariables(xml);
            List parallelVectors = transformer.getParallelVectors(xml);
            variable.put("local", localVariables);
            variable.put("global", globalVariables);
            variable.put("parallelVectors", parallelVectors);

            //  BPMN 모델을 생성한다.
            BpmnModel model = transformer.unmarshall(xml, processId);
            Tree tree = treeService.get(Long.parseLong(treeId));
            tree.setName(model.getMainProcess().getName());
            treeService.rename(tree);
            String workflowXml = transformer.convertUengineBpmnXml(transformer.createBpmnXML(model));
            String designerXml = getDesignerXml(tree.getId(), processId, xml, WorkflowStatusType.REGISTERED.toString());

            Map<String, Object> saved = save(WorkflowStatusType.REGISTERED,
                    processId, model.getMainProcess().getName(), designerXml,
                    workflowXml, variable, tree.getId(),
                    getSteps(model.getMainProcess().getFlowElements()), username);

            logger.info("The process has been saved : {}", saved);

            return saved;
        } catch (Exception ex) {
            throw new ServiceException("You can not update a workflow", ex);
        }
    }

    @Override
    public Map<String, Object> copy(String parentTreeId, Workflow workflow, String username) {
        try {
            // 신규 프로세스이므로 새로운 Process ID를 생성한다.
            String newProcessId = getCurrentDateTime() + "_" + generateUUID();
            String xml = workflow.getDesignerXml();

            //  BPMN 모델을 생성한다.
            BpmnModel model = transformer.unmarshall(xml, newProcessId);
            String processName = model.getMainProcess().getName();
            Map<String, Map<String, Object>> localVariables = transformer.getLocalVariables(xml);
            Map<String, Object> globalVariables = transformer.getGlobalVariables(xml);
            List parallelVectors = transformer.getParallelVectors(xml);

            String bpmnXML = transformer.convertUengineBpmnXml(transformer.createBpmnXML(model));

            Map<String, Object> vars = new HashMap<>();
            vars.put("local", localVariables);
            vars.put("global", globalVariables);
            vars.put("parallelVectors", parallelVectors);

            logger.info("The process has been saved. Process ID = {}, Process Name = {}", newProcessId, model.getMainProcess().getName());

            // 트리 노드를 생성한다.
            Tree parent;
            if ("/".equals(parentTreeId)) {
                parent = treeService.getRoot(TreeType.WORKFLOW, username);
            } else {
                parent = treeService.get(Long.parseLong(parentTreeId));
            }

            Tree tree = new Tree();
            tree.setName(workflow.getWorkflowName() + "_Copied");
            tree.setTreeType(TreeType.WORKFLOW);
            tree.setNodeType(NodeType.ITEM);
            tree.setUsername(username);
            Tree child = treeService.create(parent, tree, NodeType.ITEM);

            // 프로세스 정보를 기록한다.
            String designerXml = getDesignerXml(child.getId(), newProcessId, xml, WorkflowStatusType.REGISTERED.toString());
            Map<String, Object> saved = save(WorkflowStatusType.COPIED, newProcessId, processName + "_Copied",
                    designerXml, bpmnXML, vars, child.getId(),
                    getSteps(model.getMainProcess().getFlowElements()), username);

            logger.info("The process has been saved : {}", saved);

            return saved;
        } catch (Exception ex) {
            throw new ServiceException("You can not copy a workflow", ex);
        }
    }

    @Override
    public boolean rename(Map map) {
        return workflowRepository.rename(map);
    }

    private String getDesignerXml(long treeId, String processId, String xml, String status) throws JAXBException, IOException {
        Opengraph opengraph = (Opengraph) JaxbUtils.unmarshal("org.opencloudengine.flamingo2.model.opengraph", xml);
        Map map = JsonUtils.unmarshal(unescape(opengraph.getData()));
        Map<String, Object> workflow = (Map<String, Object>) map.get("workflow");

        workflow.put("process_id", processId);
        workflow.put("status", status);
        workflow.put("tree_id", treeId);

        opengraph.setData(escape(format(map)));

        return JaxbUtils.marshal("org.opencloudengine.flamingo2.model.opengraph", opengraph);
    }

    private long getSteps(Collection<FlowElement> flowElements) {
        int count = 0;
        for (FlowElement element : flowElements) {
            switch (element.getClass().getName()) {
                case "org.activiti.bpmn.model.ServiceTask":
                case "org.activiti.bpmn.model.SubProcess":
                case "org.activiti.bpmn.model.UserTask":
                case "org.activiti.bpmn.model.BusinessRuleTask":
                    count++;
                    break;
                default:
                    break;
            }
        }
        return count;
    }

    @Override
    public void delete(long treeId) {
        treeService.delete(treeId);
        workflowRepository.deleteByTreeId(treeId);
    }

    @Override
    public Workflow getByTreeId(long treeId) {
        return workflowRepository.selectByTreeId(treeId);
    }

    @Override
    public Workflow getByWorkflowId(String workflowId) {
        return workflowRepository.selectByWorkflowId(workflowId);
    }
}