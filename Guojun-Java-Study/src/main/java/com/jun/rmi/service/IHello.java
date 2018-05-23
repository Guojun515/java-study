package com.jun.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHello extends Remote {
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Throwable
	 */
	public String sayHello(String name) throws RemoteException;
}
