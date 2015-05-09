package com.intellbit.v2.exception;

public class ConsumerImportException extends IntellbitException {

	private static String _msg_text_ = "BiConsumer import Exception";
	
	public ConsumerImportException() {
		super(_msg_text_);
	}
	
	public ConsumerImportException(String msg) {
		super(msg);
	}
	
	public ConsumerImportException(Exception e) {
		super(_msg_text_, e);
	}
	
	public ConsumerImportException(String msg, Exception e) {
		super(msg, e);
	}
}
