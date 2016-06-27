package org.opencloudengine.flamingo2.spark.collector.remote;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.spark.collector.event.Environment;
import org.opencloudengine.flamingo2.spark.collector.parser.EventParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 17..
 */
public class SparkCollectorRemoteServiceImpl implements SparkCollectorRemoteService {
    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(SparkCollectorRemoteServiceImpl.class);

    @Autowired
    EventParserService eventParserService;

    @Override
    public List<Map> getEnvironment(Map params) {
        try {
            logger.info("Get Environment event (App ID: " + params.get("appid").toString() + ")");
            Environment environment = eventParserService.getEnvironment(params.get("appid").toString());
            List<Map> returnList = new ArrayList<>();

            addList(environment.getJvmInformation(), "JVM Information", 1, returnList);
            addList(environment.getSparkProperties(), "Spark Properties", 2, returnList);
            addList(environment.getSystemProperties(), "System Properties", 3, returnList);
            addList(environment.getClasspathEntries(), "Classpath Entries", 4, returnList);

            return returnList;
        } catch (IOException ex) {
            throw new ServiceException("Unable to get Environment Event");
        }
    }

    @Override
    public Map getTaskTimeline(Map params) {
        return eventParserService.getTaskTimeline(params);
    }

    private void addList(Map valueMap, String type, int sorter, List<Map> addList) {
        Object keys[] = valueMap.keySet().toArray();

        if (type.equals("Classpath Entries")) {
            int i = 1;
            for (Object keyObject : keys) {
                String key = keyObject.toString();
                Map envMap = new HashMap();
                String num = String.format("%04d", i);
                envMap.put("key", valueMap.get(key) + "-" + num);
                envMap.put("value", key);
                envMap.put("type", type);
                envMap.put("sorter", sorter);

                addList.add(envMap);
                i++;
            }
        } else {
            for (Object keyObject : keys) {
                String key = keyObject.toString();
                Map envMap = new HashMap();

                envMap.put("key", key);
                envMap.put("value", valueMap.get(key));
                envMap.put("type", type);
                envMap.put("sorter", sorter);

                addList.add(envMap);
            }
        }
    }
}