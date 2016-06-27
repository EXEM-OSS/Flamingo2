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

import java.util.List;
import java.util.Map;

/**
 * UIMA Log 정보에 대한 CRUD 기능을 처리하는 UIMA Repository
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
public interface UimaRepository {

    String NAMESPACE = UimaRepository.class.getName();

    int selectTotalCount(Map uimaMap);

    List<Uima> selectAllUimaLogs(Map uimaMap);

    List<Map<String,Object>> selectUimaLogSummary(Map uimaMap);
}
