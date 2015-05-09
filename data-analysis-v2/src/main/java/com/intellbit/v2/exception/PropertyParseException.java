package com.intellbit.v2.exception;

public class PropertyParseException extends IntellbitException {

	private static String _msg_text_ = "Property parse Exception";
	
	public PropertyParseException() {
		super(_msg_text_);
	}
	
	public PropertyParseException(String msg) {
		super(msg);
	}
	
	public PropertyParseException(Exception e) {
		super(_msg_text_, e);
	}
	
	public PropertyParseException(String msg, Exception e) {
		super(msg, e);
	}
}
