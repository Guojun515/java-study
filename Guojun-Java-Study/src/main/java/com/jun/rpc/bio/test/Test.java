package com.jun.rpc.bio.test;

import java.net.InetSocketAddress;

import com.jun.rpc.bio.client.RPCClient;
import com.jun.rpc.bio.service.HelloService;


public class Test {
	public static void main(String[] args) {
		HelloService service = RPCClient.getRemotePorxyObject(HelloService.class, new InetSocketAddress("localhost", 8088));
	    System.out.println(service.sayHello());
	}
}
