/**
 * 
 */
package com.succez.wget.simulation;


import java.io.IOException;

import org.junit.Test;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>测试模拟wget工具类<p>
 * @author sue
 * @createdate 2011-11-15
 */
public class SimulateWgetTest {

	@Test
	public void test() {
		String url = 
				"http://dev.succez.com/download/SuccezIDE/eclipse/eclipse-java-helios-SR1-win32.zip";
//				"http://dev.succez.com/download/software/Snagit%2010.rar";
//				"http://file12.mafengwo.net/M00/CC/CD/wKgBpU6EFk26MI5OAC7fiFssbEY047.rar";
//				"http://www.hao123.com/";
//		"http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java";
		
		String name = SimulateWget.judgeType(url);
		try {
			SimulateWget.simulateDown(url , name);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
