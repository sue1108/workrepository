/**
 * 
 */
package com.succez.study.webserver.common;

import java.io.File;


/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>succez<p>
 * @author sue
 * @createdate 2011-11-23
 */
public class CommonsMethod {
	
	/**
	 * <p>返回文件的类型</p>
	 * @param file	文件
	 * @return	为文件，返回：“file”；路径，返回：“dir”；其他，返回：“other”
	 */
	public static String judgeFileType(File file){
		if( file.isFile()){
			return "file";
		}else if(file.isDirectory()){
			return "directory";
		}else{
			return "other";
		}
	}
	
//	public static Logger getLoggerObject(Object object){
//		return LoggerFactory.getLogger(object.getClass());
//	}

}
