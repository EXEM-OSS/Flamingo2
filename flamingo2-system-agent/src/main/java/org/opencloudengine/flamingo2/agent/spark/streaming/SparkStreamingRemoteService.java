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
package org.opencloudengine.flamingo2.agent.spark.streaming;

import java.util.Map;

/**
 * Spark Streaming Service Remote Interface.
 * Flamingo Web의 Spark Streaming Controller 및 Spark Streaming Collector에서 요청한 작업을 수행한다.
 *
 * @author Myeongha KIM
 * @since 2.1
 */
public interface SparkStreamingRemoteService {

    // Spark Streaming Controller Remote Service

    /**
     * Spark Streaming Application을 등록한다.
     *
     * @param sparkUserWorkingPath 시스템 에이전트 내의 Spark Streaming Home Directory
     * @return true or false
     */
    boolean createSparkApplication(String sparkUserWorkingPath);

    /**
     * Spark Streaming Application을 등록한다(Jar 파일 업로드 포함).
     *
     * @param jarFileFQP Jar 파일이 업로드될 Fully Qualified Path
     * @param jarFileBytes 업로드 할 Jar File Byte Array
     * @return true or false
     */
    boolean createSparkApplication(String jarFileFQP, byte[] jarFileBytes);

    /**
     * Spark Streaming Application을 시작한다.
     *
     * @param sparkStreamingMap Spark Streaming Application을 실행하기 위한 기본정보
     * @param defaultEnvs       Spark Streaming Application을 실행하기 위한 환경변수
     * @return true or false
     */
    boolean startSparkStreamingApp(Map sparkStreamingMap, Map<String, String> defaultEnvs);

    /**
     * Spark Streaming Application을 시작한다.
     *
     * @param sparkStreamingMap Spark Streaming Application을 중지하기 위한 기본정보
     * @return true or false
     */
    boolean stopSparkStreamingApp(Map sparkStreamingMap);


    // Spark Streaming Collector Remote Service

    /**
     * 수집한 PID의 상태정보를 통해 가져온다.
     *
     * @param sparkPidFilePath PID 파일이 위치한 시스템 에이전트의 PID 파일 경로
     * @return PID is Running ? true : false
     */
    boolean getSparkStreamingAppState(String sparkPidFilePath);

    /**
     * 실행 중인 PID의 시스템 자원 사용량을 통해 가져온다.
     *
     * @param sparkPidFilePath PID 파일이 위치한 시스템 에이전트의 PID 파일 경로
     * @return System Resource Usage Information Map
     */
    Map<String, Object> getSparkStreamingAppSystemResourceUsage(String sparkPidFilePath);
}
