package com.jun.thread.lock;

import com.jun.thread.lock.reentrantlock.ReentrantLockSampleSupport;
import com.jun.thread.lock.reentrantlock.SampleSupport;
import com.jun.thread.lock.reentrantlock.SynchronizedSampleSupport;

/**
 * 
 * @Description: 测试可中断锁
 * @author v-yuguojun
 * @date 2018年5月23日 下午4:50:29
 */
public class ReentrantLockSample {  

	/**
	 * ReentrantLock 中断测试
	 */
	public static void testReentrantLock() {  
		final SampleSupport support = new ReentrantLockSampleSupport();  
		
		Thread thread1 = new Thread(() -> {
			try {  
				support.doSomething();  
			}  
			catch (Exception e) {  
				e.printStackTrace();  
			} 
		}, "ReentrantLock 中断测试1");  

		Thread thread2 = new Thread(() -> {  
			try {  
				support.doSomething();  
			}  
			catch (Exception e) {  
				System.out.println("Second Thread Interrupted without executing counter++,beacuse it waits a long time.");  
			}   
		}, "ReentrantLock 中断测试2");  

		ReentrantLockSample.executeTest(thread1, thread2); 
	}  

	/**
	 * Synchronized 中断测试
	 */
	public static void testSynchronized() {  
		final SampleSupport support2 = new SynchronizedSampleSupport();  

		Runnable runnable = new Runnable() {  
			@Override
			public void run() {  
				try {
					support2.doSomething();
				} catch (Exception e) {
					e.printStackTrace();
				}  
			}  
		};  
		
		Thread thread1 = new Thread(runnable,"synchronized 中断测试1");  
		Thread thread2 = new Thread(runnable, "synchronized 中断测试2");  

		ReentrantLockSample.executeTest(thread1, thread2);  
	}  

	/** 
	 * Make thread thread1 run faster than thread thread2, 
	 * then thread thread2 will be interruted after about 1s. 
	 * @param thread1 
	 * @param thread2
	 */  
	private static void executeTest(Thread thread1, Thread thread2) {  
		try {
			//启动thread1线程后100ms再启动thread2线程
			thread1.start();  
			Thread.sleep(100);  
			thread2.start();

			//1s后中断thread2线程，synchronized未获取锁的线程不会中断，Lock.lock()也不会
			Thread.sleep(1000);
			thread2.interrupt();
		}  
		catch (InterruptedException e) {  
			e.printStackTrace();  
		}  
	}  
}  
