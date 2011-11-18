/**
 * 
 */
package com.succez.wget.simulation;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
				"http://file12.mafengwo.net/M00/CC/CD/wKgBpU6EFk26MI5OAC7fiFssbEY047.rar";
//				"http://www.hao123.com/";
//		"http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java";
		
		String name = SimulateWget.judgeType(url);
		SimulateWget.simulateDown(url , name);
//		FileInfor file = new FileInfor();
//		file.setIn(in);
//		String type = SimulateWget.judgeType(url);
//		file.setFileName(name);
//		file.setContentBinary(bt);
//		SimulateWget.createFile(file);
	}

}
