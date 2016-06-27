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
package org.opencloudengine.flamingo2.agent.spark.streaming;

import com.google.common.base.Joiner;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hyperic.sigar.*;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.cli.*;
import org.opencloudengine.flamingo2.util.cli.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.opencloudengine.flamingo2.util.FileUtils.getPath;
import static org.opencloudengine.flamingo2.util.StringUtils.listToDelimitedString;

/**
 * Spark Streaming Remote Service Implementation
 *
 * @author Myeongha KIM
 * @since 2.1
 */
public class SparkStreamingRemoteServiceImpl implements SparkStreamingRemoteService {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(SparkStreamingRemoteServiceImpl.class);

    /**
     * SLF4J Exception Logging
     */
    private Logger exceptionLogger = LoggerFactory.getLogger("flamingo.exception");

    /**
     * Hyperic Sigar
     */
    private Sigar sigar;

    @Override
    public boolean createSparkApplication(String sparkUserWorkingPath) {
        try {
            // sparkUserWorkingPath: ${user.home.linux.path}/${user.system.agent.spark.home}/${username}/${appName_yyyyMMddHHmmss}
            File directory = new File(sparkUserWorkingPath);

            //FIXME > Not Tested
            if (!directory.exists()) {
                FileUtils.forceMkdir(directory);
            }

            setOwnership(sparkUserWorkingPath, "admin");

            logger.debug("Spark Streaming Working 디렉토리'{}'가 생성되었습니다.", sparkUserWorkingPath);

            return true;
        } catch (Exception ex) {
            throw new ServiceException("작업 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    @Override
    public boolean createSparkApplication(String jarFileFQP, byte[] jarFileBytes) {
        InputStream is = new ByteArrayInputStream(jarFileBytes);
        String sparkUserWorkingPath = getPath(jarFileFQP);

        try {
            // sparkUserWorkingPath: ${user.home.linux.path}/${user.system.agent.spark.home}/${username}/${appName_yyyyMMddHHmmss}
            File directory = new File(sparkUserWorkingPath);

            if (!directory.exists()) {
                FileUtils.forceMkdir(directory);
            }

            setOwnership(sparkUserWorkingPath, "admin");

            logger.debug("Spark Streaming Working 디렉토리'{}'가 생성되었습니다.", sparkUserWorkingPath);

            // jarFileFQP: ${user.home.linux.path}/${user.system.agent.spark.home}/${username}/${appName_yyyyMMddHHmmss}/${jarFileName}
            File file = new File(jarFileFQP);

            if (!file.createNewFile()) {
                throw new ServiceException(MessageFormatter.arrayFormat("{} 경로에 jar 파일을 업로드할 수 없습니다.", new String[]{sparkUserWorkingPath}).getMessage());
            }

            FileCopyUtils.copy(jarFileBytes, file);
            setOwnership(jarFileFQP, "admin");

            logger.debug("Spark Streaming Jar 파일'{}'이 업로드되었습니다.", jarFileFQP);

            return true;
        } catch (Exception ex) {
            throw new ServiceException("작업 디렉토리를 생성할 수 없습니다.", ex);
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
                // Ignored
            }
        }
    }

    @Override
    public boolean startSparkStreamingApp(Map sparkStreamingMap, Map<String, String> defaultEnvs) {
        String sparkUserWorkingPath = (String) sparkStreamingMap.get("sparkUserWorkingPath");
        String applicationId = (String) sparkStreamingMap.get("applicationId");
        String applicationName = (String) sparkStreamingMap.get("applicationName");

        try {
            String cmds = buildCommand(sparkStreamingMap, defaultEnvs);
            saveCommandFile(cmds, sparkUserWorkingPath);

            logger.debug("Start Spark Streaming Command Lines : {}", cmds);

//            String[] cmds = StringUtils.splitPreserveAllTokens(cli, ",");
            FileWriter fileWriter = new FileWriter(logger, sparkUserWorkingPath + "/streaming.log");

            Map<String, Object> socketParams = new HashMap<>();
            socketParams.put("applicationId", applicationId);
            socketParams.put("applicationName", applicationName);
            socketParams.put("type", "sparkStreaming");
            socketParams.put("user", sparkStreamingMap.get("user"));

            logger.debug("Start Spark Streaming Application : {}", applicationName);

            ManagedProcess managedProcess = new ManagedProcess(cmds, defaultEnvs, sparkUserWorkingPath, logger, fileWriter);
            managedProcess.setSocketParams(socketParams);
            managedProcess.runBackground();

            return true;
        } catch (Exception ex) {
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });
            return false;
        }
    }

    @Override
    public boolean stopSparkStreamingApp(Map sparkStreamingMap) {
        InputStream is = null;
        String sparkUserWorkingPath = (String) sparkStreamingMap.get("sparkUserWorkingPath");
        String applicationId = (String) sparkStreamingMap.get("applicationId");
        String state = (String) sparkStreamingMap.get("state");
        boolean deleteKey = (boolean) sparkStreamingMap.get("deleteKey");
        String sparkPidFilePath = sparkUserWorkingPath + "/PID";
        String pid;
        String killPidCli = "kill -9 {}";

        logger.debug("Spark User Working Path To Stop : {}", sparkUserWorkingPath);

        try {
            if (state.equalsIgnoreCase("RUNNING")) {
                is = new FileInputStream(new File(sparkPidFilePath).getPath());
                pid = IOUtils.toString(is, "UTF-8");

                logger.debug("Spark Streaming ApplicationId's PID : {}", pid);

                String cli = MessageFormatter.arrayFormat(killPidCli, new String[]{pid}).getMessage();

                Process process = Runtime.getRuntime().exec("/bin/sh");
                Expect expect = new ExpectBuilder()
                        .withInputs(process.getInputStream())
                        .withOutput(process.getOutputStream())
                        .withTimeout(3, TimeUnit.SECONDS)
                        .withExceptionOnFailure()
                        .build();
                logger.debug("Spark Streaming 애플리케이션이 종료합니다. CLI : '{}'", cli);

                expect.sendLine(cli);
//                process.waitFor();
                expect.close();

                logger.debug("Spark Streaming 애플리케이션이 종료되었습니다. '{}': '{}'", applicationId, pid);
            }

            if (deleteKey) {
                logger.debug("사용자의 Spark Streaming 작업 디렉토리를 삭제합니다. Working Path: {}", sparkUserWorkingPath);

                FileUtils.forceDelete(new File(sparkUserWorkingPath));

                logger.debug("선택한 사용자의 Spark Streaming 작업 디렉토리가 삭제되었습니다.");
            }

            return true;
        } catch (Exception ex) {
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
     * command line 명령어를 생성한다.
     */
    private String buildCommand(Map sparkStreamingMap, Map<String, String> defaultEnvs) throws Exception {
        String username = (String) sparkStreamingMap.get("username");
        String classPath = (String) sparkStreamingMap.get("classPath");
        String jarFileFQP = (String) sparkStreamingMap.get("jarFileFQP");
        String applicationId = (String) sparkStreamingMap.get("applicationId");
        String applicationName = (String) sparkStreamingMap.get("applicationName");
        String sparkSubmit = (String) sparkStreamingMap.get("sparkSubmit");
        String streamingClass = (String) sparkStreamingMap.get("streamingClass");
        String sparkMasterUrl = (String) sparkStreamingMap.get("sparkMasterUrl");
        String driver = (String) sparkStreamingMap.get("driver");
        String javaOpts = (String) sparkStreamingMap.get("javaOpts");
        String variableValues = (String) sparkStreamingMap.get("variableValues");
        List<String> command = new LinkedList<>();
        List<String> dependencies = new ArrayList<>();

/*        String userCli = "su {}";
        String cli = MessageFormatter.arrayFormat(userCli, new String[]{username}).getMessage();
        command.add(cli);*/

        command.add("java -cp");

        if (!jarFileFQP.isEmpty()) {
            classPath += ":" + jarFileFQP;
        }

        dependencies.add(classPath);
        command.add(Joiner.on(":").join(dependencies));

        command.add("-DApplicationId=" + applicationId);
        command.add("-DApplicationName=" + applicationName);

        if (javaOpts != null && !isEmpty(javaOpts)) {
            command.add(javaOpts);
        }

        command.add(sparkSubmit);

        command.add("--class");
        command.add(streamingClass);

        // 시스템 에이전트가 슬레이브 노드나 다른 서버에서 실행할 때 마스터 URL 지정
        command.add("--master");
        command.add(sparkMasterUrl);

        // -driver /data1/cloudine/system-agent/spark:spark-examples-1.3.0-cdh5.4.0-hadoop2.6.0-cdh5.4.0.jar
        command.add(driver);

        if (variableValues != null && !isEmpty(variableValues)) {
            String[] args = variableValues.trim().split(",");
            Collections.addAll(command, args);
        }

        return listToDelimitedString(command, " ");
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

    @Override
    public boolean getSparkStreamingAppState(String sparkPidFilePath) {
        InputStream inputStream = null;
        String pid = null;
        String pidState = null;

        try {
            inputStream = new FileInputStream(new File(sparkPidFilePath).getPath());
            pid = IOUtils.toString(inputStream, "UTF-8");

            // getProcState()'s return values : Running, Zombie, etc.
            ProcState procState = sigar.getProcState(pid);
            pidState = String.valueOf(procState.getState());

            // PID State : { SLEEP = 'S', RUN = 'R', STOP = 'T', ZOMBIE = 'Z', IDLE = 'D' }
            logger.debug("Alive PID : {}, State : {}", pid, pidState);

            return true;
        } catch (Exception ex) {
            logger.debug("Dead PID : {}, State : {}", pid, pidState);
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });

            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> getSparkStreamingAppSystemResourceUsage(String sparkPidFilePath) {
        InputStream inputStream = null;
        String pid = null;
        Map<String, Object> usageMap = new HashMap<>();

        try {
            inputStream = new FileInputStream(new File(sparkPidFilePath).getPath());
            pid = IOUtils.toString(inputStream, "UTF-8");
            ProcCpu procCpu = sigar.getProcCpu(pid);
            ProcState procState = sigar.getProcState(pid);
            ProcMem procMem = sigar.getProcMem(pid);

            usageMap.put("cpuUsage", procCpu.getPercent());
            usageMap.put("activeThreads", procState.getThreads());
            usageMap.put("memoryUsage", procMem.getSize());

            return usageMap;
        } catch (Exception ex) {
            logger.debug("Dead: PID : {}", pid);
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });

            usageMap.put("cpuUsage", "");
            usageMap.put("activeThreads", "");
            usageMap.put("memoryUsage", "");

            return usageMap;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 디렉토리 및 파일의 소유권을 변경한다.
     *
     * @param path      소유권을 변경할 디렉토리 또는 파일의 경로
     * @param username  소유자명
     */
    private void setOwnership(String path, String username) {
        // 폴더 생성 후 사용자 권한 변경
        try {
            Path p = Paths.get(path);
            UserPrincipalLookupService upls = FileSystems.getDefault().getUserPrincipalLookupService();
            UserPrincipal upl = upls.lookupPrincipalByName(username);
            Path appliedPath = Files.setOwner(p, upl);

            logger.debug("{} 사용자 디렉토리 {}의 권한이 변경되었습니다.", username, appliedPath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSigar(Sigar sigar) {
        this.sigar = sigar;
    }
}
