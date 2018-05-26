package com.jun.rpc.nio.server;

import com.jun.rpc.nio.service.HelloService;
import com.jun.rpc.nio.service.HelloServiceImpl;


/**
 * NIO方式实现服务端
 * @author FR0012
 *
 */
public class ServerStartup {
	
	private static final int DEFAULT_PORT = 8888; 
	
	private static final ServerHandler SERVER_HANDLER = new ServerHandler();  
	
	
	/**
	 * 启动程序
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{  
		SERVER_HANDLER.regist(HelloService.class, HelloServiceImpl.class);
		SERVER_HANDLER.start(DEFAULT_PORT);
	}  

}