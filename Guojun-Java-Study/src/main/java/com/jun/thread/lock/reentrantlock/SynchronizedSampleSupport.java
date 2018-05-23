package com.jun.thread.lock.reentrantlock;

/**
 * 
 * @Description: synchronized
 * @author v-yuguojun
 * @date 2018年5月23日 下午4:49:33
 */
public class SynchronizedSampleSupport extends SampleSupport {  

	@Override
	public synchronized void doSomething()  throws Exception {  
		System.out.println(Thread.currentThread().getName() + " will execute counter++.");  
		//倒计时5ms
		startTheCountdown(); 
		
		counter++;  
		System.out.println(Thread.currentThread().getName() + ":" + counter);
	}  
}  
