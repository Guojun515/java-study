package com.jun.threadpool.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 线程池的实现
 *
 */
public class ThreadPool {
	
	//线程池中默认的线程数
	private static int workerNum = 5;
	
	//工作线程
	private WorkThread[] workThreads;
	
	//已完成的任务数
	private static volatile int finshedTask = 0;
	
	//任务队列，作为缓冲，List不安全
	List<Runnable> taskQueue = new LinkedList<>();
	
	private static volatile ThreadPool threadPool ;
	
	/**
	 * 创建具有默认线程个数的线程池  
	 */
	private ThreadPool () {
		this(5);
	}
	
	/**
	 * 创建线程池,workerNum为线程池中工作线程的个数  
	 * @param workerNum
	 */
	private ThreadPool (int workerNum) {
		ThreadPool.workerNum = workerNum;
		workThreads = new WorkThread[workerNum];
		for (int i = 0; i < workerNum; i++) {
			workThreads[i] = new WorkThread();
			workThreads[i].start();
		}
	}
	
	/**
	 * 单例模式，获得一个默认线程个数的线程池  
	 * @return
	 */
	public static ThreadPool getThreadPool() {
		return getThreadPool(workerNum);
	}
	
	/**
	 * 单例模式，获得一个指定线程数的线程池
	 * workNum大于0，为线程池中工作的线程数
	 * workNum小于等于0，创建默认工作线程数的线程池
	 * @param workNum
	 * @return
	 */
	public static ThreadPool getThreadPool(int workNum) {
		if (workNum <= 0) {
			workNum = ThreadPool.workerNum;
		}
		if (threadPool == null) {
			synchronized (ThreadPool.class) {
				threadPool = new ThreadPool(workNum);
			}
		}
		return threadPool;
	}
	
	/**
	 * 执行任务，其实是把任务加入到任务队列，什么时候执行是由线程池决定
	 * @param runnable
	 */
	public void execute (Runnable runnable) {
		synchronized (taskQueue) {
			taskQueue.add(runnable);
			taskQueue.notify();
		}
	}
	
	/**
	 * 批量执行任务，其实是把任务加入到任务队列，什么时候执行是由线程池决定
	 * @param runnables
	 */
	public void execute (Runnable[] runnables) {
		synchronized (taskQueue) {
			for (Runnable r : runnables) {
				taskQueue.add(r);
			}
			taskQueue.notify();
		}
	}
	
	/**
	 * 销毁线程池，该方法保证所有线程都执行完毕才进行销毁，否则等待任务完成才销毁
	 */
	public void destroy() {
		while (!taskQueue.isEmpty()) { //如果还有任务没完成，先睡一会吧
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//工作线程停止工作，且置为null
		for (int i = 0; i < workerNum; i++) {
			workThreads[i].stopWorker();
			workThreads[i] = null;
		}
		
		threadPool = null;
		
		//清空任务队列
		taskQueue.clear();
	}
	
	 /**
	  *  返回工作线程的个数  
	  * @return
	  */
    public int getWorkThreadNumber() {  
        return workerNum;  
    }  
  
    /**
     *  返回已完成任务的个数,这里的已完成是只出了任务队列的任务个数，可能该任务并没有实际执行完成  
     * @return
     */
    public int getFinishedTasknumber() {  
        return finshedTask;  
    }  
  
    /**
     *  返回任务队列的长度，即还没处理的任务个数  
     * @return
     */
    public int getWaitTasknumber() {  
        return taskQueue.size();  
    }  
  
    /**
     *  覆盖toString方法，返回线程池信息：工作线程个数和已完成任务个数  
     */
    @Override  
    public String toString() {  
        return "WorkThread number:" + workerNum + "  finished task number:"  
                + finshedTask + "  wait task number:" + getWaitTasknumber();  
    }  
	
	
	/**
	 * 工作线程（内部类）
	 *
	 */
	private class WorkThread extends Thread {
		
		//表示该工作线程是否有效，用于结束线程
		private boolean isRunning = true;
		
		@Override
		public void run() {
			Runnable r = null;
			while (isRunning) {
				synchronized (taskQueue) {
					while (isRunning && taskQueue.isEmpty()) { //队列为空
						try {
							taskQueue.wait(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					if (!taskQueue.isEmpty()) { //取出任务
						r = taskQueue.remove(0);
					}
				}
				
				if (r != null) {
					r.run(); //执行任务
				}
				
				finshedTask ++;
				r = null;
			}
		}
		
		// 停止工作，让该线程自然执行完run方法，自然结束  
		public void stopWorker() {
			isRunning = false;
		}
	}
}
