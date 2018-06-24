package com.jun.meters;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

/**
 * https://metrics.dropwizard.io/3.1.0/getting-started/
 * @Description Metric监控工具
 * @author Guojun
 * @Date 2018年6月23日 下午5:21:06
 *
 */
public class MetricMain {
	
	/**
	 * MetricRegistry注册器，需要监控的内容都要在此注册
	 */
	private static final MetricRegistry metrics = new MetricRegistry();
	
	private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
	private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
	private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
	private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
	private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

	public static void main(String args[]) {
		//监控输出，控制台
		startReport();
				
		//内存使用监控
		metrics.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
		//垃圾回收监控
		metrics.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
		//线程数监控
		metrics.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
		//空间监控
		metrics.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
		//缓存池监控
		metrics.register(PROP_METRIC_REG_JVM_BUFFERS,new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
		
		//自定义监控
		Meter requests = metrics.meter("requests");
		doSomething(requests);
	}

	/**
	 * 一些操作
	 * @param requests
	 */
	private static void doSomething(Meter requests) {
		for (int i = 0; i < 60; i++) {
			// meter计数
			requests.mark();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 控制台输出
	 */
	private static void startReport() {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.build();
		reporter.start(1, TimeUnit.SECONDS);
	}
}
