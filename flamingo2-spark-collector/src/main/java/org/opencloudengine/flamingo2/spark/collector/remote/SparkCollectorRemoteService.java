package org.opencloudengine.flamingo2.spark.collector.remote;

import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 17..
 */
public interface SparkCollectorRemoteService {

    public List<Map> getEnvironment(Map params);

    public Map getTaskTimeline(Map params);
}
