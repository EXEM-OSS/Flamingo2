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
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.springframework.context.ApplicationContext;

/**
 * Call By Reference로 동일 JVM 상에서 Engine에서 제공하는 서비스를 제공하는 Engine Service 구현체.
 * 이 설정은 <tt>/WEB-INF/hadoop.properties</tt> 파일의 <tt>remote.engine.enabled</tt> 설정값이 <tt>false</tt>인 경우 동작한다.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class LocalEngineServiceImpl implements EngineService {

    @Override
    public HiveQueryRemoteService getHiveQueryService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(HiveQueryRemoteService.class);
    }

    @Override
    public FileSystemRemoteService getFileSystemService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        // TODO : 구현체가 여러개인 경우 Interface로 lookup하면 안됨
        return applicationContext.getBean(FileSystemRemoteService.class);
    }

    @Override
    public HistoryServerRemoteService getHistoryServerRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(HistoryServerRemoteService.class);
    }

    @Override
    public ResourceManagerRemoteService getResourceManagerRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(ResourceManagerRemoteService.class);
    }

    @Override
    public SystemMetricsRemoteService getSystemMetricsRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(SystemMetricsRemoteService.class);
    }

    @Override
    public NamenodeRemoteService getNamenodeRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(NamenodeRemoteService.class);
    }

    @Override
    public FileSystemAuditRemoteService getFileSystemAuditRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(FileSystemAuditRemoteService.class);
    }

    @Override
    public HiveMetastoreService getHiveMetastoreServcice() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(HiveMetastoreService.class);
    }

    @Override
    public VisualService getVisualService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(VisualService.class);
    }

    @Override
    public HawqService getHawqService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(HawqService.class);
    }

    @Override
    public SystemUserService getSystemUserService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(SystemUserService.class);
    }

    @Override
    public PigRemoteService getPigRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(PigRemoteService.class);
    }

    @Override
    public SchedulerRemoteService getSchedulerRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(SchedulerRemoteService.class);
    }

    @Override
    public BatchService getBatchService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(BatchService.class);
    }

    @Override
    public DesignerService getDesignerRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(DesignerService.class);
    }

    @Override
    public TaskHistoryRemoteService getTaskHistoryRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(TaskHistoryRemoteService.class);
    }

    @Override
    public WorkflowHistoryRemoteService getWorkflowHistoryRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(WorkflowHistoryRemoteService.class);
    }

    @Override
    public CLDBRemoteService getCLDBRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(CLDBRemoteService.class);
    }

    @Override
    public AlarmRemoteService getAlarmRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(AlarmRemoteService.class);
    }

    @Override
    public TreeService getTreeRemoteService() {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        return applicationContext.getBean(TreeService.class);
    }
}
