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
package org.opencloudengine.flamingo2.web;

import org.opencloudengine.flamingo2.model.rest.Organization;
import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.opencloudengine.flamingo2.web.security.AESPasswordEncoder;
import org.opencloudengine.flamingo2.web.system.LicenseService;
import org.opencloudengine.flamingo2.web.system.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    @Qualifier("passwordEncoder")
    private AESPasswordEncoder passwordEncoder;

    @Value("#{config['html.nocache']}")
    private boolean isHtmlNoCache;

    @Value("#{config['license.filename']}")
    private String licenseFilename;

    /**
     * 인덱스 페이지로 이동한다.
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView index() {
        return main();
    }

    /**
     * 인덱스 페이지로 이동한다.
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView main() {
        ModelAndView mav;
        if (licenseService.isValid(licenseFilename)) {
            mav = new ModelAndView("/index");
            mav.addObject("mode", ConfigurationHelper.getHelper().get("application.mode", "development"));
            mav.addObject("nocache", isHtmlNoCache);

            Map licenseMap = licenseService.getLicenseInfo();
            mav.addObject("expireDate", licenseMap.get("expireDate"));
            mav.addObject("isExpired", licenseMap.get("isExpired"));
            mav.addObject("days", licenseMap.get("days"));
            return mav;
        }

        mav = new ModelAndView("/license");
        mav.addObject("serverId", licenseService.getServerId());
        return mav;
    }

    /**
     * 메인 페이지로 이동한다.
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(HttpSession session) throws IOException {
        ModelAndView mav;

        if (licenseService.isValid(licenseFilename)) {
            org.springframework.security.core.userdetails.User user
                    = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("{} user has logged in.", user.getUsername());

            // 로그인한 사용자 정보를 찾아서 패스워드를 decryption한다.
            User managedUser = userService.getUser(user.getUsername());
            logger.info("{} user has logged in.", managedUser);

            managedUser.setPassword(passwordEncoder.decode(managedUser.getPassword()));

            Organization org = userService.getOrganization(managedUser.getOrgId());
            session.setAttribute("user", managedUser);
            session.setAttribute("organization", org);

            mav = new ModelAndView("/main");
            mav.addObject("username", managedUser.getUsername());
            mav.addObject("name", managedUser.getName());
            mav.addObject("email", managedUser.getEmail());
            mav.addObject("userGroup", managedUser.getUserGroup());
            mav.addObject("id", managedUser.getId());
            mav.addObject("title", ConfigurationHelper.getHelper().get("application.title"));
            mav.addObject("mode", ConfigurationHelper.getHelper().get("application.mode", "development"));
            mav.addObject("nocache", isHtmlNoCache);

            Map licenseMap = licenseService.getLicenseInfo();
            mav.addObject("isTrial", licenseService.isTrial());
            mav.addObject("expireDate", licenseMap.get("expireDate"));
            mav.addObject("isExpired", licenseMap.get("isExpired"));
            mav.addObject("days", licenseMap.get("days"));
            return mav;
        }

        mav = new ModelAndView("/license");
        mav.addObject("serverId", licenseService.getServerId());
        return mav;
    }

    /**
     * Intro
     */
    @RequestMapping(value = "/intro", method = RequestMethod.GET)
    public ModelAndView redirect() {
        Properties config = ApplicationContextRegistry.getApplicationContext().getBean("config", Properties.class);
        String introUrl = config.getProperty("web.start.additional.page");
        return new ModelAndView(introUrl);
    }

    /**
     * License
     */
    @RequestMapping(value = "/license", method = RequestMethod.GET)
    public ModelAndView license() {

        ModelAndView mav = new ModelAndView("/license");
        mav.addObject("serverId", licenseService.getServerId());
        return mav;
    }

    /**
     * 로그인 오류 페이지
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView mav;
        if (licenseService.isValid(licenseFilename)) {
            mav = new ModelAndView("/index");

            Map licenseMap = licenseService.getLicenseInfo();
            mav.addObject("SUCCESS", false);
            mav.addObject("expireDate", licenseMap.get("expireDate"));
            mav.addObject("isExpired", licenseMap.get("isExpired"));
            mav.addObject("days", licenseMap.get("days"));
            return mav;
        }

        mav = new ModelAndView("/license");
        mav.addObject("serverId", licenseService.getServerId());
        return mav;
    }

    /**
     * 세션 종료 페이지
     */
    @RequestMapping(value = "/expired", method = RequestMethod.GET)
    public ModelAndView expired() {
        return new ModelAndView("redirect:/index");
    }
}
