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
package org.opencloudengine.flamingo2.engine.hive;

import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

public class HiveThrift2ClientFactory {
    /**
     * Local Flamingo Engine Service
     */
    private static HiveThrift2Client thrift2Client;

    /**
     * Flamingo Engine 환경설정 정보를 참고하여 HiveThriftClient 서비스를 lookup한다.
     *
     * @param engineConfig Flamingo Engine Configuration
     * @param url Hive Thrift URL
     * @return Flamingo Engine 서비스
     */
    public static HiveThrift2Client lookup(EngineConfig engineConfig, String url) {
        boolean isLagacy = isLegacy(engineConfig);
        if (isLagacy) {
            return new HiveThrift2LagacyClientImpl(url);
        }
        return new HiveThrift2ClientImpl(url);
    }

    /**
     * Hive 버전이 0.13.0 이전 버전인지 확인한다.
     * <tt>hadoop.properties</tt> 파일의 <tt>hive.lagacy</tt> 속성이 <tt>true</tt>로 설정되어 있는지
     * 확인하고 <tt>true</tt>인 경우 Hive 버전이 0.13.0 이전 버전이 설치되어 있는 것으로 판단한다.
     *
     * @return Hive 0.13.0 이전 버전을 사용하고 있다면 <tt>true</tt>
     */
    private static boolean isLegacy(EngineConfig engineConfig) {
        return engineConfig.isHiveLegacy();
    }
}
