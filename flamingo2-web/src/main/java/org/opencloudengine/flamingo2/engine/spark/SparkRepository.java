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

import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 14..
 */
public interface SparkRepository {
    public final String NAMESPACE = SparkRepository.class.getName();

    public List<Map> selectJobChart();

    public List<Map> selectApplications(Map params);

    public List<Map> selectJobs(Map params);

    public List<Map> selectStages(Map params);

    public List<Map> selectExecutors(Map params);

    public List<Map> selectStorage(Map params);

    public Map selectTimeline(Map params);

    public Map selectStageDetail(Map params);
}
