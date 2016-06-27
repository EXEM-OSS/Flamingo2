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
 * @since 2.0
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
        String ackKey = (String) hdfsBrowserAuthMap.get("ackKey");
        String hdfsPathPattern = (String) hdfsBrowserAuthMap.get("hdfsPathPattern");
        boolean inserted;

        if (hdfsBrowserAuthRepository.exist(hdfsBrowserAuthMap)) {
            throw new ServiceException("The pattern information that already exists.");
        }

        /**
         * Case 1. 관리자가 사용자 가입 승인 시 사용자의 HDFS Browser 사용 권한 정보가 추가되는 경우
         * Case 2. 관리자가 직접 사용자의 HDFS Browser 사용 권한 패턴 정보를 추가할 경우
         * Case 2.1 추가하려는 경로에 하위경로포함 옵션이 적용된 HDFS Browser 사용 권한 패턴 정보가 이미 있을 경우
         */
        if (ackKey.equalsIgnoreCase("approved")) {
            inserted = hdfsBrowserAuthRepository.insertHdfsBrowserAuthAll(hdfsBrowserAuthMap) > 0;
        } else {
            boolean applyAll = (Boolean) hdfsBrowserAuthMap.get("applyAll");
            if (!applyAll) {
                hdfsBrowserAuthMap.put("hdfsPathPattern", hdfsPathPattern + "/**");
                if (hdfsBrowserAuthRepository.exist(hdfsBrowserAuthMap)) {
                    throw new ServiceException("Please delete user's hdfs path pattern with sub-directory included(/**) option before add a hdfs path pattern without sub-directory included option.");
                }
            }
            inserted = hdfsBrowserAuthRepository.insertHdfsBrowserAuth(hdfsBrowserAuthMap) > 0;
        }

        return inserted;
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
        // 리턴되는 값은 0 또는 1
        if (hdfsBrowserAuthRepository.selectHdfsBrowserUserDirAuth(dirMap) != 1) {
            throw new ServiceException("You do not have permission.");
        }
    }

    @Override
    public void getHdfsBrowserUserFileAuth(Map<String, String> fileMap) {
        // 리턴되는 값은 0 또는 1
        if (hdfsBrowserAuthRepository.selectHdfsBrowserUserFileAuth(fileMap) != 1) {
            throw new ServiceException("You do not have permission.");
        }
    }

    @Override
    public boolean deleteHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        String deleteCondition = (String) hdfsBrowserAuthMap.get("deleteCondition");

        /**
         * Case 1. 사용자 삭제 시 해당 사용자에 부여된 모든 HDFS 패턴 정보를 삭제할 때
         * Case 1.1 삭제할 모든 HDFS 패턴을 정보를 조회한다.
         * Case 2. 특정 HDFS 패턴만 삭제할 때
         * Case 2.1 삭제할 단일 HDFS 패턴 정보를 조회한다.
         */
        if (deleteCondition.equalsIgnoreCase("deleteUser")) {
            // 리턴되는 값은 0 또는 1
            if (!hdfsBrowserAuthRepository.exist(hdfsBrowserAuthMap)) {
                throw new ServiceException("HDFS authority information does not exist to delete.");
            }
        } else {
            // 리턴되는 값은 0 또는 1
            if (!hdfsBrowserAuthRepository.exist(hdfsBrowserAuthMap)) {
                throw new ServiceException("HDFS authority information does not exist to delete.");
            }
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

            if (hdfsBrowserAuthRepository.exist(hdfsAuthKeyMap)) {
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
        /*        if (currentPath.equalsIgnoreCase(filter) && userLevel != 1) {
            throw new ServiceException("You do not have permission.");
        } */
        if (currentPath.equalsIgnoreCase(filter)) {
            throw new ServiceException("You do not have permission.");
        }
    }
}
