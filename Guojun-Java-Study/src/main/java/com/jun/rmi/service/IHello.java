package com.jun.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * @Description: RMI 远程调用，需要实现Remote接口
 * @author v-yuguojun
 * @date 2018年5月25日 下午5:46:56
 */
public interface IHello extends Remote {
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Throwable
	 */
	public String sayHello(String name) throws RemoteException;
}
