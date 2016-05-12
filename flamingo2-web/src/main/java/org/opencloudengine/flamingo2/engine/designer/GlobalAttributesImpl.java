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
package org.opencloudengine.flamingo2.engine.designer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.uengine.kernel.ProcessInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class GlobalAttributesImpl implements GlobalAttributes {
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GlobalAttributes.class);

    @Autowired
    @Qualifier("config")
    private Properties config;

    private Map glovalSet = new HashMap();


    @Override
    public void registJobResultMap(ProcessInstance instance) throws Exception {
        instance.set("jobMap", new HashMap());
    }

    @Override
    public Map getJobMap(ProcessInstance instance) throws Exception {
        if (instance.get("jobMap") != null) {
            return (Map) instance.get("jobMap");
        } else {
            this.registJobResultMap(instance);
            return new HashMap();
        }
    }

    @Override
    public void setJobMap(ProcessInstance instance, Map jobMap) throws Exception {
        instance.set("jobMap", jobMap);
    }

    @Override
    public String getResolvedJsonAttr(ProcessInstance instance, String taskId, Map params) throws Exception {

        //jsonattritubes
        String jsonattritubes = params.get("json").toString();
        String[] split = jsonattritubes.split("#\\{");
        String identifier = "WORKFLOW";
        for (int i = 0; i < split.length; i++) {
            if (split[i].indexOf(identifier) == 0) {
                int cut = split[i].indexOf("}");
                if (cut != -1) {
                    String key = split[i].substring(0, cut);
                    String keyForReplace = "#\\{" + key + "\\}";
                    String wf_key = key.split("::")[1];
                    String wf_value = params.get(wf_key).toString();
                    jsonattritubes = jsonattritubes.replaceAll(keyForReplace, wf_value);
                }
            }
        }
        return jsonattritubes;
    }


    @Override
    public String getTaskStatus(ProcessInstance instance, String taskId, String lookupTaskId) throws Exception {

        Map jobMap = this.getJobMap(instance);

        String status = "STANDBY";
        if (jobMap.containsKey("status")) {
            Map statusMap = (Map) jobMap.get("status");
            if (statusMap.containsKey(lookupTaskId))
                status = statusMap.get(lookupTaskId).toString();
        }

        return status;
    }

    @Override
    public void setTaskStatus(ProcessInstance instance, String taskId, String status) throws Exception {

        Map jobMap = this.getJobMap(instance);

        //status 저장소가 없다면 생성
        if (!jobMap.containsKey("status"))
            jobMap.put("status", new HashMap<>());

        Map statusMap = (Map) jobMap.get("status");
        statusMap.put(taskId, status);
        jobMap.put("status", statusMap);
        this.setJobMap(instance, jobMap);
    }
}
