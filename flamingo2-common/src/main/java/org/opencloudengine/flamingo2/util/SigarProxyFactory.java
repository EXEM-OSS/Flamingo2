package org.opencloudengine.flamingo2.util;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SigarProxyFactory implements FactoryBean<SigarProxy>, InitializingBean {

    SigarProxy proxy;

    Sigar sigar;

    @Override
    public SigarProxy getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return SigarProxy.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        proxy = SigarProxyCache.newInstance(this.sigar);
    }

    public void setSigar(Sigar sigar) {
        this.sigar = sigar;
    }
}
