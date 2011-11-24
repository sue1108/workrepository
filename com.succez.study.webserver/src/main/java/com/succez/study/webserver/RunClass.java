package com.succez.study.webserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.webserver.thread.ThreadPoolManager;

public class RunClass {
	
	private final static Logger logger = LoggerFactory
			.getLogger(RunClass.class);

	public static void main(String args[]) {
		try {
			ThreadPoolManager manager = new ThreadPoolManager(10, true);
			HttpWebServer.acceptClientRequest(manager);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
