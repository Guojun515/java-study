package com.jun.ioc.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jun.ioc.config.Bean;
import com.jun.ioc.config.Property;
import com.jun.ioc.config.XmlConfig;
import com.jun.ioc.utils.BeanUtil;


/**
 * Created by Administrator on 2017/6/16/016.
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

    //定义一个IOC容器
    private Map<String, Object> ioc;

    private Map<String, Bean> config;

    /**
     * 构造函数
     * 1. 初始化IOC容器
     * 2. 加载配置文件，生成bean对象放入IOC容器
     * @param path
     */
    public ClassPathXmlApplicationContext(String path){
        //初始化IOC容器
        ioc = new HashMap<String, Object>();
        //读取配置文件
        config = XmlConfig.getConfig(path);
        if(config!=null){
            for(Entry<String, Bean> entry : config.entrySet()){
                String beanId = entry.getKey();
                Bean bean = entry.getValue();

                //根据bean生成相应的对象
                Object object = createBean(bean);
                ioc.put(beanId, object);
            }
        }
    }
    /**
     * 根据bean生成对象实例
     * @param bean
     * @return
     */
    @SuppressWarnings("rawtypes")
	private Object createBean(Bean bean) {
        String className = bean.getClassName();

        Class c = null;
        Object object = null;

        try {
            //根据bean的calss属性生成对象
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("您配置的class属性不合法："+className);
        }

        try {
            //该方法调用的是类的无参构造方法
            object = c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("该类缺少一个无参构造方法："+className);
        }
        //将bean的属性封装到对象中
        if(bean.getProperties() != null){
            for(Property p : bean.getProperties()){
                //情况一：配置文件中使用的是value属性注入
                if(p.getValue() != null){
                    //获取属性对应的getter方法
                    Method getMethod = BeanUtil.getSetterMethod(object,p.getName());
                    try {
                        //调用set方法注入
                        getMethod.invoke(object, p.getValue());
                    } catch (Exception e) {
                        throw new RuntimeException("属性名称不合法或者没有相应的getter方法："+p.getName());
                    }
                }
                //情况二：配置文件中使用的是ref属性注入
                if(p.getRef() != null){
                    //获取属性对应的getter方法
                    Method getMethod = BeanUtil.getSetterMethod(object,p.getName());
                    //从容器中找到依赖的对象
                    Object obj = ioc.get(p.getRef());
                    if(obj == null){
                        throw new RuntimeException("没有找到依赖的对象："+p.getRef());
                    }else{
                        //调用set方法注入
                        try {
                            getMethod.invoke(object, obj);
                        } catch (Exception e) {
                            throw new RuntimeException("属性名称不合法或者没有相应的getter方法："+p.getName());
                        }
                    }
                }
            }
        }
        return object;
    }

    @Override
    public Object getBean(String beanName) {
        return ioc.get(beanName);
    }
}
