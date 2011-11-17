package com.succez.wget.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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
	 * @param url  文件路径
	 * @return  返回需要下载文件的内容，如果url为空或者异常，则返回null
	 */
	public static FileInfor simulateDown(String url){
		FileInfor file = null;
		if( null != url && !"".equals(url) && url.trim().length() >0 ){
			file = new FileInfor();
			file.setType(SimulateWget.judgeType(url));
			file.setFileName("test");
			
//			1、创建HttpClient的对象
			HttpClient client = new DefaultHttpClient();
//			2、创建post连接方法的对象，使用post连接方式报错
//			HttpPost post = new HttpPost(url);
			HttpGet get = new HttpGet(url);
			System.out.println(get.getURI());
//			3、调用创建好的HttpClient对象的 execute方法来执行创建好的HttpPost对象
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
            	String str = client.execute(get, responseHandler);
//            	System.out.println("1:"+str);
            	file.setContent( str );
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
//				不管是否成功，都释放连接
				client.getConnectionManager().shutdown();
			}
			
		}

		return file;
	}
	
	
	/**
	 * <p>解析url地址，返回文件的后缀类型</p>
	 * @param url	url地址
	 * @return		如果url为指定文件格式，则返回文件的后缀类型，否则返回空字符串
	 */
	public static String judgeType(String url){
		int tmpLastD = url.lastIndexOf(".");
		if( tmpLastD !=-1 ){
			String type = url.substring(tmpLastD+1, url.length());
			if( type.matches("(" + StaticConstant.FILE_TYPE + ")") )   //匹配文件类型
				return type;
		}
		return "txt";
	}
	

/**
 * <p>在本地创建文件，默认存放在c盘</p>
 * @param fileInfor   FileInfor的对象，存储的是文件内容，文件名称及文件类型
 * @return  返回创建文件的相关信息
 */
	public static String createFile(FileInfor fileInfor){
//		File file = new File("c:\\"+fileInfor.getFileName()+"."+fileInfor.getType());
//		FileOutputStream fop = null;
//		Writer write = null;
//		FileWriter write = null;
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(new File("c:\\"+fileInfor.getFileName()+"."+fileInfor.getType()).getAbsoluteFile());
//			out.print(fileInfor.getContent());
			out.write(fileInfor.getContent());
			out.flush();
//			out.print("a啊asf]奥斯丁");
//			out.write("b啊");
/*			write = new FileWriter("c:\\"+fileInfor.getFileName()+"."+fileInfor.getType());
			System.out.println(write.getEncoding());
			write.write(fileInfor.getContent());
			write.flush();
*//*			fop = new FileOutputStream(file);
			write = new PrintWriter(file);
					new OutputStreamWriter(fop , "utf-8") ;//处理16位,文件可能包含中文
			write.write(fileInfor.getContent());*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.close();
/*			try {
				if( write != null )
					write.close();
//				if( fop != null)
//					fop.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			
		}
//		String str = "";
		return "";
	}

}
