package com.succez.wget.simulation.pojo;

import java.io.InputStream;

/**
 * 
 * <p>Copyright: Copyright (c) 2011<p>
 * <p>类有四个属性：fileName、type、content，contentBinary 在本地创建内容为content，文件名位fileNme，文件类型为type的文件，contentBinary为文件内容的二进制<p>
 * @author sue
 * @createdate 2011-11-15
 */
public class FileInfor {

	private String content;
	
	private String type ;
	
	private String fileName ;
	
	private InputStream in;


	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
