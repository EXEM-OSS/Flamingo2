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
package org.opencloudengine.flamingo2.engine.batch;

import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.scheduler.JobScheduler;
import org.opencloudengine.flamingo2.engine.scheduler.JobVariable;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Park on 15. 5. 12..
 */
public class BatchServiceImpl implements BatchService {

    /**
     * Job Scheduler
     */
    private JobScheduler jobScheduler;

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);

    @Override
    public String regist(Map params) {
        try {
            //EngineConfig engineConfig, String jobName, Workflow workflow, String cronExpression, HashMap jobVariables
            HashMap jobVariable = new HashMap();

            if (!StringUtils.isEmpty(params.get("vars").toString())) {
                List vars = objectMapper.readValue(params.get("vars").toString(), List.class);
                for (Object map : vars) {
                    Map m = (Map) map;
                    jobVariable.put(m.get("name"), m.get("value"));
                }
            }

            Workflow workflow = (Workflow) params.get("workflow");
            String key = JobIdGenerator.generateKey(workflow);

            Map vars = new HashMap();
            vars.put("cron", params.get("cron").toString());
            vars.put(JobVariable.WORKFLOW, workflow);
            vars.put(JobVariable.JOB_TYPE, "WORKFLOW");
            vars.put(JobVariable.JOB_NAME, params.get("jobName").toString());
            vars.put(JobVariable.JOB_KEY, key);
            vars.put(JobVariable.JOB_VARIABLES, jobVariable);

            //Insert batch table
            params.put("job_id", key);
            params.put("workflow_nm", workflow.getWorkflowName());
            //repository.regist(params);

            //StartJob
            JobKey jobKey = jobScheduler.startJob(key, params.get("username").toString(), params.get("cron").toString(), vars);

            return jobKey.getName();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException("Could not regist workflow batch. Cause: " + ex.getMessage());
        }

    }

    @Override
    public Workflow get(String jobId) {
        Map<String, Object> jobDataMap = jobScheduler.getJobDataMap(jobId, "Scheduled Job");
        return (Workflow) jobDataMap.get(JobVariable.WORKFLOW);
    }

    @Override
    public Map getJob(Map params) {
        return jobScheduler.getJobDataMap(params.get("job_id").toString(), params.get("username").toString());
    }

    @Override
    public List<Map> getJobs() {
        return jobScheduler.getJobs();
    }

    @Override
    public void suspend(Map params) {
        try {
            jobScheduler.pauseJob(params.get("name").toString(), params.get("groupName").toString());
        } catch (Exception ex) {
            throw new ServiceException("Could not suspend batch job", ex);
        }

    }

    @Override
    public void stop(Map params) {
        try {
            jobScheduler.stopJob(params.get("name").toString(), params.get("groupName").toString());
        } catch (Exception ex) {
            throw new ServiceException("Could not stop batch job", ex);
        }
    }

    @Override
    public void resume(Map params) {
        try {
            jobScheduler.resumeJob(params.get("name").toString(), params.get("groupName").toString());
        } catch (Exception ex) {
            throw new ServiceException("Could not resume batch job", ex);
        }

    }

    @Override
    public void delete(String groupName, String name) {
        jobScheduler.deleteJob(name, groupName);
    }

    @Override
    public void update(Map params) {
        jobScheduler.updateJob(params.get("JOB_KEY").toString(), params.get("username").toString(), params.get("jobName").toString(), params.get("cron").toString());
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }
}
