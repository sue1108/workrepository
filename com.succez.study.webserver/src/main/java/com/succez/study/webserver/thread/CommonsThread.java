/**
 * 
 */
package com.succez.study.webserver.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>线程，继承了Thread<br/>
 * 包含一个boolean型属性：stateFlag，表示线程的运行状态<br/>
 * 如果值为true，则表示线程正在被占用，如果为false，表示线程状态为wait
 * </p>
 * @author sue
 * @createdate 2011-11-21
 */
public class CommonsThread extends Thread {

	private final static Logger logger = LoggerFactory.getLogger(CommonsThread.class);
	
	/**
	 * 线程的运行状态，如果run则状态为true，如果为wait，则状态为false,默认状态为false
	 */
	protected boolean stateFlag = false;
	
	protected int index ;

	public int getIndex() {
		return index;
	}

	/**
	 * 线程的索引
	 * @param index	线程的索引,从0开始
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 获得线程的运行状态，2种状态：true表示线程被占用，false表示线程处于等待，默认状态为false
	 * 
	 * @return 如果线程被占用，返回true，如果线程处于wait状态，返回false
	 */
	public boolean isStateFlag() {
		logger.info("线程 {} 的状态：{}",index, stateFlag);
		return stateFlag;
	}

	/**
	 * 设置线程的运行状态，2种状态：true表示线程被占用，false表示线程处于等待，默认状态为false
	 * 
	 * @param stateFlag
	 *            设置为false，使线程处于等待状态；
	 *            设置为true，表示线程被占用
	 */
	public void setStateFlag(boolean stateFlag) {
		this.stateFlag = stateFlag;
		if (stateFlag) {
			synchronized (this) {
				this.notify();
			}
		}
	}
	
	

	/**
	 * 初始化线程池，当stateFlag为false时,将线程状态设置为wait
	 * 
	 * @throws InterruptedException
	 *             线程在活动前或活动期间处于等待、休眠或占用状态下，该线程被中断时，抛出异常
	 */
	public void setThreadInitState() throws InterruptedException {
		logger.info("设置线程{}的初始状态为wait",index);
		if (!stateFlag)
			synchronized (this) {
				this.wait();
			}
	}

}
