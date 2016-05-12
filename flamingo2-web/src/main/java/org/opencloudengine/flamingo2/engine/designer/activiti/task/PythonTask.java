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
import org.apache.commons.lang.StringUtils;
import org.opencloudengine.flamingo2.util.cli.FileWriter;
import org.opencloudengine.flamingo2.util.cli.ManagedProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.FileCopyUtils;
import org.uengine.kernel.ProcessInstance;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.opencloudengine.flamingo2.util.StringUtils.listToDelimitedString;
import static org.opencloudengine.flamingo2.util.StringUtils.unescape;
import static org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper.getHelper;

/**
 * Python 스크립트를 실행하는 태스크.
 *
 * @author Jae Hee, Lee
 * @since 2.0
 */
public class PythonTask extends InterceptorAbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(PythonTask.class);

    /**
     * Default HDFS File System URL
     */
    private String fsDefaultFS;

    @Override
    public void runTask(ProcessInstance instance) throws Exception {
        fsDefaultFS = String.format("hdfs://%s:%s", getHelper().get(clusterName + ".nn.address"), getHelper().get(clusterName + ".nn.port"));

        FileUtils.forceMkdir(new File(working));

        // Python Script를 저장한다.
        savePythonScript(unescape(resolve(variable.get("script").toString())), working);

        String cli = MessageFormatter.arrayFormat("python {}/script.py " + getParameters(), new Object[]{working}).getMessage();
        saveCommand(cli, working);
        String[] cmds = StringUtils.splitPreserveAllTokens(cli, " ");

        FileWriter fileWriter = new FileWriter(logger, working + "/task.log");

        Map<String, Object> socketParams = new HashMap<>();
        socketParams.put("identifier", this.getIdentifier());
        socketParams.put("taskId", this.getTaskId());
        socketParams.put("type", "workflow");
        socketParams.put("user", this.getUser());

        ManagedProcess managedProcess = new ManagedProcess(cmds, getDefaultEnvs(working), working, logger, fileWriter);
        managedProcess.setSocketParams(socketParams);
        managedProcess.run();
    }

    /**
     * 커맨드 라인 파라미터를 처리함.
     *
     * @return
     */
    private String getParameters() {
        List<String> command = new LinkedList<>();
        if (variable.get("commandlineValues") != null && !StringUtils.isEmpty(variable.get("commandlineValues").toString())) {
            String[] args = variable.get("commandlineValues").toString().trim().split(",");
            for (String arg : args) {
                String unescape = unescape(arg);
                String resolve = resolve(unescape);
                String e = encloseSpace(resolve);
                command.add(e);
            }
        }
        return listToDelimitedString(command, " ");
    }

    /**
     * 스크립트를 <tt>pythonScript.py</tt> 파일로 저장한다.
     *
     * @param pythonScript 커멘드
     * @param baseDir      파일을 저장할 기준경로
     * @return 저장한 파일의 절대 경로
     * @throws java.io.IOException 파일을 저장할 수 없는 경우
     */
    private String savePythonScript(String pythonScript, String baseDir) throws IOException {
        File cliPath = new File(baseDir, "script.py");
        FileCopyUtils.copy(pythonScript.getBytes(), cliPath);
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
    private String saveCommand(String script, String baseDir) throws IOException {
        File cliPath = new File(baseDir, "command.sh");
        FileCopyUtils.copy(script.getBytes(), cliPath);
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