package com.intellbi.dataobject;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class BillQueryParameter implements Serializable{

	private static final long serialVersionUID = 6299533418312460724L;
	
	private String yearMonth;
	private String phoneNo;
	private long userId;
	
	private int pageBegin;
	private int pageSize;
	
	private int pageEnd;
	
	private String orderField;
	
	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

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

	public BillQueryParameter(String yearMonth, String phoneNo, long userId, String orderField){//, int page) {
		setYearMonth(yearMonth);
		setPhoneNo(phoneNo);
		setUserId(userId);
		
		if(StringUtils.isBlank(orderField) 
				|| (!orderField.equals("regular_score") && !orderField.equals("value_change"))) {
			setOrderField("regular_score");
		}
		else {
			setOrderField(orderField);
		}
//		setPageBegin(5 * page);
//		setPageSize(size);
	}
	
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
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
