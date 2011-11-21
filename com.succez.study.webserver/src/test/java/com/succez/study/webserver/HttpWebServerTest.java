/**
 * 
 */
package com.succez.study.webserver;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>测试模拟web server<p>
 * @author sue
 * @createdate 2011-11-21
 */
public class HttpWebServerTest {

	@Test
	public void test() {
		try {
			HttpWebServer.httpServer(8090);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
