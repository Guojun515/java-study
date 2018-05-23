package com.jun.rpc.bio.service;

public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHello() {
		return "Hello RPC !";
	}

}
