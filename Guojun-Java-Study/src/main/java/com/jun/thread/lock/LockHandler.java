package com.jun.thread.lock;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockHandler {
	
	/**
	 * 非公平的锁
	 */
	private Lock lock = new ReentrantLock();
	
	/**
	 * 非公平的读写锁,如果没有写锁，读锁不受影响，如果有写锁，读锁会等待写锁完成
	 */
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(); 

	/**
	 * 存放值的一个Map
	 */
	private ConcurrentHashMap<String, String> mapValues = new ConcurrentHashMap<>();;
	
	
	//############################不加锁############################
	
	/**
	 * 不加锁，会产生并发情况
	 * @param key
	 * @param value
	 */
	public void setValueWithoutLock (String key ,String value) {
		for (Entry<String, String> entry : mapValues.entrySet()) {
			if (entry.getValue().equals(value)) {
				return;
			}
		}
		mapValues.put(key, value);
	}
	
	/**
	 * 不加锁
	 */
	public void printValueWithoutLock () {
		for (Entry<String, String> entry : mapValues.entrySet()) {
			System.out.println("[key:" + entry.getKey() + ", value:" + entry.getValue() + "]");
		}
	}
	
	//############################synchronized关键字############################
	//synchronized是基于jvm底层实现的数据同步
	//synchronized作用于非静态的方法时，锁住的是当前对象的实例
	//synchronized作用与静态方法时，锁住的是class实例，又因为class实例的数据永久存储，所以静态方法锁相当于一个全局锁
	//synchronized机制提供了对每个对象相关的隐式监视器锁的访问，并强制所有锁获取和释放都要出现在一个结构块中，当获取了多个锁，他们必须以相反的顺序释放
	//synchronized的释放是隐式的，只要线程运行的代码超出了synchronized语句块，锁就会释放
	//synchronized methods(){} 与synchronized（this）{}之间没有什么区别，只是 synchronized methods(){} 便于阅读理解，而synchronized（this）{}可以更精确的控制冲突限制访问区域，有时候表现更高效率。
	//在一个线程使用synchronized方法时调用该对象另一个synchronized方法，即一个线程得到一个对象锁后再次请求该对象锁，是永远可以拿到锁的。
	
	/**
	 * 加synchronized锁
	 * @param key
	 * @param value
	 */
	public synchronized void setValueWithSync (String key ,String value) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Entry<String, String> entry : mapValues.entrySet()) {
			if (entry.getValue().equals(value)) {
				return;
			}
		}
		mapValues.put(key, value);
		
		printValueWithSync();
	}
	
	/**
	 * 加synchronized
	 * 加锁之后效果会会造成读互斥，不能并发访问，从而影响性能
	 */
	public synchronized void printValueWithSync () {
		for (Entry<String, String> entry : mapValues.entrySet()) {
			System.out.println("[key:" + entry.getKey() + ", value:" + entry.getValue() + "]");
		}
	}
	
	//############################Lock############################
	//Lock是基于java编写，主要通过硬件依赖CUP指令实现数据同步
	//它具有与使用 synchronized 方法和语句所访问的隐式监视器锁相同的一些基本行为和语义，但功能更强大。
	//Lock机制必须显式的调用Lock对象的unlock()方法才能释放锁, 这为获取锁和释放锁不出现在同一个块结构中, 以及以更自由的顺序释放锁提供了可能. 
	
	/**
	 * 加Lock
	 * @param key
	 * @param value
	 */
	public void setValueWithLock (String key ,String value) {
		try {
			lock.lock();
			Thread.sleep(1000);
			for (Entry<String, String> entry : mapValues.entrySet()) {
				if (entry.getValue().equals(value)) {
					return;
				}
			}
			mapValues.put(key, value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock(); //释放锁
		}
		
	}
	
	/**
	 * 加Lock
	 */
	public void printValueWithLock () {
		try {
			lock.lock(); //正常的锁，不可中断
			for (Entry<String, String> entry : mapValues.entrySet()) {
				System.out.println("[key:" + entry.getKey() + ", value:" + entry.getValue() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	//############################ReadWriteLock############################
	
	/**
	 * 加readWriteLock
	 * @param key
	 * @param value
	 */
	public void setValueWithWriteLock (String key ,String value) {
		try {
			readWriteLock.writeLock().lockInterruptibly();//可中断的写锁
			Thread.sleep(1000);
			for (Entry<String, String> entry : mapValues.entrySet()) {
				if (entry.getValue().equals(value)) {
					return;
				}
			}
			mapValues.put(key, value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			readWriteLock.writeLock().unlock(); //释放锁
		}
		
	}
	
	/**
	 * ReentrantReadWriteLock，存在写锁时，会等待写锁执行完成
	 */
	public void printValueWitReadLock () {
		try {
			readWriteLock.readLock().lockInterruptibly();
			for (Entry<String, String> entry : mapValues.entrySet()) {
				System.out.println("[key:" + entry.getKey() + ", value:" + entry.getValue() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readWriteLock.readLock().unlock();
		}
	}
}
