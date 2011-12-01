package com.succez.study.server.common;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.server.HttpWebServer;

public class CommonsMethodTest {

	final static Logger logger = LoggerFactory.getLogger(HttpWebServer.class);
	@Test
	public void test() {
		String strNull = CommonsMethod.judgeFileType(null);
		String str = CommonsMethod.judgeFileType(new File("123"));
		String dirNull = CommonsMethod.listDirFiles(null);
		String dir = CommonsMethod.listDirFiles(new File("c:"));
		Object obj[] = {strNull,str,dirNull,dir};
		logger.info("{}-{}-{}",obj);
	}

}
