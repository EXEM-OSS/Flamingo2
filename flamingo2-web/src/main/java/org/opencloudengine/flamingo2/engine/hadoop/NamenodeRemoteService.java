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
package org.opencloudengine.flamingo2.engine.hadoop;

import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Hadoop 2 Namenode Agent와 통신하며 Namenode 관련 기능을 제공하는 서비스.
 */
public interface NamenodeRemoteService {

    Map getNamenodeInfo(EngineConfig engineConfig) throws IOException;

    Map getConfiguration(EngineConfig engineConfig) throws IOException;

    Map getMetrics(EngineConfig engineConfig) throws IOException;

    List getDatanodes(EngineConfig engineConfig) throws IOException;

    String getFileContents(EngineConfig engineConfig, String path, int chunkSize, long startOffset) throws IOException;

    Map<String, Long> getJVMHeap(EngineConfig engineConfig) throws IOException;

    List getDataNodes(EngineConfig engineConfig);

    List getLiveNodes(EngineConfig engineConfig);

    List getDeadNodes(EngineConfig engineConfig);

    List getDecommissioningNodes(EngineConfig engineConfig);

    List getTop5(EngineConfig engineConfig) throws IOException;

    List getRecentTrend(EngineConfig engineConfig);

}
