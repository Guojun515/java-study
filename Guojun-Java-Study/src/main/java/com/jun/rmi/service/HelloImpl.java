package com.jun.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 * @Description: 远程调用的实现，需要继承UnicastRemoteObject，该对象会生成stub与skeleton的实现
 * @author v-yuguojun
 * @date 2018年5月25日 下午5:47:45
 */
public class HelloImpl extends UnicastRemoteObject implements IHello {

	private static final long serialVersionUID = 636048801243947669L;

	public HelloImpl() throws RemoteException {
		super();
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		return "Hello " + name + "!";
	}

}
