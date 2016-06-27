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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.fs.FileSystemUtils;
import org.opencloudengine.flamingo2.model.rest.HdfsUtils;
import org.opencloudengine.flamingo2.util.FileUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper.getHelper;

/**
 * Apache Hadoop FileSystem Artifact Loader.
 *
 * @author Byoung Gon, Kim
 * @version 0.3
 */
public class HdfsArtifactLoader implements ArtifactLoader {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HdfsArtifactLoader.class);

    /**
     * Apache Hadoop FileSystem
     */
    private FileSystem fs;

    /**
     * 기본 생성자.
     */
    public HdfsArtifactLoader(String clusterName) {
        try {
            Configuration conf = new Configuration();

            // MapR이 아닌 경우
            if (!"true".equals(getHelper().get("mapr.enabled"))) {
                conf.set("fs.default.name", "hdfs://" + getHelper().get(clusterName + ".nn.address") + ":" + getHelper().get(clusterName + ".nn.port"));
                conf.set("fs.defaultFS", "hdfs://" + getHelper().get(clusterName + ".nn.address") + ":" + getHelper().get(clusterName + ".nn.port"));
            } else {
                conf.set("fs.default.name", getHelper().get(clusterName + ".mapr.fs"));
                conf.set("fs.defaultFS", getHelper().get(clusterName + ".mapr.fs"));
            }

            conf.set("fs.default.name", ConfigurationHelper.getHelper().get(clusterName + ".nn.address") + ":" + ConfigurationHelper.getHelper().get(clusterName + ".nn.port"));
            conf.set("fs.defaultFS", ConfigurationHelper.getHelper().get(clusterName + ".nn.address") + ":" + ConfigurationHelper.getHelper().get(clusterName + ".nn.port"));
            conf.set("fs.AbstractFileSystem.hdfs.impl", "org.apache.hadoop.fs.Hdfs");
            this.fs = FileSystem.get(conf);
        } catch (IOException e) {
            throw new ServiceException("HDFS initialization failed", e);
        }
    }

    @Override
    public String load(String groupId, String artifactId, String version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String loadArtifact(String artifactIdentifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> loadArtifacts(String actionBasePath, List<String> artifactIdentifiers) {
        List<String> artifacts = new ArrayList<>();
        for (String artifact : artifactIdentifiers) {
            artifacts.add(this.load(actionBasePath, artifact));
        }
        return artifacts;
    }

    @Override
    public String load(String actionBasePath, String filename) {
        String jarPath = actionBasePath + "/jars";
        String temporaryPath = FileUtils.getFilename(filename);

        // Maven Artifact인 경우 Maven Repository에서 다운로드한다.
        if (StringUtils.countOccurrencesOf(filename, ":") == 2) {
            logger.info("Jar '{}' is a artifact of Apache Maven.", filename);
            String mavenUrl = ConfigurationHelper.getHelper().get("maven.repository.url");
            String[] strings = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(filename, ":");
            return MavenArtifactLoader.downloadArtifactFromRepository(strings[0], strings[1], strings[2], mavenUrl, jarPath, "20000");
        }

        if (new File(filename).exists()) {
            logger.info("JAR '{}' is a local jar file.", filename);
            return filename;
        }

        logger.info("JAR '{}' seem to be a jar file on HDFS.", filename);

        boolean isCaching = ConfigurationHelper.getHelper().getBoolean("artifact.caching", true);
        String cachePath = ConfigurationHelper.getHelper().get("artifact.cache.path", "/temp/cache");
        String cachedFilename = cachePath + filename;
        String artifactPath;

        // if cache turn on, use jar file in cache.
        if (isCaching) {
            artifactPath = cachedFilename;
        } else {
            artifactPath = jarPath + "/" + temporaryPath;
        }

        FileSystemUtils.testCreateDir(new Path(FileSystemUtils.correctPath(FileUtils.getPath(artifactPath))));

        try {
            InputStream is = HdfsUtils.getInputStream(fs, filename);
            File outputFile = new File(artifactPath);
            FileOutputStream fos = new FileOutputStream(outputFile);
            org.springframework.util.FileCopyUtils.copy(is, fos);
            return artifactPath;
        } catch (IOException e) {
            throw new ServiceException("You can not save the JAR file.", e);
        }
    }
}
