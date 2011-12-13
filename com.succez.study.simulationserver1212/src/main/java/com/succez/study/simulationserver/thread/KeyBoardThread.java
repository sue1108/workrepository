/**
 * 
 */
package com.succez.study.simulationserver.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.Selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.simulationserver.common.StaticConstant;


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
public class KeyBoardThread implements Runnable {

	private final static Logger logger = LoggerFactory
			.getLogger(KeyBoardThread.class);

	private Selector selector;
	
	/**
	 * 控制台的监听，若控制台输入exit，使堵塞在select的线程立即返回
	 * @param selector
	 */
	public KeyBoardThread(Selector selector){
		this.selector = selector;
	}
	
	public void run() {
		logger.info("control  thread  is running......");
		// 监听控制台的输入信息
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
			logger.info("监听控制台的输入信息抛出异常：");
			e.printStackTrace();
		}
		logger.info("control  thread  is end......");
		selector.wakeup();
	}
}
