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

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.hadoop.fs.Path;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.fs.FileSystemUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Byung Gon, Kim
 * @version 0.3
 */
public class MavenArtifactLoader {


    /**
     * 지정한 Maven Artifact의 경로를 구성한다. 다음과 같이 파라미터를 지정한 경우
     * <p/>
     * <ul>
     * <li>Group Id - <tt>org.openflamingo</tt></li>
     * <li>Artifact Id - <tt>flamingo-mapreduce</tt></li>
     * <li>Version - <tt>0.1</tt></li>
     * </ul>
     * <p/>
     * 위 파라미터에 대해서 <tt>/org/openflamingo/flamingo-mapreduce/0.1/flamingo-mapreduce-0.1.jar</tt>의 다운로드 URL를 생성한다.
     *
     * @param groupId    Maven Group Id
     * @param artifactId Maven Artifact Id
     * @param version    Artifact Version
     * @return 다운로드 URL
     */
    private static String getArtifactPath(String groupId, String artifactId, String version) {
        return "/" + StringUtils.replace(groupId, ".", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }

    /**
     * 지정한 Artifact를 다운로드한다.
     *
     * @param groupId          Maven Group Id
     * @param artifactId       Maven Artifact Id
     * @param version          Artifact Version
     * @param url              Maven Repository URL
     * @param workingDirectory Local Working Directory
     * @param retry            Retry
     * @return 다운로드한 Artifact의 Local FileSystem Path
     */
    public static String downloadArtifactFromRepository(String groupId, String artifactId, String version, String url, String workingDirectory, String retry) {
        boolean isCaching = ConfigurationHelper.getHelper().getBoolean("artifact.caching", true);
        String filename = workingDirectory + "/" + artifactId + "-" + version + ".jar";
        String cachePath = ConfigurationHelper.getHelper().get("artifact.cache.path", "/temp/cache");
        String cachedFilename = cachePath + "/" + artifactId + "-" + version + ".jar";
        String artifactPath = null;

        if (isCaching) {
            artifactPath = cachedFilename;
        } else {
            artifactPath = filename;
        }

        if (url == null || workingDirectory == null) {
            throw new ServiceException("You can not access the Maven repository.");
        }

        if (!new File(artifactPath).exists()) {
            String artifactUrl = url + getArtifactPath(groupId.trim(), artifactId.trim(), version.trim());
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(artifactUrl);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(Integer.valueOf(retry), false));
            int statusCode = 0;
            try {
                statusCode = httpClient.executeMethod(method);
            } catch (IOException e) {
                throw new ServiceException("You can not download the artifact.");
            }

            if (statusCode != HttpStatus.SC_OK) {
                throw new ServiceException("You can not download the JAR file.");
            }

            // 다운로드한 파일을 저장하기 위해한 fully qualified file name을 구성한다.
            FileSystemUtils.testCreateDir(new Path(FileSystemUtils.correctPath(cachePath)));
            try {
                InputStream is = method.getResponseBodyAsStream();
                File outputFile = new File(artifactPath);
                FileOutputStream fos = new FileOutputStream(outputFile);
                org.springframework.util.FileCopyUtils.copy(is, fos);
                return artifactPath;
            } catch (IOException e) {
                throw new ServiceException("You can not save the JAR file.");
            }
        } else {
            return artifactPath;
        }
    }
}
