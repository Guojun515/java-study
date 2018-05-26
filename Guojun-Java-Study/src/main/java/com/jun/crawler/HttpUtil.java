package com.jun.crawler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @Description java 原生网络请求
 * @author Guojun
 * @Date 2018年5月26日 下午8:04:40
 *
 */
public class HttpUtil {
	
	/**
	 * POST
	 */
	public static String REQUEST_POST = "POST";
	
	/**
	 * GET
	 */
	public static String REQUEST_GET = "GET";
	
	/**
	 * UTF-8
	 */
	public static String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 信任库
	 */
	private static class MyX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}

	/**
	 * 忽略SSL检验，必须在openConnection()方法之前调用
	 * @throws Exception
	 */
	public static void ignoreSSL() throws Exception{
		X509TrustManager[] trustManagers = new X509TrustManager[]{new MyX509TrustManager()};
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustManagers, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}

	/**
	 * 获取链接
	 * @param requstUrl
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getConnection(String requstUrl) throws Exception{
		//忽略SSL检验
		HttpUtil.ignoreSSL();
		
		URL url = new URL(requstUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		
		conn.setRequestProperty("Connection", "Keep-Alive");
		
		return conn;
	}

	/**
	 * 网络请求，返回流数据
	 * @param requstUrl
	 * @param method
	 * @param headProperties
	 * @param connetTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static InputStream requestReturnIn(String requstUrl, String method, Map<String, String> headProperties, int connetTimeout, int readTimeout) throws Exception{
		InputStream in = null;

		HttpURLConnection conn = getConnection(requstUrl);

		if (method != null && !method.equals("")) {
			conn.setRequestMethod(method);
		}
		
		if (connetTimeout > 0) {
			conn.setConnectTimeout(connetTimeout);
		}
		
		if (readTimeout > 0) {
			conn.setReadTimeout(readTimeout);
		}
		
		if (headProperties != null && headProperties.size() > 0) {
			for(Map.Entry<String, String> entry : headProperties.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		if(conn.getResponseCode() == 200){
			in = conn.getInputStream();
		}
		
		return in ;
	}
	
	/**
	 * 网络请求，返回字符串
	 * @param requstUrl
	 * @param method
	 * @param charset
	 * @param headProperties
	 * @param connetTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static String requestReturnStr(String requstUrl, String method, String charset, Map<String, String> headProperties, int connetTimeout, int readTimeout) throws Exception{
		StringBuilder result = new StringBuilder();
		InputStream in = null;
		BufferedReader br = null;
		try {
			in = requestReturnIn(requstUrl, method, headProperties, connetTimeout, readTimeout);
			br = new BufferedReader(new InputStreamReader(in, charset));
			
			String line = br.readLine();
			while (line != null) {
				result.append(line).append("\n");
				line = br.readLine();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			
			if (br != null) {
				br.close();
			}
		}
		
		return result.toString();
	}
}
