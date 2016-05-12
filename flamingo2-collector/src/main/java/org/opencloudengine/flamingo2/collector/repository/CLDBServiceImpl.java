package org.opencloudengine.flamingo2.collector.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Park on 15. 6. 12..
 */
@Service
public class CLDBServiceImpl implements CLDBService {

    @Autowired
    CLDBRepository repository;

    @Override
    public void collect(String system, String name, Map<String, Object> cldbInfo) {
        repository.insert(system, name, cldbInfo);
    }
}
