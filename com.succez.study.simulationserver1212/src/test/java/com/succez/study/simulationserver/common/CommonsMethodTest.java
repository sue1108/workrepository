package com.succez.study.simulationserver.common;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommonsMethodTest {

	final static Logger logger = LoggerFactory.getLogger(CommonsMethodTest.class);
	@Test
	public void test() {
		logger.info("{}",CommonsMethod.getProjectURL());
		/*try {
			String strNull = CommonsMethod.judgeFileType(null);
			String str = CommonsMethod.judgeFileType(new File("123"));
			String dirNull;
			dirNull = CommonsMethod.listDirFiles(null);
			String dir = CommonsMethod.listDirFiles(new File("c:"));
			Object obj[] = {strNull,str,dirNull,dir};
			logger.info("{}-{}-{}",obj);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
