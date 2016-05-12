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


import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

public class HttpInvokerRequestExecutorFactoryBean implements FactoryBean, InitializingBean {

    private HttpComponentsHttpInvokerRequestExecutor requestExecutor;

    private int maxTotalConnections = 100;

    private int maxConnectionsPerRoute = 4;

    private int readTimeout = 60 * 1000;

    private int connectionTimeout = 3 * 1000;

    @Override
    public Object getObject() throws Exception {
        return requestExecutor;
    }

    @Override
    public Class<?> getObjectType() {
        return HttpInvokerRequestExecutor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        org.apache.http.conn.scheme.SchemeRegistry schemeRegistry = new org.apache.http.conn.scheme.SchemeRegistry();
        schemeRegistry.register(new org.apache.http.conn.scheme.Scheme("http", 80, org.apache.http.conn.scheme.PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new org.apache.http.conn.scheme.Scheme("https", 443, org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory()));

        org.apache.http.impl.conn.PoolingClientConnectionManager connectionManager = new org.apache.http.impl.conn.PoolingClientConnectionManager(schemeRegistry);
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

        this.requestExecutor = new HttpComponentsHttpInvokerRequestExecutor(new org.apache.http.impl.client.DefaultHttpClient(connectionManager));
        this.requestExecutor.setReadTimeout(readTimeout);
        this.requestExecutor.setConnectTimeout(connectionTimeout);
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout * 1000;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout * 1000;
    }
}
