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
package org.opencloudengine.flamingo2.web.remote;

import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.remote.LocalEngineServiceImpl;
import org.opencloudengine.flamingo2.engine.remote.RemoteEngineServiceImpl;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

/**
 * Flamingo Engine 서비스를 원격 또는 로컬에서 찾는 서비스.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class EngineLookupService {

    /**
     * Local Flamingo Engine Service
     */
    private static EngineService localEngineService = new LocalEngineServiceImpl();

    /**
     * Flamingo Engine 환경설정 정보를 참고하여 Flamingo Engine 서비스를 lookup한다.
     *
     * @param engineConfig Flamingo Engine Configuration
     * @return Flamingo Engine 서비스
     */
    public static EngineService lookup(EngineConfig engineConfig) {
        boolean isRemote = isRemoteEngine();
        if (isRemote) {
            return new RemoteEngineServiceImpl(engineConfig);
        }
        return localEngineService;
    }

    /**
     * Flamingo Engine이 원격에 배포되어 있는 엔진인지 판단한다.
     * <tt>config.properties</tt> 파일의 <tt>remote.engine.enabled</tt> 속성이 <tt>true</tt>로 설정되어 있는지
     * 확인하고 <tt>true</tt>인 경우 원격에 배포된 Flamingo Engine으로 판단한다.
     *
     * @return 원격에 Flamingo Engine이 배포되어 있다면 <tt>true</tt>
     */
    private static boolean isRemoteEngine() {
        return false;
    }
}
