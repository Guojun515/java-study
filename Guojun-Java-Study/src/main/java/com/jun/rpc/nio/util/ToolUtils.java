package com.jun.rpc.nio.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ToolUtils {
	
	private ToolUtils(){};
	
	/**
	 * 把字节码转换为对象
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static Object byteToObject (byte[] data) throws Exception {
		if (data == null) {
			return null;
		}
		
		ByteArrayInputStream byteIn = null;
		ObjectInputStream objIn = null;
		try {
			byteIn = new ByteArrayInputStream(data);
			objIn = new ObjectInputStream(byteIn);
			Object result = objIn.readObject();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (byteIn != null) {
					byteIn.close();
				}
				if (objIn != null) {
					objIn.close();
				}
			} catch (Exception e2) {
				throw e2;
			}
		}
	}
	
	/**
	 * 获取字节流
	 * @return
	 * @throws IOException
	 */
	public static byte[] objectToByte(Object obj) throws IOException{
		byte[] data = null;
		ByteArrayOutputStream byteOutput = null;
		ObjectOutputStream objOutput = null;
		try {
			byteOutput = new ByteArrayOutputStream();
			objOutput = new ObjectOutputStream(byteOutput);

			objOutput.writeObject(obj);
			data = byteOutput.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (objOutput != null) {
					objOutput.close();
				}
				if (byteOutput != null) {
					byteOutput.close();
				}
			} catch (IOException e2) {
				throw e2;
			}
		}
		return data;
	}
	
	/**
	 * 数组合并
	 * @param a
	 * @param b
	 * @return
	 */
	public static byte[] concat (byte[] a, byte[] b) {
		byte[] c  = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);

		return c;
	}
}
