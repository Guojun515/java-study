package com.jun.rpc.nio.client;

import java.net.InetSocketAddress;

import com.jun.rpc.nio.client.proxy.RemoteServiceProxy;
import com.jun.rpc.nio.service.HelloService;

/**
 * 
 * @Description RPC实现之NIO测试
 * @author Guojun
 * @Date 2018年5月26日 下午3:14:04
 *
 */
public class ClientMain {

	public static void main(String[] args) {
		HelloService service = RemoteServiceProxy.getRemotePorxyObject(HelloService.class, new InetSocketAddress("localhost", 8888));
		System.out.println(service.sayHello("BIO"));
	}
}
