/**
 * 
 */
package com.succez.study.webserver.thread;

import java.net.Socket;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.webserver.HttpWebServer;
import com.succez.study.webserver.common.StaticConstant;

/**
 * <p>
 * Copyright: Copyright (c) 2011
 * <p>
 * <p>
 * 线程池
 * <p>
 * 
 * @author sue
 * @createdate 2011-11-22
 */
public class ThreadPoolManager {
	private final static Logger logger = LoggerFactory.getLogger(ThreadPoolManager.class);
//			CommonsMethod.getLoggerObject(ThreadPoolManager.class);
			
	/**
	 * 默认最大线程数为10个
	 */
	private int maxThread = StaticConstant.MAX_THREAD;

	/**
	 * 线程池中的线程可能去执行不同的类型的thread，所以不使用泛型
	 */
	private Vector vector = null;

	/**
	 * <p>
	 * 初始化线程池
	 * </p>
	 * 
	 * @param threadCount
	 *            线程池中的线程数量,默认为20个线程
	 * @param flag
	 *            是否启动对控制台的监控，true：启动，false：不启动
	 * @throws InterruptedException
	 *             线程在活动前或活动期间处于等待、休眠或占用状态下，该线程被中断时，抛出异常
	 */
	public ThreadPoolManager(int threadCount , boolean flag) throws InterruptedException {
		if( flag )
			HttpWebServer.startControlThread(flag);//启动控制台的监听线程
		if (threadCount > 0) {
			this.maxThread = threadCount;
		}
		this.vector = new Vector(maxThread);
		// 初始化线程池，在线程池中循环创建threadCount个线程,，启动线程，并让这些线程都处于睡眠状态
		for (int i = 0; i < maxThread; i++) {
			RequestThread thread = new RequestThread();
			thread.setIndex(i);	//设置线程的索引
			vector.addElement(thread);
			thread.start();
		}
	}

	/**
	 * <p>
	 * 处理客户端的请求，如果线程池中有线程处于wait状态，则唤醒该线程，去执行客户端的请求
	 * </p>
	 * 
	 * @param socket
	 *            客户端的socket
	 */
	public void handleClientRequest(Socket socket) {
		if( StaticConstant.KEYBOARD_THREAD_FLAG ){//false 表示exit
			int index = 0;
			for (; index < maxThread; index++) {
				RequestThread thread = (RequestThread) vector.elementAt(index);
				if (!thread.isStateFlag()) { // stateFlag值为false时，线程为wait状态，则唤醒线程，执行客户端请求
					logger.info("Thread {} 被唤醒，执行客户端的请求......", (index));
					thread.setSocket(socket);
					thread.setStateFlag(true); // 状态设置为true，唤醒线程
					return;
				}
			}
			if (index == maxThread) // 线程池的线程全部被占用，则提示客户端需要等待
				logger.info("线程池已满，等待......");
		}
		logger.info("exit--------------------------");

	}

	/**
	 * <p>
	 * 处理客户端的请求，如果线程池中有线程处于wait状态，则唤醒该线程，去执行客户端的请求
	 * </p>
	 * 
	 * @param str
	 *            键盘输入的值
	 */
	public void handleKeyboardRequest(String str) {
		int index = 0;
		for (; index < maxThread; index++) {
			RequestThread thread = (RequestThread) vector.elementAt(index);
			if (!thread.isStateFlag()) { // 为wait状态，则唤醒线程，执行客户端请求
				logger.info("Thread {} 被唤醒，执行键盘输入的信息......", (index + 1));
				thread.setStateFlag(true); // 状态设置为true，唤醒线程
				return;
			}
		}
		if (index == maxThread)
			logger.info("线程池已满，等待......");

	}

}
