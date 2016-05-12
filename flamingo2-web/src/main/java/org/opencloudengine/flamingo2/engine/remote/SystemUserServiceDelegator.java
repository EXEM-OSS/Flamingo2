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
import org.opencloudengine.flamingo2.agent.system.SystemUserService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemUserServiceDelegator implements SystemUserService, InitializingBean {

    private String urls;

    private List<String> agentUrls;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.agentUrls = new ArrayList();
        String[] urls = StringUtils.splitPreserveAllTokens(this.urls, ",");
        Collections.addAll(agentUrls, urls);
    }

    @Override
    public boolean existUser(String username) {
        for (String url : agentUrls) {
            try {
                SystemUserService remoteService = getRemoteService(url);
                if (!remoteService.existUser(username)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to determine the existence of system users.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean createUser(String home, String name, String username) {
        for (String url : agentUrls) {
            try {
                SystemUserService remoteService = getRemoteService(url);
                if (!remoteService.createUser(home, name, username)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to create a system user.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean changeUser(String username, String password) {
        for (String url : agentUrls) {
            try {
                SystemUserService remoteService = getRemoteService(url);
                if (!remoteService.changeUser(username, password)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to change the password of the system user .", ex);
            }
        }
        return true;
    }

    @Override
    public boolean deleteUser(String username) {
        for (String url : agentUrls) {
            try {
                SystemUserService remoteService = getRemoteService(url);
                if (!remoteService.deleteUser(username)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to delete a system user.", ex);
            }
        }
        return true;
    }

    private SystemUserService getRemoteService(String url) {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(url);
        factoryBean.setServiceInterface(SystemUserService.class);
        factoryBean.afterPropertiesSet();
        return (SystemUserService) factoryBean.getObject();
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
