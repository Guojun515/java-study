package com.jun.threadpool.main;

import com.jun.threadpool.utils.ThreadPool;

/**
 * 
 * @Description: 自定义线程池测试类
 * @author v-yuguojun
 * @date 2018年5月23日 下午3:17:06
 */
public class ThreadPoolMain {
	
	public static void main(String[] args) {  
        // 创建3个线程的线程池  
        ThreadPool t = ThreadPool.getThreadPool(3);  
        t.execute(new Runnable[] { new Task(), new Task(), new Task() });  
        t.execute(new Runnable[] { new Task(), new Task(), new Task() });  
        
        // 所有线程都执行完成才destory 
        System.out.println(t); 
        t.destroy(); 
        System.out.println(t);  
    }  
  
    /**
     * 任务类
     */
    private static class Task implements Runnable {  
        private static volatile int i = 1;  
  
        @Override  
        public void run() { 
            System.out.println("任务 " + (i++) + " 完成");  
        }  
    }  
}
