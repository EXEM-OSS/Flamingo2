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
package org.opencloudengine.flamingo2.engine.archive.mapreduce;

import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;

import java.util.List;
import java.util.Map;

public class ArchiveMapReduceRemoteServiceImpl implements ArchiveMapReduceRemoteService {

    @Override
    public List<Map<String, Object>> getMapReduceJobSummary(Map summaryMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getMapReduceJobSummary(summaryMap);
    }

    @Override
    public List<YarnApplicationHistory> getAllMapReduceJobs(Map allMRJobsMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getAllMapReduceJobs(allMRJobsMap);
    }

    @Override
    public int getTotalCountOfAllMapReduceJobs(Map allMRJobsMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getTotalCountOfAllMapReduceJobs(allMRJobsMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobReport(Map archiveMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getMapReduceJobReport(archiveMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobCounters(Map archiveMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getMapReduceJobCounters(archiveMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobConfiguration(Map archiveMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getMapReduceJobConfiguration(archiveMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobTask(Map archiveMap) {
        ArchiveMapReduceRepository archiveMapReduceRepository = ApplicationContextRegistry.getApplicationContext().getBean(ArchiveMapReduceRepository.class);
        return archiveMapReduceRepository.getMapReduceJobTask(archiveMap);

    }
}
