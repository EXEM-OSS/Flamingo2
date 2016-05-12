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
package org.opencloudengine.flamingo2.engine.pig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.Reactor;
import reactor.event.Event;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Service
public class PigRemoteServiceImpl implements PigRemoteService {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(PigRemoteServiceImpl.class);

    private String pigTempDir;

    @Autowired
    WebSocketUtil webSocketUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Reactor reactor;

    @Value("#{config['pig.home']}")
    private String pigHome;


    /**
     * Pig Latin 스크립트를 실행한다.
     *
     * @param engineConfig 엔진 설정 정보
     * @param params       Pig 쿼리 파라미터
     */
    @Override
    public void execute(EngineConfig engineConfig, Map params) {
        Map messageMap = new HashMap();
        String message;
        //파일명은 UUID로 생성한다.
        pigTempDir = ConfigurationHelper.getHelper().get("pig.temp.dir");
        String uuid = UUID.randomUUID().toString();

        //Pig 임시폴더
        String tempDir = pigTempDir + "/" + uuid;
        //PigScript full path
        String pigScirptPath = tempDir + "/pig_latin.pig";

        //커맨드를 생성한다.
        String cmd[] = new String[]{
                pigHome + "/bin/pig",
                "-f",
                pigScirptPath,
                "-log4jconf",
                tempDir + "/log4j.properties"
        };

        try {
            //Pig
            File pigTemp = new File(pigTempDir);
            if (!pigTemp.exists()) {
                pigTemp.mkdir();
            }

            File runTemp = new File(tempDir);
            if (!runTemp.exists()) {
                runTemp.mkdir();
            }

            //PigScript 파일을 임시폴더에 생성한다.
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pigScirptPath));
            bufferedWriter.write(java.net.URLDecoder.decode(params.get("query").toString(), "UTF-8"));
            bufferedWriter.close();

            //log4j Properties 파일을 생성한다.
            getLog4JPropertiesPath(tempDir);

            //커맨드를 실행한다.
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                messageMap = new HashMap();
                messageMap.put("isError", false);
                messageMap.put("isEnd", false);
                messageMap.put("message", line);
                messageMap.put("uuid", params.get("uuid"));
                message = objectMapper.writeValueAsString(messageMap);
                webSocketUtil.PushNotification(params.get("websocketKey").toString(), "/topic/pig", message);

                Thread.sleep(10);
            }

            messageMap = new HashMap();
            messageMap.put("isError", false);
            messageMap.put("isEnd", true);
            messageMap.put("message", "FINISHED");
            messageMap.put("uuid", params.get("uuid"));
            message = objectMapper.writeValueAsString(messageMap);
            webSocketUtil.PushNotification(params.get("websocketKey").toString(), "/topic/pig", message);
        } catch (Exception e) {

            message = "{\"isError\": true, \"isEnd\": true, \"message\": \"" + e.getCause().toString() + "\", \"uuid\": \"" + params.get("uuid").toString() + "\"}";
            webSocketUtil.PushNotification(params.get("websocketKey").toString(), "/topic/pig", message);
            e.printStackTrace();
        }
    }

    /**
     * Pig Latin 스크립트를 비동기 방식으로 실행한다.
     *
     * @param engineConfig 엔진 설정 정보
     * @param params       Pig 쿼리 파라미터
     */
    @Override
    public void executeAsync(EngineConfig engineConfig, Map params) {
        params.put("engineConfig", engineConfig);
        reactor.notify("pigQuery", Event.wrap(params));
    }

    /**
     * 기준 경로에 Pig 실행을 위한 Log4J Properties 파일을 저장한다.
     *
     * @param path 로그파일의 경로
     * @return 저장한 Log4J Properties 파일의 경로
     */
    private String getLog4JPropertiesPath(String path) {
        String propertiesPath = path + "/log4j.properties";
        try {
            Properties props = new Properties();
            props.setProperty("log4j.logger.org.apache.pig", "INFO, B, stdout");
            props.setProperty("log4j.logger.org.apache.hadoop", "INFO, B, stdout");
            props.setProperty("log4j.logger.org.apache.commons", "INFO, B, stdout");
            props.setProperty("log4j.appender.B", "org.apache.log4j.FileAppender");
            props.setProperty("log4j.appender.B.file", path + "/pig.log");
            props.setProperty("log4j.appender.B.layout", "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.appender.B.layout.ConversionPattern", "%d %-5p [%c] %m%n");
            props.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
            props.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d %-5p [%c] %m%n");

            OutputStream os = new FileOutputStream(propertiesPath);
            props.store(os, "");
            os.close();
            return propertiesPath;
        } catch (Exception ex) {
            throw new ServiceException(ExceptionUtils.getMessage("Unable to create a file Log4j Properties."), ex);
        }
    }
}
