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
package org.opencloudengine.flamingo2.web.system;

import io.cloudine.flamingo2.license.LicenseUtil;
import org.apache.commons.io.IOUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class LicenseServiceImpl implements LicenseService, InitializingBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(LicenseServiceImpl.class);

    @Value("#{config['license.file.path']}")
    String licenseFilePath;

    @Value("#{config['license.filename']}")
    String licenseFilename;

    @Override
    public void afterPropertiesSet() throws Exception {
        new LicenseUtil(licenseFilePath);
    }

    public void regist(String filename, String license) {
        byte[] trns = license.getBytes();
        FileOutputStream fos = null;
        try {
            if (!new File(licenseFilePath).exists()) {
                new File(licenseFilePath).mkdirs();
            }

            fos = new FileOutputStream(new File(licenseFilePath, filename + ".lic"));
            FileCopyUtils.copy(trns, fos);
        } catch (IOException e) {
            throw new ServiceException("The license file can not be created.", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    @Override
    public Map getLicenseInfo() {
        return LicenseUtil.getLicenseInfo(licenseFilename);
    }

    @Override
    public boolean isValid(String filename) {
        return LicenseUtil.isValid(filename);
    }

    @Override
    public boolean isTrial() {
        Map licenseMap = getLicenseInfo();

        if (licenseMap.get("LICENSE_TYPE").equals("TRIAL")) {
            return true;
        }

        return false;
    }

    @Override
    public String getServerId() {
        return LicenseUtil.getServerId();
    }

    @Override
    public String getMaxNode(String filename) {
        return LicenseUtil.getMaxNode(filename);
    }

    @Override
    public void deleteLicense(String filename) {
        try {
            File file = new File(licenseFilePath, filename + ".lic");

            file.delete();
        } catch (Exception e) {
            logger.warn("Can not delete the license file");
        }
    }
}

