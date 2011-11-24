package com.succez.study.webserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

public class ThreadPoolManagerTest {

	@Test
	public void test() throws IOException, InterruptedException {/*
		
		int port = 8090;
        ThreadPoolManager manager = new ThreadPoolManager(10 ,true);   
        ServerSocket server = new ServerSocket(port);
//        System.out.println(server.getReceiveBufferSize());
//        server.setReceiveBufferSize(size);
        Socket socket = null;
        
        while(true){
        	
        	
        	socket = server.accept();
//        	socket.setSendBufferSize(65536);
        	
//        	System.out.println(socket.getTcpNoDelay());
//        	System.out.println(socket.getSoTimeout());
//        	System.out.println("send:"+socket.getSendBufferSize());
//        	System.out.println("re:"+socket.getReceiveBufferSize());
        	manager.handleClientRequest(socket);    
//        	System.out.println("----------------------------socket--------------------------------------------------");
        }
        
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String s;
//       while((s = br.readLine()) != null)
//        {
//          manager.handleRequest(s);
//        }


	*/}

}
