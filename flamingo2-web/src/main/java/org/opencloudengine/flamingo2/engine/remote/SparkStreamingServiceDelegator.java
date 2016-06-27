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

import org.apache.commons.lang.StringUtils;
import org.opencloudengine.flamingo2.agent.spark.streaming.SparkStreamingRemoteService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SparkStreamingServiceDelegator implements SparkStreamingRemoteService, InitializingBean {

    private String urls;

    private List<String> agentUrls;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.agentUrls = new ArrayList();
        String[] urls = StringUtils.splitPreserveAllTokens(this.urls, ",");
        Collections.addAll(agentUrls, urls);
    }

    @Override
    public boolean createSparkApplication(String sparkUserWorkingPath) {
        for (String url : agentUrls) {
            try {
                SparkStreamingRemoteService remoteService = getRemoteService(url);
                if (!remoteService.createSparkApplication(sparkUserWorkingPath)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to delete a system user.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean createSparkApplication(String jarFileFQP, byte[] jarFileBytes) {
        for (String url : agentUrls) {
            try {
                SparkStreamingRemoteService remoteService = getRemoteService(url);
                if (!remoteService.createSparkApplication(jarFileFQP, jarFileBytes)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to delete a system user.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean startSparkStreamingApp(Map sparkStreamingMap, Map<String, String> defaultEnvs) {
        for (String url : agentUrls) {
            try {
                SparkStreamingRemoteService remoteService = getRemoteService(url);
                if (!remoteService.startSparkStreamingApp(sparkStreamingMap, defaultEnvs)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to start spark streaming application.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean stopSparkStreamingApp(Map sparkStreamingMap) {
        for (String url : agentUrls) {
            try {
                SparkStreamingRemoteService remoteService = getRemoteService(url);
                if (!remoteService.stopSparkStreamingApp(sparkStreamingMap)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to kill spark streaming application.", ex);
            }
        }
        return true;
    }

    private SparkStreamingRemoteService getRemoteService(String url) {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(url);
        factoryBean.setServiceInterface(SparkStreamingRemoteService.class);
        factoryBean.afterPropertiesSet();
        return (SparkStreamingRemoteService) factoryBean.getObject();
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
