package com.succez.study.simulationserver.thread;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.succez.study.simulationserver.server.HandleProcess;

public class ReadThread implements Runnable{

	private SelectionKey key;

	private ByteBuffer buffer;
	
	/**
	 * 写线程
	 * @param key	当有读事件发生时，传回得SelectionKey
	 * @param buffer	客户端的请求信息
	 */
	public ReadThread(SelectionKey key, ByteBuffer buffer) {
		this.key = key;
		this.buffer = buffer;
	}

	public void run() {
		HandleProcess.getInstance().processRead(key, buffer);
	}

}
