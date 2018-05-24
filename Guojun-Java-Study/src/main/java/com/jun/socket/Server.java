package com.jun.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @Description: 服务端实现
 * @author v-yuguojun
 * @date 2018年5月24日 下午5:54:20
 */
public class Server {
	/**
	 * 服務端對象
	 */
	private static final Server SERVER = new Server();
	
	/**
	 * 线程池
	 */
	private static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
			10, 150, 30, TimeUnit.MINUTES, 
			new ArrayBlockingQueue<Runnable>(100), (runnable) -> {
				return new Thread(runnable, "测试线程" + System.currentTimeMillis());
			});
	
	public static void main(String[] args) throws InterruptedException {
		SERVER.startupServer();
	}
	
	/**
	 * 服务程序启动
	 */
	private void startupServer() {
		ServerSocket serverSocket = null;
		try {
			System.out.println("***服务器即将启动，等待客户端的连接***");

			//1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
			serverSocket = new ServerSocket(1314);
			Socket socket=null;
			//记录客户端的数量
			int count=0;

			//循环监听等待客户端的连接
			while(true){
				//调用accept()方法开始监听，等待客户端的连接
				socket=serverSocket.accept();

				//创建一个新的线程
				ServerThread serverThread=new ServerThread(socket);
				//启动线程
				EXECUTOR.execute(serverThread);

				//统计客户端的数量
				count++;
				//当前客户端的IP
				InetAddress address=socket.getInetAddress();

				System.out.println("客户端的数量：" + count);
				System.out.println("当前客户端的IP：" + address.getHostAddress());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				 try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
