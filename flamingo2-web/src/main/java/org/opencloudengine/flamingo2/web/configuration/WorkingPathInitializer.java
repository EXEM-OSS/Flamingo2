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
package org.opencloudengine.flamingo2.web.configuration;

import org.apache.commons.io.FileUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class WorkingPathInitializer implements InitializingBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(WorkingPathInitializer.class);

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        makeIfNotExist(configurationHelper.getHelper().get("visual.rdata.tmp"));
        makeIfNotExist(configurationHelper.getHelper().get("visual.output"));
        makeIfNotExist(configurationHelper.getHelper().get("flamingo.workflow.logging.dir"));
        makeIfNotExist(configurationHelper.getHelper().get("artifact.cache.path"));
        makeIfNotExist(configurationHelper.getHelper().get("pig.temp.dir"));
    }

    private void makeIfNotExist(String path) {
        try {
            File file = new File(path);
            FileUtils.forceMkdir(file);
            logger.info("It created a working directory : {}", path);
        } catch (IOException e) {
            throw new ServiceException("You can not create a working directory: " + path, e);
        }
    }
}
