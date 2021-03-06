package com.jun.rpc.nio.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import com.jun.rpc.nio.util.Call;

/**
 * 
 * @Description RPC代理类，BIO实现
 * @author Guojun
 * @Date 2018年5月26日 下午3:16:08
 *
 */
public class RemoteServiceProxy {
	
	@SuppressWarnings("unchecked")
	public static <T> T getRemotePorxyObject (final Class<?> serviceInterface, final InetSocketAddress addr) {
		 // 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
		return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Call call = new Call(serviceInterface.getName(), method.getName(), args, method.getParameterTypes());
				ClientHandler handler = new ClientHandler(addr);
				handler.doRequest(call);
				while (!call.isDone()) {
					synchronized (call) {
						call.wait();
					}
				}
				return call.getResult();
			}
		});
	}
}
