package org.opencloudengine.flamingo2.collector.repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Park on 15. 6. 12..
 */
public interface CLDBRepository {
    void insert(String system, String name, Map<String, Object> cldbInfo);
}
