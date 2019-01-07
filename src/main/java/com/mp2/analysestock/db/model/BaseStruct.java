package com.mp2.analysestock.db.model;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by kenn on 16/5/15.
 * 基础类,提供将自己转换为JSON串的功能
 */
public class BaseStruct {

    private final static Logger logger = LoggerFactory.getLogger(BaseStruct.class);

    /**
     * 对象转为JSON串
     * @return
     */
    public String toJSONString() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();

        try {
            objMapper.writeValue(witStr, this);
            logger.info("toJSONString<" + witStr + ">");
        } catch (JsonGenerationException e) {
            logger.error(e.getLocalizedMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        return witStr.toString();
    }
}
