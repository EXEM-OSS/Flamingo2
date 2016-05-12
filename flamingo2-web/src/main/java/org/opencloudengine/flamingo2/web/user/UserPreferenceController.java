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
package org.opencloudengine.flamingo2.web.user;

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.backend.UserEvent;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 사용자의 환경설정 정보에 대한 REST 요청을 처리하는 컨트롤러.
 *
 * @author Byoung Gon, Kim
 * @version 0.5
 */
@RestController
@RequestMapping("/user/preference")
public class UserPreferenceController {

    @Autowired
    UserPreferenceService preferenceService;

    @RequestMapping(value = "/event/list.json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response api(@RequestParam(defaultValue = "") String sort,
                        @RequestParam(defaultValue = "DESC") String dir,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "0") int start,
                        @RequestParam(defaultValue = "16") int limit,
                        @RequestParam(defaultValue = "") String orderby) {
        Response response = new Response();
        try {
            List<UserEvent> userEvents = preferenceService.getNotSeeEvents(SessionUtils.getUsername());
            response.getList().addAll(userEvents);
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
        }
        return response;
    }

    @RequestMapping(value = "/event/saw.json", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response api(@RequestBody Map params) {
        Response response = new Response();
        try {
            //identifiers
            String list = params.get("identifiers").toString();
            list = list.replace("[", "").replace("]", "");
            String[] split = list.split(",");
            for (int i = 0; i < split.length; i++) {
                String identifier = split[i];
                identifier = identifier.replaceAll("\\p{Space}", "");
                preferenceService.sawEvent(identifier);
            }
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
        }
        return response;
    }

}
