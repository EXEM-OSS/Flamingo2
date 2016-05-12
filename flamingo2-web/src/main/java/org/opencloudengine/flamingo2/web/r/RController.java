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
package org.opencloudengine.flamingo2.web.r;

import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.web.security.AESPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * R 및 RStudio 관련 UI 기능을 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/r")
public class RController {

    @Autowired
    @Qualifier("passwordEncoder")
    AESPasswordEncoder passwordEncoder;
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(RController.class);

    /**
     * RStudio 서버에 연결을 위한 Payload 정보를 암호화하여 전달한다.
     */
    @RequestMapping(value = "autoLogin", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ModelAndView autoLogin(HttpSession session) {
        ModelAndView mav;
        try {
            mav = new ModelAndView("rstudio");
            User user = (User) session.getAttribute("user");
            mav.addObject("u", user.getUsername());
            mav.addObject("p", user.getPassword());
        } catch (Exception ex) {
            mav = new ModelAndView("error-500");
        }
        return mav;
    }
}
