/**
 * 
 */
package com.succez.study.webserver.thread;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.webserver.HttpWebServer;
import com.succez.study.webserver.common.StaticConstant;

/**
 * <p>
 * Copyright: Copyright (c) 2011
 * <p>
 * <p>
 * 多线程处理用户的请求
 * <p>
 * 
 * @author sue
 * @createdate 2011-11-21
 */
public class RequestThread extends CommonsThread {

	private final static Logger logger = LoggerFactory.getLogger(RequestThread.class);
	
	private Socket socket = null;

/*	*//**
	 * 线程的运行状态，如果run则状态为true，如果为wait，则状态为false,默认状态为false
	 *//*
	private boolean stateFlag = false;*/

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			super.setThreadInitState();// 设置线程初始状态为wait
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			while (true) {
				if (super.stateFlag) {
					logger.info("thread {} is running......",super.index);
					boolean flag = HttpWebServer.returnClientInfor(socket);
					if (flag)
						logger.info("响应成功!");
					else
						logger.info("响应失败!");
					super.stateFlag = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
