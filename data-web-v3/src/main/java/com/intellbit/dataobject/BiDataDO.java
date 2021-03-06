package com.intellbit.dataobject;

import java.io.Serializable;

public class BiDataDO implements Serializable {
	
	private static final long serialVersionUID = 1018800062340743041L;
	
	private int id;
	private String theMonth;
	private int status;//0:不合法；1：已上传；2：已处理（可读）
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(String theMonth) {
		this.theMonth = theMonth;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
