package org.opencloudengine.flamingo2.util;

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
