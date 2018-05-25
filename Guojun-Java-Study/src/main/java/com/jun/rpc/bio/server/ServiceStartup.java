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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jun.rpc.bio.service.HelloService;
import com.jun.rpc.bio.service.HelloServiceImpl;

/**
 * 
 * @Description: RPC 服务端启动程序
 * @author v-yuguojun
 * @date 2018年5月25日 下午4:31:37
 */
public class ServiceStartup {
	/**
	 * 端口
	 */
	private static final int DEFAULT_PORT = 8088;
	
	/**
	 * 创建一个线程池
	 */
	private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(15, 150, 30, TimeUnit.SECONDS, 
			new LinkedBlockingQueue<Runnable>(100), (runnable)->{
				return new Thread(runnable, "服务端线程");
			});

	/**
	 * 注册的接口信息
	 */
	private static final HashMap<String, Class<?>> SERVICE_REGISTRY = new HashMap<>();

	/**
	 * 服务启动程序，一个单例实现（饿汉实现）
	 */
	private static final ServiceStartup SERVICE_STARTUP = new ServiceStartup();
	
	/**
	 * 服务端的状态
	 */
	private boolean isRunning = true;
	
	/**
	 * 私有化构造
	 */
	private ServiceStartup() {}

	/**
	 * 服务启动
	 * @throws IOException
	 */
	public void start() throws IOException {
		//创建一个服务端的serverSock，并 绑定监听端口
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(DEFAULT_PORT));

		//启动服务，通过一个线程池去执行
		System.out.println("Start server...");
		try {
			while (isRunning) {
				//调用accept()方法开始监听，等待连接
				EXECUTOR.execute(new ServiceTask(serverSocket.accept())); 
			}
		} finally {
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
	}

	public void stop() {
		isRunning = false;
		EXECUTOR.shutdown();
	}

	public void regist(Class<?> serviceInterface, Class<?> serviceIml) {
		SERVICE_REGISTRY.put(serviceInterface.getName(), serviceIml);
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

				Class<?> serviceClass = SERVICE_REGISTRY.get(serviceName);
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
			SERVICE_STARTUP.regist(HelloService.class, HelloServiceImpl.class);
			SERVICE_STARTUP.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
