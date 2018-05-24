package com.jun.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 请求的客户端
 * @author FR0012
 *
 */
public class Client {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 1314;

		try {
			Socket socket = new Socket(host, port);
			socket.setSoTimeout(3000 * 20);
			//输入参数
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);
			pw.write("{\"用户名\":\"admin\",\"密码\":\"123456000\"}");
			pw.flush();
			socket.shutdownOutput();
			
			//3.获取输入流，并读取服务器端的响应信息
            InputStream in=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            String info=null;
            while((info=br.readLine())!=null){
                System.out.println("我是客户端，服务器说："+info);
            }
            
            //4.关闭资源
            br.close();
            out.close();
            
            in.close();
            pw.close();
            
            socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
