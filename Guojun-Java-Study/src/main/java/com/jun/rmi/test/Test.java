package com.jun.rmi.test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {
	
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(8888);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}	
}
