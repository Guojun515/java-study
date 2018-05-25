package com.jun.rpc.bio.client.proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 
 * @Description: RPC远程调用BIO的实现
 * @author v-yuguojun
 * @date 2018年5月25日 上午11:54:47
 */
public class RemoteServiceProxy {

	/**
	 * 通过JDK动态代理生成一个接口的实现
	 * 代理类里边是通过socket实现RPC
	 * @param serviceInterface
	 * @param addr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRemoteServicePorxy (final Class<T> serviceInterface, final InetSocketAddress addr) {
		//使用Proxy创建代理对象
		return (T) Proxy.newProxyInstance(
				//一个ClassLoader对象，定义了由哪个ClassLoader来对生成的代理对象进行加载 
				RemoteServiceProxy.class.getClassLoader(),

				// 要代理类继承的接口
				new Class[]{serviceInterface}, 

				/*
				 * 实现InvocationHandler接口，代理对象执行时，会调用该接口的invoke方法执行被代理的方法
				 * proxy:　　指代JDK动态生成的最终代理对象
				 * method:　 指代的是我们所要调用真实对象的某个方法的Method对象
				 * args:　　 指代的是调用真实对象某个方法时接受的参数
				 */
				(proxy, method, args) -> {
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
				});
	}
}
