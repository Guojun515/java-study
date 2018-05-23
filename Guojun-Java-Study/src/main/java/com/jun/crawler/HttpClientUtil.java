package com.jun.crawler;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

public class HttpClientUtil {

	private static PoolingHttpClientConnectionManager poolingManager;

	@SuppressWarnings("unused")
	private static CloseableHttpClient client;

	static {
		try {
			
			ConnectionSocketFactory psf = PlainConnectionSocketFactory.getSocketFactory();
			LayeredConnectionSocketFactory ssf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", psf)
					.register("https", ssf)
					.build();
			poolingManager = new PoolingHttpClientConnectionManager(registry);
			// 最大连接数
			poolingManager.setMaxTotal(200);
			// 将每个路由基础的连接增加到20
			poolingManager.setDefaultMaxPerRoute(50);

			// 请求的上下文
			RequestConfig requestConfig = RequestConfig.custom()
					// 与服务器连接超时时间：httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接
					.setConnectTimeout(60000)
					// 从连接池中获取连接的超时时间  
					.setConnectionRequestTimeout(60000)
					// socket读数据超时时间：从服务器获取响应数
					.setSocketTimeout(60000)
					.build();

			client = HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setConnectionManager(poolingManager)
					.setRetryHandler(new HttpRequestRetryHandler() {
						@Override
						public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
							if (executionCount >= 5) {
								// 如果已经重试了5次，就放弃
								return false;
							}
							if (exception instanceof InterruptedIOException) {
								// 超时
								return false;
							}
							if (exception instanceof UnknownHostException) {
								// 目标服务器不可达
								return false;
							}
							if (exception instanceof ConnectTimeoutException) {
								// 连接被拒绝
								return false;
							}
							if (exception instanceof SSLException) {
								// ssl握手异常
								return false;
							}
							HttpClientContext clientContext = HttpClientContext.adapt(context);
							HttpRequest request = clientContext.getRequest();
							boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
							if (idempotent) {
								// 如果请求是幂等的，就再次尝试
								return true;
							}
							return false;
						}
					})
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
