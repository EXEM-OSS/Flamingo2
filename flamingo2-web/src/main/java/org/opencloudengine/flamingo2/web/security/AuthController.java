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
package org.opencloudengine.flamingo2.web.security;

import org.opencloudengine.flamingo2.agent.system.SystemUserService;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.system.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Security Auth Controller
 *
 * @author Myeong Ha, Kim
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@Controller
@RequestMapping("/auth")
public class AuthController extends DefaultController {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("passwordEncoder")
    private AESPasswordEncoder passwordEncoder;

    @Value("#{config['user.system.agent.apply']}")
    private boolean systemAgentApply;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        logger.info("Delete the session and go to the login screen.");
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView("/index");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("/main");
    }

    /**
     * 사용자가 사용자 등록 정보를 입력하면 입력한 정보로 회원가입 처리한다.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@RequestParam String username,
                                 @RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword) {
        String unescapedPassword = EscapeUtils.unescape(password);

        if (password.equalsIgnoreCase(confirmPassword)) {
            Map userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("name", name);
            userMap.put("email", email);
//			userMap.put("password", passwordEncoder.encode(unescapedPassword));
            userMap.put("password", unescapedPassword); // 관리자 승인 후 리눅스 사용자 비번 변경 후 암호화처리
            userService.createUser(userMap);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/index");
        return mav;
    }

    /**
     * 로그인한 사용자의 패스워드를 변경한다.
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Response changePassword(@RequestBody Map userMap) {
        EngineService engineService = this.getEngineService((String) userMap.get("clusterName"));
        SystemUserService systemUserService = engineService.getSystemUserService();
        String username = SessionUtils.getUsername();
        String newPassword = EscapeUtils.unescape((String) userMap.get("password"));
        Map map = new HashMap();
        map.put("username", username);
        map.put("password", passwordEncoder.encode(newPassword));
        boolean systemAgentResult = false;
        boolean changed = false;

        if (systemAgentApply) {
            systemAgentResult = systemUserService.changeUser(username, newPassword);
        }

        if (!systemAgentApply || systemAgentResult) {
            changed = userService.updatePassword(map);
        }

        Response response = new Response();
        response.setSuccess(changed);
        return response;
    }
}
