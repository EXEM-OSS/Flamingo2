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
package org.opencloudengine.flamingo2.engine.designer.activiti.task;

import java.util.List;

/**
 * JAR 파일과 같은 Artifact를 로딩하는 loader의 추상화 인터페이스.
 *
 * @author Byoung Gon, Kim
 * @since 0.2
 */
public interface ArtifactLoader {

    /**
     * Artifact를 로딩한다.
     *
     * @param groupId    Maven Group Id
     * @param artifactId Maven Artifact Id
     * @param version    Artifact Version
     * @return 실제 Artifact가 있는 물리 경로의 위치
     */
    String load(String groupId, String artifactId, String version);

    /**
     * Artifact를 로딩한다.
     *
     * @param artifactIdentifier Artitact 식별자(<tt>groupId:artifactId:version</tt>)
     * @return 실제 Artifact가 있는 물리 경로의 위치
     */
    String loadArtifact(String artifactIdentifier);

    /**
     * Artifact를 로딩한다.
     *
     * @param artifactIdentifiers Artitact 식별자(<tt>groupId:artifactId:version</tt>)
     * @return 실제 Artifact가 있는 물리 경로의 위치
     */
    List<String> loadArtifacts(String actionBasePath, List<String> artifactIdentifiers);

    /**
     * Artifact를 로딩한다.
     *
     * @return 실제 Artifact가 있는 물리 경로의 위치
     */
    String load(String actionBasePath, String filename);

}
