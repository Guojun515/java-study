package com.jun.thread.main;

import com.jun.thread.lock.ReentrantLockSample;

/**
 * 
 * @Description: 线程锁中断测试
 * @author v-yuguojun
 * @date 2018年5月23日 下午5:20:39
 */
public class ReentrantLockSampleMain {

	public static void main(String[] args) {  
		System.out.println("【Synchronized 中断测试启动】");
		ReentrantLockSample.testSynchronized();  
		
		System.out.println("【ReentrantLock 中断测试启动】");
		ReentrantLockSample.testReentrantLock(); 
	}
}
