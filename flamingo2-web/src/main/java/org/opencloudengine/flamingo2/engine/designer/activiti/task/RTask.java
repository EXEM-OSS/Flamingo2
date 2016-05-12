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

import org.apache.commons.io.FileUtils;
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
 * R 스크립트를 실행하는 태스크
 *
 * @author Jae Hee, Lee
 * @since 2.0
 */
public class RTask extends InterceptorAbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(RTask.class);

    /**
     * Hadoop Configuration
     */
    private Map<String, Object> hadoopConf = new HashMap<>();

    /**
     * Default HDFS File System URL
     */
    private String fsDefaultFS;

    @Override
    public void runTask(ProcessInstance instance) throws Exception {
        fsDefaultFS = String.format("hdfs://%s:%s", getHelper().get(clusterName + ".nn.address"), getHelper().get(clusterName + ".nn.port"));

        FileUtils.forceMkdir(new File(working));

        // 쉘 스크립트 내용을 저장한다.
        saveScriptFile(buildCommand(working), working);

        // 실행 스크립트를 생성한다.
        String cli = MessageFormatter.arrayFormat("sh {}/script.sh", new Object[]{working}).getMessage();
        saveCommandFile(cli, working);

        // 실행 스크립트를 저장한다.
        if (variable.get("script") != null && !StringUtils.isEmpty(variable.get("script").toString())) {
            saveRScriptFile(unescape(resolve(variable.get("script").toString())), working);
        }

        // 실행 스크립트를 문자배열로 만든다.
        String[] cmds = StringUtils.splitPreserveAllTokens(cli, " ");

        // 로깅 파일의 FileWriter를 생성한다.
        FileWriter fileWriter = new FileWriter(logger, working + "/task2.log");

        // 소켓으로 로그를 보낼 때 필요한 헤더정보를 설정한다.
        Map<String, Object> socketParams = new HashMap<>();
        socketParams.put("identifier", getIdentifier());
        socketParams.put("taskId", getTaskId());
        socketParams.put("type", "workflow");
        socketParams.put("user", getUser());

        // 프로세스를 실행한다.
        ManagedProcess managedProcess = new ManagedProcess(cmds, getDefaultEnvs(), working, logger, fileWriter);
        managedProcess.setSocketParams(socketParams);
        managedProcess.run();
    }

    /**
     * command line 명령어를 생성한다.
     */
    private String buildCommand(String working) {
        List<String> command = new LinkedList<>();

        Map<String, String> defaultEnvs = getDefaultEnvs();
        Set<String> keys = defaultEnvs.keySet();
        for (String key : keys) {
            if (!StringUtils.isEmpty(defaultEnvs.get(key))) {
                command.add(MessageFormatter.arrayFormat("export {}={}\n", new Object[]{
                        key, defaultEnvs.get(key)
                }).getMessage());
            }
        }

        command.add(getHelper().get("r.home") + "/R");
        command.add("CMD");
        command.add("BATCH");

        if (variable.get("nosave") != null && !StringUtils.isEmpty(variable.get("nosave").toString())) {
            command.add("--no-save");
        }

        if (variable.get("norestore") != null && !StringUtils.isEmpty(variable.get("norestore").toString())) {
            command.add("--no-restore");
        }

        injectCommandLineParameters(command);

        command.add(working + "/script.R");
        command.add(working + "/task.log");

        return org.opencloudengine.flamingo2.util.StringUtils.listToDelimitedString(command, " ");
    }

    private void injectCommandLineParameters(List<String> command) {
        if (variable.get("commandlineValues") != null && !org.apache.commons.lang.StringUtils.isEmpty(variable.get("commandlineValues").toString())) {
            String[] args = variable.get("commandlineValues").toString().trim().split(",");
            StringBuilder builder = new StringBuilder();
            if (args.length > 0) {
                builder.append("'--args");
            }
            for (String arg : args) {
                String e = encloseSpace(resolve(unescape(arg)));
                builder.append(" ").append(e);
            }
            if (args.length > 0) {
                builder.append("'");
            }
            command.add(builder.toString());
        }
    }

    /**
     * 스크립트를 <tt>script.R</tt> 파일로 저장한다.
     *
     * @param script  스크립트
     * @param baseDir 파일을 저장할 기준경로
     * @return 저장한 파일의 절대 경로
     * @throws java.io.IOException 파일을 저장할 수 없는 경우
     */
    private String saveRScriptFile(String script, String baseDir) throws IOException {
        File cliPath = new File(baseDir, "script.R");
        FileCopyUtils.copy(script.getBytes(), cliPath);
        return cliPath.getAbsolutePath();
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
    public Map<String, String> getDefaultEnvs() {
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