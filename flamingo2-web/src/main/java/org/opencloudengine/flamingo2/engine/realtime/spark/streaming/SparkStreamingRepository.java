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
package org.opencloudengine.flamingo2.engine.realtime.spark.streaming;

import org.opencloudengine.flamingo2.core.repository.PersistentRepository;
import org.opencloudengine.flamingo2.model.rest.IoTService;
import org.opencloudengine.flamingo2.model.rest.SparkStreaming;
import org.opencloudengine.flamingo2.model.rest.SparkStreamingHistory;

import java.util.List;
import java.util.Map;

public interface SparkStreamingRepository extends PersistentRepository<SparkStreaming, Long> {

    String NAMESPACE = SparkStreamingRepository.class.getName();

    List<IoTService> selectIoTServices(Map ioTMap);

    int selectTotalCountOfSparkStreamingApps(Map sparkStreamingMap);

    List<SparkStreaming> selectAllSparkStreamingApps(Map sparkStreamingMap);

    List<Map<String, Object>> selectAllSparkStreamingAppsSummary(Map sparkStreamingMap);

    int insertSparkStreamingApp(Map sparkStreamingMap);

    boolean updateSparkStreamingApp(Map sparkStreamingMap);

    boolean deleteSparkStreamingApp(String applicationId);

    int selectOptimalServer(String agent);

}
