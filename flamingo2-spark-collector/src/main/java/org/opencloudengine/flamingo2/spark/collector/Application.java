/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.spark.collector;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.apache.commons.io.IOUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.io.InputStream;
import java.util.*;

/**
 * Created by Hyokun Park on 15. 8. 10..
 */
@EnableAutoConfiguration
@ImportResource(value = {"classpath:/applicationContext*.xml"})
@ComponentScan(basePackages = {"org.opencloudengine.flamingo2.spark.collector"})
public class Application {
    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static final long MEGA_BYTES = 1024 * 1024;

    private static final String UNKNOWN = "Unknown";

    public static void main(String[] args) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");

        System.out.println("___________.__                 .__                      ________     _________                   __        _____                         __   \n" +
                "\\_   _____/|  | _____    _____ |__| ____    ____   ____ \\_____  \\   /   _____/__________ _______|  | __   /  _  \\    ____   ____   _____/  |_ \n" +
                " |    __)  |  | \\__  \\  /     \\|  |/    \\  / ___\\ /  _ \\ /  ____/   \\_____  \\\\____ \\__  \\\\_  __ \\  |/ /  /  /_\\  \\  / ___\\_/ __ \\ /    \\   __\\\n" +
                " |     \\   |  |__/ __ \\|  Y Y  \\  |   |  \\/ /_/  >  <_> )       \\   /        \\  |_> > __ \\|  | \\/    <  /    |    \\/ /_/  >  ___/|   |  \\  |  \n" +
                " \\___  /   |____(____  /__|_|  /__|___|  /\\___  / \\____/\\_______ \\ /_______  /   __(____  /__|  |__|_ \\ \\____|__  /\\___  / \\___  >___|  /__|  \n" +
                "     \\/              \\/      \\/        \\//_____/                \\/         \\/|__|       \\/           \\/         \\//_____/      \\/     \\/      ");

        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        app.setRegisterShutdownHook(true);
        ApplicationContext ctx = app.run(args);

        System.setProperty("PID", SystemUtils.getPid());

        Properties properties = new Properties();

        InputStream inputStream = null;
        try {
            inputStream = ctx.getResource("classpath:/app.properties").getInputStream();

            properties.load(inputStream);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot load a 'classpath:/app.properties' file.", ex);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            for (Logger LOGGER : context.getLoggerList()) {
                if (LOGGER instanceof ch.qos.logback.classic.Logger) {
                    ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) LOGGER;
                    for (Iterator<Appender<ILoggingEvent>> index = logbackLogger.iteratorForAppenders(); index.hasNext(); ) {
                        Appender<ILoggingEvent> appender = index.next();
                        if ("FILE".equals(appender.getName()) && appender instanceof ch.qos.logback.core.rolling.RollingFileAppender) {
                            ch.qos.logback.core.rolling.RollingFileAppender logbackAppender = (ch.qos.logback.core.rolling.RollingFileAppender) appender;
                            logger.info("Log file is {}", logbackAppender.getFile());
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        StringBuilder builder = new StringBuilder();

        printHeader(builder, "Application Information");
        Properties appProps = new Properties();
        appProps.put("Instance", StringUtils.isEmpty(System.getProperty("instance")) ? "** UNKNOWN **" : System.getProperty("instance"));
        appProps.put("Version", properties.get("version"));
        appProps.put("Build Date", properties.get("build.timestamp"));
        appProps.put("Build Number", properties.get("build.number"));
        appProps.put("Revision Number", properties.get("revision.number"));
        appProps.put("Organization", properties.get("organization"));
        appProps.put("Homepage", properties.get("homepage"));

        Properties systemProperties = System.getProperties();
        appProps.put("Java Version", systemProperties.getProperty("java.version", UNKNOWN) + " - " + systemProperties.getProperty("java.vendor", UNKNOWN));
        appProps.put("Current Working Directory", systemProperties.getProperty("user.dir", UNKNOWN));

        print(builder, appProps);

        Properties memPros = new Properties();
        final Runtime rt = Runtime.getRuntime();
        final long maxMemory = rt.maxMemory() / MEGA_BYTES;
        final long totalMemory = rt.totalMemory() / MEGA_BYTES;
        final long freeMemory = rt.freeMemory() / MEGA_BYTES;
        final long usedMemory = totalMemory - freeMemory;

        memPros.put("Maximum Allowable Memory", maxMemory + "MB");
        memPros.put("Total Memory", totalMemory + "MB");
        memPros.put("Free Memory", freeMemory + "MB");
        memPros.put("Used Memory", usedMemory + "MB");

        print(builder, memPros);

        printHeader(builder, "Java System Properties");
        Properties sysProps = new Properties();
        for (final Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
            sysProps.put(entry.getKey(), entry.getValue());
        }

        print(builder, sysProps);

        printHeader(builder, "System Environments");
        Map<String, String> getenv = System.getenv();
        Properties envProps = new Properties();
        Set<String> strings = getenv.keySet();
        for (String key : strings) {
            String message = getenv.get(key);
            envProps.put(key, message);
        }

        print(builder, envProps);

        System.out.println(builder.toString());

        logger.info("============================================================");
        logger.info(" Now starting ..... PID: " + SystemUtils.getPid());
        logger.info("============================================================");

    }

    private static void printHeader(StringBuilder builder, String message) {
        builder.append(org.slf4j.helpers.MessageFormatter.format("\n== {} =====================\n", message).getMessage()).append("\n");
    }

    private static void print(StringBuilder builder, Properties props) {
        int maxLength = getMaxLength(props);
        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = props.getProperty(key);
            builder.append("  ").append(key).append(getCharacter(maxLength - key.getBytes().length, " ")).append(" : ").append(value).append("\n");
        }
    }

    private static int getMaxLength(Properties props) {
        Enumeration<Object> keys = props.keys();
        int maxLength = -1;
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (maxLength < 0) {
                maxLength = key.getBytes().length;
            } else if (maxLength < key.getBytes().length) {
                maxLength = key.getBytes().length;
            }
        }
        return maxLength;
    }

    /**
     * 지정한 크기 만큼 문자열을 구성한다.
     *
     * @param size      문자열을 구성할 반복수
     * @param character 문자열을 구성하기 위한 단위 문자열. 반복수만큼 생성된다.
     * @return 문자열
     */
    private static String getCharacter(int size, String character) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(character);
        }
        return builder.toString();
    }
}
