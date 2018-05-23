package com.jun.threadpool.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description PatternThreadFactory
 * @author Guojun
 * @Date 2017年11月18日 上午12:45:01
 *
 */
public class PatternThreadFactory implements ThreadFactory{
	
	private static AtomicInteger i = new AtomicInteger();

	@Override
	public Thread newThread(Runnable paramRunnable) {
		return new Thread(paramRunnable, "Pattern测试线程" + i.incrementAndGet());
	}

}
