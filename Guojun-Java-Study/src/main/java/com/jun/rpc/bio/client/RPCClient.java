package com.jun.rpc.bio.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RPCClient {
	
	@SuppressWarnings("unchecked")
	public static <T> T getRemotePorxyObject (final Class<?> serviceInterface, final InetSocketAddress addr) {
		 // 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
		return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Socket socket = null;
				ObjectInputStream input = null;
				ObjectOutputStream output = null;
				
				try {
					// 2.创建Socket客户端，根据指定地址连接远程服务提供者
					socket = new Socket();
					socket.connect(addr);
					
					// 3.将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
					output = new ObjectOutputStream(socket.getOutputStream());
					
					output.writeUTF(serviceInterface.getName());
					output.writeUTF(method.getName());
					output.writeObject(method.getParameterTypes());
					output.writeObject(args);
					
					// 4.同步阻塞等待服务器返回应答，获取应答后返回
					input = new ObjectInputStream(socket.getInputStream());
					return input.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (output != null) {
							output.close();
						}
						
						if (input != null) {
							input.close();
						}
						
						if(socket != null) {
							socket.close();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				return null;
			}
		});
	}
}
