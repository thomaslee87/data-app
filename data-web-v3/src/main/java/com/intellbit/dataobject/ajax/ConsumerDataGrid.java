package com.intellbit.dataobject.ajax;

import java.io.Serializable;

public class ConsumerDataGrid implements Serializable {
	
	private static final long serialVersionUID = -1222230414973285034L;
	
	private DataGridRequest aoData;
	private int taskType;
	private String bizdate;
	private String orderby;
	private String hideDone;
	
	public DataGridRequest getAoData() {
		return aoData;
	}
	public void setAoData(DataGridRequest aoData) {
		this.aoData = aoData;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public String getBizdate() {
		return bizdate;
	}
	public void setBizdate(String bizdate) {
		this.bizdate = bizdate;
	}
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	public String getHideDone() {
		return hideDone;
	}
	public void setHideDone(String hideDone) {
		this.hideDone = hideDone;
	}
	
}
