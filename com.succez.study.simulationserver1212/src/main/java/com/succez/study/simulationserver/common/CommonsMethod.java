package com.succez.study.simulationserver.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>公共方法<p>
 * @author sue
 * @createdate 2011-11-23
 */
public class CommonsMethod {
	
	/**
	 * <p>返回文件的类型</p>
	 * @param file	文件路径
	 * @return	为空或者不存在，返回“other”；为文件，返回：“file”；目录，返回：“directory”；其他，返回：“other”
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
	 * 返回目录file下的所有文件
	 * @param file	目录路径
	 * @return	如果file为目录，则返回该目录下的所有文件；其他情况，返回错误提示信息
	 * @throws IOException 
	 */
	public static String listDirFiles(File file) throws IOException {
		if (null == file) {
			return "file is null";
		} else {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				StringBuffer sbf = new StringBuffer(StaticConstant.STRINGBUFFER_LEN);
				if (null != files && files.length > 0) {
					int i = 0;
					sbf.append(FileUtils.readFileToString(new File("e://webserver/content.txt"),"gbk"));
//					将以下内容加入到了文件中
					/*sbf.append("<html><style type='text/css'>"
							+ "a { color:#00f; text-decoration:none; }"
							+ "a:hover { color:#f00; text-decoration:underline; }"
							+ ".file { color:#FF8000}"
							+ ".dir { color:#7B7B7B}"
							+ "</style>"
							+ "<table border=1 cellPadding=0 cellSpacing=0 "
							+ "style='text-align:center;' align='center' valign='center'>"
							+ "<tr><td colspan='3' height='30px'>"
							+ "<a href='../'>上一层目录(默认为c盘根目录)</a>"
							+ "&nbsp;&nbsp;目录下的文件列表</td></tr>"
							+ "<tr ><td height='30px' width='50px'>ID</td>"
							+ "<td width='150px'>Type</td>"
							+ "<td width='200px'>Name</td></tr>");*/
					String type = null;
					for (File fi : files) {
//						第一列
						sbf.append("<tr onMouseOver=this.style.backgroundColor='#FFDCB9' onMouseOut=this.style.backgroundColor='#fff'><td height='30px'>");
						sbf.append(++i);
						type = CommonsMethod.judgeFileType(fi);
						sbf.append("</td><td><a target='_blank' href='");
						sbf.append(fi.getName());
						/**
						 * 此处不加"/"的会产生的现象： 当进入第二层目录后，前一层目录丢失，如：
						 * 第一次请求的路径：http://localhost:8090/e/test/
						 * 此路径下有一个ext文件夹，点击此目录超链接后，另开窗口路径为:
						 * http://localhost:8090/e/test/ext
						 * 另开窗口显示为ext文件夹下的所有内容，当鼠标放在离开窗口内容超链接上的时候，显示的路径为：
						 * http://localhost:8090/e/test/index.html，上一层ext路径没有显示
						 * 
						 * 导致上述问题的原因是：文件夹名少了"/"，如果少了"/"，
						 * 在此页面中点击超链接时，会默认为在同一级目录下，便会少了一级目录，就会出现上述现象
						 */
						if ("directory".equals(type)) {
							sbf.append("/");
						}
						sbf.append("'>");
						sbf.append(fi.getName());
						sbf.append("</a></td><td>");
						
//						第二列
						sbf.append(new SimpleDateFormat("yyyy/MM/dd hh:mm").format(fi.lastModified()));
						sbf.append("</td>");
						
//						第三列
						if ("file".equals(type))
							sbf.append("<td class='file'>文件");
						else if ("directory".equals(type))
							sbf.append("<td class='dir'>文件夹");
//						sbf.append(type);
						sbf.append("</td>");
						
//						第四列
						sbf.append("<td>");
						sbf.append((fi.length() - 1) / 1024 + 1);
						sbf.append("KB</td></tr>");
					}
					sbf.append("</table></html>");
					return sbf.toString();
				} else {
					return "目录下没有文件";
				}
			} else {
				return "此路径不为目录";
			}
		}
		
	}

}
