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
package org.opencloudengine.flamingo2.web.realtime.spark.streaming;

import org.apache.commons.lang.SystemUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.realtime.spark.streaming.SparkStreamingRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.model.rest.IoTService;
import org.opencloudengine.flamingo2.util.FileUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.remote.EngineLookupService;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper.getHelper;

/**
 * Real-Time Spark Streaming REST Controller
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
@RestController
@RequestMapping("/realtime/spark/streaming")
public class SparkStreamingController {

    @Value("#{config['user.system.agent.urls']}")
    private String systemAgentUrls;

    @Value("#{config['user.home.linux.path']}")
    private String linuxUserHome;

    @Value("#{config['user.system.agent.spark.home']}")
    private String systemAgentSparkHome;

    @Value("#{config['spark.home']}")
    private String sparkHome;

    @Value("#{config['spark.submit']}")
    private String sparkSubmit;

    @Value("#{config['spark.master.url']}")
    private String sparkMasterUrl;

    @Value("#{config['spark.classPath']}")
    private String sparkClassPath;

    @Value("#{config['spark.examples.jar.path']}")
    private String sparkExamplesJarPath;

    @Value("#{config['java.home']}")
    private String javaHome;

    /**
     * IoT 서비스 목록을 가져온다
     *
     * @param clusterName   클러스터명
     * @param node          IoT 서비스 Tree ID
     * @return  IoT Services List
     */
    @RequestMapping(value = "ioTServices", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getIoTServices(@RequestParam String clusterName,
                                   @RequestParam int depth,
                                   @RequestParam(defaultValue = "IoT") String node,
                                   @RequestParam String serviceId,
                                   @RequestParam String serviceTypeId,
                                   @RequestParam String deviceTypeId,
                                   @RequestParam String columnsType) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();

        Map ioTMap = new HashMap();
        ioTMap.put("depth", depth);
        ioTMap.put("node", node);
        ioTMap.put("serviceId", serviceId);
        ioTMap.put("serviceTypeId", serviceTypeId);
        ioTMap.put("deviceTypeId", deviceTypeId);
        ioTMap.put("columnsType", columnsType);

        List<IoTService> ioTServiceList = ssrs.getIoTServices(ioTMap);
        /**
         * Ext Tree 구조에서 자식 노드에 해당하는 결과값만을 보여주기 위해서는 트리 노드의 ID와 DB의 ID를 구분해야 함.
         * IoT 서비스 목록에서 선택한 서비스에 해당하는 결과를 필터링 해서 UI로 전달해야 한다.
         *
         * Case 1. depth 0: serviceName | serviceId             ex) 스포츠 | 10000001
         * Case 2. depth 1: serviceTypeName | serviceTypeId     ex) 수영 | SW
         * Case 3. depth 2: deviceTypeName | deviceTypeId       ex) 수영속도센서 | DEV_TYPE_SW1
         */

        for (IoTService ioTService : ioTServiceList) {
            switch (depth) {
                case 0:
                    ioTService.setTreeId(node + "/" + ioTService.getServiceId());
                    ioTService.setText(ioTService.getServiceName());
                    ioTService.setNodeName(ioTService.getServiceId());
                    ioTService.setLeaf(false);
                    break;
                case 1:
                    ioTService.setTreeId(node + "/" + ioTService.getServiceTypeId());
                    ioTService.setText(ioTService.getServiceTypeName());
                    ioTService.setNodeName(ioTService.getServiceTypeId());
                    ioTService.setLeaf(false);
                    break;
                case 2:
                    ioTService.setTreeId(node + "/" + ioTService.getDeviceTypeId());
                    ioTService.setText(ioTService.getDeviceTypeName());
                    ioTService.setNodeName(ioTService.getDeviceTypeId());
                    ioTService.setLeaf(true);
                    break;
            }
        }

        Response response = new Response();
        response.getList().addAll(ioTServiceList);
        response.setSuccess(true);
        return response;
    }

    /**
     * 등록된 Spark Streaming Application 목록을 가져온다.
     *
     * @param clusterName   클러스터명
     * @param page          페이지 목록
     * @param limit         한 페이지당 보여줄 행 수
     * @return  true or false
     */
    @RequestMapping(value = "sparkStreamingApps", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getAllSparkStreamingApps(@RequestParam String clusterName,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int limit) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();
        String username = getSessionUsername();
        int userLevel = getSessionUserLevel();

        Map sparkStreamingJobsMap = new HashMap();
        sparkStreamingJobsMap.put("username", username);
        sparkStreamingJobsMap.put("userLevel", userLevel);
        sparkStreamingJobsMap.put("clusterName", clusterName);
        sparkStreamingJobsMap.put("page", page);
        sparkStreamingJobsMap.put("limit", limit);

        Response response = new Response();
        response.setTotal(ssrs.getTotalCountOfSparkStreamingApps(sparkStreamingJobsMap));
        response.getList().addAll(ssrs.getAllSparkStreamingApps(sparkStreamingJobsMap));
        response.setSuccess(true);
        return response;
    }

    /**
     * Spark Streaming Application을 등록한다.
     *
     * @param req   Application을 등록할 정보
     * @return  result
     * @throws IOException
     */
    @RequestMapping(value = "createSparkStreamingApp", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public String createSparkStreamingApp(HttpServletRequest req) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(req.getParameter("clusterName"));
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();
        List<String> systemAgentList = new ArrayList<>();

        // 애플리케이션을 등록할 때 DB에 등록된 각 서버의 애플리케이션 개수 체크 후 가장 적은 서버 쪽으로 우선적으로 등록한다.
        if (!systemAgentUrls.isEmpty()) {
            if (systemAgentUrls.contains(",")) {
                String[] agentItems = systemAgentUrls.split(",");
                Collections.addAll(systemAgentList, agentItems);
            } else {
                systemAgentList.add(systemAgentUrls);
            }
        }

        // 애플리케이션이 가장 적게 등록된 서버를 우선적으로 선택한다.
        String systemAgentUrl = ssrs.getOptimalServer(systemAgentList);

        System.out.println("OptimalServer : " + systemAgentUrl);

        // hostname:port
        String url = StringUtils.getVariableKey(systemAgentUrl, ":");
        int port = Integer.parseInt(StringUtils.getVariableValue(systemAgentUrl, ":"));

        String applicationName = req.getParameter("applicationName");
        String variables = req.getParameter("variables");
        String username = req.getParameter("username");
        String streamingClass = req.getParameter("streamingClass");
        String javaOpts = req.getParameter("javaOpts");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = simpleDateFormat.format(new Date());

        String sparkUserWorkingPath = linuxUserHome + SystemUtils.FILE_SEPARATOR + systemAgentSparkHome
                + SystemUtils.FILE_SEPARATOR + username + SystemUtils.FILE_SEPARATOR + applicationName + "_" + formattedDate;

        DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) req;
        MultipartFile uploadedFile = request.getFile("jarFile");
        Map sparkStreamingMap = new HashMap();
        boolean result;

        sparkStreamingMap.put("url", url);
        sparkStreamingMap.put("port", port);
        sparkStreamingMap.put("server", url + ":" + port);
        sparkStreamingMap.put("applicationId", applicationName + "_" + formattedDate);
        sparkStreamingMap.put("applicationName", applicationName);
        sparkStreamingMap.put("streamingClass", streamingClass);
        sparkStreamingMap.put("javaOpts", javaOpts);
        sparkStreamingMap.put("variables", variables);
        sparkStreamingMap.put("username", username);
        sparkStreamingMap.put("sparkUserWorkingPath", sparkUserWorkingPath);

        if (!uploadedFile.isEmpty()) {
            InputStream inputStream;
            inputStream = uploadedFile.getInputStream();
            byte[] jarFileBytes = FileCopyUtils.copyToByteArray(inputStream);
            String jarFilename = FileUtils.getFilename(req.getParameter("jarFile"));

            // Fully Qualified Jar File Path: systemAgentHome + /spark + /username + /date + /filename
            String jarFileFQP = sparkUserWorkingPath + SystemUtils.FILE_SEPARATOR + jarFilename;

            sparkStreamingMap.put("jarFileFQP", jarFileFQP);
            sparkStreamingMap.put("jarFilename", jarFilename);

            result = ssrs.createSparkApplication(sparkStreamingMap, jarFileBytes);
        } else {
            sparkStreamingMap.put("jarFileFQP", "");
            sparkStreamingMap.put("jarFilename", "");

            result = ssrs.createSparkApplication(sparkStreamingMap);
        }

        Response response = new Response();
        response.setSuccess(result);

        return new ObjectMapper().writeValueAsString(response);
    }

    /**
     * 선택한 Spark Streaming Application을 실행한다.
     *
     * @param sparkStreamingMap Spark Streaming Application Map
     * @return true or false
     */
    @RequestMapping(value = "startSparkStreamingApp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response startSparkStreamingApp(@RequestBody Map sparkStreamingMap) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine((String) sparkStreamingMap.get("clusterName"));
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();

        String username = getSessionUsername();
        String variables = (String) sparkStreamingMap.get("variables");
        String server = (String) sparkStreamingMap.get("server");
        List<String> variableList = new ArrayList<>();

        if (!variables.isEmpty()) {
            String[] variableItems = variables.split(",");

            for (String variable : variableItems) {
                variableList.add(StringUtils.getVariableKey(variable, ":"));
            }

            sparkStreamingMap.put("variableValues", StringUtils.join(variableList, ","));
        }

        sparkStreamingMap.put("url", getUrl(server));
        sparkStreamingMap.put("port", getPort(server));
        sparkStreamingMap.put("username", username);
        sparkStreamingMap.put("classPath", sparkClassPath);
        sparkStreamingMap.put("sparkSubmit", sparkSubmit);
        sparkStreamingMap.put("sparkHome", sparkHome);
        sparkStreamingMap.put("sparkMasterUrl", sparkMasterUrl);
        sparkStreamingMap.put("driver", sparkExamplesJarPath);
        sparkStreamingMap.put("user", SessionUtils.get());

        Response response = new Response();
        response.setSuccess(ssrs.startSparkStreamingApp(sparkStreamingMap, getDefaultEnvs()));

        return response;
    }

    /**
     * 선택한 Spark Streaming Application을 실행한다.
     *
     * @param sparkStreamingMap Spark Streaming Application Map
     * @return true or false
     */
    @RequestMapping(value = "stopSparkStreamingApp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response stopSparkStreamingApp(@RequestBody Map sparkStreamingMap) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine((String) sparkStreamingMap.get("clusterName"));
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();

        String server = (String) sparkStreamingMap.get("server");

        sparkStreamingMap.put("url", getUrl(server));
        sparkStreamingMap.put("port", getPort(server));
        sparkStreamingMap.put("deleteKey", false);

        Response response = new Response();
        response.setSuccess(ssrs.stopSparkStreamingApp(sparkStreamingMap));

        return response;
    }

    /**
     * 선택한 Spark Streaming Application을 실행한다.
     *
     * @param sparkStreamingMap Spark Streaming Application Map
     * @return true or false
     */
    @RequestMapping(value = "killSparkStreamingApp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response killSparkStreamingApp(@RequestBody Map sparkStreamingMap) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine((String) sparkStreamingMap.get("clusterName"));
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();

        String server = (String) sparkStreamingMap.get("server");

        sparkStreamingMap.put("url", getUrl(server));
        sparkStreamingMap.put("port", getPort(server));
        sparkStreamingMap.put("deleteKey", true);

        Response response = new Response();
        response.setSuccess(ssrs.killSparkStreamingApp(sparkStreamingMap));

        return response;
    }

    /**
     * 선택한 Spark Streaming Applicaton의 Summary 정보를 가져온다.
     *
     * @param clusterName       클러스터명
     * @param server            서버정보
     * @param applicationId     애플리케이션 ID
     * @return Summary List
     */
    @RequestMapping(value = "getSparkStreamingAppSummary", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getSparkStreamingAppSummary(@RequestParam String clusterName,
                                                @RequestParam String server,
                                                @RequestParam String applicationId,
                                                @RequestParam(defaultValue = "") String startDate,
                                                @RequestParam(defaultValue = "") String endDate) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        SparkStreamingRemoteService ssrs = engineService.getSparkStreamingRemoteService();
        String username = getSessionUsername();
        Map sparkStreamingMap = new HashMap();

        if (!server.isEmpty()) {
            sparkStreamingMap.put("url", getUrl(server));
            sparkStreamingMap.put("port", getPort(server));
        }

        sparkStreamingMap.put("username", username);
        sparkStreamingMap.put("applicationId", applicationId);
        sparkStreamingMap.put("startDate", startDate);
        sparkStreamingMap.put("endDate", endDate);

        List<Map<String, Object>> historyList = ssrs.getSparkStreamingAppSummary(sparkStreamingMap);

        Response response = new Response();
        response.getList().addAll(historyList);
        response.setSuccess(true);
        return response;
    }

    /**
     * 현재 세션의 사용자명을 가져온다.
     *
     * @return username
     */
    private String getSessionUsername() {
        return SessionUtils.getUsername();
    }

    /**
     * 현재 세션의 사용자 등급 정보를 가져온다.
     *
     * @return level
     */
    private int getSessionUserLevel() {
        return SessionUtils.getLevel();
    }

    /**
     * 원격지에 설치된 시스템 에이전트의 Url 정보를 가져온다.
     *
     * @param server 서버 정보
     * @return Url
     */
    private String getUrl(String server) {
        String[] agent = StringUtils.splitPreserveAllTokens(server, ":");
        return agent[0];
    }

    /**
     * 원격지에 설치된 시스템 에이전트의 Port 정보를 가져온다.
     *
     * @param server 서버 정보
     * @return Port
     */
    private int getPort(String server) {
        String[] agent = StringUtils.splitPreserveAllTokens(server, ":");
        return Integer.parseInt(agent[1]);
    }

    /**
     * 스크립트를 실행하기 위해서 필요한 환경변수를 가져온다.
     *
     * @return 환경변수
     */
    private Map<String, String> getDefaultEnvs() {
        Map<String, String> envs = new HashMap<>();

        envs.put("PATH", "/bin:/usr/bin:/usr/local/bin" + ":" + getHelper().get("hadoop.home") + "/bin" + ":" + getHelper().get("hive.home") + "/bin" + ":" + getHelper().get("pig.home") + "/bin");
        envs.put("HADOOP_CLIENT_OPTS", MessageFormatter.format("-javaagent:{}=resourcescript:mr2.bm", getHelper().get("flamingo.mr.agent.jar.path")).getMessage());
        envs.put("JAVA_HOME", javaHome);
        envs.put("HADOOP_HDFS_HOME", getHelper().get("hadoop.hdfs.home"));
        envs.put("HADOOP_MAPRED_HOME", getHelper().get("hadoop.mapred.home"));
        envs.put("HADOOP_HOME", getHelper().get("hadoop.home"));
        envs.put("HIVE_HOME", getHelper().get("hive.home"));
        envs.put("PIG_HOME", getHelper().get("pig.home"));

        if (!isEmpty(getHelper().get("hadoop.user.name"))) {
            envs.put("HADOOP_USER_NAME", getHelper().get("hadoop.user.name"));
        } else {
            envs.put("HADOOP_USER_NAME", getSessionUsername());
        }

        return envs;
    }
}
