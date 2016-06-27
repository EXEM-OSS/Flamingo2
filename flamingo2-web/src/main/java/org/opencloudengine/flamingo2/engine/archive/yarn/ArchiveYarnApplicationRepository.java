package org.opencloudengine.flamingo2.engine.archive.yarn;

import org.opencloudengine.flamingo2.core.repository.PersistentRepository;
import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;

import java.util.List;
import java.util.Map;

public interface ArchiveYarnApplicationRepository extends PersistentRepository<YarnApplicationHistory, Long> {

    String NAMESPACE = ArchiveYarnApplicationRepository.class.getName();

    List<Map<String,Object>> getYarnApplicationSummary(Map summaryMap);

    List<YarnApplicationHistory> getAllApplications(Map allAppMap);

    int getTotalCountOfYarnAllApplications(Map allAppMap);

    Map<String, Object> getApplicationReport(Map archiveMap);
}
