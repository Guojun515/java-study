package com.jun.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试可中断锁
 * @author FR0012
 *
 */

public class ReentrantLockSample {  

	public static void main(String[] args) {  
		testSynchronized();  
		//testReentrantLock();  
	}  

	public static void testReentrantLock() {  
		final SampleSupport1 support = new SampleSupport1();  
		
		Thread first = new Thread(new Runnable() {  
			public void run() {  
				try {  
					support.doSomething();  
				}  
				catch (InterruptedException e) {  
					e.printStackTrace();  
				}  
			}  
		});  

		Thread second = new Thread(new Runnable() {  
			public void run() {  
				try {  
					support.doSomething();  
				}  
				catch (InterruptedException e) {  
					System.out.println("Second Thread Interrupted without executing counter++,beacuse it waits a long time.");  
				}  
			}  
		});  

		executeTest(first, second);  
	}  

	public static void testSynchronized() {  
		final SampleSupport2 support2 = new SampleSupport2();  

		Runnable runnable = new Runnable() {  
			public void run() {  
				support2.doSomething();  
			}  
		};  

		Thread third = new Thread(runnable);  
		Thread fourth = new Thread(runnable);  

		executeTest(third, fourth);  
	}  

	/** 
	 * Make thread a run faster than thread b, 
	 * then thread b will be interruted after about 1s. 
	 * @param a 
	 * @param b 
	 */  
	public static void executeTest(Thread a, Thread b) {  
		try {
			//启动a线程后100ms再启动b线程
			a.start();  
			Thread.sleep(100);  
			b.start();

			Thread.sleep(1000);
			b.interrupt();//1s后中断b线程，synchronized未获取锁的线程不会中断，Lock.lock()也不会
		}  
		catch (InterruptedException e) {  
			e.printStackTrace();  
		}  
	}  
}  

abstract class SampleSupport {  

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
}  

class SampleSupport1 extends SampleSupport {  

	private final ReentrantLock lock = new ReentrantLock();  

	public void doSomething() throws InterruptedException {  
		lock.lockInterruptibly(); // 可中断线程，如果用lock()则不能中断
		System.out.println(Thread.currentThread().getName() + " will execute counter++.");  
		startTheCountdown();  //倒计时5ms
		try {  
			counter++;  
			
			System.out.println(counter);
		}
		finally {  
			lock.unlock();  
		}  
	}  
}  

class SampleSupport2 extends SampleSupport {  

	public synchronized void doSomething() {  
		System.out.println(Thread.currentThread().getName() + " will execute counter++.");  
		startTheCountdown(); //倒计时5ms
		
		counter++;  
		System.out.println(counter);
	}  
}  