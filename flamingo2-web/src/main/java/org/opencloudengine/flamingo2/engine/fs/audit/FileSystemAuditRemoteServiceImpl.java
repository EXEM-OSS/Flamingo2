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
package org.opencloudengine.flamingo2.engine.fs.audit;

import org.opencloudengine.flamingo2.model.rest.*;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FileSystemAuditRemoteServiceImpl implements FileSystemAuditRemoteService {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(FileSystemAuditRemoteServiceImpl.class);

    @Override
    public List<AuditHistory> getAuditHistories(Map auditConditionMap) {
        AuditRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(AuditRepository.class);
        return repository.selectByCondition(auditConditionMap);
    }

    @Override
    public int getTotalCountOfAuditHistories(String startDate, String endDate, String path, String auditType, String username) {
        AuditRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(AuditRepository.class);
        return repository.getTotalCountByCondition(startDate, endDate, path, auditType, username);
    }

    @Override
    public void log(String engineId, String engineName, String username, FileSystemType fileSystemType, AuditType auditType, FileType fileType, RequestType requestType, String from, String to, long length) {
        AuditRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(AuditRepository.class);
        AuditHistory history = new AuditHistory();
        history.setClusterId(engineId);
        history.setClusterName(engineName);
        history.setUsername(username);
        history.setWorkDate(new Date());
        history.setFileSystemType(fileSystemType);
        history.setAuditType(auditType);
        history.setFileType(fileType);
        history.setRequestType(requestType);
        history.setFrom(from);
        history.setTo(to);
        history.setLength(length);

        repository.insert(history);
    }

    @Override
    public List<Top10> auditTop10(String startDate, String endDate, String searchType, String username) {
        AuditRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(AuditRepository.class);
        return repository.auditTop10(startDate, endDate, searchType, username);
    }

    @Override
    public List<NowStatus> auditNowStatus(String startDate, String endDate, String searchType, String username) {
        AuditRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(AuditRepository.class);
        return repository.auditNowStatus(startDate, endDate, searchType, username);
    }

    @Override
    public List<Trends> auditTrend(String startDate, String endDate, String searchType, String username) {
        AuditRepository repository = ApplicationContextRegistry.getApplicationContext().getBean(AuditRepository.class);
        return repository.auditTrend(startDate, endDate, searchType, username);
    }
}
