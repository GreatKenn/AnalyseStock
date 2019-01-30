package com.mp2.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class JsonUtil {
    /**
     * 对象转为JSON串
     *
     * @return
     */
    public static String toJSONString(Object obj) {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();

        try {
            objMapper.writeValue(witStr, obj);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return witStr.toString();
    }

    /**
     * json 转对象
     *
     * @param jsonString
     * @param prototype
     * @param <T>
     * @return
     */
    public static <T> T getEntity(String jsonString, Class<T> prototype) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper.readValue(jsonString, prototype);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
