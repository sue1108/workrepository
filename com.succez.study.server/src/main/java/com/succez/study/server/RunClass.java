package com.succez.study.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.server.pojo.InitParam;

public class RunClass {
	
	private final static Logger logger = LoggerFactory
			.getLogger(RunClass.class);

	public static void main(String args[]) {
		try {
//			ThreadPoolManager pool = new ThreadPoolManager(10);
			InitParam param  = HttpWebServer.init(true, 8090);
			HttpWebServer server = new HttpWebServer();
			int i = 0;
			while(true){
				logger.info("start:{}",++i);
				server.start(param);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
