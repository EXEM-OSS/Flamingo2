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

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HDFS Browser의 사용 권한을 관리하기 위한 HDFS Browser Authority Service Implements
 *
 * @author Myeongha KIM
 */
@Service
public class HdfsBrowserAuthServiceImpl implements HdfsBrowserAuthService {

    @Autowired
    HdfsBrowserAuthRepository hdfsBrowserAuthRepository;

    @Override
    public List<Map> getHdfsAuthAll() {
        return hdfsBrowserAuthRepository.selectHdfsAuthAll();
    }

    @Override
    public Map getHdfsBrowserAuthDetail(Map hdfsAuthMap) {
        return hdfsBrowserAuthRepository.selectHdfsAuthDetail(hdfsAuthMap);
    }

    @Override
    public List<Map> getUserAuthAll() {
        return hdfsBrowserAuthRepository.selectUserAuth();
    }

    @Override
    public List<Map> getUserLevelAll() {
        return hdfsBrowserAuthRepository.selectUserLevel();
    }

    @Override
    public boolean createHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        if (hdfsBrowserAuthRepository.exist(hdfsBrowserAuthMap) > 0) {
            throw new ServiceException("The pattern information that already exists.");
        }

        return hdfsBrowserAuthRepository.insertHdfsBrowserAuth(hdfsBrowserAuthMap) > 0;
    }

    @Override
    public List<String> getHdfsBrowserPatternAll(String username) {
        List<String> paths = hdfsBrowserAuthRepository.selectHdfsBrowserPatternAll(username);

        if (paths.isEmpty()) {
            throw new ServiceException("Pattern information does not exist.");
        }

        return paths;
    }

    @Override
    public void getHdfsBrowserUserDirAuth(Map<String, String> dirMap) {
        if (hdfsBrowserAuthRepository.selectHdfsBrowserUserDirAuth(dirMap) < 1) {
            throw new ServiceException("You do not have permission.");
        }
    }

    @Override
    public void getHdfsBrowserUserFileAuth(Map<String, String> fileMap) {
        if (hdfsBrowserAuthRepository.selectHdfsBrowserUserFileAuth(fileMap) < 1) {
            throw new ServiceException("You do not have permission.");
        }
    }

    @Override
    public boolean deleteHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        if (hdfsBrowserAuthRepository.exist(hdfsBrowserAuthMap) < 1) {
            throw new ServiceException("HDFS authority information does not exist to delete.");
        }

        return hdfsBrowserAuthRepository.deleteHdfsBrowserAuth(hdfsBrowserAuthMap) > 0;
    }

    @Override
    public boolean updateHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        Map updateValuesMap = new HashMap();

        updateValuesMap.putAll((Map) hdfsBrowserAuthMap.get("hdfsAuthModFormValues"));

        String newHdfsPathPattern = (String) updateValuesMap.get("new_hdfs_path_pattern");
        String newUserAuth = (String) updateValuesMap.get("new_user_auth");
        String newUserLevel = (String) updateValuesMap.get("new_user_level");

        // 패턴, 권한, 등급이 변경된 경우 키값으로 조회
        if (newHdfsPathPattern.equalsIgnoreCase("isNewValue") || newUserAuth.equalsIgnoreCase("isNewValue")
                || newUserLevel.equalsIgnoreCase("isNewValue")) {
            Map hdfsAuthKeyMap = new HashMap();
            hdfsAuthKeyMap.put("hdfsPathPattern", updateValuesMap.get("hdfs_path_pattern"));
            hdfsAuthKeyMap.put("authId", updateValuesMap.get("auth_id"));
            hdfsAuthKeyMap.put("level", updateValuesMap.get("level"));

            if (hdfsBrowserAuthRepository.exist(hdfsAuthKeyMap) > 0) {
                throw new ServiceException("The pattern information that already exists.");
            }
        }

        return hdfsBrowserAuthRepository.updatedHdfsBrowserAuth(hdfsBrowserAuthMap) > 0;
    }

    @Override
    public String validateHdfsPathPattern(String dirPath, List<String> paths) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String authPattern = null;

        for (String pattern : paths) {
            boolean isMatch = antPathMatcher.match(pattern, dirPath);

            if (isMatch) {
                authPattern = pattern;
                break;
            }
        }

        if (authPattern == null) {
            throw new ServiceException("You do not have permission.");
        }

        return authPattern;
    }

    @Override
    public void validateHdfsHomeWritePermission(String currentPath, String filter, int userLevel) {
        if (currentPath.equalsIgnoreCase(filter) && userLevel != 1) {
            throw new ServiceException("You do not have permission.");
        }
    }
}
