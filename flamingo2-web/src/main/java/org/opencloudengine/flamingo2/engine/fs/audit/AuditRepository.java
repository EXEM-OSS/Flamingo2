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

import org.opencloudengine.flamingo2.core.repository.PersistentRepository;
import org.opencloudengine.flamingo2.model.rest.AuditHistory;
import org.opencloudengine.flamingo2.model.rest.NowStatus;
import org.opencloudengine.flamingo2.model.rest.Top10;
import org.opencloudengine.flamingo2.model.rest.Trends;

import java.util.List;
import java.util.Map;

public interface AuditRepository extends PersistentRepository<AuditHistory, Long> {

    String NAMESPACE = AuditRepository.class.getName();

    /**
     * 지정한 조건의 파일 시스템 처리 이력을 조회한다.
     *
     * auditConditionMap 처리 이력 조건 정보
     * @return 파일 시스템 실행 이력 목록
     */
    List<AuditHistory> selectByCondition(Map auditConditionMap);

    /**
     * 지정한 사용자의 파일 시스템 처리 이력의 개수룰 반환한다.
     *
     * @param startDate 시작날짜
     * @param endDate   종료 날짜
     * @param path      조회할 경로
     * @param auditType 파일 처리 유형
     * @param username  사용자명
     * @return 파일 시스템 처리 이력의 개수
     */
    int getTotalCountByCondition(String startDate, String endDate, String path, String auditType, String username);

    /**
     * 사용자 별 파일 시스템 처리 순위를 조회한다.
     *
     * @param startDate 시작날짜
     * @param endDate   종료 날짜
     * @param type      파일 처리 유형
     * @param username  사용자명
     * @return 사용자 별 파일 시스템 처리 순위
     */

    List<Top10> auditTop10(String startDate, String endDate, String type, String username);

    /**
     * 현재 파일 시스템 처리 이력을 조회한다.
     *
     * @param startDate 시작날짜
     * @param endDate   종료 날짜
     * @param type      파일 처리 유형
     * @param username  사용자명
     * @return 사용자 별 파일 시스템 처리 순위
     */
    List<NowStatus> auditNowStatus(String startDate, String endDate, String type, String username);

    /**
     * 기간별 파일 시스템 처리 흐름을 반환한다.
     *
     * @param startDate 시작날짜
     * @param endDate   종료 날짜
     * @param type      파일 처리 유형
     * @param username  사용자명
     * @return 사용자 별 파일 시스템 처리 순위
     */
    List<Trends> auditTrend(String startDate, String endDate, String type, String username);
}
