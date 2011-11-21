/**
 * 
 */
package com.succez.study.webserver;

import java.io.IOException;
import java.net.Socket;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>多线程处理用户的请求<p>
 * @author sue
 * @createdate 2011-11-21
 */
public class RequestThread implements Runnable{
	
	private Socket socket = null;

	public RequestThread(Socket socket){
		this.socket = socket;
	}
	public void run() {
		try {
			boolean flag = HttpWebServer.returnClientInfor( socket );
			if(flag)
				System.out.println("响应成功！");
			else
				System.out.println("响应失败");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
