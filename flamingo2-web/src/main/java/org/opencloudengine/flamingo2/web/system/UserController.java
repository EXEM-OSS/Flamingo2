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
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.fs.FileSystemRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.system.UserRemoteService;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.security.AESPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRemoteService userRemoteService;

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

    /**
     * 사용자를 승인한다.
     * 승인이 이루어지면 서버에 설정되어 있는 System Agent를 통해서 Linux 사용자 계정을 생성한다.
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
        userRemoteService = engineService.getUserRemoteService();
        fileSystemRemoteService = engineService.getFileSystemService();

        String name = userMap.get("name");
        String username = userMap.get("username");
        String password = userService.getUserPassword(username);
        boolean systemAgentResult = false;
        boolean updated = false;

        /**
         * 사용자 승인 조건
         *
         * Case 1. 시스템 에이전트 사용시
         * Case 1.1 시스템 유저 생성
         * Case 1.2 시스템 유저 비밀번호 변경
         * Case 1.3 로컬 사용자 DB 바밀번호 암호화 후 업데이트
         * Case 1.4 HDFS 유저 홈 디렉토리 생성
         * Case 1.5 시스템 및 HDFS 사용자 홈 디렉토리 경로 정보 업데이트
         * Case 1.6 승인된 사용자의 HDFS Browser 사용 권한 생성
         * Case 1.7 승인된 사용자의 Workflow Designer Tree 사용 권한 생성
         *
         * Case 2. 시스템 에이전트 미사용시
         * Case 2.1 로컬 사용자 DB 바밀번호 암호화 후 업데이트
         * Case 2.2 HDFS 유저 홈 디렉토리 생성
         * Case 2.3 시스템 및 HDFS 사용자 홈 디렉토리 경로 정보 업데이트
         * Case 2.4 승인된 사용자의 HDFS Browser 사용 권한 생성
         * Case 2.5 승인된 사용자의 Workflow Designer Tree 사용 권한 생성
         */
        if (systemAgentApply) {
            if (userRemoteService.createUser(linuxUserHome, name, username, password)) {
                systemAgentResult = userRemoteService.updatePassword(username, password);
            }
        }

        String linuxUserHomePath = linuxUserHome + SystemUtils.FILE_SEPARATOR + username;
        String hdfsUserHomePath = hdfsUserHome + SystemUtils.FILE_SEPARATOR + username;
        String hdfsPathPattern = hdfsUserHome + SystemUtils.FILE_SEPARATOR + username + "/**";

        /**
         * 사용자 비밀번호 암호화 및 HDFS 사용자 홈 디렉토리 생성, 사용자 승인, 워크플로우 디자이너 사용자 Tree 계정 추가
         *
         * Condition: systemAgentApply is false
         *
         * Case 1. 시스템 에이전트 미사용시
         * Case 1.1 로컬 사용자 DB 바밀번호 암호화 후 업데이트
         * Case 1.2 HDFS 유저 홈 디렉토리 생성
         * Case 1.3 로컬의 사용자 DB 업데이트(승인)
         * Case 1.4 로컬의 워크플로우 디자이너 DB에 사용자 Tree 경로 추가
         *
         * Condition: systemAgentResult is true
         *
         * Case 2. 시스템 에이전트를 통해 리눅스 사용자를 생성완료 했을 때
         * Case 2.1 로컬 사용자 DB 바밀번호 암호화 후 업데이트
         * Case 2.2 HDFS 유저 홈 디렉토리 생성
         * Case 2.3 로컬 사용자 DB 업데이트(승인)
         * Case 2.4 로컬의 워크플로우 디자이너 DB에 사용자 Tree 경로 추가
         */
        // System Agent를 사용하지 않는 경우는 로컬 DB의 사용자 정보만 갱신한다.
        if (!systemAgentApply || systemAgentResult) {
            if (userService.acknowledge(username, passwordEncoder.encode(password))) {
                if (fileSystemRemoteService.createHdfsUserHome(engineConfig, hdfsUserHome, username)) {
                    userMap.put("linuxUserHome", linuxUserHomePath);
                    userMap.put("hdfsUserHome", hdfsUserHomePath);
                    if (userService.updateUserHomeInfo(userMap)) {
                        userMap.put("hdfsPathPattern", hdfsPathPattern);
                        userMap.put("ackKey", "approved");
                        if (hdfsBrowserAuthService.createHdfsBrowserAuth(userMap)) {
                            updated = userService.createWorkflowDesignerUser(username);
                        }
                    }
                }
            }
        }

        Response response = new Response();
        response.setSuccess(updated);
        return response;
    }

    /**
     * 관리자가 직접 사용자를 생성한다.
     * 생성시 데이터베이스에만 사용자 정보를 추가하고, 추가된 사용자는 비활성화 상태가 된다.
     * 관리자가 승인을 한 후에 사용자가 활성화 되고 System Agent(값이 True일 때)에서 사용자 생성을 요청한다.
     * 해당 시스템에는 Linux 사용자로 계정이 생성된다.
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
     * 사용자의 패스워드를 변경한다.
     * 사용자의 패스워드를 변경하면 리눅스 시스템의 사용자도 모두 패스워드가 변경된다.
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
        userRemoteService = engineService.getUserRemoteService();

        String username = (String) userMap.get("username");
        String newPassword = EscapeUtils.unescape((String) userMap.get("newPassword"));
        boolean systemAgentResult = false;
        boolean updated = false;

        if (systemAgentApply) {
            systemAgentResult = userRemoteService.updatePassword(username, newPassword);
        }

        if (!systemAgentApply || systemAgentResult) {
            userMap.put("encodedNewPassword", passwordEncoder.encode(newPassword));
            updated = userService.updatePassword(userMap);
        }

        Response response = new Response();
        response.setSuccess(updated);
        return response;
    }

    /**
     * 사용자를 삭제한다.
     * 사용자를 삭제하면 리눅스 시스템의 사용자도 모두 삭제된다.
     *
     * @param userMap 사용자 정보
     */
    @RequestMapping(value = "deleteUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response deleteUser(@RequestBody Map userMap) {
        String clusterName = (String) userMap.get("clusterName");
        EngineService engineService = this.getEngineService(clusterName);
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        userRemoteService = engineService.getUserRemoteService();
        fileSystemRemoteService = engineService.getFileSystemService();

        String username = (String) userMap.get("username");
        String hdfsUserHome = (String) userMap.get("hdfsUserHome");
        boolean userStatus = (boolean) userMap.get("status");
        boolean deleted = false;
        userMap.put("deleteCondition", "deleteUser");
        userMap.put("hdfsPathPattern", hdfsUserHome);

        /**
         * 사용자 삭제 조건
         *
         * Case 1. 시스템 에이전트 사용
         * Case 1.1 사용자 승인 완료 상태
         * Case 1.1.1 삭제할 사용자가 존재할 경우
         *      >> 삭제 순서 : DB User -> Linux User -> HDFS User Home -> HDFS Authority -> Workflow Designer Tree User
         * Case 1.1.2 삭제할 사용자가 존재하지 않을 경우
         *      >> 삭제 순서 : DB User -> HDFS User Home -> HDFS Authority -> Workflow Designer Tree User
         * Case 1.2 사용자 승인 대기 상태
         *      >> 삭제 순서 : DB User -> Linux User
         * Case 2. 시스템 에이전트 미사용
         * Case 2.1 사용자 승인 완료 상태
         *      >> 삭제 순서 : DB User -> HDFS User Home -> HDFS Authority -> Workflow Designer Tree User
         * Case 2.2 사용자 승인 대기 상태
         *      >> 삭제 순서 : DB User -> Linux User
         */
        if (systemAgentApply) {
            if (userStatus) {
                if (userService.deleteUser(username)) {
                    if (userRemoteService.existUser(username)) {
                        if (userRemoteService.deleteUser(username)) {
                            if (fileSystemRemoteService.deleteHdfsUserHome(engineConfig, hdfsUserHome)) {
                                if (hdfsBrowserAuthService.deleteHdfsBrowserAuth(userMap)) {
                                    deleted = userService.deleteWorkflowDesignerUser(username);
                                }
                            }
                        }
                    } else {
                        logger.info("리눅스 사용자가 존재하지 않습니다.");
                        if (fileSystemRemoteService.deleteHdfsUserHome(engineConfig, hdfsUserHome)) {
                            if (hdfsBrowserAuthService.deleteHdfsBrowserAuth(userMap)) {
                                deleted = userService.deleteWorkflowDesignerUser(username);
                            }
                        }
                    }
                }
            } else {
                deleted = userService.deleteUser(username);
            }
        } else {
            if (userStatus) {
                if (userService.deleteUser(username)) {
                    if (fileSystemRemoteService.deleteHdfsUserHome(engineConfig, hdfsUserHome)) {
                        if (hdfsBrowserAuthService.deleteHdfsBrowserAuth(userMap)) {
                            deleted = userService.deleteWorkflowDesignerUser(username);
                        }
                    }
                }
            } else {
                deleted = userService.deleteUser(username);
            }
        }

        Response response = new Response();
        response.setSuccess(deleted);
        return response;
    }

    /**
     * 사용자 정보를 수정한다.
     * 비밀번호 변경 시 리눅스 시스템 사용자의 비밀번호도 변경한다.
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
        userRemoteService = engineService.getUserRemoteService();

        String username = (String) userMap.get("username");
        String newPassword = EscapeUtils.unescape((String) userMap.get("newPassword"));
        boolean systemAgentResult = false;
        boolean updatedUser = false;

        if (newPassword.isEmpty()) {
            updatedUser = userService.updateUserInfo(userMap);
        } else {
            if (systemAgentApply) {
                systemAgentResult = userRemoteService.updatePassword(username, newPassword);
            }
            if (!systemAgentApply || systemAgentResult) {
                userMap.put("encodedNewPassword", passwordEncoder.encode(newPassword));
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