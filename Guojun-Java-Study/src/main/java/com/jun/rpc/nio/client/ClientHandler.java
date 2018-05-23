package com.jun.rpc.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.jun.rpc.nio.util.Call;
import com.jun.rpc.nio.util.ToolUtils;

public class ClientHandler {

	/**
	 * 渠道选择器
	 */
	private Selector selector = null;

	/**
	 * 停止请求
	 */
	private boolean isStop =false;

	/**
	 * 获得一个链接
	 * @param addr
	 */
	public ClientHandler(InetSocketAddress addr) {
		try {
			// 获得一个Socket通道  
			SocketChannel channel = SocketChannel.open();  
			// 设置通道为非阻塞  
			channel.configureBlocking(false);  

			// 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调  
			// 用channel.finishConnect();才能完成连接  
			channel.connect(addr);  

			// 获得一个通道管理器  
			this.selector = Selector.open();
			//将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事
			channel.register(selector, SelectionKey.OP_CONNECT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  设置请求参数
	 * @param call
	 */
	public void setParams(Call call){
		SelectionKey key = null;
		try {
			while (!isStop) {
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				selector.select();
				while (it.hasNext()) {
					key = it.next();
					it.remove();

					//链接事件发生
					if (key.isConnectable()) {
						this.request(key, call);
					}

					//读取信息
					if (key.isReadable()) {
						this.read(key, call);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (key != null) { 
				try {
					this.close(key);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} 
	}
	
	/**
	 * 发送请求
	 * @param key
	 * @param call
	 * @throws IOException
	 */
	private void request(SelectionKey key, Call call) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		//如果正在链接则完成链接
		//如果SocketChannel在非阻塞模式下，此时调用connect()，该方法可能在连接建立之前就返回了，也可能在之后，所以要进行判断
		if (channel.isConnectionPending()) {
			channel.finishConnect();
		}
		//设置为非阻塞
		channel.configureBlocking(false);

		//写入数据
		channel.write(ByteBuffer.wrap(call.getByte()));

		//在和服务器获得链接之后，为了可以收到服务端的信息，需要给渠道设置可读的权限
		channel.register(selector, SelectionKey.OP_READ);
	}
	
	//获取结果
	private void read (SelectionKey key, Call call) throws Exception {
		// 服务器可读取消息:得到事件发生的Socket通道  
		SocketChannel channel = (SocketChannel) key.channel();  
		// 创建读取的缓冲区  
		ByteBuffer buffer = ByteBuffer.allocate(1024); 
		int r = channel.read(buffer);  

		byte[] data = null;
		while (r > 0) {
			buffer.flip();
			byte[] d = new byte[buffer.remaining()];
			buffer.get(d);
			if (data == null) {
				data = d;
			} else {
				data = ToolUtils.concat(data, d);
			}

			buffer.clear();
			r = channel.read(buffer);
		}
		Object result = ToolUtils.byteToObject(data);
		synchronized (call) {
			call.setResult(result);
			call.setDone(true);
			call.notify();
		}
		
		//关闭链接
		this.close(key);
	}
	
	/**
	 * 关闭链接
	 * @param key
	 * @throws IOException
	 */
	private void close (SelectionKey key) throws IOException {
		//自动关闭链接
		key.cancel();
		key.channel().close();
		isStop = true;
	}
}
