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
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

/**
 * Remote Invocation 방식으로 다른 JVM 상에서 Engine에서 제공하는 서비스를 제공하는 Engine Service 구현체.
 * 이 설정은 <tt>/WEB-INF/hadoop.properties</tt> 파일의 <tt>remote.engine.enabled</tt> 설정값이 <tt>true</tt>인 경우 동작한다.
 * 또한 이렇게 설정된 경우 <tt>*.engine.address</tt>과 <tt>*.engine.port</tt>으로 정의한 값에 따라서 다른 JVM에 배포되어 있는
 * Flamingo Engine에 접근하게 된다.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class RemoteEngineServiceImpl implements EngineService {

    /**
     * Flamingo Engine Configuration
     */
    private EngineConfig engineConfig;

    public RemoteEngineServiceImpl(EngineConfig engineConfig) {
        this.engineConfig = engineConfig;
    }

    @Override
    public HiveQueryRemoteService getHiveQueryService() {
        String url = getRemoteServiceUrl("hive");
        return getRemoteService(url, HiveQueryRemoteService.class);
    }

    @Override
    public FileSystemRemoteService getFileSystemService() {
        String url = getRemoteServiceUrl("hdfs");
        return getRemoteService(url, FileSystemRemoteService.class);
    }

    @Override
    public HistoryServerRemoteService getHistoryServerRemoteService() {
        String url = getRemoteServiceUrl("history");
        return getRemoteService(url, HistoryServerRemoteService.class);
    }

    @Override
    public ResourceManagerRemoteService getResourceManagerRemoteService() {
        String url = getRemoteServiceUrl("resourcemanager");
        return getRemoteService(url, ResourceManagerRemoteService.class);
    }

    @Override
    public SystemMetricsRemoteService getSystemMetricsRemoteService() {
        String url = getRemoteServiceUrl("system");
        return getRemoteService(url, SystemMetricsRemoteService.class);
    }

    @Override
    public NamenodeRemoteService getNamenodeRemoteService() {
        String url = getRemoteServiceUrl("namenode");
        return getRemoteService(url, NamenodeRemoteService.class);
    }

    @Override
    public FileSystemAuditRemoteService getFileSystemAuditRemoteService() {
        String url = getRemoteServiceUrl("audit");
        return getRemoteService(url, FileSystemAuditRemoteService.class);
    }

    @Override
    public HiveMetastoreService getHiveMetastoreServcice() {
        String url = getRemoteServiceUrl("hivemetastore");
        return getRemoteService(url, HiveMetastoreService.class);
    }

    @Override
    public VisualService getVisualService() {
        String url = getRemoteServiceUrl("visual");
        return getRemoteService(url, VisualService.class);
    }

    @Override
    public HawqService getHawqService() {
        String url = getRemoteServiceUrl("hawq");
        return getRemoteService(url, HawqService.class);
    }

    @Override
    public SystemUserService getSystemUserService() {
        String url = getRemoteServiceUrl("systemuser");
        return getRemoteService(url, SystemUserService.class);
    }

    @Override
    public PigRemoteService getPigRemoteService() {
        String url = getRemoteServiceUrl("pig");
        return getRemoteService(url, PigRemoteService.class);
    }

    @Override
    public SchedulerRemoteService getSchedulerRemoteService() {
        String url = getRemoteServiceUrl("scheduler");
        return getRemoteService(url, SchedulerRemoteService.class);
    }

    @Override
    public BatchService getBatchService() {
        String url = getRemoteServiceUrl("batch");
        return getRemoteService(url, BatchService.class);
    }

    @Override
    public DesignerService getDesignerRemoteService() {
        String url = getRemoteServiceUrl("designer");
        return getRemoteService(url, DesignerService.class);
    }

    @Override
    public TaskHistoryRemoteService getTaskHistoryRemoteService() {
        String url = getRemoteServiceUrl("taskHistory");
        return getRemoteService(url, TaskHistoryRemoteService.class);
    }

    @Override
    public WorkflowHistoryRemoteService getWorkflowHistoryRemoteService() {
        String url = getRemoteServiceUrl("workflowHistory");
        return getRemoteService(url, WorkflowHistoryRemoteService.class);
    }

    @Override
    public CLDBRemoteService getCLDBRemoteService() {
        String url = getRemoteServiceUrl("cldb");
        return getRemoteService(url, CLDBRemoteService.class);
    }

    @Override
    public AlarmRemoteService getAlarmRemoteService() {
        String url = getRemoteServiceUrl("alarm");
        return getRemoteService(url, AlarmRemoteService.class);
    }

    @Override
    public TreeService getTreeRemoteService() {
        String url = getRemoteServiceUrl("tree");
        return getRemoteService(url, TreeService.class);
    }

    private <T> T getRemoteService(String url, Class<T> clazz) {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(url);
        factoryBean.setServiceInterface(clazz);
        factoryBean.afterPropertiesSet();
        return (T) factoryBean.getObject();
    }

    private String getRemoteServiceUrl(String serviceName) {
        return arrayFormat("http://{}:{}/remote/{}", new Object[]{"locahost", "9090", serviceName}).getMessage();
    }

}
