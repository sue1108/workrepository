package com.succez.study.webserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCase {

	@Test
	public void test() {
		String str = "c.htm";
		str = str.replaceFirst("/", "://");
		System.out.println(str);
	}

}
