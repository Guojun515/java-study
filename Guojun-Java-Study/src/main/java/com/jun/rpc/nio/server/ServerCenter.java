package com.jun.rpc.nio.server;

import com.jun.rpc.nio.service.HelloService;
import com.jun.rpc.nio.service.HelloServiceImpl;


/**
 * NIO方式实现服务端
 * @author FR0012
 *
 */
public class ServerCenter {
	
	private static int DEFAULT_PORT = 8888; 
	
	private static ServerHandler serverHandle;  
	
	/**
	 * 默认端口吗启动方法
	 */
	public static void start(){  
		start(DEFAULT_PORT);  
	}  
	
	/**
	 * 启动方法
	 * @param port
	 */
	public static synchronized void start(int port){  
		if(serverHandle!=null) {
			serverHandle.stop();  
		}
		serverHandle = new ServerHandler(port);  
		new Thread(serverHandle,"Server").start(); 
	} 
	
	public static synchronized void regist(Class<?> serviceInterface, Class<?> serviceIml) {
		serverHandle.regist(serviceInterface, serviceIml);
	}
	
	/**
	 * 停止方法
	 */
	public static void stop () {
		serverHandle.stop();
	}
	
	/**
	 * 启动程序
	 * @param args
	 */
	public static void main(String[] args){  
		start();
		regist(HelloService.class, HelloServiceImpl.class);
	}  

}