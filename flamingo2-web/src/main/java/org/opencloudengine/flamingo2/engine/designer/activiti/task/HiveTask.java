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
import org.opencloudengine.flamingo2.engine.fs.FileSystemUtils;
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
import static org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper.getHelper;

/**
 * Hive 스크립트를 실행하는 태스크.
 *
 * @author Jae Hee, Lee
 * @since 2.0
 */
public class HiveTask extends InterceptorAbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HiveTask.class);

    /**
     * Default HDFS File System URL
     */
    private String fsDefaultFS;

    @Override
    public void runTask(ProcessInstance instance) throws Exception {
        fsDefaultFS = String.format("hdfs://%s:%s", getHelper().get(clusterName + ".nn.address"), getHelper().get(clusterName + ".nn.port"));

        FileUtils.forceMkdir(new File(working));

        // Hive Script를 저장한다.
        String hiveScriptPath = working + "/script.hive";
        String resovled = resolve(params.getString("script"));
        FileSystemUtils.saveToFile(resovled.getBytes(), hiveScriptPath);
        saveScriptFile(buildCommand(hiveScriptPath), working);

        String cli = MessageFormatter.arrayFormat("sh {}/script.sh", new Object[]{working}).getMessage();
        saveCommandFile(cli, working);
        String[] cmds = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(cli, " ");

        FileWriter fileWriter = new FileWriter(logger, working + "/task.log");

        Map<String, Object> socketParams = new HashMap<>();
        socketParams.put("identifier", this.getIdentifier());
        socketParams.put("taskId", this.getTaskId());
        socketParams.put("type", "workflow");
        socketParams.put("user", this.getUser());

        ManagedProcess managedProcess = new ManagedProcess(cmds, getDefaultEnvs(), working, logger, fileWriter);
        managedProcess.setSocketParams(socketParams);
        managedProcess.run();
    }

    /**
     * 커맨드라인을 <tt>script.hive</tt> 파일로 저장한다.
     *
     * @param hiveScriptPath Hive 스크립트 경로
     * @return 저장한 파일의 절대 경로
     */
    private String buildCommand(String hiveScriptPath) {
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

        command.add(getHelper().get("hive.home") + "/bin/hive");

        command.add("-f");
        command.add(hiveScriptPath);

        setHiveConf(command);

        return org.opencloudengine.flamingo2.util.StringUtils.listToDelimitedString(command, " ");
    }

    public void setHiveConf(List<String> hiveConf) {
        hiveConf.add("--hiveconf " + encloseSpace("mapreduce.job.name") + "=" + encloseSpace(this.getTaskHistory().getName()));
        hiveConf.add("--hiveconf " + encloseSpace("mapred.job.name") + "=" + encloseSpace(this.getTaskHistory().getName()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.action.id") + "=" + encloseSpace(this.getTaskHistory().getTaskId()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.action.name") + "=" + encloseSpace(this.getTaskHistory().getName()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.job.id") + "=" + encloseSpace(this.getTaskHistory().getIdentifier()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.job.stringId") + "=" + encloseSpace(this.getWorkflowHistory().getJobStringId()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.job.name") + "=" + encloseSpace(this.getWorkflowHistory().getJobName()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.workflow.id") + "=" + encloseSpace(this.getWorkflowHistory().getWorkflowId()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.workflow.instance.id") + "=" + encloseSpace(this.getTaskHistory().getProcessId()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.username") + "=" + encloseSpace(this.getTaskHistory().getUsername()));
        hiveConf.add("--hiveconf " + encloseSpace("flamingo.log.path") + "=" + encloseSpace(this.getTaskHistory().getLogDirectory()));
        hiveConf.add("--hiveconf " + encloseSpace("fs.defaultFS") + "=" + fsDefaultFS);

        if (variable.get("hadoopKeys") != null && variable.get("hadoopValues") != null && !StringUtils.isEmpty(variable.get("hadoopKeys").toString()) && !StringUtils.isEmpty(variable.get("hadoopValues").toString())) {
            String[] hadoopKeys = (variable.get("hadoopKeys").toString()).split(",");
            String[] hadoopValues = (variable.get("hadoopValues").toString()).split(",");

            for (int i = 0; i < hadoopKeys.length; i++) {
                hiveConf.add("--hiveconf " + StringUtils.unescape(encloseSpace(hadoopKeys[i])) + "=" + org.opencloudengine.flamingo2.util.StringUtils.unescape(encloseSpace(hadoopValues[i])));
            }
        }
    }

    /**
     * 커맨드라인을 <tt>cli.sh</tt> 파일로 저장한다.
     *
     * @param script  커맨드 라인
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
     * 스크립트를 <tt>script</tt> 파일로 저장한다.
     *
     * @param command 스크립트
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

        // Hive 쿼리 실행시 Flamingo의 사용자를 적용하고자 하는 경우와 그렇지 않은 경우를 구분한다.
        try {
            boolean b = Boolean.parseBoolean(getHelper().get(clusterName + ".hive.apply.flamingo.username"));
            if (b) {
                // hive.apply.flamingo.username이 true인 경우 Flamingo의 사용자를 적용한다.
                if (!isEmpty(getHelper().get("hadoop.user.name"))) {
                    envs.put("HADOOP_USER_NAME", getHelper().get("hadoop.user.name"));
                } else {
                    envs.put("HADOOP_USER_NAME", this.username);
                }
            } else {
                // 만약 이 옵션을 false로 설정하면 지정한 사용자로 적용한다.
                envs.put("HADOOP_USER_NAME", getHelper().get(clusterName + ".hive.username"));
            }
        } catch (Exception ex) {
            if (!isEmpty(getHelper().get("hadoop.user.name"))) {
                envs.put("HADOOP_USER_NAME", getHelper().get("hadoop.user.name"));
            } else {
                envs.put("HADOOP_USER_NAME", this.username);
            }
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