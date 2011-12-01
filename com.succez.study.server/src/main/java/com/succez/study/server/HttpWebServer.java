package com.succez.study.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

import com.succez.study.server.common.CommonsMethod;
import com.succez.study.server.common.StaticConstant;
import com.succez.study.server.pojo.InitParam;
import com.succez.study.server.thread.KeyBoardThread;
import com.succez.study.server.thread.ReadThread;
import com.succez.study.server.thread.WriteThread;

/**
 * 
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>模拟web server<p>
 * @author sue
 * @createdate 2011-12-1
 */
public class HttpWebServer {

	protected final static Logger logger = LoggerFactory.getLogger(HttpWebServer.class);

	
	/**
	 * 初始化：打开一个选择器，并打开一个ServerSocketChannel，设置为异步IO，并绑定端口号：port<br/>
	 * 使用已被打开的选择器Selector对象，注册已被打开的ServerSocketChannel
	 * @param flag	启动控制台的监听线程参数，true：启动监听、false：不启动
	 * @param port	监听端口，范围在5001,-65535之间，默认为8090
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static InitParam init(boolean flag , int port) throws IOException, InterruptedException{
		logger.info("init start");
		InitParam param = new InitParam();
		if (flag) { // 启动控制台的监听
			KeyBoardThread keyThread = new KeyBoardThread(flag);
			keyThread.start();
		}
		/*ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2,
				20, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), 
				new ThreadPoolExecutor.DiscardOldestPolicy());*/
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10);
		param.setService(service);
		port = (port > 5000) && (port < 65535) ? port : 8090;
		Selector selector = Selector.open(); // 打开一个选择器
		param.setSelector(selector);
		param = HttpWebServer.openServerSocketChannel(port, param);
		logger.info("init end");
		return param;
	}
	

	/**
	 * 打开一个ServerSocketChannel，设置为异步IO，并绑定端口号：port<br/>
	 * 使用已被打开的选择器Selector对象，注册已被打开的ServerSocketChannel
	 * @param port	绑定的端口号
	 * @param selector	已被打开的选择器
	 * @return	返回一个键值
	 * @throws IOException	打开服务器套接字通道，或设置为异步，或绑定端口号，发生IO异常
	 */
	public static InitParam openServerSocketChannel( int port , InitParam param) throws IOException {
		Selector selector = param.getSelector();
		ServerSocketChannel ssc = ServerSocketChannel.open(); // 打开服务器套接字通道
		ssc.configureBlocking(false); // 设置为异步IO
		// ssc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(),port)); // 绑定端口号
		ssc.socket().bind(new InetSocketAddress("localhost", port)); // 绑定端口号
		// register the newly opened ServerSocketChannels with our Selector
		SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT); 
		param.setSsc(ssc);
		param.setKey(key);
		return param;
	}

	/**
	 * 单一程序
	 * @param param	初始化的参数
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void start(InitParam param) throws IOException , InterruptedException {
		
		Selector selector = param.getSelector();
		int num = 0;
		while (true) {
			if (!StaticConstant.KEYBOARD_THREAD_FLAG) {
				break;
			} else if( StaticConstant.KEYBOARD_THREAD_FLAG ){
				/**
				 * 此处使用selectNow：启动了控制台监听，当控制台输入exit表示退出时， 需要再有一次请求，才会真正退出
				 * ，原因是--使用了select，程序一直处于阻塞，除非调用wakeup，使阻塞立即返回，
				 * 但是在程序执行的过程中，并不知道何时eixt时处于阻塞状态；
				 * 而使用selectNow，非阻塞模式下，当监听到exit后，立即能退出
				 */
				// num = selector.select();此方法处于阻塞模式的选择操作
				num = selector.selectNow(); // 此方法处于非阻塞模式的选择操作
				if (num <= 0) {
					if (!StaticConstant.KEYBOARD_THREAD_FLAG)
						break;
				} else if (num > 0) {
					this.loopWaitRequest(param);
//					logger.info("-----------num:{}", num);
				}
			}
		}
		this.end(param);

	}
	

	
	public  void end(InitParam param) throws IOException{
//		需要先取消注册
		param.getKey().cancel();
		IOUtils.closeQuietly(param.getSsc());
		param.getSelector().close();
		System.out.println("end listen port");
		System.exit(0);
	}


	/**
	 * 
	 * @param param	初始化后的参数
	 * @throws IOException	
	 */
	public void loopWaitRequest(InitParam param) throws IOException {
		Selector selector = param.getSelector();
		Set selectKeys = selector.selectedKeys(); // 返回keys
		Iterator it = selectKeys.iterator(); // 遍历
		while (it.hasNext()) {
			try {
				SelectionKey sk = null;
				sk = (SelectionKey) it.next();
				it.remove();
				
				if ((sk.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {// Listening for new connections
					this.listenNewConnections(sk, selector);// // 接受请求,设置为read accept new connection
					
				} else {
					if ((sk.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {// Incoming I/O
						read(sk, selector); //读数据，如果有数据，设置为write
//						param.getService().execute(new ReadThread(sk, selector)); // 线程池

					} else if ((sk.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
						write(sk); // 写数据
//						param.getService().execute(new WriteThread(sk)); // 线程池
					}
				}
			} finally {}
		}
	}
	
	/**
	 * 读
	 * @param sk
	 * @param selector
	 * @return
	 * @throws IOException
	 */
	public boolean read(SelectionKey sk , Selector selector) throws IOException{
		SocketChannel readySc = (SocketChannel) sk.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024*4);
		buffer.clear();
		int num = readySc.read(buffer);
		if( num <= 0){
			logger.info("no data");
			IOUtils.closeQuietly(readySc);
		}else if ( num > 0) { // 如果有请求，则读数据
			String url = this.readData(buffer); // 取得请求路径
			SelectionKey key = readySc.register(selector, SelectionKey.OP_WRITE);
			key.attach(url);
			
//			key.cancel();
		}
		return true;
	}
	
	/**
	 * 写
	 * @param sk
	 * @return
	 * @throws IOException
	 */
	public boolean write(SelectionKey sk) throws IOException{
		SocketChannel readySc = (SocketChannel) sk.channel();
		String url = (String) sk.attachment();
		File file = new File(url);
		String fileType = CommonsMethod.judgeFileType(file);
		sk.cancel();
		if ("file".equals(fileType)) { // 文件，返回文件信息
			this.writeFile(readySc, file);
			
		} else if ("directory".equals(fileType)) { // 目录，列出目录下的文件
			this.writeData(readySc, CommonsMethod.listDirFiles(file));
			
		} else { // 其他类型，返回404错误
			StringBuffer sbf = new StringBuffer(StaticConstant.STRINGBUFFER_LEN);
			sbf.append("路径不存在！<br/>");
			sbf.append("请尝试其他链接");
			this.writeData(readySc, sbf.toString());
		}
		return true;
	}

	public  void listenNewConnections(SelectionKey sk, Selector selector)
			throws IOException {
		// accept new connection
		ServerSocketChannel readySsc = (ServerSocketChannel) sk.channel();
		SocketChannel readySc = readySsc.accept();
		readySc.configureBlocking(false);
		readySc.register(selector, SelectionKey.OP_READ);
	}

	public  String readData(ByteBuffer buffer) throws IOException {
		buffer.flip();
		byte bt[] = new byte[buffer.limit()];
		buffer.get(bt);
		String tmpRout = new String(bt);
		if( null != tmpRout && !"".equals(tmpRout)){
			String rout[] = tmpRout.split(" ");
			if( rout.length > 1 && rout[1].length() > 1){
				tmpRout = rout[1].substring(1);		// 	ig/jserror
				int index = tmpRout.indexOf("/");
				if( -1 != index){
					if( tmpRout.substring(0, index).length() == 1)
						tmpRout = tmpRout.replaceFirst("/", "://");
				}
			}
		}
		logger.info("客户端请求路径：{}",tmpRout);
		return tmpRout;
	}

	

	public  void writeData(SocketChannel readySc , String content) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024*4);
		buffer.clear();
		buffer.put(content.getBytes("gbk"));
		buffer.flip();
		readySc.write(buffer);
		buffer.clear();
		IOUtils.closeQuietly(readySc);
	}
	
	public  void writeFile(SocketChannel readySc , File file)  throws IOException {
		int fileLen = (int) file.length();
		if( fileLen < 1)
			return;
		FileInputStream fis = new FileInputStream(file);
		FileChannel fc = fis.getChannel();
		long fileSize = fc.size();
		int length = 1024*4;
		int n = (int) ((fileSize - 1)/length + 1);
		int position = 0;
		logger.info("length:{},n:{}",fileSize,n);
		long time = System.currentTimeMillis();
		int i = 0;
		for( ; i < n ; i++){
			fc.transferTo(position, length, readySc);
			position += length;
			if( i == (n-1)){
				length = (int) (fileSize - (n-1)*length);
			}
			logger.info("i:{},length:{}",i , length);
		}
		if( i == n){
			System.out.println(System.currentTimeMillis()-time);
			IOUtils.closeQuietly(fis);
			fc.close();
			IOUtils.closeQuietly(readySc);
		}
		
	}
}
