package com.jun.rpc.nio.test;

import java.net.InetSocketAddress;

import com.jun.rpc.nio.client.RPCClient;
import com.jun.rpc.nio.service.HelloService;

public class Test {
	public static void main(String[] args) {
		for (int i =0; i < 1000; i++) {
			new Thread("RPC调用线程（" + (i+1) + "）"){
				@Override
				public void run() {
					HelloService service = RPCClient.getRemotePorxyObject(HelloService.class, new InetSocketAddress("192.168.2.51", 8888));
					System.out.println(Thread.currentThread().getName() + "==================>" + service.sayHello(Thread.currentThread().getName()));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
}
