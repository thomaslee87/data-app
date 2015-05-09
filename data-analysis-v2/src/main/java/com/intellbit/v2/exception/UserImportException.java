package com.intellbit.v2.exception;

public class UserImportException extends IntellbitException {

	private static String _msg_text_ = "BiUser import Exception";
	
	public UserImportException() {
		super(_msg_text_);
	}
	
	public UserImportException(String msg) {
		super(msg);
	}
	
	public UserImportException(Exception e) {
		super(_msg_text_, e);
	}
	
	public UserImportException(String msg, Exception e) {
		super(msg, e);
	}
}
