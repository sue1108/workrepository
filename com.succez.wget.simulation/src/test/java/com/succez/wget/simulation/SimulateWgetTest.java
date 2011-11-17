/**
 * 
 */
package com.succez.wget.simulation;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.succez.wget.simulation.pojo.FileInfor;

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
				"http://www.google.com.hk/";
//		"http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java";
		
//		url.endsWith(".java");
		FileInfor file = SimulateWget.simulateDown(url);
		SimulateWget.createFile(file);
		
//		System.out.println(str);
//		fail("Not yet implemented");
	}

}
