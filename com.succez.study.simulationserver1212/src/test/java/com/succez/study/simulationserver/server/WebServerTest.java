package com.succez.study.simulationserver.server;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class WebServerTest {

	@Test
	public void test() {
		WebServer server = WebServer.getInstance();
		try {
			server.initServer(8090);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
