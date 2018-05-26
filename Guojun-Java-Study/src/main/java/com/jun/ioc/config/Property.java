package com.jun.ioc.config;

/**
 * 
 * @Description Bean属性文件描述
 * @author Guojun
 * @Date 2018年5月26日 下午3:51:59
 *
 */
public class Property {

	/**
	 * 属性名称
	 */
    private String name;
    
    /**
     * 属性值
     */
    private String value;
    
    /**
     * 注入的属性
     */
    private String ref;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getRef() {
        return ref;
    }
    
    public void setRef(String ref) {
        this.ref = ref;
    }
}
