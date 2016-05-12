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
package org.opencloudengine.flamingo2.web.system;

import org.apache.commons.lang.SystemUtils;
import org.opencloudengine.flamingo2.agent.system.SystemUserService;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.fs.FileSystemRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.security.AESPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Management REST Controller
 *
 * @author Myeong Ha, Kim
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@RestController
@RequestMapping("/system/user")
public class UserController extends DefaultController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileSystemRemoteService fileSystemRemoteService;

    @Autowired
    private HdfsBrowserAuthService hdfsBrowserAuthService;

    @Autowired
    @Qualifier("passwordEncoder")
    private AESPasswordEncoder passwordEncoder;

    @Value("#{config['user.system.agent.apply']}")
    private boolean systemAgentApply;

    @Value("#{config['user.home.linux.path']}")
    private String linuxUserHome;

    @Value("#{config['user.home.hdfs.path']}")
    private String hdfsUserHome;

    @Value("#{config['user.home.hdfs.path']}")
    private String hadoopUserHome;

    /**
     * 사용자를 승인한다. 승인이 이루어지면 서버에 설정되어 있는 System Agent를 통해서 Linux 사용자 계정을 생성한다.
     *
     * @param userMap 사용자 정보
     */
    @RequestMapping(value = "acknowledge", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response acknowledge(@RequestBody Map<String, String> userMap) {
        EngineService engineService = this.getEngineService(userMap.get("clusterName"));
        EngineConfig engineConfig = this.getEngineConfig(userMap.get("clusterName"));
        SystemUserService systemUserService = engineService.getSystemUserService();
        fileSystemRemoteService = engineService.getFileSystemService();

        String name = userMap.get("name");
        String username = userMap.get("username");
        String password = userService.getUserPassword(username);
        userMap.put("linuxUserHome", linuxUserHome + SystemUtils.FILE_SEPARATOR + username);
        userMap.put("hdfsUserHome", hdfsUserHome + SystemUtils.FILE_SEPARATOR + username);
        boolean systemAgentResult = false;
        boolean updated = false;

        // Linux 사용자를 생성 후 비밀번호를 변경한다.
        if (systemAgentApply) {
            if (systemUserService.createUser(linuxUserHome, name, username)) {
                systemAgentResult = systemUserService.changeUser(username, password);
            }
        }

        // System Agent를 사용하지 않는 경우는 로컬 DB의 사용자 정보만 갱신한다.
        if (!systemAgentApply || systemAgentResult) {
            if (userService.acknowledge(username, passwordEncoder.encode(password))) {
                if (fileSystemRemoteService.createHdfsUserHome(engineConfig, hdfsUserHome, username)) {
                    if (userService.updateUserHomeInfo(userMap)) {
                        userMap.put("hdfsPathPattern", hadoopUserHome + SystemUtils.FILE_SEPARATOR + username + "/**");
                        userMap.put("ackKey", "approved");

                        updated = hdfsBrowserAuthService.createHdfsBrowserAuth(userMap);
                    }
                }
            }
        }

        Response response = new Response();
        response.setSuccess(updated);
        return response;
    }

    /**
     * 관리자가 직접 사용자를 생성한다. 회원가입시 사용자가 생성되고, 관리자가 사용자를 생성한다.
     * 생성시 데이터베이스에만 사용자 정보를 추가하고, 사용자 정보가 추가되는 경우 사용자는 기본으로
     * disabled 상태가 된다. 이때 acknowledge를 하게 되면 사용자가 활성화 되고
     * System Agent에서 사용자 생성을 요청한다. 그러면 시스템에는 Linux 사용자로 계정이 생성된다.
     *
     * @param userMap 생성할 사용자 정보
     */
    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response createUser(@RequestBody Map userMap) {
        String unescapedPassword = EscapeUtils.unescape((String) userMap.get("password"));
        userMap.put("password", unescapedPassword);

        boolean createdUser = userService.createUserByManager(userMap);

        Response response = new Response();
        response.setSuccess(createdUser);
        return response;
    }

    /**
     * 사용자의 패스워드를 변경한다. 사용자의 패스워드를 변경하면 리눅스 시스템의 사용자도 모두 패스워드가 변경된다.
     *
     * @param clusterName 클러스터정보
     * @param userMap     사용자 정보
     */
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response updatePassword(@RequestParam String clusterName, @RequestBody Map userMap) {
        EngineService engineService = this.getEngineService(clusterName);
        SystemUserService systemUserService = engineService.getSystemUserService();

        String username = (String) userMap.get("username");
        String newPassword = EscapeUtils.unescape((String) userMap.get("password"));
        boolean systemAgentResult = false;
        boolean updated = false;

        if (systemAgentApply) {
            systemAgentResult = systemUserService.changeUser(username, newPassword);
        }

        if (!systemAgentApply || systemAgentResult) {
            userMap.put("password", passwordEncoder.encode(newPassword));
            updated = userService.updatePassword(userMap);
        }

        Response response = new Response();
        response.setSuccess(updated);
        return response;
    }

    /**
     * 사용자를 삭제한다. 사용자를 삭제하면 리눅스 시스템의 사용자도 모두 삭제된다.
     *
     * @param userMap 사용자 정보
     */
    @RequestMapping(value = "deleteUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response deleteUser(@RequestBody Map userMap) {
        EngineService engineService = this.getEngineService((String) userMap.get("clusterName"));
        EngineConfig engineConfig = this.getEngineConfig((String) userMap.get("clusterName"));
        SystemUserService systemUserService = engineService.getSystemUserService();
        fileSystemRemoteService = engineService.getFileSystemService();

        String username = (String) userMap.get("username");
        String hdfsUserHome = (String) userMap.get("hdfsUserHome");
        boolean userStatus = (boolean) userMap.get("status");
        boolean deleted = false;
        userMap.put("deleteCondition", "deleteUser");
        userMap.put("hdfsPathPattern", hdfsUserHome + SystemUtils.FILE_SEPARATOR + "%");

        /**
         * 사용자 삭제 조건
         * 1. 시스템 에이전트 사용
         * 1.1 사용자 승인 완료 상태
         * 1.2 사용자 승인 대기 상태
         * 2. 시스템 에이전트 미사용
         * 2.1 사용자 승인 완료 상태
         * 2.2 사용자 승인 대기 상태
         */
        if (systemAgentApply) {
            if (userStatus) {
                if (userService.deleteUser(username)) {
                    if (systemUserService.deleteUser(username)) {
                        if (fileSystemRemoteService.deleteHdfsUserHome(engineConfig, hdfsUserHome)) {
                            deleted = hdfsBrowserAuthService.deleteHdfsBrowserAuth(userMap);
                        }
                    }
                }
            } else {
                if (userService.deleteUser(username)) {
                    deleted = systemUserService.deleteUser(username);
                }
            }
        } else {
            if (userStatus) {
                if (userService.deleteUser(username)) {
                    if (fileSystemRemoteService.deleteHdfsUserHome(engineConfig, hdfsUserHome)) {
                        deleted = hdfsBrowserAuthService.deleteHdfsBrowserAuth(userMap);
                    }
                }
            } else {
                if (userService.deleteUser(username)) {
                    deleted = systemUserService.deleteUser(username);
                }
            }
        }

        Response response = new Response();
        response.setSuccess(deleted);
        return response;
    }

    /**
     * 사용자 정보를 수정한다. 비밀번호 변경 시 리눅스 시스템 사용자의 비밀번호도 변경한다.
     *
     * @param userMap 사용자 정보
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response updateUserInfo(@RequestBody Map userMap) {
        EngineService engineService = this.getEngineService((String) userMap.get("clusterName"));
        SystemUserService systemUserService = engineService.getSystemUserService();

        String username = (String) userMap.get("username");
        String newPassword = EscapeUtils.unescape((String) userMap.get("password"));
        boolean systemAgentResult = false;
        boolean updatedUser = false;

        if (newPassword.isEmpty()) {
            updatedUser = userService.updateUserInfo(userMap);
        } else {
            if (systemAgentApply) {
                systemAgentResult = systemUserService.changeUser(username, newPassword);
            }
            if (!systemAgentApply || systemAgentResult) {
                userMap.put("password", passwordEncoder.encode(newPassword));
                updatedUser = userService.updateUserInfo(userMap);
            }
        }

        Response response = new Response();
        response.setSuccess(updatedUser);
        return response;
    }

    /**
     * 콤보 박스에서 선택한 소속에 포함된 사용자 전체 목록을 가져온다.
     *
     * @param orgId        소속 ID
     * @param conditionKey 조회 조건값
     * @param condition    조회값
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "userList", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response getUsers(@RequestParam(defaultValue = "0") long orgId,
                             @RequestParam(defaultValue = "") String conditionKey,
                             @RequestParam(defaultValue = "") String condition) {
        Response response = new Response();

        Map conditionMap = new HashMap();
        conditionMap.put("orgId", orgId);
        conditionMap.put("conditionKey", conditionKey);
        conditionMap.put("condition", condition);

        List<Map> users = userService.getUserAll(conditionMap);

        if (users != null) {
            response.getList().addAll(users);
            response.setTotal(response.getList().size());
        } else {
            response.setTotal(0);
        }

        response.setSuccess(true);
        return response;
    }

    /**
     * 조직 전체 목록을 가져온다.
     *
     * @param condition 조회 조건
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "organizationList", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response getOrganizationAll(@RequestParam String condition) { // FIXME > Map으로 변경 후 테스트
        Map defaultMap = new HashMap();
        List<Map> organization = new ArrayList<>();

        if (condition.equalsIgnoreCase("all")) {
            defaultMap.put("org_name", "ALL");
            organization.add(defaultMap);
        }

        organization.addAll(userService.getOrganizationAll());

        Response response = new Response();
        response.getList().addAll(organization);
        response.setTotal(response.getList().size());
        response.setSuccess(true);
        return response;
    }

    /**
     * 관리자가 소속 정보를 생성한다.
     *
     * @param organizationMap 생성할 소속 정보
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "createOrganization", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response createOrganization(@RequestBody Map organizationMap) {
        boolean createdOrganization = userService.createOrganization(organizationMap);

        Response response = new Response();
        response.setSuccess(createdOrganization);
        return response;
    }

    /**
     * 소속을 삭제한다.
     *
     * @param organizationMap 소속 정보
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "deleteOrganization", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response deleteOrganization(@RequestBody Map organizationMap) {
        boolean deletedOrganization = userService.deleteOrganization(Long.parseLong(String.valueOf(organizationMap.get("id"))));

        Response response = new Response();
        response.setSuccess(deletedOrganization);
        return response;
    }

    /**
     * 소속 정보를 수정한다.
     *
     * @param organizationMap 소속 정보
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "updateOrganizationInfo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response updateOrganizationInfo(@RequestBody Map organizationMap) {
        boolean updatedOrganization = userService.updateOrganizationInfo(organizationMap);

        Response response = new Response();
        response.setSuccess(updatedOrganization);
        return response;
    }
}