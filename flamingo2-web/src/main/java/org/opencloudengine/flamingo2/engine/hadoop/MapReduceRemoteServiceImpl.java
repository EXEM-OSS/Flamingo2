package org.opencloudengine.flamingo2.engine.hadoop;

import org.apache.hadoop.mapreduce.v2.util.MRApps;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by Park on 15. 7. 29..
 */
public class MapReduceRemoteServiceImpl implements MapReduceRemoteService {
    @Override
    public Map<String, Object> getAllJobs() {
        return null;
    }

    @Override
    public Map<String, Object> getJobInfo(String jobId) {
        return null;
    }

    @Override
    public Map<String, Object> getJobConf(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/conf", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getJob(String jobId) {
        return null;
    }

    @Override
    public Map<String, Object> getAttempts(String jobId) {
        return null;
    }

    @Override
    public Map<String, Object> getCounters(String proxyServerUrl, String jobId) {
        String url = MessageFormatter.arrayFormat("http://{}/proxy/{}/ws/v1/mapreduce/jobs/{}/counters", new String[]{proxyServerUrl, this.getApplicationId(jobId), jobId}).getMessage();
        return invoke(url);
    }

    @Override
    public Map<String, Object> getTasks(String jobId) {
        return null;
    }

    @Override
    public Map<String, Object> getTaskCounters(String jobId, String taskId) {
        return null;
    }

    @Override
    public Map<String, Object> getTaskAttempts(String jobId, String taskId) {
        return null;
    }

    @Override
    public Map<String, Object> getTaskAttemptsCounters(String jobId, String taskId, String attemptId) {
        return null;
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
