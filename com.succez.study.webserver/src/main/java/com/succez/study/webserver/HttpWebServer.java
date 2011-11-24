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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.webserver.common.CommonsMethod;
import com.succez.study.webserver.common.StaticConstant;
import com.succez.study.webserver.thread.KeyBoardThread;
import com.succez.study.webserver.thread.ThreadPoolManager;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>模拟web server<p>
 * @author sue
 * @createdate 2011-11-21
 */
public class HttpWebServer {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpWebServer.class);
	
	/**
	 * 启动控制台的监听线程
	 * @param flag	true，启动监听，false，不启动
	 */
	public static void startControlThread(boolean flag) {
		KeyBoardThread keyThread = new KeyBoardThread(flag);
		keyThread.start();
	}
	
	/**
	 * 接收客户端的请求
	 * @param manager	线程池的对象
	 * @throws IOException	打开套接字时可能发生I/O 错误
	 */
	public static void acceptClientRequest(ThreadPoolManager manager)
			throws IOException {
		ServerSocket server = new ServerSocket(8090);
		Socket socket = null;
		while (true) {
			logger.info("------------{}", StaticConstant.KEYBOARD_THREAD_FLAG);
			if (StaticConstant.KEYBOARD_THREAD_FLAG) { // false 表示exit
				socket = server.accept();
				manager.handleClientRequest(socket);
			} else {
				logger.info("--------------------------exit");
				break;
			}
		}
	}

	/**
	 * 
	 * @param socket
	 * @return
	 * @throws IOException	在创建输入流时发生 I/O 错误
	 */
	public static String getRequestFileName(InputStream in) throws IOException {
		InputStreamReader isr = null;
		BufferedReader read = null;
		String str = "";
		try{
			isr = new InputStreamReader(in);
			// 分析请求的信息
			read = new BufferedReader(isr, StaticConstant.BUFFERREADER_LEN);
			// 请求头的第一行为请求行：GET /d/test/ascII.htm HTTP/1.1 方法-URI-协议/版本
			str = read.readLine();
			logger.info("请求行：{}", str);
		}finally{
//			IOUtils.closeQuietly(read);
//			IOUtils.closeQuietly(isr);
		}
		return str.split(" ")[1].substring(1); // 请求行使用空格隔开，第二个为URI取得URI
	}
	
	public static void writeToClient(){
		
	}
	
	public static String listDirFiles(File file){
		if( file.isDirectory()){
			File files[] = file.listFiles();
			StringBuffer sbf = new StringBuffer(StaticConstant.STRINGBUFFER_LEN);
			if( null != files && files.length > 0){
				sbf.append("目录下的文件列表：<br/>");
				int i = 0;
				for (File fi : files) {
					sbf.append(CommonsMethod.judgeFileType(fi));
					sbf.append(++i);
					sbf.append(":");
					sbf.append(fi.getName());
					sbf.append("<br/>");
				}
				return sbf.toString();
			}else{
				return "目录下没有文件";
			}
			
		}else{
			return "此路径不为目录";
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
		boolean flag = true;
		if (null != socket) {
			logger.info("客户端ip：{}", socket.getInetAddress());
			OutputStream out = null;
			InputStream in = null;
			try {
				in = socket.getInputStream();
				// 得到文件的存放路径，value:d/test/ascII.htm
				// d盘下的test文件夹下的ascII.htm文件，如果为目录，则列出目录的文件
				// 获得URI
				String str = HttpWebServer.getRequestFileName(in);
				logger.info("文件路径:{}", str);
				str = str.replaceFirst("/", "://");
				File file = new File(str);
				String fileType = CommonsMethod.judgeFileType(file); // 判断文件类型
				out = socket.getOutputStream();
				if ("file".equals(fileType)) { // 文件，返回文件信息
					int len = (int) file.length();
					System.out.println("length:" + len);
					FileUtils.copyFile(file, out); // 使用apache提供的类FileUtils提供的方法，直接将文件写入到流中
				} else if ("directory".equals(fileType)) { // 目录，列出目录下的文件
					out.write(HttpWebServer.listDirFiles(file).getBytes("gbk"));
				} else { // 其他类型，返回404错误
					StringBuffer sbf = new StringBuffer(
							StaticConstant.STRINGBUFFER_LEN);
					sbf.append("路径不存在！<br/>");
					sbf.append("请尝试其他链接");
					out.write(sbf.toString().getBytes("gbk"));
					flag = false;
				}
			} finally {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(in);
			}
		}
		return flag;
	}
	
	
	public static void main(String args[]) {/*
		// 默认端口为8080
		int port = 8090;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = 8090;
			logger.info("输入端口参数不合法，默认端口为：8090");
			e.printStackTrace();
		} finally {
			try {
				new ThreadPoolManager(10 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	*/}
	
	
}
