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
package org.opencloudengine.flamingo2.web.uima;

import org.opencloudengine.flamingo2.model.rest.Uima;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 로컬 DB에 저장된 플라밍고 UIMA Log 정보를 관리하기 위한 UIMA Service Implements
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
@Service
public class UimaServiceImpl implements UimaService {

    @Autowired
    UimaRepository uimaRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public int getTotalCount(Map uimaMap) {
        return uimaRepository.selectTotalCount(uimaMap);
    }

    @Override
    public List<Uima> getAllUimaLogs(Map uimaMap) {
        return uimaRepository.selectAllUimaLogs(uimaMap);
    }

    @Override
    public List<Map<String, Object>> getUimaLogSummary(Map uimaMap) {
        return uimaRepository.selectUimaLogSummary(uimaMap);
    }
}
