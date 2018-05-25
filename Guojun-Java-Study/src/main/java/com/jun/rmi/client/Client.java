package com.jun.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.jun.rmi.service.IHello;

/**
 * RMI实现的步骤
 * 1、 生成一个远程接口 
 * 2、 实现远程对象(服务器端程序)
 * 3、 生成占位程序和骨干网(服务器端程序)
 * 4、 编写服务器程序 
 * 5、 编写客户程序 
 * 6、 注册远程对象 
 * 7、 启动远程对象   
 * 
 * @author v-yuguojun
 * @date 2018年5月25日 下午6:02:05
 */
public class Client {
	
	public static void main (String[] args) {
		try {
			IHello hello = (IHello) Naming.lookup("rmi://localhost:8888/hello");
			
			System.out.println(hello.sayHello("Guojun"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
