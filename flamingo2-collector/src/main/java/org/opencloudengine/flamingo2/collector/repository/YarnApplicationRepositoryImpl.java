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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
@Repository
public class YarnApplicationRepositoryImpl implements YarnApplicationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map selectByApplicationId(String systemId, String applicationId) {
        try {
            return jdbcTemplate.queryForMap("SELECT ID, SYSTEM, APP_ID, APP_TYPE FROM FL_CL_YARN WHERE APP_ID = ? AND SYSTEM = ?", new Object[]{applicationId, systemId});
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void insetApplicationConfirm(String systemId, String applicationId, String applicationType) {
        jdbcTemplate.update("INSERT INTO FL_CL_YARN (SYSTEM, APP_ID, APP_TYPE) VALUES (?,?,?)", new Object[]{systemId, applicationId, applicationType});
    }

    @Override
    public void insertApplicationInfo(String systemId, String applicationId, String applicationType, Map application, String log) {
        jdbcTemplate.update("INSERT INTO FL_CL_YARN_DUMP (system, applicationId, applicationType, progress, queue, memorySeconds, rpcPort, amHost, usedResourcesMemory, startTime, reservedResourcesVcores, reservedResourcesMemory, trackingUrl, yarnApplicationState, neededResourcesVcores, name, numReservedContainers, usedResourcesVcores, finishTime, numUsedContainers, finalApplicationStatus, user, neededResourcesMemory, vcoreSeconds, diagnostics, log) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                systemId,
                application.get("applicationId"),
                application.get("applicationType"),
                application.get("progress"),
                application.get("queue"),
                application.get("memorySeconds"),
                application.get("rpcPort"),
                application.get("amHost"),
                application.get("usedResourcesMemory"),
                new Date((Long) application.get("startTime")),
                application.get("reservedResourcesVcores"),
                application.get("reservedResourcesMemory"),
                application.get("trackingUrl"),
                application.get("yarnApplicationState").toString(),
                application.get("neededResourcesVcores"),
                application.get("name"),
                application.get("numReservedContainers"),
                application.get("usedResourcesVcores"),
                new Date((Long) application.get("finishTime")),
                application.get("numUsedContainers"),
                application.get("finalApplicationStatus").toString(),
                application.get("user"),
                application.get("neededResourcesMemory"),
                application.get("vcoreSeconds"),
                application.get("diagnostics"),
                log
        });
    }
}
