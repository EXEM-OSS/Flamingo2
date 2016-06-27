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

import org.opencloudengine.flamingo2.agent.spark.SparkCollectorRemoteService;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.List;
import java.util.Map;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

/**
 * Created by Hyokun Park on 15. 8. 14..
 */
public class SparkRemoteServiceImpl implements SparkRemoteService {

    @Override
    public List<Map> getJobChart() {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectJobChart();
    }

    @Override
    public List<Map> getApplications(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectApplications(params);
    }

    @Override
    public List<Map> getJobs(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectJobs(params);
    }

    @Override
    public List<Map> getStages(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectStages(params);
    }

    @Override
    public List<Map> getEnvironment(EngineConfig engineConfig, Map params) {
        SparkCollectorRemoteService remoteService = getSparkCollectorRemoteService(engineConfig);
        return remoteService.getEnvironment(params);
    }

    @Override
    public List<Map> getExecutors(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectExecutors(params);
    }

    @Override
    public List<Map> getStorage(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectStorage(params);
    }

    @Override
    public Map getTimeline(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectTimeline(params);
    }

    @Override
    public Map getStageDetail(Map params) {
        SparkRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(SparkRepository.class);
        return repository.selectStageDetail(params);
    }

    @Override
    public Map getTaskTimeline(EngineConfig engineConfig, Map params) {
        SparkCollectorRemoteService remoteService = getSparkCollectorRemoteService(engineConfig);
        return remoteService.getTaskTimeline(params);
    }

    private SparkCollectorRemoteService getSparkCollectorRemoteService(EngineConfig engineConfig) {
        String url =  arrayFormat("http://{}:{}/remote/sparkCollector", new Object[]{engineConfig.getSparkCollectorAddress(), engineConfig.getSparkCollectorPort()}).getMessage();
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(url);
        factoryBean.setServiceInterface(SparkCollectorRemoteService.class);
        factoryBean.afterPropertiesSet();
        return (SparkCollectorRemoteService) factoryBean.getObject();
    }
}
