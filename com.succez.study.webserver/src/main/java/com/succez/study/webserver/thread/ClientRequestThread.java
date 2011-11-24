package com.succez.study.webserver.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.webserver.HttpWebServer;

public class ClientRequestThread extends CommonsThread implements Callable<Boolean>{
	
private final static Logger logger = LoggerFactory.getLogger(ClientRequestThread.class);
	
	private Socket socket = null;
	
	private int index ;

	public int getIndex() {
		return index;
	}

	/**
	 * 线程的索引
	 * @param index	线程的索引
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Boolean call() throws Exception {
		try {
			super.setThreadInitState();//设置线程初始状态为wait
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		
		try {
			while (true) {
				boolean flag = false;
				if (super.stateFlag) {
					logger.info("thread is running......");
					flag = HttpWebServer.returnClientInfor(socket);
/*					if (flag)
						logger.info("响应成功!");
					else
						logger.info("响应失败!");*/
					super.stateFlag = false;
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}

}
