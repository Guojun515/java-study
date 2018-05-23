package com.jun.rpc.nio.util;

import java.io.IOException;
import java.io.Serializable;

public class Call implements Serializable {
	private static final long serialVersionUID = 3241351563527629480L;

	private Object result;
	
	private boolean done;
	
	private String serverName;

	private  String method;

	private Object[] args;
	
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
		return ToolUtils.objectToByte(this);
	}

	/**
	 * 把字节码转换为对象
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static Call getCall (byte[] data) throws Exception {
		return (Call) ToolUtils.byteToObject(data);
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
