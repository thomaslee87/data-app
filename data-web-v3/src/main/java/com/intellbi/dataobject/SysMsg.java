package com.intellbi.dataobject;

import java.io.Serializable;

public class SysMsg implements Serializable {
	
	private static final long serialVersionUID = -8929492065552341031L;
	
	public static final int NOTLONGIN = -2;
	public static final int ERROR = -1;
	public static final int SUCCESS = 0;
	
	private int msgCode;
	private String msgText;
	
	public int getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	
	
}
