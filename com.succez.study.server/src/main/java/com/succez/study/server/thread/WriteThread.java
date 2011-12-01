package com.succez.study.server.thread;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.succez.study.server.HttpWebServer;


public class WriteThread implements Runnable{

	private SelectionKey sk = null;
	
	public WriteThread(SelectionKey sk){
		this.sk = sk;
	}

	public void run() {
		try {
			new HttpWebServer().write(sk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
