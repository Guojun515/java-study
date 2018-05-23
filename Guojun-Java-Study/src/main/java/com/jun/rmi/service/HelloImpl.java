package com.jun.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
