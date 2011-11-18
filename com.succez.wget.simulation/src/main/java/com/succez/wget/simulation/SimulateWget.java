package com.succez.wget.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.succez.wget.simulation.common.StaticConstant;
import com.succez.wget.simulation.pojo.FileInfor;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>模拟wget工具<p>
 * @author sue
 * @createdate 2011-11-15
 */
public class SimulateWget {
	
	/**
	 * <p>取得文件内容，路径为：url</p>
	 * @param url	文件路径
	 * @param file	文件名
	 */
	public static void simulateDown(String url ,String file){
		if( null != url && !"".equals(url) && url.trim().length() >0 ){
//			1、创建HttpClient的对象
			HttpClient client = new DefaultHttpClient();
//			2、创建post连接方法的对象，使用post连接方式报错
			HttpGet get = new HttpGet(url);
//			3、调用创建好的HttpClient对象的 execute方法来执行创建好的HttpPost对象
			InputStream in = null;
			OutputStream out = null;
			HttpResponse response = null;
			try {
				response = client.execute(get); 
				HttpEntity entity = response.getEntity();  //得到实体
				if (entity != null) {
					in = entity.getContent();
					out = new FileOutputStream(new File("c:\\"+file));
					System.out.println("1:"+new Date());
					int tmp = 0;
					byte b[] = new byte[1024];
					while( (tmp = in.read(b))!= -1 ){
						out.write(b,  0 , tmp);
						out.flush();
					}
					
					/*//单个字节写入，速度很慢
					 * while( (tmp = in.read())!= -1){
						out.write(tmp);
					}
					out.flush();*/
					System.out.println("2:"+new Date());
				}
			}catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if( out != null)
						out.close();
					if( in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
//					不管是否成功，都释放连接
					client.getConnectionManager().shutdown();
				}
			}
		}
		
	}
	
	public static void simulateDownOld(String url ,String file){
		if( null != url && !"".equals(url) && url.trim().length() >0 ){
//			1、创建HttpClient的对象
			HttpClient client = new DefaultHttpClient();
//			2、创建post连接方法的对象，使用post连接方式报错
			HttpGet get = new HttpGet(url);
//			3、调用创建好的HttpClient对象的 execute方法来执行创建好的HttpPost对象
			HttpResponse response = null;
			InputStream in = null;
			OutputStream out = null;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();  //得到实体
				 if (entity != null) {   
					 in = entity.getContent();
					 out = new FileOutputStream(new File("c:\\"+file));
					 //单个字节写入，速度很慢
					 int tmp = 0;
					 while( (tmp = in.read())!= -1){
						 out.write(tmp);
					 }
					 out.flush();
//					 一次读入多个字节
//					 byte bt[] = new byte[1024*1024];
//					 int dataLen = (int) entity.getContentLength();
/*					 int len = bt.length;
					 if(dataLen < len)
						 len = dataLen;
					int tmp = 0;
					System.out.println("time:"+new SimpleDateFormat("hhMMss").format(new Date()));
					while( ( tmp = in.read(bt, 0, len)) != -1 ){
							 out.write(bt,0,len);
							 out.flush();
//						 }
					 }
					System.out.println("time:"+new SimpleDateFormat("hhMMss").format(new Date()));*/
					 
/*					 byte b[] = new byte[(int) entity.getContentLength()];
					 in.read(b);
					 out.write(b);*/
				 }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if( out != null)
						out.close();
					if( in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
//					不管是否成功，都释放连接
					client.getConnectionManager().shutdown();
				}
			}
		}
		
	}
	
	/**
	 * <p>解析url地址，返回文件的后缀类型</p>
	 * @param url	url地址
	 * @return		如果url为指定文件格式，则返回文件名称，否则返回以当前时间命名文件
	 */
	public static String judgeType(String url){
//		http://file12.mafengwo.net/M00/CC/CD/wKgBpU6EFk26MI5OAC7fiFssbEY047.rar
		int tmpLastD = url.lastIndexOf(".");
		if( tmpLastD !=-1 ){
			String type = url.substring(tmpLastD+1, url.length());
			int index = url.lastIndexOf("/");
			String fileName = "";
			if( type.matches("(" + StaticConstant.FILE_TYPE + ")") && index != -1) {  //匹配文件类型
				fileName = url.substring(index+1, url.length());  //文件名
				return fileName;
			}
		}
		return  new SimpleDateFormat("yyMMdd_hhmmss").format(new Date())+".html";
	}
	
	
	/**
	 * <p>在本地创建文件，默认存放在c盘</p>
	 * @param fileInfor   FileInfor的对象，存储的是文件内容，文件名称及文件类型
	 * @return  返回创建文件的相关信息
	 */
		public static String createFile(FileInfor file){
			OutputStream out = null;
			try {
				InputStream in = file.getIn();
				out = new FileOutputStream(new File("c:\\"+file.getFileName()));
				 /*//单个字节写入，速度很慢
				 int tmp = 0;
				 while( (tmp = in.read())!= -1){
					 out.write(tmp);
				 }*/
//				 一次读入多个字节
				 int off = 0;//偏移量
				 int len = 1024*1024;
				 byte bt[] = new byte[len];
				 int dataLen = 0;
				 while( (dataLen = in.read(bt, off, len)) != -1 ){
					 if( dataLen < len )
						 len = dataLen;
					 out.write(bt, off, len);
					 off += len;
					 out.flush();
				 }
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "";
		}

}
