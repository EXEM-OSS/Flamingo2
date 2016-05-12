/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.collector.repository;

import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public interface MapReduceJobRepository {

    /**
     * Job ID 및 System ID로 해당 MapReduce Job 기록이 있는지 확인한다.
     *
     * @param systemId System ID (예; <tt>default</tt>)
     * @param jobId    MapReduce Job ID
     * @return MapReduce Job 정보
     */
    Map selectByJobId(String systemId, String jobId);

    /**
     * MapReduce Job의 수집 기록을 기록한다.
     *
     * @param systemId System ID (예; <tt>default</tt>)
     * @param jobId    MapReduce Job ID
     */
    void insertMapReduceJobConfirm(String systemId, String jobId);

    /**
     * MapReduce Job 정보를 기록한다.
     *
     * @param systemId System ID (예; <tt>default</tt>)
     * @param jobId    MapReduce Job ID
     * @param job      MapReduce Job 메타정보
     * @param jobConf  MapReduce Job Configuration
     * @param attempts MapReduce Map, Reduce Task
     * @param counters Job Counter
     * @param jobType  Job의 유형 (예; <tt>MAPREDUCE</tt>, <tt>PIG</tt>, <tt>HIVE</tt>)
     * @param username Flamingo의 사용자명
     */
    void insertMapReduceJobInfo(String systemId, String jobId, Map job, String jobConf, String attempts, String counters, String jobType, String username);
}
