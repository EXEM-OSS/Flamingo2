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
import org.opencloudengine.flamingo2.agent.system.SystemUserAgentService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.system.UserRemoteService;
import org.opencloudengine.flamingo2.util.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

public class SystemUserServiceDelegator implements UserRemoteService, InitializingBean {

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
                SystemUserAgentService agentService = getAgentService(url);
                if (!agentService.existUser(username)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to determine the existence of system users.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean createUser(String linuxUserHome, String username, String name, String password) {
        if (!FileUtils.pathValidator(linuxUserHome)) {
            throw new ServiceException("Invalid path. Please check the path.");
        }

        for (String url : agentUrls) {
            try {
                SystemUserAgentService agentService = getAgentService(url);
                if (agentService.existUser(username) || !agentService.createUser(username, name, password, linuxUserHome)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to create a system user.", ex);
            }
        }
        return true;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) {
        for (String url : agentUrls) {
            try {
                SystemUserAgentService agentService = getAgentService(url);
                if (!agentService.changeUser(username, newPassword)) {
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
                SystemUserAgentService agentService = getAgentService(url);
                if (!agentService.deleteUser(username)) {
                    return false;
                }
            } catch (Exception ex) {
                throw new ServiceException("Unable to delete a system user.", ex);
            }
        }
        return true;
    }

    /**
     * System Agent와 통신하기 위한 정보를 가져온다.
     *
     * @param url System Agent Url
     * @return System User Service
     */
    private SystemUserAgentService getAgentService(String url) {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(arrayFormat("http://{}/remote/agent/system", new Object[]{url}).getMessage());
        factoryBean.setServiceInterface(SystemUserAgentService.class);
        factoryBean.afterPropertiesSet();
        return (SystemUserAgentService) factoryBean.getObject();
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
