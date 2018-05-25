package com.jun.rmi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.jun.rmi.service.HelloImpl;
import com.jun.rmi.service.IHello;

/**
 * 注册远程对象,向客户端提供远程对象服务  
 * 远程对象是在远程服务上创建的，你无法确切地知道远程服务器上的对象的名称 
 * 但是，将远程对象注册到RMI Service之后，客户端就可以通过RMI Service请求 到该远程服务对象的stub了，利用stub代理就可以访问远程服务对象了  
 * @author v-yuguojun
 * @date 2018年5月25日 下午5:53:24
 */
public class Server {

	public static void main(String[] args) {
		try {
			
			// 生成stub和skeleton,并返回stub代理引用 
			IHello hello = new HelloImpl();

			//本地创建并启动RMI Service，被创建的Registry服务将在指定的端口上侦听到来的请求 
			LocateRegistry.createRegistry(8888);
			
			// 将stub代理绑定到Registry服务的URL上  
			Naming.rebind("rmi://localhost:8888/hello", hello);

			System.out.print("Server Ready");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
