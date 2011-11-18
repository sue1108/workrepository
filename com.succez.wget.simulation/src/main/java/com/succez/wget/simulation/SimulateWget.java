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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
 * <p>
 * Copyright: Copyright (c) 2011
 * <p>
 * <p>
 * 模拟wget工具
 * <p>
 * 
 * @author sue
 * @createdate 2011-11-15
 */
public class SimulateWget {

	
	/**
	 * <p>获得url地址的内容，默认存放路径在c盘根目录下</p>
	 * <p>如果路径url为空，url路径异常以及路径下的文件不存等，则直接return，不作任何处理</p>
	 * @param url	目标地址
	 * @param fileName	目标文件名
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void simulateDown(String url, String fileName) throws IllegalStateException, IOException {
		if ( !(null != url && !"".equals(url) && url.trim().length() > 0) )
			return;

		// 1、创建HttpClient的对象
		HttpClient client = new DefaultHttpClient();
		// 2、创建post连接方法的对象，使用post连接方式报错
		HttpGet get = new HttpGet(url);
		// 3、调用创建好的HttpClient对象的 execute方法来执行创建好的HttpPost对象
		InputStream in = null;
//		OutputStream out = null;
		HttpResponse response = null;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity(); // 得到实体
			if (entity != null) {
				in = entity.getContent();
				File file = new File("c:\\" + fileName);
				FileUtils.copyInputStreamToFile(in, file);		//使用FIleUtils提供的静态方法，直接将流写入到文件中
				/*out = new FileOutputStream(new File("c:\\" + fileName));
				int tmp = 0;
				byte b[] = new byte[1024*4];
				while ((tmp = in.read(b)) != -1) {
					out.write(b, 0, tmp);
				}*/
			}
		}finally {
//			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
			// 不管是否成功，都释放连接
			client.getConnectionManager().shutdown();
		}

	}

	

	/**
	 * <p>
	 * 解析url地址，返回文件的后缀类型
	 * </p>
	 * 
	 * @param url
	 *            url地址
	 * @return 如果url为指定文件格式，则返回文件名称，否则返回以当前时间命名文件
	 */
	public static String judgeType(String url) {
		int tmpLastD = url.lastIndexOf(".");
		if (tmpLastD != -1) {
			String type = url.substring(tmpLastD + 1, url.length());
			int index = url.lastIndexOf("/");
			String fileName = "";
			if (type.matches("(" + StaticConstant.FILE_TYPE + ")")
					&& index != -1) { // 匹配文件类型
				fileName = url.substring(index + 1, url.length()); // 文件名
				return fileName;
			}
		}
		return new SimpleDateFormat("yyMMdd_hhmmss").format(new Date())
				+ ".html";
	}


}
