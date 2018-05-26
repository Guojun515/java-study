package com.jun.ioc.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Description Bean的描述类
 * @author Guojun
 * @Date 2018年5月26日 下午3:53:28
 *
 */
public class Bean {

	/**
	 * Bean的ID
	 */
    private String id;
    
    /**
     * Bean的类名（全名）
     */
    private String className;
    
    /**
     * bean节点下可以有多个property节点
     */
    private List<Property> properties = new ArrayList<Property>();

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public List<Property> getProperties() {
        return properties;
    }
    
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
    
    @Override
    public String toString() {
        return "Bean [id=" + id + ", className=" + className
                + ", properties=" + properties + "]";
    }

}
