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

/**
 * JAR 파일과 같은 Artifact를 다운로드하는 Artifact Loader를 생성하는 Factory.
 * 다양한 환경에 따라서 Artifact를 로딩하는 전략이 다를 수 있으므로 다운로드하는 전략을 추가하기 위해서는
 * 여기에 Artifact Loader를 추가할 수 있다.
 * <p/>
 * <pre>
 * ArtifactLoader loader = ArtifactLoaderFactory.getArtifactLoader(context);
 * String fullyQualifiedPath = loader.load(filename);
 * </pre>
 *
 * @author Byoung Gon, Kim
 * @version 0.3
 */
public class ArtifactLoaderFactory {

    /**
     * Artifact Loader를 생성한다.
     *
     * @return Artifact Loader의 구현체
     */
    public static ArtifactLoader getArtifactLoader(String clusterName) {
        return new HdfsArtifactLoader(clusterName);
    }

}
