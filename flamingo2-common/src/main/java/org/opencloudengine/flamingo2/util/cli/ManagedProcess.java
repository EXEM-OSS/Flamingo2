package org.opencloudengine.flamingo2.util.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * {@link Process}의 관리 기능을 제공하기 위한 Process.
 * 출력은 데드락을 방지하기 위해서 별도 쓰레드로 처리한다.
 *
 * @author Byoung Gon, Kim
 * @since 0.5
 */
public class ManagedProcess {

    /**
     * 프로세스의 작업 디렉토리
     */
    private final String workingDir;

    /**
     * 실행할 커맨드 라인 리스트
     */
    private final List<String> cmd;

    /**
     * Key Value 환경 변수 목록
     */
    private final Map<String, String> env;

    /**
     * Process Startup의 Count Down Latch
     */
    private final CountDownLatch startupLatch;

    /**
     * Process Complete의 Count Down Latch
     */
    private final CountDownLatch completeLatch;

    /**
     * Process ID
     */
    private volatile int processId;

    /**
     * 커맨드 라인에서 실행하는 프로세스
     */
    private volatile Process process;

    /**
     * 소켓으로 보낼 paramSet
     */
    private Map socketParams;

    /**
     * Jackson Json
     * */
    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * SLF4J Logging
     */
    private Logger logger;

    private FileWriter fileWriter;

    private FileWriter errWriter;

    /**
     * 기본 생성자.
     *
     * @param cmd        공백을 구분자로 갖는 커맨드 라인 문자열
     * @param env        Key Value 환경 변수
     * @param workingDir 작업 디렉토리
     * @param logger     로거
     */
    public ManagedProcess(final String cmd, final Map<String, String> env, final String workingDir, final Logger logger) {
        this.cmd = StringUtils.stringToCollection(cmd, " ");
        this.env = env;
        this.workingDir = workingDir;
        this.processId = -1;
        this.startupLatch = new CountDownLatch(1);
        this.completeLatch = new CountDownLatch(1);
        this.logger = logger;
    }

    /**
     * 기본 생성자.
     *
     * @param cmd        커맨드 라인 목록
     * @param env        Key Value 환경 변수
     * @param workingDir 작업 디렉토리
     * @param logger     로거
     */
    public ManagedProcess(final List<String> cmd, final Map<String, String> env, final String workingDir, final Logger logger) {
        this.cmd = cmd;
        this.env = env;
        this.workingDir = workingDir;
        this.processId = -1;
        this.startupLatch = new CountDownLatch(1);
        this.completeLatch = new CountDownLatch(1);
        this.logger = logger;
    }

    public ManagedProcess(final List<String> cmd, final Map<String, String> env, final String workingDir, final Logger logger, FileWriter fileWriter) {
        this.cmd = cmd;
        this.env = env;
        this.workingDir = workingDir;
        this.processId = -1;
        this.startupLatch = new CountDownLatch(1);
        this.completeLatch = new CountDownLatch(1);
        this.logger = logger;
        this.fileWriter = fileWriter;
        String baseDir = fileWriter.getBaseDir();
        this.errWriter = new FileWriter(logger, baseDir + "/err.log");

    }

    public ManagedProcess(final String[] cmds, final Map<String, String> env, final String workingDir, final Logger logger, FileWriter fileWriter) {
        this.cmd = StringUtils.arrayToCollection(cmds);
        this.env = env;
        this.workingDir = workingDir;
        this.processId = -1;
        this.startupLatch = new CountDownLatch(1);
        this.completeLatch = new CountDownLatch(1);
        this.logger = logger;
        this.fileWriter = fileWriter;
        String baseDir = fileWriter.getBaseDir();
        this.errWriter = new FileWriter(logger, baseDir + "/err.log");
    }

    public Map getSocketParams() {
        return socketParams;
    }

    public void setSocketParams(Map socketParams) {
        this.socketParams = socketParams;
    }

    //소켓에 보낼 파라미터를 더한다.
    public void addSocketParam(String key, String value) {
        this.socketParams.put(key, value);
    }

    public void removeSocketParam(String key) {
        this.socketParams.remove(key);
    }

    /**
     * 이 프로세스를 실행한다. 프로세스가 완료될 때까지 블로킹한다.
     */
    public void run() throws IOException {
        if (this.isStarted() || this.isComplete()) {
            throw new IllegalStateException("프로세스는 오직 1번만 실행할 수 있습니다.");
        }

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.directory(new File(workingDir));
        builder.environment().putAll(env);
        this.process = builder.start();
        this.processId = processId(process);
        if (processId == 0) {
            logger.warn("PID를 알 수 없습니다.");
        } else {
            // 프로세스 파일에 프로세스 ID를 기록한다.
            String baseDir = fileWriter.getBaseDir();
            File pid = new File(baseDir, "PID");
            FileCopyUtils.copy(String.valueOf(processId).getBytes(), pid);
        }

        this.startupLatch.countDown();

        LogGobbler outputGobbler = new LogGobbler(new InputStreamReader(process.getInputStream()), logger, 30, fileWriter, socketParams);
        LogGobbler errorGobbler = new LogGobbler(new InputStreamReader(process.getErrorStream()), logger, 30, errWriter, socketParams);
        LogGobbler totalGobbler = new LogGobbler(new InputStreamReader(process.getErrorStream()), logger, 30, fileWriter, socketParams);

        outputGobbler.start();
        errorGobbler.start();
        totalGobbler.start();
        int exitCode = -1;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            logger.warn("프로세스를 인터럽트할 수 없습니다.", e);
        }

        completeLatch.countDown();

        // 종료하기 전에 모든 로그가 완료될때까지 대기한다.
        outputGobbler.awaitCompletion(5000);
        errorGobbler.awaitCompletion(5000);

        // 종료코드를 기록한다.
        String baseDir = fileWriter.getBaseDir();
        File code = new File(baseDir, "CODE");
        FileCopyUtils.copy(String.valueOf(exitCode).getBytes(), code);

        if (exitCode != 0) {
            logger.warn("프로세스의 종료 코드는 {}입니다.", exitCode);
            throw new ServiceException(exitCode, errorGobbler.getRecentLog());
        }
    }

    /**
     * 프로세스의 종료를 대기한다.
     *
     * @throws InterruptedException 대기중에 쓰레드가 인터럽트에 걸리는 경우
     */
    public void awaitCompletion() throws InterruptedException {
        this.completeLatch.await();
    }

    /**
     * 프로세스 시작을 대기한다.
     *
     * @throws InterruptedException 대기중에 쓰레드가 인터럽트가 걸리는 경우
     */
    public void awaitStartup() throws InterruptedException {
        this.startupLatch.await();
    }

    /**
     * 실행중인 프로세스의 Process ID를 반환한다.
     *
     * @return Process ID 또는 알 수 없는 경우 -1
     */
    public int getProcessId() {
        checkStarted();
        return this.processId;
    }

    /**
     * 프로세스를 종료한다. 이 기능은 프로세스가 시작되지 않은 상태에서는 동작하지 않는다.
     *
     * @param time 대기 시간
     * @param unit 시간 단위
     * @return 지정한 지간에 프로세스가 종료된 경우 <tt>true</tt>
     */
    public boolean softKill(final long time, final TimeUnit unit)
            throws InterruptedException {
        checkStarted();
        if (processId != 0 && isStarted()) {
            try {
                Runtime.getRuntime().exec("kill " + processId);
                return completeLatch.await(time, unit);
            } catch (IOException e) {
                logger.error("프로세스를 강제 종료할 수 없습니다.", e);
            }
            return false;
        }
        return false;
    }

    /**
     * 쓰레드 덤프를 생성한다. 이 기능은 서버측의 콘솔 로그를 확인해야 한다.
     * 이 기능은 프로세스가 시작되지 않은 상태에서는 동작하지 않는다.
     *
     * @throws InterruptedException 현제 프로세스가 인터럽트가 걸린 경우
     */
    public void dumpThread() throws InterruptedException {
        checkStarted();
        if (processId != 0 && isStarted()) {
            try {
                Runtime.getRuntime().exec("kill -3" + processId);
            } catch (IOException e) {
                logger.error("쓰레드 덤프를 생성할 수 없습니다.", e);
            }
        }
    }

    /**
     * 강제로 프로세스를 종료한다. 이 기능은 프로세스가 시작되지 않은 상태에서는 동작하지 않는다.
     */
    public void hardKill() {
        checkStarted();
        if (isRunning()) {
            process.destroy();
        }
    }

    /**
     * 현재 실행중인 프로세스의 Process ID를 반환한다.
     *
     * @param process Process ID를 확인할 프로세스
     * @return Process ID
     */
    private int processId(final Process process) {
        int processId = 0;
        try {
            Field f = process.getClass().getDeclaredField("pid");
            f.setAccessible(true);

            processId = f.getInt(process);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processId;
    }

    /**
     * 프로세스가 시작되었는지 확인하다.
     *
     * @return 프로세스가 시작되었다면 <tt>true</tt>
     */
    public boolean isStarted() {
        return startupLatch.getCount() == 0L;
    }

    /**
     * 프로세스가 종료되었는지 확인한다.
     *
     * @return 프로세스가 종료되었다면 <tt>true</tt>
     */
    public boolean isComplete() {
        return completeLatch.getCount() == 0L;
    }

    /**
     * 프로세스가 현재 실행중인지 확인한다.
     *
     * @return 프로세스가 현재 실행행중이라면 <tt>true</tt>
     */
    public boolean isRunning() {
        return isStarted() && !isComplete();
    }

    /**
     * 프로세스가 시작되었는지 확인한다.
     *
     * @throws IllegalStateException 쓰레드가 아직 시작하지 않은 경우
     */
    public void checkStarted() {
        if (!isStarted()) {
            throw new IllegalStateException("프로세스가 아직 시작하지 않았습니다.");
        }
    }

    @Override
    public String toString() {
        return "Managed Process(cmd = " + Joiner.on(" ").join(cmd) + ", env = " + env + ", working = " + workingDir + ")";
    }

    private static class LogGobbler extends Thread {

        /**
         * 로그 메시지를 읽어들이는 Reader
         */
        private final BufferedReader inputReader;

        /**
         * SLF4J Logger
         */
        private final Logger logger;

        /**
         * 순환 버퍼
         */
        private final CircularBuffer<String> buffer;

        private FileWriter fileWriter;

        /**
         * 소켓으로 보낼 socketParams
         */
        private Map socketParams;

        /**
         * 소켓으로 보내는 유저정보.
         */
        private User user;

        /**
         * 기본 생성자.
         *
         * @param inputReader  Reader
         * @param logger       Log4J Logger
         * @param bufferLines  Circular Buffer의 크기
         * @param fileWriter   파일 라이터
         * @param socketParams 소켓 파라메터
         */
        public LogGobbler(final Reader inputReader, final Logger logger, final int bufferLines, FileWriter fileWriter, Map socketParams) {
            this.fileWriter = fileWriter;
            this.inputReader = new BufferedReader(inputReader);
            this.logger = logger;
            this.buffer = new CircularBuffer(bufferLines);
            this.socketParams = socketParams;
            if(socketParams.containsKey("user")){
                this.user = (User) socketParams.get("user");
            }
        }

        @Override
        public void run() {
            try {
                ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
                WebSocketUtil webSocketUtil = applicationContext.getBean(WebSocketUtil.class);

                // 실행중인 현재 쓰레드가 인터럽트가 걸리지 않은 경우에 대해서 로그의 입력 스트림을 읽어들인다.
                while (!Thread.currentThread().isInterrupted()) {
                    String line = inputReader.readLine();
                    if (line == null) {
                        return;
                    }

                    // 로그 메시지를 버퍼에 추가하고 로그를 남긴다.
                    if (fileWriter != null) fileWriter.log(line);
                    buffer.append(line);


                    //socketParams 가 있고 유저의 웹소켓이 있다면 브로드캐스팅.
                    if (socketParams != null) {
                        Map sendMessage = new HashMap();
                        sendMessage.put("data", line);
                        sendMessage.put("command", "logCasting");
                        sendMessage.put("username", user.getUsername());
                        sendMessage.putAll(socketParams);
                        String message = objectMapper.writeValueAsString(sendMessage);

                        webSocketUtil.PushNotification(user.getUsername(), "/topic/cloudine", message);
                    }
                }
            } catch (IOException e) {
                logger.error("로깅 스트림을 읽을 수 없습니다.", e);
            }
        }

        /**
         * 완료될떄 까지 대기한다.
         *
         * @param waitMs 대기시간(millis)
         */
        public void awaitCompletion(final long waitMs) {
            try {
                join(waitMs);
            } catch (InterruptedException e) {
                logger.info("IO 쓰레드가 인터럽트되었습니다.", e);
            }
        }

        /**
         * 최근 로그 메시지를 반환한다. 로그 메시지의 반환은 순환 버퍼의 내용을 기준으로한다.
         *
         * @return 최근 로그 메시지 문자열
         */
        public String getRecentLog() {
            return Joiner.on(System.getProperty("line.separator")).join(buffer);
        }
    }
}