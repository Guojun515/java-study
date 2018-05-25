package com.jun.rpc.bio.client;

import java.net.InetSocketAddress;

import com.jun.rpc.bio.client.proxy.RemoteServiceProxy;
import com.jun.rpc.bio.service.HelloService;

/**
 * 
 * @Description: RPC 远程调用
 * @author v-yuguojun
 * @date 2018年5月25日 下午2:25:38
 */
public class ClientMain {
	
	public static void main(String[] args) {
		HelloService service = RemoteServiceProxy.getRemoteServicePorxy(HelloService.class, new InetSocketAddress("localhost", 8088));
	    System.out.println(service.sayHello());
	}
}
