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
package org.opencloudengine.flamingo2.engine.designer;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import org.opencloudengine.flamingo2.engine.designer.activiti.WorkflowService;
import org.opencloudengine.flamingo2.engine.tree.TreeService;
import org.opencloudengine.flamingo2.model.rest.NodeType;
import org.opencloudengine.flamingo2.model.rest.Tree;
import org.opencloudengine.flamingo2.model.rest.TreeType;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DesignerServiceImpl implements DesignerService {

    /**
     * SLF4J Logging
     */
    private Logger exceptionLogger = LoggerFactory.getLogger("flamingo.exception");

    @Override
    public void delete(Map map) {
        TreeService treeService = ApplicationContextRegistry.getApplicationContext().getBean(TreeService.class);
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        if (NodeType.valueOf(((String) map.get("nodeType")).toUpperCase()) == NodeType.ITEM) {
            workflowService.delete(Long.parseLong((String) map.get("id")));
        }

        Tree tree = new Tree(Long.parseLong((String) map.get("id")));
        tree.setTreeType(TreeType.valueOf(((String) map.get("treeType")).toUpperCase()));
        treeService.delete(tree.getId());
    }

    @Override
    public String loadDesignerXml(Long treeId) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        Workflow workflow = workflowService.getByTreeId(treeId);
        return workflow.getDesignerXml();
    }

    @Override
    public String loadBpmnXml(Long treeId) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        Workflow workflow = workflowService.getByTreeId(treeId);
        return workflow.getWorkflowXml();
    }

    @Override
    public Workflow getWorkflow(Long treeId) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        return workflowService.getByTreeId(treeId);
    }

    @Override
    public Workflow getWorkflow(String workflowId) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        return workflowService.getByWorkflowId(workflowId);
    }

    @Override
    public Map<String, Object> save(String parentTreeId, String treeId, String processId, String xml, String username) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        Map<String, Object> saved;
        if (StringUtils.isEmpty(treeId)) {
            saved = workflowService.saveAsNew(parentTreeId, xml, username);
        } else {
            saved = workflowService.saveAsUpdate(treeId, processId, xml, username);
        }

        return saved;
    }

    @Override
    public Map<String, Object> copy(Map<String, Object> map, String username) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        String parentTreeId = String.valueOf(map.get("parentTreeId"));
        long treeId = Long.parseLong(String.valueOf(map.get("treeId")));
        Workflow workflow = getWorkflow(treeId);
        return workflowService.copy(parentTreeId, workflow, username);
    }

    @Override
    public String[] idList(String working, final String prefix) {
        return new File(working).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix);
            }
        });
    }

    @Override
    public void killProccess(String logDirectory) {
        Expect expect = null;
        try {
            Process process = Runtime.getRuntime().exec("/bin/sh");
            expect = new ExpectBuilder()
                    .withInputs(process.getInputStream())
                    .withOutput(process.getOutputStream())
                    .withTimeout(1, TimeUnit.SECONDS)
                    .withExceptionOnFailure()
                    .build();
            expect.sendLine("kill -9 `cat \"" + logDirectory + "/PID\"`");
            expect.close();
        } catch (Exception e) {
            exceptionLogger.warn("The process can not be killed", e);
        }
    }

    @Override
    public boolean rename(long treeId, String workflowName) {
        WorkflowService workflowService = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowService.class);
        TreeService treeService = ApplicationContextRegistry.getApplicationContext().getBean(TreeService.class);
        Map map = new HashMap();
        map.put("treeId", treeId);
        map.put("workflowName", workflowName);

        Tree tree = treeService.get(treeId);
        tree.setName(workflowName);
        return workflowService.rename(map) && treeService.rename(tree) > 0;
    }
}
