package com.succez.study.simulationserver.thread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.succez.study.simulationserver.server.HandleProcess;

public class WriteThread implements Runnable{
	
	private SocketChannel channel;
	
	private String url = "c://";
	
	/**
	 * 读线程
	 * @param channel	已经被注册的通道
	 * @param url	请求路径，缺省值为C盘根目录
	 */
	public WriteThread(SocketChannel channel , String url){
		this.channel = channel;
		if (!(null == url || "".equals(url)))
			this.url = url;
	}

	public void run() {
		try {
			HandleProcess.getInstance().processWrite(channel, url);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
