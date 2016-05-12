/**
 * Copyright (C) 2011 Flamingo Project (http://www.opencloudengine.org).
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
package org.opencloudengine.flamingo2.agent.system;

import org.hyperic.sigar.Sigar;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SigarFactory implements DisposableBean, FactoryBean<Sigar>, InitializingBean {

    Sigar sigar;

    @Override
    public void destroy() throws Exception {
        try {
            if (sigar != null) sigar.close();
        } catch (Exception ex) {
        }
    }

    @Override
    public Sigar getObject() throws Exception {
        return sigar;
    }

    @Override
    public Class<?> getObjectType() {
        return Sigar.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.sigar = new Sigar();
    }
}
