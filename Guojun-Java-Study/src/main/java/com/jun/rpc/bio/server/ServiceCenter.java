package com.jun.rpc.bio.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jun.rpc.bio.service.HelloService;
import com.jun.rpc.bio.service.HelloServiceImpl;


public class ServiceCenter implements Server {

	//通过获取当前程序运行的兑现来获取java虚拟机的可用处理器数创建一个线程池
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private static final HashMap<String, Class<?>> serviceRegistry = new HashMap<>();

	private static boolean isRunning = true;

	private static int port ;

	public ServiceCenter (int port) {
		ServiceCenter.port = port;
	}

	@Override
	public void start() throws IOException {
		//创建一个服务端的serverSock，并 绑定监听端口
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(port));

		//启动服务，通过一个线程池去执行
		System.out.println("Start server...");
		try {
			while (isRunning) {
				executor.execute(new ServiceTask(serverSocket.accept())); //调用accept()方法开始监听，等待连接
			}
		} finally {
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
	}

	@Override
	public void stop() {
		isRunning = false;
		executor.shutdown();
	}

	@Override
	public void regist(Class<?> serviceInterface, Class<?> serviceIml) {
		serviceRegistry.put(serviceInterface.getName(), serviceIml);
	}

	@Override
	public boolean isRunning() {
		return ServiceCenter.isRunning;
	}

	@Override
	public int getPort() {
		return ServiceCenter.port;
	}

	private static class ServiceTask extends Thread {

		Socket client = null;

		public ServiceTask (Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			ObjectInputStream input = null;
			ObjectOutputStream output = null;
			try {
				input = new ObjectInputStream(client.getInputStream());
				output = new ObjectOutputStream(client.getOutputStream());

				//按输入顺序读取传过来的对象流（一次只能读取一个对象，多个对象多次读取）；
				String serviceName = input.readUTF();
				String methodName = input.readUTF();
				Class<?>[] paramTypes = (Class<?>[]) input.readObject();
				Object[] arguments = (Object[]) input.readObject();

				Class<?> serviceClass = serviceRegistry.get(serviceName);
				if (serviceClass == null) {
					output.writeObject("404！The server not found!");
				}

				Method method = serviceClass.getMethod(methodName, paramTypes);
				Object result = method.invoke(serviceClass.newInstance(), arguments);

				output.writeObject(result);

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

					if(client != null) {
						client.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 启动方法
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Server serviceServer = new ServiceCenter(8088);
			serviceServer.regist(HelloService.class, HelloServiceImpl.class);
			serviceServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
