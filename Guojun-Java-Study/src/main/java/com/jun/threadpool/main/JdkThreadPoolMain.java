package com.jun.threadpool.main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jun.regex.common.PatternManager;
import com.jun.threadpool.common.PatternThreadFactory;

/**
 * @Description JDK 自带的线程池使用
 * @author Guojun
 * @Date 2017年11月17日 下午11:56:19
 *
 */
public class JdkThreadPoolMain {
	public static void main(String[] args) {
		final String regex = "<aa>(.+)</aa>";
		int len = 10;
		
		/*
		 * 线程池 corePoolSize，maximumPoolSize，workQueue的关系
		 * workQueue是BlockingQueue类型，SynchronousQueue，LinkedBlockingQueue，ArrayBlockingQueue是它的实现类
		 * 	-> 直接提交。SynchronousQueue队列，该队列的特点就是在某次添加元素后必须等待其他线程取走后才能继续添加。所以，当队列中有元素存在
		 * 	         时，继续往改队列加入新的元素时，线程池会创建新的线程消费队列中的元素后才会往队列里边新增元素，当线程数等于maximumPoolSize时，
		 * 	         继续往队列新增元素，则执行异常策略
		 * 
		 * 	-> 无界队列。LinkedBlockingQueue是属于无界队列，其特点是可以往队列里边不断新增元素。所以，当线程池中运行的线程数达到corePoolSize
		 *     时，新增的元素一直往队列里边存，不会再产生新的线程（相当于maximumPoolSize无效）
		 *     
		 * 	-> 有界队列。ArrayBlockingQueue是属于有界队列，其特点是可以往队列里边不断新增元素，但是队列的大小固定。所以，当线程池中运行的线程数达到
		 * 	   corePoolSize时，新增的元素一直往队列里边存，直到队列存满后，还有元素进来就会创建新的线程，知道线程数达到maximumPoolSize时，就会
		 *     使用拒绝策略来处理。
		 *     
		 * keepAliveTime
		 * 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
		 * 
		 * unit
		 * 时间单位，TimeUnit
		 * 
		 * threadFactory
		 * 新建线程的工厂
		 * 
		 * RejectedExecutionHandler
		 * 当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理。这里使用默认的
		 * 
		 * 学习地址参考：http://dongxuan.iteye.com/blog/902571
		 */
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 1500, 30, TimeUnit.MINUTES, 
				new ArrayBlockingQueue<Runnable>(100), new PatternThreadFactory());
		
		for (int i = 0; i < len; i ++) {
			executor.execute(()->{
				PatternManager patternManager = PatternManager.getInstance();
				Pattern pattern = patternManager.buildPattern(regex);
				Matcher match = pattern.matcher("***<aa>zhang</aa>****");
				while(match.find()) {
					System.out.println(match.group(1));
				}
			});
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (executor.getQueue().size() == 0) {
			executor.shutdown();
		}
	}
}
