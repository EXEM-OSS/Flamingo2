package org.opencloudengine.flamingo2.engine.archive.yarn;

import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Archive Yarn Application Remote Interface.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public interface ArchiveYarnRemoteService {

    List<Map<String, Object>> getYarnApplicationSummary(Map summaryMap);

    List<YarnApplicationHistory> getAllApplications(Map allAppMap);

    int getTotalCountOfYarnAllApplications(Map allAppMap);

    Map<String, Object> getApplicationReport(Map archiveMap);
}
