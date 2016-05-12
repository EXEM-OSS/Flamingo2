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
package org.opencloudengine.flamingo2.web.tree;

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.tree.TreeService;
import org.opencloudengine.flamingo2.model.rest.NodeType;
import org.opencloudengine.flamingo2.model.rest.Tree;
import org.opencloudengine.flamingo2.model.rest.TreeType;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Category를 지원하는 Tree의 공통 기능을 제공하는 Controller.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
@Controller
@RequestMapping("/tree")
public class TreeController extends DefaultController implements InitializingBean {

    /**
     * ROOT 노드의 ID
     */
    private final static String ROOT = "/";

    /**
     * 최초 애플리케이션이 초기화할 때 JOB, WORKFLOW 등과 같은 TREE를 추가한다.
     *
     * @throws Exception Tree를 초기화할 수 없는 경우
     */
    @Override
    public void afterPropertiesSet() throws Exception {
    }

    /**
     * 지정한 트리 유형의 특정 노드에 속한 자식 노드 목록을 반환한다.
     *
     * @param type 노드 유형
     * @param node 자식 노드를 탐색할 노드
     * @return HTTP REST Response
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response get(@RequestParam String clusterName, @RequestParam String type, @RequestParam String node) {
        EngineService engineService = this.getEngineService(clusterName);
        TreeService treeService = engineService.getTreeRemoteService();

        Response response = new Response();
        Tree parent;
        if (ROOT.equals(node)) {
            // ROOT 노드라면 Tree Type의 ROOT 노드를 부모 노드로 설정한다.
            parent = treeService.getRoot(TreeType.valueOf(type.trim()), SessionUtils.getUsername());
        } else {
            // ROOT 노드가 아니라면 PK인 Tree Id를 부모 노드로 설정한다.
            parent = treeService.get(Long.parseLong(node));
        }

        // 부모 노드의 자식 노드를 조회한다.
        List<Tree> childs = treeService.getChilds(parent.getId());
        for (Tree tree : childs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", tree.getId());
            map.put("cls", NodeType.FOLDER.equals(tree.getNodeType()) ? "folder" : "file");
            map.put("text", tree.getName());
            map.put("leaf", !NodeType.FOLDER.equals(tree.getNodeType()));
            response.getList().add(map);
        }
        response.setSuccess(true);
        return response;
    }

    /**
     * 노드를 이동한다.
     * <ul>
     * <li>from - 이동할 노드(노드)</li>
     * <li>to - 최종 목적지 노드(폴더)</li>
     * </ul>
     *
     * @param map from, to를 포함하는 노드
     * @return Response REST JAXB Object
     */
    @RequestMapping(value = "move", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response move(@RequestBody Map<String, String> map) {
        EngineService engineService = this.getEngineService(map.get("clusterName"));
        TreeService treeService = engineService.getTreeRemoteService();

        Response response = new Response();
        treeService.move(map.get("from"), map.get("to"), TreeType.valueOf(map.get("type")));
        response.setSuccess(true);
        return response;
    }
}
