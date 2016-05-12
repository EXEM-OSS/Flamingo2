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
package org.opencloudengine.flamingo2.engine.hadoop;

import org.apache.hadoop.mapreduce.v2.util.MRApps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryServerRemoteServiceImpl implements HistoryServerRemoteService {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(HistoryServerRemoteServiceImpl.class);

    @Override
    public Map<String, Object> getHistoryServerInfo(String historyServerUrl) {
        String url = MessageFormatter.format("http://{}/ws/v1/history", historyServerUrl).getMessage();
        Map<String, Object> returnMap = invoke(url);
        return returnMap;
    }

    @Override
    public Map<String, Object> getJobInfo(String historyServerUrl, String jobId) {
        Map<String, Object> returnMap = new HashMap<>();

        returnMap.putAll(this.getJob(historyServerUrl, jobId));
        returnMap.putAll(this.getAttempts(historyServerUrl, jobId));
        returnMap.putAll(this.getJobConf(historyServerUrl, jobId));

        Map<String, Object> counterMap = this.getCounters(historyServerUrl, jobId);
        Map<String, Object> jobCounters = (Map<String, Object>) counterMap.get("jobCounters");
        List<Map<String, Object>> counterGroup = (List<Map<String, Object>>) jobCounters.get("counterGroup");
        List<Map<String, Object>> groupList = new ArrayList<>();

        for (Map<String, Object> counter : counterGroup) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) counter.get("counter");
            List<Map<String, Object>> counterList = new ArrayList<>();

            for (Map<String, Object> map : list) {
                map.put("leaf", true);

                counterList.add(map);
            }

            counter.put("name", counter.get("counterGroupName"));
            counter.remove("counter");
            counter.remove("counterGroupName");
            counter.put("children", counterList);

            groupList.add(counter);
        }

        returnMap.put("jobCounters", groupList);

        Map<String, Object> task = (Map<String, Object>) this.getTasks(historyServerUrl, jobId).get("tasks");
        List<Map<String, Object>> taskList = (List<Map<String, Object>>) task.get("task");
        List<Map<String, Object>> tasks = new ArrayList<>();

        for (Map<String, Object> taskMap : taskList) {
            String taskId = taskMap.get("id").toString();
            taskMap.putAll(this.getTaskCounters(historyServerUrl, jobId, taskId));
            Map<String, Object> taskAttempt = (Map<String, Object>) this.getTaskAttempts(historyServerUrl, jobId, taskId).get("taskAttempts");
            List<Map<String, Object>> taskAttemptList = (List<Map<String, Object>>) taskAttempt.get("taskAttempt");
            List<Map<String, Object>> taskAttempts = new ArrayList<>();

            for (Map<String, Object> taskAttemptMap : taskAttemptList) {
                String taskAttemptId = taskAttemptMap.get("id").toString();

                Map<String, Object> taskAttemptsCounters = this.getTaskAttemptsCounters(historyServerUrl, jobId, taskId, taskAttemptId);
                Map<String, Object> jobTaskAttemptCounters = (Map<String, Object>) taskAttemptsCounters.get("jobTaskAttemptCounters");
                List<Map<String, Object>> taskAttemptCounterGroup = (List<Map<String, Object>>) jobTaskAttemptCounters.get("taskAttemptCounterGroup");
                List<Map<String, Object>> taskAttemptCounterGroupList = new ArrayList<>();

                for (Map<String, Object> counter : taskAttemptCounterGroup) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) counter.get("counter");
                    List<Map<String, Object>> counterList = new ArrayList<>();

                    for (Map<String, Object> map : list) {
                        map.put("leaf", true);
                        map.put("id", map.get("name"));

                        counterList.add(map);
                    }

                    counter.put("id", counter.get("counterGroupName"));
                    counter.remove("counterGroupName");
                    counter.remove("counter");
                    counter.put("children", counterList);

                    taskAttemptCounterGroupList.add(counter);
                }

                if (taskAttemptCounterGroupList.size() > 0) {
                    taskAttemptMap.put("children", taskAttemptCounterGroupList);
                } else {
                    taskAttemptMap.put("leaf", true);
                }

                taskAttempts.add(taskAttemptMap);
            }
            taskMap.put("taskAttempts", taskAttempts);

            tasks.add(taskMap);
        }

        returnMap.put("tasks", tasks);

        return returnMap;
    }

    @Override
    public Map<String, Object> getJobs(String historyServerUrl) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs", historyServerUrl).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getJob(String historyServerUrl, String jobId) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs/{}", historyServerUrl, jobId).getMessage();
        Map result = invoke(url);
/*
        try {
            Map<String, Object> conf = getJobConf(historyServerUrl, jobId);
            logger.debug("{}", conf);
            if (conf.get("flamingo.workflow.id") != null) {
                result.put("flamingo_workflow_id", conf.get("flamingo.workflow.id"));
            }
            if (conf.get("flamingo.workflow.name") != null) {
                result.put("flamingo_workflow_name", conf.get("flamingo.workflow.name"));
            }
            if (conf.get("flamingo.username") != null) {
                result.put("flamingo_username", conf.get("flamingo.username"));
            }
            if (conf.get("flamingo.job.stringId") != null) {
                result.put("flamingo_job_id", conf.get("flamingo.job.stringId"));
            }
            if (conf.get("flamingo.action.name") != null) {
                result.put("flamingo_action_name", conf.get("flamingo.action.name"));
            }
        } catch (Exception ex) {
            logger.warn("Unable to retreive configuration of job.", ex);
        }
*/
        return result;
    }

    @Override
    public Map<String, Object> getJobToList(String historyServerUrl, String jobId) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs/{}", historyServerUrl, jobId).getMessage();

        Map<String, Object> jobRestMap = invoke(url);
        Map<String, Object> jobMap = (Map<String, Object>) jobRestMap.get("job");
        Map<String, Object> returnMap = new HashMap<>();
        List<Map<String, Object>> jobList = new ArrayList<>();


        for (Object keyset : jobMap.keySet().toArray()) {
            Map<String, Object> map = new HashMap<>();
            String key = keyset.toString();
            map.put("key", key);
            map.put("value", jobMap.get(key));

            jobList.add(map);
        }

        returnMap.put("job", jobList);
        return returnMap;
    }

    @Override
    public Map<String, Object> getAttempts(String historyServerUrl, String jobId) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs/{}/jobattempts", historyServerUrl, jobId).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getCounters(String historyServerUrl, String jobId) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs/{}/counters", historyServerUrl, jobId).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getJobConf(String historyServerUrl, String jobId) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs/{}/conf", historyServerUrl, jobId).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTasks(String historyServerUrl, String jobId) {
        String url = MessageFormatter.format("http://{}/ws/v1/history/mapreduce/jobs/{}/tasks", historyServerUrl, jobId).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTaskCounters(String historyServerUrl, String jobId, String taskId) {
        String url = MessageFormatter.arrayFormat("http://{}/ws/v1/history/mapreduce/jobs/{}/tasks/{}/counters", new String[]{historyServerUrl, jobId, taskId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTaskAttempts(String historyServerUrl, String jobId, String taskId) {
        String url = MessageFormatter.arrayFormat("http://{}/ws/v1/history/mapreduce/jobs/{}/tasks/{}/attempts", new String[]{historyServerUrl, jobId, taskId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTaskAttemptsCounters(String historyServerUrl, String jobId, String taskId, String attemptId) {
        String url = MessageFormatter.arrayFormat("http://{}/ws/v1/history/mapreduce/jobs/{}/tasks/{}/attempts/{}/counters", new String[]{historyServerUrl, jobId, taskId, attemptId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getJobConfByProxy(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/conf", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getJobByProxy(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getAttemptsByProxy(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/jobattempts", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getCountersByProxy(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/counters", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTasksByProxy(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/tasks", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTaskCountersByProxy(String proxyServerUrl, String jobId, String taskId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/tasks/{}/counters", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId, taskId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTaskAttemptsByProxy(String proxyServerUrl, String jobId, String taskId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/tasks/{}/attempts", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId, taskId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTaskAttemptsCountersByProxy(String proxyServerUrl, String jobId, String taskId, String attemptId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/tasks/{}/attempt/{}/counters", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId, taskId, attemptId}).getMessage();
        return invoke(url);
    }

    @Override
    public List getJobsByApplication(String applicationId, String proxyServerUrl) {
        String url = MessageFormatter.format("http://{}/proxy/{}/ws/v1/mapreduce/jobs", proxyServerUrl, applicationId).getMessage();
        Map result = invoke(url);

        List<Map<String, Object>> returnList = new ArrayList<>();
        Map<String, Object> jobMap = (Map<String, Object>) result.get("jobs");
        List<Map<String, Object>> jobList = (List<Map<String, Object>>) jobMap.get("job");
        for (Map<String, Object> job : jobList) {
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("state", job.get("state").toString());
            returnMap.put("reducesTotal", job.get("reducesTotal").toString());
            returnMap.put("mapsCompleted", job.get("mapsCompleted").toString());
            returnMap.put("startTime", job.get("startTime").toString());
            returnMap.put("id", job.get("id").toString());
            returnMap.put("name", job.get("name").toString());
            returnMap.put("reducesCompleted", job.get("reducesCompleted").toString());
            returnMap.put("mapsTotal", job.get("mapsTotal").toString());
            returnMap.put("finishTime", job.get("finishTime").toString());
            returnMap.put("reduceProgress", job.get("reduceProgress").toString());
            returnMap.put("mapProgress", job.get("mapProgress").toString());

            returnList.add(returnMap);
        }

        return returnList;
    }

    private String getApplicationId(String jobId) {
        return MRApps.toJobID(jobId).getAppId().toString();
    }

    private static Map invoke(String url) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<Map> response = template.getForEntity(url, Map.class);
        return response.getBody();
    }
}
