package com.succez.study.server.thread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.server.HttpWebServer;


public class ReadThread implements Runnable{

	protected final Logger logger = LoggerFactory.getLogger(ReadThread.class);

	private SelectionKey sk = null;

	private Selector selector = null;

	public ReadThread( SelectionKey sk , Selector selector){
		this.sk = sk;
		this.selector = selector;
	}

	public void run() {
		try {
			new HttpWebServer().read(sk, selector);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
