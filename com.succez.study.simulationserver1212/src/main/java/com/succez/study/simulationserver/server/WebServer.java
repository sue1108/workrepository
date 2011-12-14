package com.succez.study.simulationserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.study.simulationserver.common.StaticConstant;
import com.succez.study.simulationserver.thread.KeyBoardThread;
import com.succez.study.simulationserver.thread.ReadThread;
import com.succez.study.simulationserver.thread.WriteThread;


public class WebServer {

	private final static Logger logger = LoggerFactory.getLogger(WebServer.class);
			
	private static WebServer instance = new WebServer();
	
	private ExecutorService service ;
	
	private Selector selector;
	
	private ServerSocketChannel serverSockerChannel;
	
	private SelectionKey selectionKey;

	public static WebServer getInstance() {
		synchronized (instance) {
			if (null == instance)
				instance = new WebServer();
		}
		return instance;
	}

	/**
	 * init web server<br/>
	 * A valid port value is between 5000 and 65535 , default value:8090
	 * @param port	the port value
	 * @throws IOException	If an I/O error occurs 
	 */
	public void initServer(int port) throws IOException {
		port = (port > 5000) && (port < 65535) ? port : 8090;
//		有效的进程数（向 Java 虚拟机返回可用处理器的数目*4）
		service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
		this.selector = Selector.open();
		
//		启动控制台的监听
		Thread thread = new Thread(new KeyBoardThread(this.selector));
		thread.start();
		
		this.serverSockerChannel = ServerSocketChannel.open();
//		设置为异步
		this.serverSockerChannel.configureBlocking(false);
//		绑定
		this.serverSockerChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
//		this.serverSockerChannel.socket().bind(new InetSocketAddress("localhost", port));
//		注册到Selector，设置为Accept
		this.selectionKey = this.serverSockerChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	/**
	 * start web server
	 * @throws IOException	If an I/O error occurs
	 */
	public void start() throws IOException {
		while (true) {
			if (0 == this.selector.selectNow()){
				if (!StaticConstant.KEYBOARD_THREAD_FLAG) {
					break;
				}
				continue;
			}
//			有事件发生，传回一组SelectionKey
			Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
			Iterator<SelectionKey> it = selectionKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				process(key);
			}
		}
		end();
	}
	
	/**
	 * 读取SelectionKey，获得已注册的SocketChannel，从SocketChannel中读取并处理数据
	 * @param key
	 * @throws IOException	If an I/O error occurs
	 */
	private void process(SelectionKey key) throws IOException {
//		accept a new Connection
		if (key.isAcceptable()) {
			logger.info("accept");
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
//			注册感兴趣事件为read
			channel.register(selector, SelectionKey.OP_READ);
		} else {
			SocketChannel channel = (SocketChannel) key.channel();
//			读事件
			if (key.isReadable()) {
				logger.info("read");
				ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
				buffer.clear();
				int lenData = channel.read(buffer);
				if (0 < lenData) {
					logger.info(" data");
					service.execute(new ReadThread(key, buffer));
				} else {
					logger.info("no data");
					key.cancel();
					IOUtils.closeQuietly(channel);
				}
				logger.info("read end");
//				写事件
			} else if (key.isWritable()) {
				logger.info("write");
				String url = (String) key.attachment();
				service.execute(new WriteThread(channel, url));
				key.cancel();
			}
		}
	}
	
	/**
	 * end web server
	 * @throws IOException If an I/O error occurs
	 */
	public void end() throws IOException {
		try {
			logger.info(" web server end");
//			 需要先取消注册
			this.selectionKey.cancel();
			IOUtils.closeQuietly(this.serverSockerChannel);
			this.selector.close();
		} finally {
			System.exit(0);
		}
	}

}
