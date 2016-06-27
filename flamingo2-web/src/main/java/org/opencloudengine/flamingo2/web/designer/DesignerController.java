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

/**
 * Designer REST Controller
 *
 * @author Jae Hee, Lee
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@RestController
@RequestMapping("/designer")
public class DesignerController extends DefaultController {

    /**
     * 워크플로우를 실행한다.
     *
     * @param params 노드 생성을 위한 Key Value
     * @return UI에서 트랜잭션 추적을 위한 정보를 반환한다.
     */
    @RequestMapping("/run")
    public Response run(@RequestBody Map<String, Object> params) {
        EngineService engineService = this.getEngineService((String) params.get("clusterName"));

        UserEvent userEvent = UserEvent.create("워크플로우 '" + params.get("name") + "'을 실행중입니다.", "RUNNING");
        params.put("event", userEvent);

        User user = SessionUtils.get();
        params.put("user", user);

        Long treeId = Long.parseLong(String.valueOf(params.get("treeId")));
        Workflow workflow = engineService.getDesignerRemoteService().getWorkflow(treeId);
        params.put("workflow", workflow);

        params.put("executeFrom", "designer");

        SchedulerRemoteService service = engineService.getSchedulerRemoteService();

        try {
            service.prepareRun(params);
        } catch (Exception ex) {
            throw new ServiceException("Unable to prepare a workflow.", ex);
        }

        try {
            service.runImmediatly(params);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get a workflow", ex);
        }

        Response response = new Response();
        response.getMap().put("name", params.get("name"));
        response.getMap().put("identifier", userEvent.getIdentifier());
        response.setSuccess(true);
        return response;
    }

    /**
     * 워크플로우 히스토리를 확인한다.
     *
     * @param params 히스토리 확인을 위한 Key Value
     * @return 워크플로우 히스토리를 반환한다.
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response status(@RequestBody Map params) {
        EngineService engineService = this.getEngineService((String) params.get("clusterName"));

        WorkflowHistory workflowHistory;

        try {
            WorkflowHistoryRemoteService workflowHistoryRemoteService = engineService.getWorkflowHistoryRemoteService();
            workflowHistory = workflowHistoryRemoteService.getWorkflowHistory(params.get("jobId").toString());
        } catch (Exception ex) {
            throw new ServiceException("Unable to get a workflow history.", ex);
        }

        Response response = new Response();
        response.setObject(workflowHistory);
        response.setSuccess(true);
        return response;
    }

    /**
     * Designer xml을 확인한다.
     *
     * @param clusterName 클러스터 이름
     * @param treeId      tree 아이디
     * @return Designer xml을 반환한다.
     */
    @RequestMapping(value = "/load", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response load(@RequestParam String clusterName, @RequestParam Long treeId) {
        EngineService engineService = this.getEngineService(clusterName);

        String designerXml;

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            designerXml = designerRemoteService.loadDesignerXml(treeId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to load a designer xml.", ex);
        }

        Response response = new Response();
        response.setObject(designerXml);
        response.setSuccess(true);
        return response;
    }

    /**
     * 워크플로우를 저장한다.
     *
     * @param clusterName  클러스터 이름
     * @param processId    process 아이디
     * @param treeId       tree 아이디
     * @param parentTreeId 부모 Tree 아이디
     * @return 저장한 워크플로우 정보를 반환한다.
     */
    @RequestMapping("/save")
    public Response save(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "") String processId,
                         @RequestParam(defaultValue = "") String treeId,
                         @RequestParam(defaultValue = "") String parentTreeId,
                         @RequestBody String xml) {
        EngineService engineService = this.getEngineService(clusterName);

        Map saved;

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            saved = designerRemoteService.save(parentTreeId, treeId, processId, xml, SessionUtils.getUsername());
        } catch (Exception ex) {
            throw new ServiceException("Unable to save a workflow.", ex);
        }

        Response response = new Response();
        response.getMap().put("id", saved.get("id"));
        response.getMap().put("process_id", saved.get("workflowId"));
        response.getMap().put("process_definition_id", saved.get("definitionId"));
        response.getMap().put("deployment_id", saved.get("deploymentId"));
        response.getMap().put("tree_id", saved.get("treeId"));
        response.getMap().put("status", saved.get("status"));
        response.setSuccess(true);
        return response;
    }

    /**
     * 워크플로우를 저장한다.
     *
     * @param clusterName 클러스터 이름
     * @param treeId      tree 아이디
     * @return bpmn xml을 반환한다.
     */
    @RequestMapping("/show")
    public Response show(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "-1") Long treeId) {
        EngineService engineService = this.getEngineService(clusterName);

        String bpmnXml;

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            bpmnXml = designerRemoteService.loadBpmnXml(treeId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to save a workflow.", ex);
        }

        Response response = new Response();
        response.setObject(bpmnXml);
        response.setSuccess(true);
        return response;
    }

    /**
     * 워크플로우를 복사한다.
     *
     * @param map 복사를 위한 Key Value
     * @return 복사한 워크플로우 정보를 반환한다.
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public Response copy(@RequestBody Map<String, Object> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName").toString());

        Map<String, Object> copy;

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            copy = designerRemoteService.copy(map, SessionUtils.getUsername());
        } catch (Exception ex) {
            throw new ServiceException("Unable to copy a workflow.", ex);
        }

        Response response = new Response();
        response.setMap(copy);
        response.setSuccess(true);
        return response;
    }

    /**
     * 새로운 노드를 생성한다.
     *
     * @param map 노드 생성을 위한 Key Value
     * @return Response REST JAXB Object
     */
    @RequestMapping(value = "new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response create(@RequestBody Map<String, String> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName"));

        TreeService treeService = engineService.getTreeRemoteService();

        TreeType treeType = TreeType.valueOf(map.get("treeType").toUpperCase());
        NodeType nodeType = NodeType.valueOf(map.get("nodeType").toUpperCase());

        Tree parent;
        Tree child;
        Tree tree;

        try {
            parent = "/".equals(map.get("id")) ? treeService.getRoot(treeType, SessionUtils.getUsername()) : treeService.get(Long.parseLong(map.get("id")));
        } catch (Exception ex) {
            throw new ServiceException("Unable to get a parent workflow.", ex);
        }

        try {
            child = new Tree();
            child.setName(map.get("name"));
            child.setTreeType(treeType);
            child.setNodeType(nodeType);
            child.setRoot(false);
            child.setUsername(SessionUtils.getUsername());
            child.setParent(parent);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a child workflow.", ex);
        }

        try {
            tree = treeService.create(parent, child, nodeType);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a workflow.", ex);
        }

        Response response = new Response();
        response.getMap().put("id", tree.getId());
        response.getMap().put("text", tree.getName());
        response.getMap().put("cls", "folder");
        response.getMap().put("leaf", false);
        response.setSuccess(true);
        return response;
    }

    /**
     * 워크플로우를 삭제한다.
     *
     * @param map 노드 생성을 위한 Key Value
     * @return Response REST JAXB Object
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@RequestBody Map map) {
        EngineService engineService = this.getEngineService(map.get("clusterName").toString());

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            designerRemoteService.delete(map);
        } catch (Exception ex) {
            throw new ServiceException("Unable to delete a workflow.", ex);
        }

        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    /**
     * 현재 노드명을 변경한다. 노드명을 변경하기 위해 다음의 값이 필요하다.
     * <ul>
     * <li>클러스터 이름</li>
     * <li>이름을 변경할 노드의 ID</li>
     * <li>변경할 노드명</li>
     * </ul>
     *
     * @param map 노드 이름 변경을 위한 Key Value
     * @return Response REST JAXB Object
     */
    @RequestMapping(value = "rename", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response rename(@RequestBody Map<String, String> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName"));

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            designerRemoteService.rename(Long.parseLong(map.get("id")), map.get("name"));
        } catch (Exception ex) {
            throw new ServiceException("Unable to rename a workflow.", ex);
        }

        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    /**
     * 워크플로우를 가져온다.
     *
     * @param clusterName 클러스터 이름
     * @param treeId      tree 아이디
     * @return 워크플로우를 반환한다.
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response get(@RequestParam(defaultValue = "") String clusterName,
                        @RequestParam(defaultValue = "-1") Long treeId) {
        EngineService engineService = this.getEngineService(clusterName);

        Map describe;

        try {
            DesignerService designerRemoteService = engineService.getDesignerRemoteService();
            describe = BeanUtils.describe(designerRemoteService.getWorkflow(treeId));
        } catch (Exception e) {
            throw new ServiceException("Unable to get a workflow", e);
        }

        Response response = new Response();
        response.setMap(describe);
        response.setSuccess(true);
        return response;
    }
}
