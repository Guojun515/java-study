package com.jun.rpc.nio.util;

import java.io.IOException;
import java.io.Serializable;

/**
 * 
 * @Description 对象描述
 * @author Guojun
 * @Date 2018年5月26日 下午3:05:44
 *
 */
public class Call implements Serializable {
	private static final long serialVersionUID = 3241351563527629480L;

	/**
	 * 返回结果
	 */
	private Object result;
	
	/**
	 * 状态，根据此状态判断数据有没有返回
	 */
	private boolean done;
	
	/**
	 * 服务名称，对应的接口名称
	 */
	private String serverName;

	/**
	 * 方法名称
	 */
	private  String method;

	/**
	 * 参数
	 */
	private Object[] args;
	
	/**
	 * 参数类型
	 */
	private Class<?>[] paramTypes;

	public Call (String serverName, String method, Object[] args, Class<?>[] paramTypes) {
		this.serverName = serverName;
		this.method = method;
		this.args = args;
		this.paramTypes = paramTypes;
	}

	/**
	 * 获取字节流
	 * @return
	 * @throws IOException
	 */
	public byte[] getByte() throws IOException{
		return SerializableUtils.objectToByte(this);
	}

	/**
	 * 把字节码转换为对象
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static Call getCall (byte[] data) throws Exception {
		return (Call) SerializableUtils.byteToObject(data);
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}
}
