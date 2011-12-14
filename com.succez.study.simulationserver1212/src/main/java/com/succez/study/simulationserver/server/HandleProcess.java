package com.succez.study.simulationserver.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.simulationserver.common.CommonsMethod;
import com.succez.study.simulationserver.common.StaticConstant;

public class HandleProcess {
	
	private final static Logger logger = LoggerFactory.getLogger(HandleProcess.class);
	
	private static HandleProcess instance = new HandleProcess();
	
	public static HandleProcess getInstance() {
		synchronized (instance) {
			if (null == instance)
				instance = new HandleProcess();
		}
		return instance;
	}
	
	/**
	 * read
	 * @param key	当有读事件发生时，传回得SelectionKey
	 * @param buffer	客户端的请求信息
	 * @throws Exception 
	 */
	public void processRead(SelectionKey key, ByteBuffer buffer) throws Exception {
		String url = handleData(buffer);
		key.attach(url);
		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	/**
	 * write
	 * @param channel
	 * @param url	
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void processWrite(SocketChannel channel , String url) throws FileNotFoundException, IOException{
		File file = new File(url);
		String fileType = CommonsMethod.judgeFileType(file);
//		 文件，返回文件信息
		if ("file".equals(fileType)) { 
			writeFile(channel, file);

//			 目录，列出目录下的文件
		} else if ("directory".equals(fileType)) { 
			writeData(channel, CommonsMethod.listDirFiles(file));

//			 其他类型，返回错误提示信息
		} else {
			writeData(channel, CommonsMethod.getErrorInfor());
//			writeData(channel, FileUtils.readFileToString(new File(StaticConstant.URL_ERROR)));
		}
	}
	
	/**
	 * handle request data 
	 * @param buffer	客户端的请求信息，	
	 * @return	客户端的请求路径，若buffer为null或者请求路径不符合规则，返回c盘根目录
	 * @throws Exception	请求方法为非GET，手动抛出的异常
	 */
	public String handleData(ByteBuffer buffer) throws Exception {
		buffer.flip();
		String tmpRout = new String(buffer.array(),"gbk");
		if (null == tmpRout || "".equals(tmpRout)) {
			return StaticConstant.URL_DEFAULT;
		} else {
			String rout[] = tmpRout.split(" ");
			if (rout.length > 1 && rout[1].length() > 1) {
//				GET /reg.jsp HTTP/ (CRLF)，如果请求方法为非get，手动抛出异常信息
				if (!rout[0].equals("GET"))
					throw new Exception("请求方法为：post或其他方法，抛出异常");
				
				tmpRout = rout[1].substring(1); // ig/jserror
				int index = tmpRout.indexOf("/");
//				确保请求路径的第一层为盘符
				if ((-1 != index)
						&& (1 == tmpRout.substring(0, index).length())) {
					tmpRout = tmpRout.replaceFirst("/", "://");
				} else {
					tmpRout = StaticConstant.URL_DEFAULT;
				}
			} else {
				tmpRout = StaticConstant.URL_DEFAULT;
			}
		}
//		 logger.info("客户端请求路径：{}", tmpRout);
		return tmpRout;
	}
	
	/**
	 * 客户端请求类型为文件类型，将文件内容回写入客户端
	 * 
	 * @param socket	已被注册且感兴趣事件为write的通道，若为null，直接return
	 * @param file	回写客户端的文件
	 * @throws IOException
	 * @throws FileNotFoundException
	 *             if the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading
	 */
	public void writeFile(SocketChannel socket, File file) throws IOException,
			FileNotFoundException {
		if (null == socket)
			return;
		FileInputStream fis = null;
		FileChannel fc = null;
//		long time = System.currentTimeMillis();
		try {
			int fileLen = (int) file.length();
			if (fileLen < 1)
				return;
			fis = new FileInputStream(file);
			fc = fis.getChannel();
			long fileSize = fc.size(); // 文件长度
			fc.transferTo(0, fileSize, socket);
			
		} finally {
//			logger.info("time:{}", System.currentTimeMillis() - time);
			IOUtils.closeQuietly(fis);
			fc.close();
			IOUtils.closeQuietly(socket);
		}
	}
	
	/**
	 * 将content内容回写入客户端
	 * 
	 * @param socket	已被注册且感兴趣事件为write的通道，若为null，直接return
	 * @param content
	 *            回写客户端的内容
	 * @throws IOException
	 * @throws UnsupportedEncodingException	If the named charset is not supported	
	 */
	public void writeData(SocketChannel socket, String content)
			throws IOException, UnsupportedEncodingException {
		if (null == socket)
			return;
		try {
			int len = 1024 * 2;
			int contentLen = content.length();// 回写内容的长度
			/**
			 * buffer.clear(); 
			 * buffer.put(content.getBytes("gbk"));
			 * buffer.flip(); 
			 * socket.write(buffer);
			 *  buffer.clear();
			 * 原来代码直接将内容put到ByteBuffer中。
			 * 当内容长度超过ByteBuffer长度后，抛出异常BufferOverflowException
			 * 
			 * 修改后代码，ByteBuffer的长度设置为1024*4，而每次只读取1024*2，有些字符在转换为byte后长度会变长
			 * 避免抛出异常BufferOverflowException
			 */
			int n = (contentLen - 1) / len + 1;
			ByteBuffer buffer = ByteBuffer.allocate(1024*4) ;
			int startIndex = 0;
			int endIndex = 0;
			for (int i = 0; i < n; i++) {
				buffer.clear();
				if (i == (n - 1)) {
					endIndex = contentLen;
				} else
					endIndex = startIndex + len;
				buffer.put(content.substring(startIndex, endIndex).getBytes("gbk"));
				startIndex =  endIndex;
				buffer.flip();
				socket.write(buffer);
			}
		} finally {
			IOUtils.closeQuietly(socket);
		}

	}

}
