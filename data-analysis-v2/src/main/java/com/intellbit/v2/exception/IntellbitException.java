package com.intellbit.v2.exception;

public class IntellbitException extends Exception {

	private static String _msg_text_ = "Intellbit Exception";
	
	public IntellbitException() {
		super(_msg_text_);
	}
	
	public IntellbitException(String msg) {
		super(msg);
	}
	
	public IntellbitException(Exception e) {
		super(_msg_text_, e);
	}
	
	public IntellbitException(String msg, Exception e) {
		super(msg, e);
	}
}
