package com.succez.study.webserver.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>监听控制台的输入信息<p>
 * <p>如果返回false，则表示退出应用程序</p>
 * @author sue
 * @createdate 2011-11-24
 */
public class ControlThread implements Callable<Boolean>{

	private final static Logger logger = LoggerFactory.getLogger(ControlThread.class);
	
	public Boolean call() throws Exception {
		boolean flag = true;
		logger.info("控制台监听  thread  is running......");
		// 监听控制台的输入信息
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String controlInfor; // 记录控制台输入的信息
		try {
			while (null != (controlInfor = br.readLine())) {
				if ("exit".equals(controlInfor)) {
					flag = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("--------------------------准备退出应用程序-------------------------");
		return flag;
	}

}
