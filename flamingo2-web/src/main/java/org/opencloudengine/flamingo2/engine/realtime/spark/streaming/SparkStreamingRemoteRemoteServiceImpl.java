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
package org.opencloudengine.flamingo2.engine.realtime.spark.streaming;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.hadoop.RemoteInvocation;
import org.opencloudengine.flamingo2.model.rest.IoTService;
import org.opencloudengine.flamingo2.model.rest.SparkStreaming;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;

import java.util.*;

public class SparkStreamingRemoteRemoteServiceImpl extends RemoteInvocation implements SparkStreamingRemoteService {

    /**
     * 시스템 에이전트의 Spark Streaming Remote Service Interface와 통신하기 위한 Spring Bean ID
     */
    public static final String SPARK_STREAMING_REMOTE_SERVICE = "sparkStreamingAgent";

    @Override
    public List<IoTService> getIoTServices(Map ioTMap) {
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);
        return sparkStreamingRepository.selectIoTServices(ioTMap);
    }

    @Override
    public boolean createSparkApplication(Map sparkStreamingMap) {
        org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService sparkStreamingRemoteService = getSparkStreamingRemoteService(sparkStreamingMap);
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);

        if (!sparkStreamingRemoteService.createSparkApplication((String) sparkStreamingMap.get("sparkUserWorkingPath"))) {
            throw new ServiceException("Cannot create spark streaming working directory.");
        }

        return sparkStreamingRepository.insertSparkStreamingApp(sparkStreamingMap) > 0;
    }

    @Override
    public boolean createSparkApplication(Map sparkStreamingMap, byte[] jarFileBytes) {
        org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService sparkStreamingRemoteService = getSparkStreamingRemoteService(sparkStreamingMap);
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);

        if (!sparkStreamingRemoteService.createSparkApplication((String) sparkStreamingMap.get("jarFileFQP"), jarFileBytes)) {
            throw new ServiceException("Cannot create spark streaming working directory.");
        }

        return sparkStreamingRepository.insertSparkStreamingApp(sparkStreamingMap) > 0;
    }

    @Override
    public boolean startSparkStreamingApp(Map sparkStreamingMap, Map<String, String> defaultEnvs) {
        org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService sparkStreamingRemoteService = getSparkStreamingRemoteService(sparkStreamingMap);
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);

        if (sparkStreamingRemoteService.startSparkStreamingApp(sparkStreamingMap, defaultEnvs)) {
            sparkStreamingMap.put("state", "RUNNING");
        }

        return sparkStreamingRepository.updateSparkStreamingApp(sparkStreamingMap);
    }

    @Override
    public boolean stopSparkStreamingApp(Map sparkStreamingMap) {
        org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService sparkStreamingRemoteService = getSparkStreamingRemoteService(sparkStreamingMap);
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);

        if (sparkStreamingRemoteService.stopSparkStreamingApp(sparkStreamingMap)) {
            sparkStreamingMap.put("state", "STANDBY");
        }

        return sparkStreamingRepository.updateSparkStreamingApp(sparkStreamingMap);
    }

    @Override
    public boolean killSparkStreamingApp(Map sparkStreamingMap) {
        org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService sparkStreamingRemoteService = getSparkStreamingRemoteService(sparkStreamingMap);
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);
        String applicationId = (String) sparkStreamingMap.get("applicationId");

        return sparkStreamingRemoteService.stopSparkStreamingApp(sparkStreamingMap) && sparkStreamingRepository.deleteSparkStreamingApp(applicationId);
    }

    @Override
    public int getTotalCountOfSparkStreamingApps(Map sparkStreamingMap) {
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);
        return sparkStreamingRepository.selectTotalCountOfSparkStreamingApps(sparkStreamingMap);
    }

    @Override
    public List<SparkStreaming> getAllSparkStreamingApps(Map sparkStreamingMap) {
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);
        return sparkStreamingRepository.selectAllSparkStreamingApps(sparkStreamingMap);
    }

    @Override
    public List<Map<String, Object>> getSparkStreamingAppSummary(Map sparkStreamingMap) {
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);
        return sparkStreamingRepository.selectAllSparkStreamingAppsSummary(sparkStreamingMap);
    }

    @Override
    public String getOptimalServer(List<String> systemAgentList) {
        SparkStreamingRepository sparkStreamingRepository = ApplicationContextRegistry.getApplicationContext().getBean(SparkStreamingRepository.class);

        // 0번째 등록된 시스템 에이전트부터 등록된 애플리케이션 수 조회
        int count = sparkStreamingRepository.selectOptimalServer(systemAgentList.get(0));
        // count 값(애플리케이션 등록수)이 큰 시스템 에이전트일 경우만 업데이트
        String systemAgent = systemAgentList.get(0);

        // 애플리케이션이 가장 적게 등록된 서버를 찾는다.
        if (systemAgentList.size() > 1) {
            for (int i = 1; i < systemAgentList.size(); i++) {
                int countToCompare = sparkStreamingRepository.selectOptimalServer(systemAgentList.get(i));
                if (count > countToCompare) {
                    count = countToCompare;
                    systemAgent = systemAgentList.get(i);
                }
            }
        }

        return systemAgent;
    }

    /**
     * Remote에 구동 중인 System Agent 서비스를 획득한다.
     *
     * @param sparkStreamingMap Remote System Agent 접근에 필요한 서버 정보
     * @return {@link org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService}
     */
    private org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService getSparkStreamingRemoteService(Map sparkStreamingMap) {
        String url = (String) sparkStreamingMap.get("url");
        int port = (int) sparkStreamingMap.get("port");
        String remoteServiceUrl = this.getRemoteServiceUrl(url, port, SPARK_STREAMING_REMOTE_SERVICE);

        return this.getRemoteService(remoteServiceUrl, org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService.class);
    }
}

