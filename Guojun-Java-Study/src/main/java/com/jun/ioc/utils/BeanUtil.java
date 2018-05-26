package com.jun.ioc.utils;

import java.lang.reflect.Method;

/**
 * 
 * @Description 工具类
 * @author Guojun
 * @Date 2018年5月26日 下午3:50:12
 *
 */
public class BeanUtil {

    /**
     * 获取obj类的name属性的setter方法
     * @param obj
     * @param name
     * @return
     */
    public static Method getSetterMethod(Object obj,String name){
        Method method = null;
        //setter方法名称（驼峰）
        name = "set"+name.substring(0,1).toUpperCase()+name.substring(1);
        try {
            Method[] methods = obj.getClass().getMethods();
            //遍历该类的所有方法
            for(int i=0;i<methods.length;i++){
                Method m = methods[i];
                if(m.getName().equals(name)){
                    method = obj.getClass().getMethod(name,m.getParameterTypes());
                    break;
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }
}
