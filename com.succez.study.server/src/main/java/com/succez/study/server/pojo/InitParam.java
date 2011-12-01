package com.succez.study.server.pojo;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class InitParam {

	private ServerSocketChannel ssc;

	private Selector selector;

	private SelectionKey key;

	private ThreadPoolExecutor threadPool;

	private ExecutorService service;

	public ExecutorService getService() {
		return service;
	}

	public void setService(ExecutorService service) {
		this.service = service;
	}

	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}

	public ServerSocketChannel getSsc() {
		return ssc;
	}

	public void setSsc(ServerSocketChannel ssc) {
		this.ssc = ssc;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public SelectionKey getKey() {
		return key;
	}

	public void setKey(SelectionKey key) {
		this.key = key;
	}

}
