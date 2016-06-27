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
package org.opencloudengine.flamingo2.engine.realtime.spark.streaming;

import org.opencloudengine.flamingo2.model.rest.IoTService;
import org.opencloudengine.flamingo2.model.rest.SparkStreaming;
import org.opencloudengine.flamingo2.model.rest.SparkStreamingHistory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Spark Streaming Service Remote Interface.
 *
 * @author Myeongha KIM
 * @since 2.1
 */
public interface SparkStreamingRemoteService {

    List<IoTService> getIoTServices(Map ioTMap);

    /**
     * Spark Streaming 애플리케이션을 원격지의 Spark Streaming 홈 디렉토리에 저장한다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @return true or false
     */
    boolean createSparkApplication(Map sparkStreamingMap);

    /**
     * Spark Streaming 애플리케이션을 원격지의 Spark Streaming 홈 디렉토리에 저장한다. (사용자가 업로드할 Jar 파일 포함)
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @param jarFileBytes      사용자가 업로드할 Jar 파일
     * @return true or false
     */
    boolean createSparkApplication(Map sparkStreamingMap, byte[] jarFileBytes);

    /**
     * 선택한 Spark Streaming 애플리케이션을 실행한다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @param defaultEnvs       애플리케이션 실행에 필요한 환경변수
     * @return true or false
     */
    boolean startSparkStreamingApp(Map sparkStreamingMap, Map<String, String> defaultEnvs);

    /**
     * 선택한 Spark Streaming 애플리케이션을 중지한다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @return true or false
     */
    boolean stopSparkStreamingApp(Map sparkStreamingMap);

    /**
     * 선택한 Spark Streaming 애플리케이션을 종료 및 해당 정보를 모두 삭제한다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @return true or false
     */
    boolean killSparkStreamingApp(Map sparkStreamingMap);

    /**
     * 등록한 Spark Streaming 애플리케이션의 개수를 가져온다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @return Spark Streaming 애플리케이션의 총 개수
     */
    int getTotalCountOfSparkStreamingApps(Map sparkStreamingMap);

    /**
     * 등록한 Spark Streaming 애플리케이션의 목록을 가져온다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @return Spark Streaming 애플리케이션 목록
     */
    List<SparkStreaming> getAllSparkStreamingApps(Map sparkStreamingMap);

    /**
     * 선택한 Spark Streaming 애플리케이션의 요약 정보를 가져온다.
     *
     * @param sparkStreamingMap 애플리케이션 정보
     * @return Spark Streaming 애플리케이션의 요약 정보 목록
     */
    List<Map<String, Object>> getSparkStreamingAppSummary(Map sparkStreamingMap);

    /**
     * 시스템 에이전트 목록 중 DB에 애플리케이션이 가장 적게 등록된 서버를 가져온다
     *
     * @param systemAgentList   시스템 에이전트 URL 목록
     * @return  System Agent URL
     */
    String getOptimalServer(List<String> systemAgentList);

}
