package com.intellbi.dataobject;

import java.util.Date;

import com.intellbi.utils.MyDateUtils.Period;

public class BillQuery {
    
    private String theBizMonth;

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
	
	private int taskStateContract;
	
	private int hideDone;
	
	private Date viewDate;
	
	
	public Date getViewDate() {
		return viewDate;
	}


	public void setViewDate(Date viewDate) {
		this.viewDate = viewDate;
	}


	public BillQuery(int userId, String phoneNo, String theMonth, Period contractQuery, Pagination pagination, int taskStateContract, int hideDone,String theBizMonth){
		setUserId(userId);
		setPhoneNo(phoneNo);
		setTheMonth(theMonth);
		setContractQuery(contractQuery);
		setPagination(pagination);
		setTaskStateContract(taskStateContract);
		setHideDone(hideDone);
		setTheBizMonth(theBizMonth);
	}
	

	public int getTaskStateContract() {
		return taskStateContract;
	}



	public void setTaskStateContract(int taskStateContract) {
		this.taskStateContract = taskStateContract;
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


    public int getHideDone() {
        return hideDone;
    }


    public void setHideDone(int hideDone) {
        this.hideDone = hideDone;
    }


    public String getTheBizMonth() {
        return theBizMonth;
    }


    public void setTheBizMonth(String theBizMonth) {
        this.theBizMonth = theBizMonth;
    }
}
