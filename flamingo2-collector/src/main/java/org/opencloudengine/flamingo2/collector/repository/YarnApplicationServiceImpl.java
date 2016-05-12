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
package org.opencloudengine.flamingo2.collector.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
@Service
public class YarnApplicationServiceImpl implements YarnApplicationService {

    @Autowired
    private YarnApplicationRepository repository;

    @Override
    public boolean exist(String systemId, String applicationId) {
        return repository.selectByApplicationId(systemId, applicationId) != null;
    }

    @Override
    public void confirm(String systemId, String applicationId, String applicationType, Map<String, Object> applicationReport, String log) throws IOException {
        repository.insetApplicationConfirm(systemId, applicationId, applicationType);
        repository.insertApplicationInfo(systemId, applicationId, applicationType, applicationReport, log);
    }
}
