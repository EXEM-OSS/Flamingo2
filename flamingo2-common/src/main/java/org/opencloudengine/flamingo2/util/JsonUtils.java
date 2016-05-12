package org.opencloudengine.flamingo2.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jayway.jsonpath.JsonPath;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson JSON Utility.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class JsonUtils {
    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
        objectMapper.registerModule(simpleModule);
    }

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * 지정한 Object를 Jackson JSON Object Mapper를 이용하여 JSON으로 변환한다.
     *
     * @param obj JSON으로 변환할 Object
     * @return JSON String
     * @throws java.io.IOException JSON으로 변환할 수 없는 경우
     */
    public static String marshal(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    public static Map unmarshal(String json) throws IOException {
        // Role을 잘못 정의하는 경우 "null" 등이 들어오는 경우가 있음.
        if ("\"null\"".equals(json)) {
            return new HashMap();
        }
        return objectMapper.readValue(json, Map.class);
    }

    public static List unmarshalToList(String json) throws IOException {
        return objectMapper.readValue(json, List.class);
    }

    /**
     * 지정한 Object를 Jackson JSON Object Mapper를 이용하여 JSON으로 변환한다.
     *
     * @param obj JSON으로 변환할 Object
     * @return JSON String
     */
    public static String format(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new ServiceException("JSON을 파싱할 수 없습니다.", ex);
        }
    }

    public static String getValueIgnore(String json, String expression) {
        Object read = null;
        try {
            read = JsonPath.read(json, expression);
            return (String) read;
        } catch (ClassCastException ex) {
            return String.valueOf(read);
        } catch (Exception ex) {
            return "";
        }
    }

}
