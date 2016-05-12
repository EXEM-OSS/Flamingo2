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
import org.apache.commons.lang.StringUtils;
import org.opencloudengine.flamingo2.engine.fs.FileSystemUtils;
import org.opencloudengine.flamingo2.engine.hadoop.NamenodeRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.ConfigurationUtils;
import org.opencloudengine.flamingo2.util.cli.FileWriter;
import org.opencloudengine.flamingo2.util.cli.ManagedProcess;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.FileCopyUtils;
import org.uengine.kernel.ProcessInstance;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.opencloudengine.flamingo2.util.StringUtils.listToDelimitedString;
import static org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper.getHelper;

/**
 * Java를 실행하는 태스크
 *
 * @author Jae Hee, Lee
 * @since 2.0
 */
public class JavaTask extends InterceptorAbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(JavaTask.class);

    /**
     * Default HDFS File System URL
     */
    private String fsDefaultFS;

    @Override
    public void runTask(ProcessInstance instance) throws Exception {
        fsDefaultFS = String.format("hdfs://%s:%s", getHelper().get(clusterName + ".nn.address"), getHelper().get(clusterName + ".nn.port"));

        FileUtils.forceMkdir(new File(working));

        buildCoreSiteXml(working);

        saveScriptFile(buildCommand(working), working);

        String cli = MessageFormatter.arrayFormat("sh {}/script.sh", new Object[]{working}).getMessage();
        saveCommandFile(cli, working);
        String[] cmds = StringUtils.splitPreserveAllTokens(cli, " ");

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
            if (!isEmpty(defaultEnvs.get(key))) {
                command.add(MessageFormatter.arrayFormat("export {}={}\n", new Object[]{key, defaultEnvs.get(key)}).getMessage());
            }
        }

        command.add(getHelper().get("java.home") + "/bin/java");

        if (variable.get("javaOpts") != null && !StringUtils.isEmpty(variable.get("javaOpts").toString())) {
            command.add(resolve(variable.get("javaOpts").toString().trim()));
        }

        command.add("-classpath");
        downloadClasses(command, working);

        if (variable.get("driver") != null && !StringUtils.isEmpty(variable.get("driver").toString())) {
            command.add(resolve(variable.get("driver").toString().trim()));
        }

        if (variable.get("variableValues") != null && !StringUtils.isEmpty(variable.get("variableValues").toString())) {
            String[] args = variable.get("variableValues").toString().trim().split(",");
            for (String arg : args) {
                command.add(resolve(arg));
            }
        }

        return listToDelimitedString(command, " ");
    }

    /**
     * Java JAR 파일을 다운로드한다.
     *
     * @param command 커맨드 라인
     */
    private void downloadClasses(List<String> command, String working) {
        List<String> dependencies = new ArrayList<>();
        ArtifactLoader artifactLoader = ArtifactLoaderFactory.getArtifactLoader(clusterName);
        String artifactCachePath = variable.getProperty("working");

        dependencies.add(artifactLoader.load(artifactCachePath, variable.get("jar").toString().trim()));

        if (variable.get("path") != null && !StringUtils.isEmpty(variable.get("path").toString())) {
            String[] classpaths = variable.get("path").toString().trim().split(",");
            for (String classpath : classpaths) {
                dependencies.add(artifactLoader.load(artifactCachePath, resolve(classpath)));
            }
        }
        dependencies.add(working);
        command.add(Joiner.on(":").join(dependencies));
    }

    /**
     * 하둡 환경설정을 <tt>core-site.xml</tt> 파일로 저장한다.
     *
     * @param working 파일을 저장할 기준경로
     * @return 저장한 파일의 절대 경로
     */
    private String buildCoreSiteXml(String working) {
        Map<String, Object> hadoopConf = new HashMap<>();
        buildHadoopConf(hadoopConf);

        String coresitePath = working + "/core-site.xml";
        String confXml = ConfigurationUtils.mapToXML(hadoopConf);
        FileSystemUtils.saveToFile(confXml.getBytes(), coresitePath);
        return coresitePath;
    }

    /**
     * Hadoop MapReduce Job 동작에 필요한 Key Value 형식의 파라미터를 구성한다.
     * Key와 Value를 모두 지정하는 경우 기본 형식은 <tt>--KEY VALUE</tt> 이며
     * Key를 생략하는 경우 <tt>VALUE1 VALUE2 ...</tt> 형식으로 구성한다.
     */
    private void buildHadoopConf(Map<String, Object> hadoopConf) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService rmrs = engineService.getResourceManagerRemoteService();
        NamenodeRemoteService nrs = engineService.getNamenodeRemoteService();

        Map<String, Object> configuration = rmrs.getConfiguration(engineConfig);
        Set<String> rmkeys = configuration.keySet();
        for (String key : rmkeys) {
            try {
                if (configuration.get(key) != null) {
                    hadoopConf.put(key, "<![CDATA[" + configuration.get(key) + "]]>");
                }
            } catch (Exception ex) {
                // NPE
            }
        }

        // MapR이 아닌 경우
        if (!"true".equals(getHelper().get("mapr.enabled"))) {
            try {
                Map conf = nrs.getConfiguration(engineConfig);
                Set nnkeys = conf.keySet();
                for (Object key : nnkeys) {
                    try {
                        if (configuration.get(key) != null) {
                            hadoopConf.put((String) key, "<![CDATA[" + configuration.get(key) + "]]>");
                        }
                    } catch (Exception ex) {
                        // NPE
                    }
                }
            } catch (IOException e) {
                hadoopConf.put("fs.defaultFS", "hdfs://" + getHelper().get(clusterName + ".nn.address") + ":" + getHelper().get(clusterName + ".nn.port"));
            }
        } else {
            hadoopConf.put("fs.defaultFS", getHelper().get(clusterName + ".mapr.fs"));
        }

        hadoopConf.put("mapreduce.framework.name", "yarn");
        hadoopConf.put("mapreduce.job.name", this.getTaskHistory().getName());
        hadoopConf.put("mapred.job.name", this.getTaskHistory().getName());

        hadoopConf.put("flamingo.action.id", "" + this.getTaskHistory().getTaskId());
        hadoopConf.put("flamingo.action.name", this.getTaskHistory().getName());
        hadoopConf.put("flamingo.job.id", "" + this.getTaskHistory().getIdentifier());
        hadoopConf.put("flamingo.job.stringId", this.getWorkflowHistory().getJobStringId());
        hadoopConf.put("flamingo.job.name", this.getWorkflowHistory().getJobName());
        hadoopConf.put("flamingo.workflow.id", this.getWorkflowHistory().getWorkflowId());
        hadoopConf.put("flamingo.workflow.instance.id", this.getTaskHistory().getProcessId());
        hadoopConf.put("flamingo.username", this.getTaskHistory().getUsername());
        hadoopConf.put("flamingo.workflow.name", this.getWorkflowHistory().getWorkflowName());
        hadoopConf.put("flamingo.log.path", this.getTaskHistory().getLogDirectory());

        if (variable.get("hadoopKeys") != null && variable.get("hadoopValues") != null && !StringUtils.isEmpty(variable.get("hadoopKeys").toString()) && !StringUtils.isEmpty(variable.get("hadoopValues").toString())) {
            String[] hadoopKeys = (variable.get("hadoopKeys").toString()).split(",");
            String[] hadoopValues = (variable.get("hadoopValues").toString()).split(",");

            for (int i = 0; i < hadoopKeys.length; i++) {
                hadoopConf.put(hadoopKeys[i], hadoopValues[i]);
            }
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
     * 커멘트를 <tt>command.sh</tt> 파일로 저장한다.
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