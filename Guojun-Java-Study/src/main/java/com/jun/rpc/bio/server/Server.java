package com.jun.rpc.bio.server;

import java.io.IOException;

public interface Server {
	
	/**
	 * 服务启动方法
	 * @throws IOException
	 */
	public void start() throws IOException;
	
	/**
	 * 服务停止方法
	 */
	public void stop();
	
	/**
	 * 服务注册方法
	 * @param serviceInterface
	 * @param serviceIml
	 */
	public void regist(Class<?> serviceInterface, Class<?> serviceIml);
	
	/**
	 * 服务的运行状态
	 * @return
	 */
	public boolean isRunning();
	
	/**
	 * 获取服务的运行端口
	 * @return
	 */
	public int getPort();
}
