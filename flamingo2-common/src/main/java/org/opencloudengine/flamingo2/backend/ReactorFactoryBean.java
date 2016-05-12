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
package org.opencloudengine.flamingo2.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.ReactorSpec;
import reactor.core.spec.Reactors;
import reactor.function.Consumer;
import reactor.spring.factory.EventRouting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static reactor.event.selector.Selectors.$;

public class ReactorFactoryBean implements FactoryBean<Reactor>, InitializingBean, ApplicationContextAware {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(ReactorFactoryBean.class);

    private volatile Reactor reactor;

    private Map<String, Consumer> consumers;

    private ApplicationContext applicationContext;

    public ReactorFactoryBean(Environment env) {
        this(env, null, null);
    }

    public ReactorFactoryBean(Environment env, String dispatcher) {
        this(env, dispatcher, null);
    }

    public ReactorFactoryBean(Environment env, String dispatcher, EventRouting eventRouting) {
        Assert.notNull(env, "Environment cannot be null.");

        ReactorSpec spec = Reactors.reactor().env(env);
        if (null != dispatcher) {
            if ("sync".equals(dispatcher)) {
                spec.synchronousDispatcher();
            } else {
                spec.dispatcher(dispatcher);
            }
        }
        if (null != eventRouting) {
            switch (eventRouting) {
                case BROADCAST_EVENT_ROUTING:
                    spec.broadcastEventRouting();
                    break;
                case RANDOM_EVENT_ROUTING:
                    spec.randomEventRouting();
                    break;
                case ROUND_ROBIN_EVENT_ROUTING:
                    spec.roundRobinEventRouting();
                    break;
            }
        }
        this.reactor = spec.get();
    }

    @Override
    public Reactor getObject() throws Exception {
        return reactor;
    }

    @Override
    public Class<?> getObjectType() {
        return Reactor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.consumers == null || this.consumers.size() == 0) {
            this.consumers = new HashMap();

            Map<String, Consumer> beans = applicationContext.getBeansOfType(Consumer.class);
            Set<String> names = beans.keySet();
            for (String name : names) {
                Consumer consumer = beans.get(name);
                ConsumerNameAware aware = (ConsumerNameAware) consumer;
                this.consumers.put(aware.getName(), consumer);
            }

            Set<String> selectors = consumers.keySet();
            for (String selector : selectors) {
                Consumer consumer = consumers.get(selector);
                reactor.on($(selector), consumer);
                logger.info("[Reactor] Registered :: Selector '{}'", selector);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
