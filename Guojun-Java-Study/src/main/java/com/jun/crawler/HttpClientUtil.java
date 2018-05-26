package com.jun.crawler;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @Description HttpClient学习
 * @author Guojun
 * @Date 2018年5月26日 下午8:01:15
 *
 */
public class HttpClientUtil {
	
	/**
	 * 未知异常代码
	 */
	public static final int UNKNOWN_EXCEPTION_CODE = -400;

	private static CloseableHttpClient client;

	static {
		try {
			//socket链接工厂
			ConnectionSocketFactory psf = PlainConnectionSocketFactory.getSocketFactory();
			//https忽略主机校验
			LayeredConnectionSocketFactory ssf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
			//注册器,设置协议http和https对应的处理socket链接工厂的对象 
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", psf)
					.register("https", ssf)
					.build();
			
			//通过注册器构造连接池管理器
			PoolingHttpClientConnectionManager poolingManager = new PoolingHttpClientConnectionManager(registry);
			// 最大连接数
			poolingManager.setMaxTotal(200);
			// 将每个路由基础的连接增加到50
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

			//构造CloseableHttpClient对象
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

	/**
	 * 发送post请求，表单请求方式application/x-www-form-urlencoded
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, Map<String, String> params, String charset) throws Exception {
		String result = "";  
		
		//创建POSt请求对象
		HttpPost httpPost = new HttpPost(url);
		
		//组装请求参数
		List<NameValuePair> requestParams = new ArrayList<>();
		if (params != null && params.size() > 0) {
			params.forEach((paramName, paramValue) ->{
				NameValuePair nameValuePair = new BasicNameValuePair(paramName, paramValue);
				requestParams.add(nameValuePair);
			});
		}
		
		//post请求中添加请求参数
		if (requestParams.size() > 0) {
			HttpEntity requestEntity = new UrlEncodedFormEntity(requestParams, charset);
			httpPost.setEntity(requestEntity);
		}
		
		//设置投信息
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.setHeader("User-Agent", "Guojun System");
		
		//执行请求
		CloseableHttpResponse response = client.execute(httpPost);
		if (response == null) {
			throw new RuntimeException("请求返回异常");
		}

		if (response.getStatusLine() == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			int errorCode = response.getStatusLine() == null ? UNKNOWN_EXCEPTION_CODE : response.getStatusLine().getStatusCode();
			throw new RuntimeException(StringUtils.join("请求返回异常，异常代码：", errorCode));
		}
		
		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			//按指定编码转换结果实体为String类型  
			result = EntityUtils.toString(responseEntity, charset); 
		}
		
		response.close();
        
        return result;  
	}
	
	public static void main(String[] args) throws Exception {
		String url="https://segmentfault.com/a/1190000012316621#articleHeader5";  
	    String body = doPost(url, null,"utf-8");  
	    System.out.println("响应结果：");  
	    System.out.println(body);  
	  
	    System.out.println("-----------------------------------");  
	  
	    url = "https://www.yeetrack.com/?p=782#HTTP-connection-routing";
	    body = doPost(url, null, "utf-8");  
	    System.out.println("响应结果：");  
	    System.out.println(body);  
	}
}
