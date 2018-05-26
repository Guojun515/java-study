package com.jun.ioc.core;

/**
 * 
 * @Description Bean构建工厂
 * @author Guojun
 * @Date 2018年5月26日 下午3:55:01
 *
 */
public interface BeanFactory {

	/**
	 * 获取Bean
	 * @param beanName
	 * @return
	 */
    Object getBean(String beanName);
}
