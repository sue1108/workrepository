/**
 * 
 */
package com.succez.study.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.succez.study.webserver.common.StaticConstant;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>模拟web server<p>
 * @author sue
 * @createdate 2011-11-21
 */
public class HttpWebServer {

	/**
	 * <p></p>
	 * @param port	用户请求的端口号
	 * @throws IOException	
	 */
	public static void httpServer(int port) throws IOException{
		ServerSocket server = null;
		Socket socket = null;
		try{
			server = new ServerSocket(port);
			while(true){	
				socket = server.accept();
				RequestThread thread = new RequestThread(socket);	//将用户的请求加入到多线程中
				thread.run();	
			}
		}finally{
			server.close();
		}
	}
	

	/**
	 * <p>获得用户的请求，根据用户请求的类型返回相应的内容</p>
	 * @param socket	Socket对象
	 * @return	返回boolean类型，如果写入流成功，则返回true，否则返回false
	 * @throws IOException	当从socket中读取输入流时、将字节写入到socket的输出流、将文件内容拷贝到输出流等情况下
	 * 可能发生I/O错误而抛出异常
	 */
	public static boolean returnClientInfor(Socket socket) throws IOException {
		boolean flag = false;

		if (null != socket) {
			InputStream in = null;
			OutputStream out = null;
			InputStreamReader isr = null;
			BufferedReader read = null;
			try {
				in = socket.getInputStream();
				isr = new InputStreamReader(in);
				System.out.println(socket.getInetAddress());

				// 分析请求的信息
				read = new BufferedReader(isr, StaticConstant.BUFFERREADER_LEN);
				String str = "";

				while (true) {
					str = read.readLine();
					System.out.println("请求行：" + str);
					// 请求行：GET /ascII.htm HTTP/1.1 方法-URI-协议/版本,读取到这一行后，退出循环
					if (str.startsWith("GET"))
						break;
				}

				str = str.split(" ")[1]; // 请求行使用空格隔开，第二个为URI取得URI
				System.out.println("URI:" + str);
				File file = new File("c:\\test" + str); // 默认请求内容存放在c盘
				out = socket.getOutputStream();
				if (file.exists()) { // 请求文件存在
					FileUtils.copyFile(file, out); // 使用apache提供的类FileUtils提供的方法，直接将文件写入到流中
					flag = true;
				} else {
					StringBuffer sbf = new StringBuffer(
							StaticConstant.STRINGBUFFER_LEN);
					sbf.append("文件不存在！<br/>");
					sbf.append("请尝试其他链接");
					out.write(sbf.toString().getBytes("gbk"));
					flag = false;
				}
				out.flush();
			} finally {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(read);
				IOUtils.closeQuietly(isr);
				IOUtils.closeQuietly(socket);
			}
		}
		return flag;
	}
	
	
	public static void main(String args[]) {
		// 默认端口为8080
		int port = 8090;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = 8090;
			System.out.println("输入端口参数不合法，默认端口为：8090");
			e.printStackTrace();
		} finally {
			try {
				HttpWebServer.httpServer(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
