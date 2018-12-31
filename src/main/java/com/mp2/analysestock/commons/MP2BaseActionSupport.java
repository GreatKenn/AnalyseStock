package com.mp2.analysestock.commons;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kenn on 16/5/12.
 */
public class MP2BaseActionSupport extends ActionSupport {

    //Spring
    public ApplicationContext acMyBatis = new ClassPathXmlApplicationContext("spring-mybatis.xml");

    /**
     * 获取当前所在服务起的URL
     * @return
     */
    public String getCurrentServerURL() {
        HttpServletRequest requet = ServletActionContext.getRequest();

        return requet.getScheme() + "://" + requet.getServerName() + ":" + requet.getServerPort();
    }
}
