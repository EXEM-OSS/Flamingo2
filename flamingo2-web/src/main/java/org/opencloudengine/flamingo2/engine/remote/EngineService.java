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
package org.opencloudengine.flamingo2.engine.remote;

import org.opencloudengine.flamingo2.agent.system.SystemUserService;
import org.opencloudengine.flamingo2.engine.batch.BatchService;
import org.opencloudengine.flamingo2.engine.designer.DesignerService;
import org.opencloudengine.flamingo2.engine.fs.FileSystemRemoteService;
import org.opencloudengine.flamingo2.engine.fs.audit.FileSystemAuditRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.HistoryServerRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.NamenodeRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.hawq.HawqService;
import org.opencloudengine.flamingo2.engine.history.TaskHistoryRemoteService;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRemoteService;
import org.opencloudengine.flamingo2.engine.hive.HiveMetastoreService;
import org.opencloudengine.flamingo2.engine.hive.HiveQueryRemoteService;
import org.opencloudengine.flamingo2.engine.monitoring.AlarmRemoteService;
import org.opencloudengine.flamingo2.engine.monitoring.CLDBRemoteService;
import org.opencloudengine.flamingo2.engine.pig.PigRemoteService;
import org.opencloudengine.flamingo2.engine.scheduler.SchedulerRemoteService;
import org.opencloudengine.flamingo2.engine.tree.TreeService;
import org.opencloudengine.flamingo2.engine.visual.VisualService;

/**
 * Flamingo Engine에서 제공하는 서비스에 접근할 수 있도록 관련 서비스를 모두 모아둔 Facade 인터페이스.
 */
public interface EngineService {

    HiveQueryRemoteService getHiveQueryService();

    FileSystemRemoteService getFileSystemService();

    HistoryServerRemoteService getHistoryServerRemoteService();

    ResourceManagerRemoteService getResourceManagerRemoteService();

    SystemMetricsRemoteService getSystemMetricsRemoteService();

    NamenodeRemoteService getNamenodeRemoteService();

    FileSystemAuditRemoteService getFileSystemAuditRemoteService();

    HiveMetastoreService getHiveMetastoreServcice();

    VisualService getVisualService();

    HawqService getHawqService();

    SystemUserService getSystemUserService();

    PigRemoteService getPigRemoteService();

    SchedulerRemoteService getSchedulerRemoteService();

    BatchService getBatchService();

    DesignerService getDesignerRemoteService();

    TaskHistoryRemoteService getTaskHistoryRemoteService();

    WorkflowHistoryRemoteService getWorkflowHistoryRemoteService();

    CLDBRemoteService getCLDBRemoteService();

    AlarmRemoteService getAlarmRemoteService();

    TreeService getTreeRemoteService();
}
