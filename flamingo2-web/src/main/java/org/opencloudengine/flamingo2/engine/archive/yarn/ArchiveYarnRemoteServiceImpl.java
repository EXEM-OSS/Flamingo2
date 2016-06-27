package org.opencloudengine.flamingo2.engine.archive.yarn;

import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;

import java.util.List;
import java.util.Map;

public class ArchiveYarnRemoteServiceImpl implements ArchiveYarnRemoteService {

    @Override
    public List<Map<String, Object>> getYarnApplicationSummary(Map summaryMap) {
        ArchiveYarnApplicationRepository archiveYarnApplicationRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveYarnApplicationRepository.class);
        return archiveYarnApplicationRepository.getYarnApplicationSummary(summaryMap);
    }

    @Override
    public List<YarnApplicationHistory> getAllApplications(Map allAppMap) {
        ArchiveYarnApplicationRepository archiveYarnApplicationRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveYarnApplicationRepository.class);
        return archiveYarnApplicationRepository.getAllApplications(allAppMap);
    }

    @Override
    public int getTotalCountOfYarnAllApplications(Map allAppMap) {
        ArchiveYarnApplicationRepository archiveYarnApplicationRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveYarnApplicationRepository.class);
        return archiveYarnApplicationRepository.getTotalCountOfYarnAllApplications(allAppMap);
    }

    @Override
    public Map<String, Object> getApplicationReport(Map archiveMap) {
        ArchiveYarnApplicationRepository archiveYarnApplicationRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveYarnApplicationRepository.class);
        return archiveYarnApplicationRepository.getApplicationReport(archiveMap);
    }
}
