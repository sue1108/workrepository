/**
 * 
 */
package com.succez.study.server.common;

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
	 * @param file	文件路径
	 * @return	为空或者不存在，返回null；为文件，返回：“file”；路径，返回：“dir”；其他，返回：“other”
	 */
	public static String judgeFileType(File file){
		if( null == file  ){
			return "other";
		}else{
			if( file.isFile()){
				return "file";
			}else if(file.isDirectory()){
				return "directory";
			}else{
				return "other";
			}
		}
	}
	
	/**
	 * 根据file返回该目录下的所有文件
	 * @param file	目录路径
	 * @return	返回目录下的所有文件
	 */
	public static String listDirFiles(File file){
		if( null == file ){
			return "file is null";
		}else{
			if( file.isDirectory()){
				File files[] = file.listFiles();
				StringBuffer sbf = new StringBuffer(StaticConstant.STRINGBUFFER_LEN);
				if( null != files && files.length > 0){
					sbf.append("目录下的文件列表：<br/>");
					int i = 0;
					for (File fi : files) {
						sbf.append(CommonsMethod.judgeFileType(fi));
						sbf.append(++i);
						sbf.append(":");
						sbf.append(fi.getName());
						sbf.append("<br/>");
					}
					return sbf.toString();
				}else{
					return "目录下没有文件";
				}
				
			}else{
				return "此路径不为目录";
			}
		}
		
	}
	
//	public static Logger getLoggerObject(Object object){
//		return LoggerFactory.getLogger(object.getClass());
//	}

}
