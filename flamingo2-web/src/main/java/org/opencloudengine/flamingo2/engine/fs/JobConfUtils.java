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
package org.opencloudengine.flamingo2.engine.fs;

import org.apache.hadoop.conf.Configuration;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JobConf JSON Utility.
 *
 * @author Byoung Gon, Kim
 * @since 1.2
 */
public class JobConfUtils {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(JobConfUtils.class);

    private static Logger exceptionLogger = LoggerFactory.getLogger("flamingo.exception");

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map toMap(String escapedJson) {
        try {
            Map result = new HashMap();
            String unescaped = StringUtils.unescape(escapedJson);
            List<Map> list = objectMapper.readValue(unescaped, List.class);
            for (Map map : list) {
                result.put(map.get("name"), map.get("value"));
            }
            return result;
        } catch (Exception ex) {
            exceptionLogger.warn("{}", ex.getMessage(), ex);
            return new HashMap();
        }
    }

    /**
     * Escaped JSON을 Configuration으로 변경한다.
     *
     * @param escapedJson Escaped JSON
     * @return Hadoop Configuration
     */
    public static Configuration toConfiguration(String escapedJson) {
        try {
            Configuration configuration = new Configuration();
            String unescaped = StringUtils.unescape(escapedJson);
            List<Map> list = objectMapper.readValue(unescaped, List.class);
            for (Map map : list) {
                configuration.set((String) map.get("name"), (String) map.get("value"));
            }
            return configuration;
        } catch (Exception ex) {
            exceptionLogger.warn("{}", ex.getMessage(), ex);
            return new Configuration();
        }
    }

}
