package com.intellbit.dataobject;

import java.io.Serializable;

public class RespMsg<T> implements Serializable {
	
	private static final long serialVersionUID = -8929492065552341031L;
	
	private int errCode;
	private String errMsg;
	T data;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
