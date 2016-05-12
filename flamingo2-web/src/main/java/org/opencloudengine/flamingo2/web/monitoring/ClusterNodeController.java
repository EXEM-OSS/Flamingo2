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
package org.opencloudengine.flamingo2.web.monitoring;

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.util.TimeSeriesUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/monitoring/clusternode")
public class ClusterNodeController extends DefaultController {

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response nodes(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getNodes(engineConfig));
        return response;
    }

    @RequestMapping(value = "/timeseries", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response timeseries(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        List applications = service.listApplications(null, null, true, engineConfig);

        Response response = new Response();
        response.setSuccess(true);

        // 가장 이른 시간과 늦은 시간을 위해서 시간으로 정렬한다.
        TreeMap timeMapForOrdering = new TreeMap();
        for (Object obj : applications) {
            Map app = (Map) obj;
            Long startTime = (Long) app.get("startTime");
            String appId = (String) app.get("applicationId");
            timeMapForOrdering.put(startTime, appId);
        }

        if (applications.size() > 0) {
            Date start = new Date((Long) timeMapForOrdering.firstEntry().getKey());
            Date end = new Date((Long) timeMapForOrdering.lastEntry().getKey());

            Map<String, AtomicInteger> vcoreSecondsSeries = TimeSeriesUtils.getTimeSeries(start, end);
            Map<String, AtomicInteger> memorySecondsSeries = TimeSeriesUtils.getTimeSeries(start, end);

            for (Object obj : applications) {
                Map application = (Map) obj;
                String startTimeStr = DateUtils.parseDate(new Date((Long) application.get("startTime")), "yyyy-MM-dd");

                Long vcoreSeconds = (Long) application.get("vcoreSeconds");
                Long memorySeconds = (Long) application.get("memorySeconds");
                Integer neededResourcesMemory = (Integer) application.get("neededResourcesMemory");
                Integer neededResourcesVcores = (Integer) application.get("neededResourcesVcores");

                // Cluster Node : VCore
                if (vcoreSecondsSeries.get(startTimeStr) == null) {
                    vcoreSecondsSeries.put(startTimeStr, new AtomicInteger(0));
                }
                vcoreSecondsSeries.get(startTimeStr).addAndGet(vcoreSeconds.intValue());

                if (vcoreSeconds < 0) {
                    vcoreSecondsSeries.get(startTimeStr).addAndGet(neededResourcesVcores.intValue());
                } else {
                    vcoreSecondsSeries.get(startTimeStr).addAndGet(vcoreSeconds.intValue());
                }

                // Cluster Node : Memory
                if (memorySecondsSeries.get(startTimeStr) == null) {
                    memorySecondsSeries.put(startTimeStr, new AtomicInteger(0));
                }

                if (memorySeconds < 0) {
                    memorySecondsSeries.get(startTimeStr).addAndGet(neededResourcesMemory.intValue());
                } else {
                    memorySecondsSeries.get(startTimeStr).addAndGet(memorySeconds.intValue());
                }
            }

            Set<String> keys = vcoreSecondsSeries.keySet();
            for (String key : keys) {
                HashMap kv = new HashMap();
                kv.put("time", key);
                kv.put("vcoreSum", vcoreSecondsSeries.get(key).get());
                kv.put("memorySum", memorySecondsSeries.get(key).get());
                response.getList().add(kv);
            }
        }
        return response;
    }

}