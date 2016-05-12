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
package org.opencloudengine.flamingo2.collector;

import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.Receiver;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class JGroupsChannelFactory implements DisposableBean, FactoryBean<Channel>, InitializingBean {

    private String config;

    private String clusterName;

    private JChannel channel;

    private Receiver receiver;

    @Override
    public void afterPropertiesSet() throws Exception {
        channel = new JChannel();
        channel.connect(clusterName);
        channel.setOpt(Channel.AUTO_RECONNECT, Boolean.TRUE);
        if (receiver != null) channel.setReceiver(receiver);
    }

    @Override
    public Channel getObject() throws Exception {
        return channel;
    }

    @Override
    public Class<?> getObjectType() {
        return Channel.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        if (!channel.isOpen()) {
            channel.close();
        }
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
