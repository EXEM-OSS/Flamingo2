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
package org.opencloudengine.flamingo2.collector.jobs;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public abstract class RemoteInvocation extends QuartzJobBean {

    public <T> T getRemoteService(String url, Class<T> clazz) {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(url);
        factoryBean.setServiceInterface(clazz);
        factoryBean.afterPropertiesSet();
        return (T) factoryBean.getObject();
    }

    public String getRemoteServiceUrl(String ip, int port, String serviceName) {
        return arrayFormat("http://{}:{}/remote/agent/{}", new Object[]{ip, port, serviceName}).getMessage();
    }
}
