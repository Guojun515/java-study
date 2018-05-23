package com.jun.thread.lock.reentrantlock;

/**
 * 
 * @Description: 线程锁的学习
 * @author v-yuguojun
 * @date 2018年5月23日 下午4:43:36
 */
public abstract class SampleSupport {  
	
	protected int counter;  
	
	/** 
	 * 简单的倒计时，倒计时5秒
	 */  
	public void startTheCountdown() {  
		long currentTime = System.currentTimeMillis();  
		for (;;) {  
			long diff = System.currentTimeMillis() - currentTime;  
			if (diff > 5000) {  
				break;  
			}  
		}  
	} 
	
	/**
	 * 实现一些操作
	 */
	public abstract void doSomething() throws Exception;
}  
