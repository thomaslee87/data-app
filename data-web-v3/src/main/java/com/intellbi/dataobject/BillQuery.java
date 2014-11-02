package com.intellbi.dataobject;

import com.intellbi.utils.MyDateUtils.Period;

public class BillQuery {

	/**
	 * 用户ID，vip经理
	 */
	private int userId;
	
	/**
	 * 客户电话
	 */
	private String phoneNo;
	
	/**
	 * 数据月，格式YYYYMM，用于定位分表
	 */
	private String theMonth;
	
	/**
	 * 分页信息
	 */
	private Pagination pagination;
	
	/**
	 * 合约期限制
	 */
	private Period contractQuery;
	
	public BillQuery(int userId, String phoneNo, String theMonth, Period contractQuery, Pagination pagination){
		setUserId(userId);
		setPhoneNo(phoneNo);
		setTheMonth(theMonth);
		setContractQuery(contractQuery);
		setPagination(pagination);
	}
	

	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public Period getContractQuery() {
		return contractQuery;
	}


	public void setContractQuery(Period contractQuery) {
		this.contractQuery = contractQuery;
	}


	public String getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(String theMonth) {
		this.theMonth = theMonth;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
}
