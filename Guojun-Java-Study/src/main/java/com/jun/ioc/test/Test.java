package com.jun.ioc.test;

import java.util.Map;
import java.util.Map.Entry;

import com.jun.ioc.bean.Address;
import com.jun.ioc.bean.User;
import com.jun.ioc.config.Bean;
import com.jun.ioc.config.XmlConfig;
import com.jun.ioc.core.BeanFactory;
import com.jun.ioc.core.ClassPathXmlApplicationContext;


/**
 * Created by Administrator on 2017/6/16/016.
 */
public class Test {
	
	public static void main(String[] args) {
		testIOC();
		testConfig();
	}
	
	/**
	 * 测试IOC容器
	 */
	private static void testIOC(){

		BeanFactory bf = new ClassPathXmlApplicationContext("/com/jun/ioc/ApplicationContext.xml");
		User user = (User) bf.getBean("user");
		System.out.println(user);
		System.out.println("address hashcode:"+user.getAddress().hashCode());

		Address address = (Address) bf.getBean("address");
		System.out.println(address);
		System.out.println("address hashcode:"+address.hashCode());
	}
	
	/**
	 * 测试读取配置文件
	 */
	private static void testConfig(){
		Map<String,Bean> map = XmlConfig.getConfig("/com/jun/ioc/ApplicationContext.xml");
		for (Entry<String, Bean> entry : map.entrySet()) {
			System.out.println(entry.getKey()+"==="+entry.getValue());
		}
	}
	
}
