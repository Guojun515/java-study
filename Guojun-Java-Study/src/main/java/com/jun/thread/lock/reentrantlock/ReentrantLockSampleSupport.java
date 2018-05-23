package com.jun.thread.lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @Description: ReentrantLock
 * @author v-yuguojun
 * @date 2018年5月23日 下午4:49:13
 */
public class ReentrantLockSampleSupport extends SampleSupport {  

	private final ReentrantLock lock = new ReentrantLock();  

	@Override
	public void doSomething() throws Exception {  
		// 可中断线程，如果用lock()则不能中断
		lock.lockInterruptibly();
		System.out.println(Thread.currentThread().getName() + " will execute counter++.");  
		
		try {
			// 倒计时5ms
			startTheCountdown();  
			counter++;  

			System.out.println(Thread.currentThread().getName() + ":"  + counter);
		}finally {
			lock.unlock(); 
		}
	}  
}  
