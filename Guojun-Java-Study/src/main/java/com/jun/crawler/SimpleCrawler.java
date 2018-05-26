package com.jun.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jun.http.HttpUtil;

/**
 * 微信表情包下载
 * @author FR0012
 *
 */

public class SimpleCrawler {

	/**
	 * 下载危险表情包
	 * @param urlStr
	 * @param fileName
	 * @param filePath
	 * @throws Exception
	 */
	public void grapWeixinExpressioin (String urlStr, String fileName, String filePath) throws Exception {
		String regex = "data-src=[\"]([http://]{7}[a-zA-Z0-9\\?\\./_]+wx_fmt=([a-zA-Z]+))[\"]";

		InputStream in = null;
		Reader reader = null;
		BufferedReader bufferedReader = null;
		try {
			//存放的目录
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			} else {
				File[] files = dir.listFiles();
				for(int i= 0 ; i < files.length; i++){
					files[i].delete();
				}
			}
			
			String html = HttpUtil.requestReturnStr(urlStr, HttpUtil.REQUEST_GET, HttpUtil.DEFAULT_CHARSET, null, 60000, 60000);
			
			Matcher matcher = Pattern.compile(regex).matcher(html);
			while (matcher.find()) {
				String imgUrl = matcher.group(1);
				String suffix = matcher.group(2);
				this.downloadImage(dir, fileName, suffix, imgUrl);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (bufferedReader != null ) {
				bufferedReader.close();
			}

			if (reader != null) {
				reader.close();
			}

			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * 下载图片
	 * @param filePath
	 * @param fileName
	 * @param urlStrs
	 * @throws Exception
	 */
	public void downloadImage (File dir, String fileName, String suffix , String imageUrl) throws Exception {
		InputStream in = null;
		FileOutputStream out = null;

		try {
			in = HttpUtil.requestReturnIn(imageUrl, HttpUtil.REQUEST_GET, null, 60000, 60000);
			if(in != null){
				File file = new File(dir, fileName + System.currentTimeMillis() + "." + suffix);
				out = new FileOutputStream(file);

				byte[] b = new byte[1024];
				int len = 0;
				while ((len = in.read(b)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
			}
			System.out.println("已下载图片："+imageUrl);
		} catch (Exception e) {
			throw e;
		} finally {
			if(out != null){
				out.close();
			}

			if(in != null){
				in.close();
			}
		}
	}

	public static void main(String[] args) {
		SimpleCrawler weixinExpressionDownload = new SimpleCrawler();
		try {
			weixinExpressionDownload.grapWeixinExpressioin("https://mp.weixin.qq.com/s/e1yzKg6-XjbW5jRzlg4qCA", "年轻人怎样跟阿姨说话", "F:/表情包/年轻人怎样跟阿姨说话");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
