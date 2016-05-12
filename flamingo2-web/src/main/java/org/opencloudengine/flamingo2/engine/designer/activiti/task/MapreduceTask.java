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

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.util.cli.FileWriter;
import org.opencloudengine.flamingo2.util.cli.ManagedProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.FileCopyUtils;
import org.uengine.kernel.ProcessInstance;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.opencloudengine.flamingo2.util.StringUtils.unescape;
import static org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper.getHelper;

/**
 * Mapreduce를 실행하는 태스크
 *
 * @author Jae Hee, Lee
 * @since 2.0
 */
public class MapreduceTask extends InterceptorAbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(MapreduceTask.class);

    /**
     * Default HDFS File System URL
     */
    private String fsDefaultFS;

    @Override
    public void runTask(ProcessInstance instance) throws Exception {
        fsDefaultFS = String.format("hdfs://%s:%s", getHelper().get(clusterName + ".nn.address"), getHelper().get(clusterName + ".nn.port"));

        FileUtils.forceMkdir(new File(working));

        variable.setProperty("jarLocalPath", downloadJar(working, variable.get("jar").toString().trim()));

        saveScriptFile(buildCommand(working), working);

        String cli = MessageFormatter.arrayFormat("sh {}/script.sh", new Object[]{working}).getMessage();
        saveCommandFile(cli, working);
        String[] cmds = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(cli, " ");

        FileWriter fileWriter = new FileWriter(logger, working + "/task.log");

        Map<String, Object> socketParams = new HashMap<>();
        socketParams.put("identifier", getIdentifier());
        socketParams.put("taskId", getTaskId());
        socketParams.put("type", "workflow");
        socketParams.put("user", getUser());

        ManagedProcess managedProcess = new ManagedProcess(cmds, getDefaultEnvs(working), working, logger, fileWriter);
        managedProcess.setSocketParams(socketParams);
        managedProcess.run();
    }

    /**
     * command line 명령어를 생성한다.
     */
    private String buildCommand(String working) {
        List<String> command = new LinkedList<>();

        Map<String, String> defaultEnvs = getDefaultEnvs(working);
        Set<String> keys = defaultEnvs.keySet();
        for (String key : keys) {
            if (!StringUtils.isEmpty(defaultEnvs.get(key))) {
                command.add(MessageFormatter.arrayFormat("export {}={}\n", new Object[]{key, defaultEnvs.get(key)}).getMessage());
            }
        }

        command.add(getHelper().get("hadoop.home") + "/bin/hadoop");

        if (variable.get("mapreduce") == null) {
            command.add("jar");
            command.add(variable.get("jarLocalPath").toString().trim());
            command.add(variable.get("driver").toString().trim());

            injectLibJars(command);
            injectKeyValues(command);
            injectInputPath(command);
            injectOutputPath(command);
            injectCommandLineParameters(command);
        } else {
            HashMap variables = (HashMap) variable.get("mapreduce");

            if (variables != null && variables.size() > 0) {
                command.add("jar");
                command.add(downloadJar(working, variable.get("jar").toString().trim()));
                command.add(variables.get("driver").toString().trim());

                injectLibJars(command);

                for (Object key : variables.keySet()) {
                    if ("params".equals(key)) {
                        for (Object value : (ArrayList) variables.get(key)) {
                            command.add(value.toString().trim());
                        }
                    }
                }
            }
        }

        return StringUtils.listToDelimitedString(command, " ");
    }

    /**
     * Hadoop 커맨드 라인의 <tt>-libjars</tt> 옵션을 구성한다.
     */
    private void injectLibJars(List<String> command) {
        if (variable.get("path") != null && !StringUtils.isEmpty(variable.get("path").toString())) {
            List<String> dependencies = new ArrayList<>();

            command.add("-libjars");

            String[] paths = (variable.get("path").toString()).split(",");

            for (String path : paths) {
                ArtifactLoader artifactLoader = ArtifactLoaderFactory.getArtifactLoader(clusterName);
                String artifactCachePath = getHelper().get("artifact.cache.path");
                String jarPath = artifactLoader.load(artifactCachePath, path);
                dependencies.add(jarPath);
            }

            command.add(Joiner.on(",").join(dependencies));
        }
    }

    /**
     * Hadoop 커맨드 라인의 <tt>-Dkey=value</tt> 옵션을 구성한다.
     */
    private void injectKeyValues(List<String> command) {
        List<String> keyvalues = new ArrayList<>();

        if (variable.get("hadoopKeys") != null && variable.get("hadoopValues") != null && !StringUtils.isEmpty(variable.get("hadoopKeys").toString()) && !StringUtils.isEmpty(variable.get("hadoopValues").toString())) {
            String[] hadoopKeys = (variable.get("hadoopKeys").toString()).split(",");
            String[] hadoopValues = (variable.get("hadoopValues").toString()).split(",");

            for (int i = 0; i < hadoopKeys.length; i++) {
                keyvalues.add("-D" + StringUtils.unescape(encloseSpace(hadoopKeys[i])) + "=" + StringUtils.unescape(encloseSpace(hadoopValues[i])));
            }
        }

        // Flamingo 관련 파라미터 추가
        keyvalues.add("-D" + encloseSpace("mapreduce.job.name") + "=" + encloseSpace(this.getTaskHistory().getName()));
        keyvalues.add("-D" + encloseSpace("mapred.job.name") + "=" + encloseSpace(this.getTaskHistory().getName()));
        keyvalues.add("-D" + encloseSpace("flamingo.action.id") + "=" + encloseSpace(this.getTaskHistory().getTaskId()));
        keyvalues.add("-D" + encloseSpace("flamingo.action.name") + "=" + encloseSpace(this.getTaskHistory().getName()));
        keyvalues.add("-D" + encloseSpace("flamingo.job.id") + "=" + encloseSpace(this.getTaskHistory().getIdentifier()));
        keyvalues.add("-D" + encloseSpace("flamingo.job.stringId") + "=" + encloseSpace(this.getWorkflowHistory().getJobStringId()));
        keyvalues.add("-D" + encloseSpace("flamingo.job.name") + "=" + encloseSpace(this.getWorkflowHistory().getJobName()));
        keyvalues.add("-D" + encloseSpace("flamingo.workflow.id") + "=" + encloseSpace(this.getWorkflowHistory().getWorkflowId()));
        keyvalues.add("-D" + encloseSpace("flamingo.workflow.instance.id") + "=" + encloseSpace(this.getTaskHistory().getProcessId()));
        keyvalues.add("-D" + encloseSpace("flamingo.username") + "=" + encloseSpace(this.getTaskHistory().getUsername()));
        keyvalues.add("-D" + encloseSpace("flamingo.log.path") + "=" + encloseSpace(this.getTaskHistory().getLogDirectory()));
        keyvalues.add("-D" + encloseSpace("fs.defaultFS") + "=" + fsDefaultFS);

        command.add(Joiner.on(" ").join(keyvalues));
    }

    /**
     * Hadoop 커맨드 라인의 <tt>-input</tt> 옵션을 구성한다.
     */
    private void injectInputPath(List<String> command) {
        if (variable.get("input") != null && !StringUtils.isEmpty(variable.get("input").toString())) {
            command.add("-input");
            String[] inputs = (variable.get("input").toString()).split(",");
            ArrayList inputPaths = new ArrayList();
            for (String input : inputs) {
                String resolve = resolve(input);
                inputPaths.add(resolve);
            }
            command.add(Joiner.on(",").join(inputPaths));
        }
    }

    /**
     * Hadoop 커맨드 라인의 <tt>-output</tt> 옵션을 구성한다.
     */
    private void injectOutputPath(List<String> command) {
        if (variable.get("output") != null && !StringUtils.isEmpty(variable.get("output").toString())) {
            command.add("-output");
            String[] outputs = (variable.get("output").toString()).split(",");
            ArrayList outputPaths = new ArrayList();
            for (String output : outputs) {
                String resolve = resolve(output);
                outputPaths.add(resolve);
            }
            command.add(Joiner.on(",").join(outputPaths));
        }
    }

    /**
     * Hadoop 커맨드 라인의 <tt>arguments</tt> 옵션을 구성한다.
     */
    private void injectCommandLineParameters(List<String> command) {
        if (variable.get("commandlineValues") != null && !org.apache.commons.lang.StringUtils.isEmpty(variable.get("commandlineValues").toString())) {
            String[] args = variable.get("commandlineValues").toString().trim().split(",");
            StringBuilder builder = new StringBuilder();
            for (String arg : args) {
                String unescape = unescape(arg);
                String resolve = resolve(unescape);
                String e = encloseSpace(resolve);
                builder.append(" ").append(e);
            }
            command.add(builder.toString());
        }
    }

    /**
     * MapReduce JAR 파일을 다운로드한다.
     */
    private String downloadJar(String actionBasePath, String filename) {
        if (filename != null && !StringUtils.isEmpty(filename)) {
            ArtifactLoader artifactLoader = ArtifactLoaderFactory.getArtifactLoader(clusterName);
            return artifactLoader.load(actionBasePath, filename);
        } else {
            throw new ServiceException("You can download the MapReduce JAR file.");
        }
    }

    /**
     * 스크립트를 <tt>script.sh</tt> 파일로 저장한다.
     *
     * @param script  스크립트
     * @param baseDir 파일을 저장할 기준경로
     * @return 저장한 파일의 절대 경로
     * @throws java.io.IOException 파일을 저장할 수 없는 경우
     */
    private String saveScriptFile(String script, String baseDir) throws IOException {
        File cliPath = new File(baseDir, "script.sh");
        FileCopyUtils.copy(script.getBytes(), cliPath);
        return cliPath.getAbsolutePath();
    }

    /**
     * 스크립트를 <tt>command.sh</tt> 파일로 저장한다.
     *
     * @param command 커멘드
     * @param baseDir 파일을 저장할 기준경로
     * @return 저장한 파일의 절대 경로
     * @throws java.io.IOException 파일을 저장할 수 없는 경우
     */
    private String saveCommandFile(String command, String baseDir) throws IOException {
        File cliPath = new File(baseDir, "command.sh");
        FileCopyUtils.copy(command.getBytes(), cliPath);
        return cliPath.getAbsolutePath();
    }

    /**
     * 스크립트를 실행하기 위해서 필요한 환경변수를 가져온다.
     *
     * @return 환경변수
     */
    public Map<String, String> getDefaultEnvs(String working) {
        Map<String, String> envs = new HashMap<>();

        envs.put("PATH", "/bin:/usr/bin:/usr/local/bin" + ":" + getHelper().get("hadoop.home") + "/bin" + ":" + getHelper().get("hive.home") + "/bin" + ":" + getHelper().get("pig.home") + "/bin");
        envs.put("HADOOP_CLIENT_OPTS", MessageFormatter.format("-javaagent:{}=resourcescript:mr2.bm", getHelper().get("flamingo.mr.agent.jar.path")).getMessage());
        envs.put("JAVA_HOME", getHelper().get("java.home"));
        envs.put("HADOOP_HDFS_HOME", getHelper().get("hadoop.hdfs.home"));
        envs.put("HADOOP_MAPRED_HOME", getHelper().get("hadoop.mapred.home"));
        envs.put("HADOOP_HOME", getHelper().get("hadoop.home"));
        envs.put("HIVE_HOME", getHelper().get("hive.home"));
        envs.put("PIG_HOME", getHelper().get("pig.home"));
        if (!isEmpty(getHelper().get("hadoop.user.name"))) {
            envs.put("HADOOP_USER_NAME", getHelper().get("hadoop.user.name"));
        } else {
            envs.put("HADOOP_USER_NAME", this.username);
        }

        String[] environmentKeys = getValues("environmentKeys");
        String[] environmentValues = getValues("environmentValues");

        if (environmentKeys != null) {
            for (int i = 0; i < environmentKeys.length; i++) {
                envs.put(environmentKeys[i], environmentValues[i]);
            }
        }

        return envs;
    }
}