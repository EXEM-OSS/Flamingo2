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

import java.util.List;
import java.util.Map;

/**
 * Archive Yarn Application Remote Interface.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public interface ArchiveMapReduceRemoteService {

    List<Map<String, Object>> getMapReduceJobSummary(Map summaryMap);

    List<YarnApplicationHistory> getAllMapReduceJobs(Map allMRJobsMap);

    int getTotalCountOfAllMapReduceJobs(Map allMRJobsMap);

    Map<String, Object> getMapReduceJobReport(Map archiveMap);

    Map<String, Object> getMapReduceJobCounters(Map archiveMap);

    Map<String, Object> getMapReduceJobConfiguration(Map archiveMap);

    Map<String,Object> getMapReduceJobTask(Map archiveMap);
}
