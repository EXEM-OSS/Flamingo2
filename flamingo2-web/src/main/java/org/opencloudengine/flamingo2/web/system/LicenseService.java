package org.opencloudengine.flamingo2.web.system;

import java.util.Map;

public interface LicenseService {

    void regist(String filename, String license);

    public Map getLicenseInfo();

    public boolean isValid(String filename);

    public boolean isTrial();

    public String getServerId();

    public String getMaxNode(String filename);

    public void deleteLicense(String filename);
}
