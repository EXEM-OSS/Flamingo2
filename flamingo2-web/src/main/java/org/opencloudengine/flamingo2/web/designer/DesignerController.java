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
package org.opencloudengine.flamingo2.web.designer;

import org.apache.commons.beanutils.BeanUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.backend.UserEvent;
import org.opencloudengine.flamingo2.engine.designer.DesignerService;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.scheduler.SchedulerRemoteService;
import org.opencloudengine.flamingo2.engine.tree.TreeService;
import org.opencloudengine.flamingo2.model.rest.*;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/designer")
public class DesignerController extends DefaultController {

    @RequestMapping("/run")
    public Response run(@RequestBody Map<String, Object> params) {
        EngineService engineService = this.getEngineService((String) params.get("clusterName"));

        UserEvent userEvent = UserEvent.create("워크플로우 '" + params.get("name") + "'을 실행중입니다.", "RUNNING");
        params.put("event", userEvent);
        User user = SessionUtils.get();
        params.put("user", user);

        Long treeId = Long.parseLong((String) params.get("treeId"));
        Workflow workflow = engineService.getDesignerRemoteService().getWorkflow(treeId);
        params.put("workflow", workflow);

        // Scheduler로 Job실행을 요청한다.
        SchedulerRemoteService service = engineService.getSchedulerRemoteService();
        boolean prepareRun = service.prepareRun(params);
        if (prepareRun) {
            service.runImmediatly(params);
        }

        // UI에서 트랜잭션 추적을 위한 정보를 반환한다.
        Response response = new Response();
        response.getMap().put("name", params.get("name"));
        response.getMap().put("identifier", userEvent.getIdentifier());
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response status(@RequestBody Map params) {
        EngineService engineService = this.getEngineService((String) params.get("clusterName"));
        WorkflowHistoryRemoteService workflowHistoryRemoteService = engineService.getWorkflowHistoryRemoteService();

        Response response = new Response();
        WorkflowHistory workflowHistory = workflowHistoryRemoteService.getWorkflowHistory(params.get("jobId").toString());
        if (workflowHistory != null) {
            response.setObject(workflowHistory);
        }
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response load(@RequestParam String clusterName, @RequestParam Long treeId) {
        EngineService engineService = this.getEngineService(clusterName);
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();

        Response response = new Response();
        String designerXml = designerRemoteService.loadDesignerXml(treeId);
        response.setObject(designerXml);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping("/save")
    public Response save(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "") String processId,
                         @RequestParam(defaultValue = "") String treeId,
                         @RequestParam(defaultValue = "") String parentTreeId,
                         @RequestBody String xml) {
        EngineService engineService = this.getEngineService(clusterName);
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();
        Response response = new Response();

        Map saved = designerRemoteService.save(parentTreeId, treeId, processId, xml, SessionUtils.getUsername());
        response.getMap().put("id", saved.get("id"));
        response.getMap().put("process_id", saved.get("workflowId"));
        response.getMap().put("process_definition_id", saved.get("definitionId"));
        response.getMap().put("deployment_id", saved.get("deploymentId"));
        response.getMap().put("tree_id", saved.get("treeId"));
        response.getMap().put("status", saved.get("status"));
        response.setSuccess(true);
        return response;
    }

    @RequestMapping("/show")
    public Response show(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "-1") Long treeId) {

        EngineService engineService = this.getEngineService(clusterName);
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();
        Response response = new Response();
        response.setObject(designerRemoteService.loadBpmnXml(treeId));
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public Response copy(@RequestBody Map<String, Object> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName").toString());
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();

        Response response = new Response();
        response.setMap(designerRemoteService.copy(map, SessionUtils.getUsername()));
        response.setSuccess(true);
        return response;
    }

    /**
     * 새로운 노드를 생성한다. 노드를 생성하기 위해서 필요한 것은 다음과 같다.
     * <ul>
     * <li>부모 노드의 ID</li>
     * <li>생성할 Tree의 유형(예; <tt>JOB, WORKFLOW</tt>)</li>
     * <li>노드의 유형(예; <tt>FOLDER, ITEM</tt>)(</li>
     * <li>노드의 이름(</li>
     * <li>ROOT 노드 여부(기본값은 <tt>false</tt>)(</li>
     * </ul>
     *
     * @param map 노드 생성을 위한 Key Value
     * @return Response REST JAXB Object
     */
    @RequestMapping(value = "new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response newDirectory(@RequestBody Map<String, String> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName"));
        TreeService treeService = engineService.getTreeRemoteService();

        Response response = new Response();
        TreeType treeType = TreeType.valueOf(map.get("treeType").toUpperCase());
        NodeType nodeType = NodeType.valueOf(map.get("nodeType").toUpperCase());

        Tree parent;

        if ("/".equals(map.get("id"))) {
            // ROOT 노드라면 Tree Type의 ROOT 노드를 부모 노드로 설정한다.
            parent = treeService.getRoot(treeType, SessionUtils.getUsername());
        } else {
            // 새로운 노드를 추가하기 위해서 부모 노드를 먼저 알아낸다.
            long parentId = Long.parseLong(map.get("id"));
            parent = treeService.get(parentId);
        }

        Tree child = new Tree();
        child.setName(map.get("name"));
        child.setTreeType(treeType);
        child.setNodeType(nodeType);
        child.setRoot(false);
        child.setUsername(SessionUtils.getUsername());
        child.setParent(parent);

        // 부모 노드에 속한 자식 노드를 생성하고 그 결과를 구성한다.
        Tree tree = treeService.create(parent, child, nodeType);
        response.getMap().put("id", tree.getId());
        response.getMap().put("text", tree.getName());
        response.getMap().put("cls", "folder");
        response.getMap().put("leaf", false);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@RequestBody Map map) {
        EngineService engineService = this.getEngineService(map.get("clusterName").toString());
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();

        Response response = new Response();
        designerRemoteService.delete(map);
        response.setSuccess(true);
        return response;
    }

    /**
     * 현재 노드명을 변경한다. 노드명을 변경하기 위해 다음의 값이 필요하다.
     * <ul>
     * <li>이름을 변경할 노드의 ID</li>
     * <li>변경할 노드명</li>
     * </ul>
     *
     * @param map 노드 생성을 위한 Key Value
     * @return Response REST JAXB Object
     */
    @RequestMapping(value = "rename", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response renameDirectory(@RequestBody Map<String, String> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName"));
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();

        Response response = new Response();
        response.setSuccess(designerRemoteService.rename(Long.parseLong(map.get("id")), map.get("name")));
        return response;
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response get(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "-1") Long treeId) {
        EngineService engineService = this.getEngineService(clusterName);
        DesignerService designerRemoteService = engineService.getDesignerRemoteService();

        Response response = new Response();
        Workflow workflow = designerRemoteService.getWorkflow(treeId);
        try {
            response.setMap(BeanUtils.describe(workflow));
            response.setSuccess(true);
        } catch (Exception e) {
            throw new ServiceException("Unable to get a workflow", e);
        }
        return response;
    }
}
