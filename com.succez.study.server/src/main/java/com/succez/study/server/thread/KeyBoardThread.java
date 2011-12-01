/**
 * 
 */
package com.succez.study.server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.server.common.StaticConstant;


/**
 * <p>
 * Copyright: Copyright (c) 2011
 * <p>
 * <p>
 * 监听控制台的输入信息
 * <p>
 * 
 * @author sue
 * @createdate 2011-11-23
 */
public class KeyBoardThread extends CommonsThread {

	private final static Logger logger = LoggerFactory
			.getLogger(KeyBoardThread.class);

	/**
	 * 
	 * @param state
	 *            设置线程的状态，若设置为true，则启动线程，否则，线程一直处于等待状态
	 */
	public KeyBoardThread(boolean state) {
		super.stateFlag = state;
	}

	public void run() {
		if (super.stateFlag) {
			logger.info("control  thread  is running......");
			// 监听控制台的输入信息
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String controlInfor; // 记录控制台输入的信息
			try {
				while (null != (controlInfor = br.readLine())) {
					if ("exit".equals(controlInfor)) {
						logger.info("......waiting for exit......");
						StaticConstant.KEYBOARD_THREAD_FLAG = false;
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("control  thread  is end......");
	}
}
