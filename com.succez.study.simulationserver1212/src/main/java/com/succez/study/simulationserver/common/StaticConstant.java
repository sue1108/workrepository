package com.succez.study.simulationserver.common;

import java.nio.ByteBuffer;


/**
 * 此类用来存放全局的静态常量
 * @author sue
 * @createdate 2011-11-9
 */
public class StaticConstant {
	
	/**
	 * 监听控制台的参数，默认情况为true，若设置为false，表示退出程序
	 */
	public static boolean KEYBOARD_THREAD_FLAG = true;
	
	public static boolean END_FLAG[] = {false};

	/**
	 * 初始化BufferReader对象默认的存储空间
	 */
	public static int BUFFERREADER_LEN = 8;

	/**
	 * 创建List对象的时候初始容量为5
	 */
	public static int LIST_LEN = 5;
	
	/**
	 * 创建vector对象的时候初始容量为10
	 */
	public static int VECTOR_LEN = 10;

	/**
	 * 初始化StringBuffer对象默认的存储空间
	 */
	public static int STRINGBUFFER_LEN = 8;
	
	/**
	 * 初始化线程池中的线程数量
	 */
	public static int MAX_THREAD = 20;
	
	public static ByteBuffer BUFFER = ByteBuffer.allocate(1024*4);
	
	public static int CONTROL_INDEX = 0;
	
	/**
	 * 文件列表内容路径
	 */
	public static String URL_CONTENT = CommonsMethod.getProjectURL()+"/webserver/content.txt";
	
	/**
	 * 访问路径不存在，错误提示文件路径
	 */
	public static String URL_ERROR = CommonsMethod.getProjectURL()+"/webserver/error.txt";
	
	/**
	 * 目录下没有文件
	 */
	public static String URL_NO_FILE = CommonsMethod.getProjectURL()+"/webserver/nofile.txt";
	
	/**
	 * 默认盘符
	 */
	public static String URL_DEFAULT = "c://";
	
	
}
