package com.jun.thread.main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jun.thread.lock.LockHandler;

/**
 * 
 * @Description 线程锁的测试
 * @author Guojun
 * @Date 2018年5月23日 下午8:37:54
 *
 */
public class LockHandlerMain {
	/**
	 * 线程池
	 */
	private static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
			10, 150, 30, TimeUnit.MINUTES, 
			new ArrayBlockingQueue<Runnable>(100), (runnable) -> {
				return new Thread(runnable, "测试线程" + System.currentTimeMillis());
			});

	public static void main(String[] args) {
		LockHandlerMain lockHandlerMain = new  LockHandlerMain();
		lockHandlerMain.readWithoutLockTest();
		//lockHandlerMain.syncTest();
		//lockHandlerMain.lockTest();
		//lockHandlerMain.readWriteLockTest();
		
		EXECUTOR.shutdown();
	}
	
	/**
	 * 无锁测试, 10个线程同时写入，会出现重复数据
	 * @param handler
	 */
	public void readWithoutLockTest () {
		LockHandler handler = new LockHandler();
		String[] values = new String[] {"aa","dd","bb","ee","ff","gg","aa","aa"};
		
		//写 线程
		for (int i = 0; i < 10; i++) {
			EXECUTOR.execute(() -> {
				for (String value : values) {
					handler.setValueWithoutLock(value + System.currentTimeMillis() + "_" + Math.random(),value);
				}
			});
		}

		//读线程
		EXECUTOR.execute(() -> {
			handler.printValueWithoutLock();;
		});
	}

	/**
	 * 读写锁测试
	 * @param handler
	 */
	public void readWriteLockTest () {
		LockHandler handler = new LockHandler();
		
		//写 线程1
		EXECUTOR.execute(() -> {
			System.out.println("写入数据开始1"+System.currentTimeMillis());
			handler.setValueWithWriteLock("book1","MySQL从入门到删库跑路!");
			System.out.println("写入数据结束1"+System.currentTimeMillis());
		});

		//写线程2
		EXECUTOR.execute(() -> {
			System.out.println("写入数据开始2"+System.currentTimeMillis());
			handler.setValueWithWriteLock("book2","java从入门到放弃!");
			System.out.println("写入数据结束2"+System.currentTimeMillis());
		});

		//读线程1
		EXECUTOR.execute(() -> {
			System.out.println("读取数据开始1"+System.currentTimeMillis());
			handler.printValueWitReadLock();
			System.out.println("读取数据结束1"+System.currentTimeMillis());
		});

		//读线程2
		EXECUTOR.execute(() -> {
			System.out.println("读取数据开始2"+System.currentTimeMillis());
			handler.printValueWitReadLock();
			System.out.println("读取数据结束2"+System.currentTimeMillis());
		});
	}

	/**
	 * synchronized 测试
	 * @param handler
	 */
	public void syncTest () {
		LockHandler handler = new LockHandler();
		
		//写线程1
		EXECUTOR.execute(() -> {
			System.out.println("写入数据开始1"+System.currentTimeMillis());
			handler.setValueWithSync("book1","MySQL从入门到删库跑路!");
			System.out.println("写入数据结束1"+System.currentTimeMillis());
		});

		//写线程2
		EXECUTOR.execute(() -> {
			System.out.println("写入数据开始2"+System.currentTimeMillis());
			handler.setValueWithSync("book2","java从入门到放弃!");
			System.out.println("写入数据结束2"+System.currentTimeMillis());
		});

		//读线程1
		EXECUTOR.execute(() -> {
			System.out.println("读取数据开始1"+System.currentTimeMillis());
			handler.printValueWithSync();;
			System.out.println("读取数据结束1"+System.currentTimeMillis());
		});

		//读线程2
		EXECUTOR.execute(() -> {
			System.out.println("读取数据开始2"+System.currentTimeMillis());
			handler.printValueWithSync();
			System.out.println("读取数据结束2"+System.currentTimeMillis());
		});
	}


	/**
	 * lock 测试
	 * @param handler
	 */
	public void lockTest () {
		LockHandler handler = new LockHandler();
		
		//写线程1
		EXECUTOR.execute(() -> {
			System.out.println("写入数据开始1"+System.currentTimeMillis());
			handler.setValueWithLock("book1","MySQL从入门到删库跑路!");
			System.out.println("写入数据结束1"+System.currentTimeMillis());
		});

		//写线程2
		EXECUTOR.execute(() -> {
			System.out.println("写入数据开始2"+System.currentTimeMillis());
			handler.setValueWithLock("book2","java从入门到放弃!");
			System.out.println("写入数据结束2"+System.currentTimeMillis());
		});

		//读线程1
		EXECUTOR.execute(() -> {
			System.out.println("读取数据开始1"+System.currentTimeMillis());
			handler.printValueWithLock();;
			System.out.println("读取数据结束1"+System.currentTimeMillis());
		});

		//读线程2
		EXECUTOR.execute(() -> {
			System.out.println("读取数据开始2"+System.currentTimeMillis());
			handler.printValueWithLock();
			System.out.println("读取数据结束2"+System.currentTimeMillis());
		});
	}
}
