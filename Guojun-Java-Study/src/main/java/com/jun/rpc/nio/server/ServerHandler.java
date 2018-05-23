package com.jun.rpc.nio.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;

import com.jun.rpc.nio.util.Call;
import com.jun.rpc.nio.util.ToolUtils;

public class ServerHandler implements Runnable {
	
	private static final HashMap<String, Class<?>> serviceRegistry = new HashMap<>();

	/**
	 * Selector会不断轮询注册在其上的Channel,如果某个Channel上边发生读或者写事件，这个Channel就处于就绪状态，
	 * 会被Selector轮询出来，然后通过SelectionKey可以获取就绪Channel的集合，进行后续的I/O操作。
	 */
	private Selector selector;

	/**
	 * 判断这个服务的启动状态
	 */
	private volatile boolean started;

	/**
	 * 默认构造方法
	 * @param port
	 */
	public ServerHandler (int port) {
		try {
			//打开监听渠道
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			//设置为true,此渠道为阻塞模式；设置为false，此渠道为非阻塞模式
			serverSocketChannel.configureBlocking(false);
			//绑定端口
			serverSocketChannel.socket().bind(new InetSocketAddress(port));

			//获得一个通道管理器
			selector = Selector.open();
			//将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，  
			//当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。  
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			//服务启动
			started = true;
			System.out.println("The server start success !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		//循环遍历selector
		while (started) {
			SelectionKey selectionKey = null;
			try {
				//无论是否有读写，selector每隔1秒被唤醒一次
				selector.select(1000);
				//阻塞，只有当至少一个注册事件发生的时候才会被继续
				//selector.select();

				// 获得selector中选中的项的迭代器，选中的项为注册的事件  
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					selectionKey = it.next();
					it.remove();

					//判断这个selectionKey是否有效的
					if (!selectionKey.isValid()) { 
						continue;
					}

					//这个key代表是一个新接入的请求
					if (selectionKey.isAcceptable()) {
						this.accept(selectionKey);
					}

					//读写取信息
					if (selectionKey.isReadable()) {
						Object result = this.read(selectionKey);
						if (result != null) {
							this.write(selectionKey, result);
						} else {
							selectionKey.cancel();
							selectionKey.channel().close();
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					selectionKey.cancel();
					selectionKey.channel().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 客户端新的链接
	 * @param selectionKey
	 * @throws IOException
	 */
	private void accept (SelectionKey selectionKey) throws IOException {
		ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();

		//通过ServerSocketChannel的accept()创建SocketChannel, 完成该操作意味着完成TCP的三次握手
		SocketChannel sc = ssc.accept();
		//设置为非阻塞
		sc.configureBlocking(false);
		//在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。  
		sc.register(selector, SelectionKey.OP_READ);
	}

	/**
	 * 读取信息
	 * @param selectionKey
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private Object read (SelectionKey selectionKey) throws Exception{
		SocketChannel channel = (SocketChannel) selectionKey.channel();

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int read = channel.read(buffer);
		if (read == -1) {
			return null;
		}
		byte[] data = null;
		while (read > 0) {
			//将buffer切换到读的模式，将position设为0，并将limit设置成之前的值
			//换句话说，position用于标记读的位置，limit表示之前写入了多少个byte、char等 —— 现在能读取多少个byte、char等。
			buffer.flip();

			byte[] b = new byte[buffer.remaining()];
			buffer.get(b);
			if (data == null) {
				data = b;
			} else {
				data = ToolUtils.concat(data, b);
			}

			buffer.clear();
			read = channel.read(buffer);
		}
		
		Call call = Call.getCall(data);
		
		//业务逻辑调用
		Class<?> targetClass = serviceRegistry.get(call.getServerName());
		Method method = targetClass.getMethod(call.getMethod(), call.getParamTypes());
		Object result = method.invoke(targetClass.newInstance(), call.getArgs());
		return result;

	}

	/**
	 * 回写信息
	 * @param selectionKey
	 * @param data
	 * @throws IOException
	 */
	private void write(SelectionKey selectionKey, Object data) throws IOException {
		SocketChannel channel = (SocketChannel) selectionKey.channel();

		byte[] b = ToolUtils.objectToByte(data);
		ByteBuffer buffer = ByteBuffer.wrap(b);

		channel.write(buffer);
	}

	/**
	 * 停止方法
	 */
	public void stop(){  
		started = false;  
	} 
	
	/**
	 * 服务注册
	 * @param serviceInterface
	 * @param serviceIml
	 */
	public void regist(Class<?> serviceInterface, Class<?> serviceIml) {
		serviceRegistry.put(serviceInterface.getName(), serviceIml);
	}

}