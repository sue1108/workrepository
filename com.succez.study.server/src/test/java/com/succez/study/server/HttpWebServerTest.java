package com.succez.study.server;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.server.pojo.InitParam;

public class HttpWebServerTest {

	private final static Logger logger = LoggerFactory
			.getLogger(HttpWebServerTest.class);
	
	@Test
	public void test() {
		try {
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
