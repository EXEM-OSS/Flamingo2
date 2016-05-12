package org.opencloudengine.flamingo2.collector.repository;

import java.util.Map;

/**
 * Created by Park on 15. 6. 12..
 */
public interface CLDBService {
    void collect(String system, String name, Map<String, Object> cldbInfo);
}
