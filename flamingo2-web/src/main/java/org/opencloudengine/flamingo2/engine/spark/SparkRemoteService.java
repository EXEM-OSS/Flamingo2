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
package org.opencloudengine.flamingo2.engine.spark;

import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 14..
 */
public interface SparkRemoteService {

    public List<Map> getJobChart();

    public List<Map> getApplications(Map params);

    public List<Map> getJobs(Map params);

    public List<Map> getStages(Map params);

    public List<Map> getEnvironment(EngineConfig engineConfig, Map params);

    public List<Map> getExecutors(Map params);

    public List<Map> getStorage(Map params);

    public Map getTimeline(Map params);

    public Map getStageDetail(Map params);

    public Map getTaskTimeline(EngineConfig engineConfig, Map params);
}
