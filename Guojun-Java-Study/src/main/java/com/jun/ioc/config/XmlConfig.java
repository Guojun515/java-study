package com.jun.ioc.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
 * @Description 读取xml配置文件
 * @author Guojun
 * @Date 2018年5月26日 下午3:40:31
 *
 */
public class XmlConfig {

    /**
     * 读取配置文件
     * @param path 配置文件路径
     * @return
     */
	@SuppressWarnings("unchecked")
	public static Map<String, Bean> getConfig(String path){
        Map<String, Bean> configMap = new HashMap<String, Bean>(100);
       
        //使用dom4j和xpath读取xml文件
        Document doc = null;
        SAXReader reader = new SAXReader();
        
        //获取配置文件的路径
        InputStream in = XmlConfig.class.getResourceAsStream(path);
        try {
            doc = reader.read(in);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("请检查您的xml配置文件路径是否正确！");
        }
        
        //定义xpath，取出所有的bean
        String xpath = "//bean";
        
        //对bean进行遍历
        List<Element> list = doc.selectNodes(xpath);
        if(list!=null){
            for (Element beanEle : list) {
            	//封装到bean对象中
                Bean bean = new Bean();
                //bean节点的id
                String id = beanEle.attributeValue("id");
                //bean节点的class属性
                String className = beanEle.attributeValue("class");
                bean.setId(id);
                bean.setClassName(className);

                //获取bean节点下所有的property节点
                List<Element> proList = beanEle.elements("property");
                if(proList != null){
                	//封装到property属性中
                    for (Element proEle : proList) {
                        Property prop = new Property();
                        String propName = proEle.attributeValue("name");
                        String propValue = proEle.attributeValue("value");
                        String propRef = proEle.attributeValue("ref");
                        prop.setName(propName);
                        prop.setValue(propValue);
                        prop.setRef(propRef);

                        bean.getProperties().add(prop);
                    }
                }
                
                //id是不应重复的
                if(configMap.containsKey(id)){
                    throw new RuntimeException("bean节点ID重复：" + id);
                }
                
                //将bean封装到map中
                configMap.put(id, bean);
            }
        }
        
        return configMap;
    }
}
