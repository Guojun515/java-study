package com.jun.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.jun.rmi.service.IHello;


public class Client {
	
	public static void main (String[] args) {
		try {
			IHello hello = (IHello) Naming.lookup("rmi://localhost:8888/hello");
			
			System.out.println(hello.sayHello("Guojun"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
