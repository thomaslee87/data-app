package com.intellbi.dataobject;

import java.io.Serializable;

public class BillQueryParameter implements Serializable{

	private static final long serialVersionUID = 6299533418312460724L;
	
	private long yearMonth;
	private String phoneNo;
	private long userId;
	
	private int pageBegin;
	private int pageSize;
	
	private int pageEnd;
	
	public int getPageEnd() {
		return pageEnd;
	}

	public void setPageEnd(int pageEnd) {
		this.pageEnd = pageEnd;
	}

	public int getPageBegin() {
		return pageBegin;
	}

	public void setPageBegin(int pageBegin) {
		this.pageBegin = pageBegin;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public BillQueryParameter(long yearMonth, String phoneNo, long userId){//, int page) {
		setYearMonth(yearMonth);
		setPhoneNo(phoneNo);
		setUserId(userId);
		
//		setPageBegin(5 * page);
//		setPageSize(size);
	}
	
	public long getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(long yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
