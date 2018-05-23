package com.jun.thread.test;

import com.jun.thread.lock.LockHandler;

public class Test {
	public static void main(String[] args) {
		String[] values = new String[] {"aa","dd","bb","ee","ff","gg","aa","aa"};
		//初始化，单线程模式不用锁
		LockHandler handler = new LockHandler();
		for (String value : values) {
			handler.setValueWithoutLock(value + System.currentTimeMillis() + "_" + Math.random(),value);
		}
		
		syncTest(handler);
		//lockTest(handler);
		//readWriteLockTest(handler);
	}
	
	/**
	 * 读写锁测试
	 * @param handler
	 */
	public static void readWriteLockTest (final LockHandler handler) {
		new Thread("Write1 Thread"){
			@Override
			public void run() {
				System.out.println("写入数据开始1"+System.currentTimeMillis());
				handler.setValueWithWriteLock("book1","MySQL从入门到删库跑路!");
				System.out.println("写入数据结束1"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Write2 Thread"){
			@Override
			public void run() {
				System.out.println("写入数据开始2"+System.currentTimeMillis());
				handler.setValueWithWriteLock("book2","java从入门到放弃!");
				System.out.println("写入数据结束2"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Read1 Thread"){
			@Override
			public void run() {
				System.out.println("读取数据开始1"+System.currentTimeMillis());
				handler.printValueWitReadLock();
				System.out.println("读取数据结束1"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Read2 Thread"){
			@Override
			public void run() {
				System.out.println("读取数据开始2"+System.currentTimeMillis());
				handler.printValueWitReadLock();
				System.out.println("读取数据结束2"+System.currentTimeMillis());
			};
		}.start();
	}
	
	/**
	 * synchronized 测试
	 * @param handler
	 */
	public static void syncTest (final LockHandler handler) {
		new Thread("Write1 Thread"){
			@Override
			public void run() {
				System.out.println("写入数据开始1"+System.currentTimeMillis());
				handler.setValueWithSync("book1","MySQL从入门到删库跑路!");
				System.out.println("写入数据结束1"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Write2 Thread"){
			@Override
			public void run() {
				System.out.println("写入数据开始2"+System.currentTimeMillis());
				handler.setValueWithSync("book2","java从入门到放弃!");
				System.out.println("写入数据结束2"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Read1 Thread"){
			@Override
			public void run() {
				System.out.println("读取数据开始1"+System.currentTimeMillis());
				handler.printValueWithSync();;
				System.out.println("读取数据结束1"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Read2 Thread"){
			@Override
			public void run() {
				System.out.println("读取数据开始2"+System.currentTimeMillis());
				handler.printValueWithSync();
				System.out.println("读取数据结束2"+System.currentTimeMillis());
			};
		}.start();
	}
	
	
	/**
	 * lock 测试
	 * @param handler
	 */
	public static void lockTest (final LockHandler handler) {
		new Thread("Write1 Thread"){
			@Override
			public void run() {
				System.out.println("写入数据开始1"+System.currentTimeMillis());
				handler.setValueWithLock("book1","MySQL从入门到删库跑路!");
				System.out.println("写入数据结束1"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Write2 Thread"){
			@Override
			public void run() {
				System.out.println("写入数据开始2"+System.currentTimeMillis());
				handler.setValueWithLock("book2","java从入门到放弃!");
				System.out.println("写入数据结束2"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Read1 Thread"){
			@Override
			public void run() {
				System.out.println("读取数据开始1"+System.currentTimeMillis());
				handler.printValueWithLock();;
				System.out.println("读取数据结束1"+System.currentTimeMillis());
			};
		}.start();
		
		new Thread("Read2 Thread"){
			@Override
			public void run() {
				System.out.println("读取数据开始2"+System.currentTimeMillis());
				handler.printValueWithLock();
				System.out.println("读取数据结束2"+System.currentTimeMillis());
			};
		}.start();
	}
}
