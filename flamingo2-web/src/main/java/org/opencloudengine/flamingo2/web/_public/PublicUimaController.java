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
package org.opencloudengine.flamingo2.web._public;

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.web.uima.UimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Apache UIMA REST Controller
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
@RestController
@RequestMapping("/public/uima")
public class PublicUimaController {

    @Autowired
    private UimaService uimaService;

    /**
     * 등록된 Spark Streaming Application 목록을 가져온다.
     *
     * @param page          페이지 목록
     * @param limit         한 페이지당 보여줄 행 수
     * @return  true or false
     */
    @RequestMapping(value = "logs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getAllSparkStreamingApps(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int limit) {
        Map uimaMap = new HashMap();
        uimaMap.put("page", page);
        uimaMap.put("limit", limit);

        Response response = new Response();
        response.setTotal(uimaService.getTotalCount(uimaMap));
        response.getList().addAll(uimaService.getAllUimaLogs(uimaMap));
        response.setSuccess(true);
        return response;
    }

    /**
     * 선택한 Spark Streaming Applicaton의 Summary 정보를 가져온다.
     *
     * @return Summary List
     */
    @RequestMapping(value = "summary", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getSparkStreamingAppSummary(@RequestParam(defaultValue = "") String startDate,
                                                @RequestParam(defaultValue = "") String endDate) {
        Map uimaMap = new HashMap();
        uimaMap.put("startDate", startDate);
        uimaMap.put("endDate", endDate);

        Response response = new Response();
        response.getList().addAll(uimaService.getUimaLogSummary(uimaMap));
        response.setSuccess(true);
        return response;
    }
}
