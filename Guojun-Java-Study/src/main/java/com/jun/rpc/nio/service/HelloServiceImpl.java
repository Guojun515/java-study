package com.jun.rpc.nio.service;

public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHello(String name) {
		return "Hello " + name + "!";
	}

}
