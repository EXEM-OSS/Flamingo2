/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.spark.collector.jobs;

import com.google.gson.Gson;
import org.opencloudengine.flamingo2.spark.collector.parser.ApplicationAggregate;
import org.opencloudengine.flamingo2.spark.collector.parser.EventParserRepository;
import org.opencloudengine.flamingo2.spark.collector.parser.EventParserService;
import org.opencloudengine.flamingo2.spark.collector.parser.value.*;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 13..
 */
public class SparkCollectJob extends QuartzJobBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(SparkCollectJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Spark Application의 수집을 시작합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        EventParserRepository eventParserRepository = applicationContext.getBean(EventParserRepository.class);
        EventParserService eventParserService = applicationContext.getBean(EventParserService.class);

        try {
            logger.info("Getting Spark application list");
            List<String> appList = eventParserService.getFilePathList();
            Map params = new HashMap();
            for (String appPath : appList) {
                try {
                    String split[] = appPath.split("/");
                    String filename = split[split.length - 1];

                    if (filename.indexOf(".inprogress") > 0) {
                        continue;
                    }

                    params.put("location", appPath);
                    if (eventParserRepository.countApp(params) == 0) {
                        logger.info("Getting start Spark application:" + appPath);
                        ApplicationAggregate aggregate = eventParserService.getApplicationAggregate(filename);
                        String appID = aggregate.getApps().getAppID();
                        logger.info("Spark Application ID: " + appID);

                        Apps apps = aggregate.getApps();
                        apps.setLocation(appPath);
                        eventParserRepository.insertApps(apps);

                        List<Integer> jobIDList = aggregate.getJobAggregate().getJobIDs();

                        for (Integer jobID : jobIDList) {
                            Job job = aggregate.getJobAggregate().getJobMap().get(jobID);
                            job.setAppID(appID);

                            eventParserRepository.insertJob(job);
                        }

                        List<String> stageIDList = aggregate.getStageAggregate().getStageIDs();

                        for (String stageID : stageIDList) {
                            Stage stage = aggregate.getStageAggregate().getStageMap().get(stageID);
                            stage.setAppID(appID);

                            if (stage.isSkip) {
                                stage.setStatus("Skipped");
                                stage.setSubmitted(null);
                                stage.setCompleted(null);
                            }

                            eventParserRepository.insertStage(stage);
                        }

                        stageIDList = aggregate.getStageDetailAggregate().getStageIDs();

                        for (String stageID : stageIDList) {
                            String stageIDSplit[] = stageID.split("-");
                            Integer jobID = aggregate.getJobAggregate().getStageJobMap().get(Integer.valueOf(stageIDSplit[0]));
                            StageDetail stageDetail = aggregate.getStageDetailAggregate().getStageDetailMap().get(stageID);

                            Map detail = new HashMap();

                            detail.put("appid", appID);
                            detail.put("jobid", jobID);
                            detail.put("stageid", stageIDSplit[0]);
                            detail.put("attemptid", stageIDSplit[1]);

                            detail.put("type", "ExecutorDeserializeTime");
                            detail.put("json", toJsonList(stageDetail.getExecutorDeserializeTime()));
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "ExecutorRunTime");
                            detail.put("json", toJsonList(stageDetail.getExecutorRunTime()));
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "ResultSerializationTime");
                            detail.put("json", toJsonList(stageDetail.getResultSerializationTime()));
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "GettingResultTime");
                            detail.put("json", toJsonList(stageDetail.getGettingResultTime()));
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "JvmGCTime");
                            detail.put("json", toJsonList(stageDetail.getJvmGCTime()));
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "ResultSize");
                            detail.put("json", stageDetail.getResultSize().toJson());
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "MemoryBytesSpilled");
                            detail.put("json", stageDetail.getMemoryBytesSpilled().toJson());
                            eventParserRepository.insertStageDetail(detail);

                            detail.put("type", "DiskBytesSpilled");
                            detail.put("json", stageDetail.getDiskBytesSpilled().toJson());
                            eventParserRepository.insertStageDetail(detail);
                        }

                        List<String> executorIDs = aggregate.getExecutorAggregate().getExecutorList();

                        for (String executorID : executorIDs) {
                            Executor executor = aggregate.getExecutorAggregate().getExecutorMap().get(executorID);
                            executor.setAppID(appID);
                            eventParserRepository.insertExecutor(executor);
                        }

                        List<Integer> rddIDs = aggregate.getStorageAggregate().getRddIDs();

                        for (Integer rddID : rddIDs) {
                            Storage storage = aggregate.getStorageAggregate().getStorageMap().get(rddID);

                            if (storage.isUpdated) {
                                if (storage.getUseDisk() || storage.getUseExternalBlockStore() || storage.getUseMemory()) {
                                    storage.setAppID(appID);
                                    eventParserRepository.insertStorage(storage);
                                }
                            }
                        }

                        List<Timeline> timelineList = aggregate.getTimelineAggregate().getTimelineList();

                        for (Timeline timeline : timelineList) {
                            timeline.setAppID(appID);

                            eventParserRepository.insertTimeline(timeline);
                        }

                        logger.info("Getting finish Spark application:" + appPath);
                    }
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    continue;
                }
            }

            logger.info("Spark Application의 수집이 완료되었습니다.");
        } catch (Exception e) {
            logger.info("Spark Application의 수집 중 오류가 발생하였습니다.");
            e.printStackTrace();
        }
    }

    private String toJsonList(Map map) {
        Object keys[] = map.keySet().toArray();
        Gson gson = new Gson();
        List<Map> jsonList = new ArrayList<>();

        for (Object key : keys) {
            Map jsonMap = new HashMap();

            jsonMap.put("key", key.toString());
            jsonMap.put("value", map.get(key).toString());

            jsonList.add(jsonMap);
        }

        return gson.toJson(jsonList);
    }
}
