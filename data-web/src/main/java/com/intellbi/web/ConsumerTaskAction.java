package com.intellbi.web;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dataobject.BiDataDO;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.service.ConsumerBillService;
import com.intellbi.service.impl.BiDataServiceImpl;
import com.opensymphony.xwork2.ActionSupport;

public class ConsumerTaskAction extends ActionSupport {

	private static final long serialVersionUID = 6542977997657343468L;
	
	private Logger logger = Logger.getLogger(ConsumerTaskAction.class);
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	@Autowired
	private BiDataServiceImpl biDataService;

	private int userId = -1;
	private int theMonth = -1;
	private int pageSize = 50;
	private int currPage = 1;
	private String phoneNo;
	
	private ConsumerBillDO consumerBillDO;
	
	private List<ConsumerBillDO> bills;
	
	private List<ConsumerBillDO> recentBills;
	
	public ConsumerTaskAction(){
		recentBills = new ArrayList<ConsumerBillDO>();
	}
	
	public String getConsumerBills() {
		BiDataDO biDataDO = biDataService.getRecentBiDataDO();
		if(theMonth <= 0)
			theMonth = biDataDO.getTheMonth();
		logger.info("get consumer bills for month: " + theMonth);
		setBills(consumerBillService.getAllMonthBills(theMonth, null, userId, currPage, pageSize));
		return SUCCESS;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getConsumerBillsJson() {
		setConsumerBillDO(consumerBillService.getMonthBill(theMonth, phoneNo, userId));
		return SUCCESS;
	}
	
	public String getRecentConsumerBillsJson() {
		int RECENT_MONTHS = 6;
		
		recentBills.clear();
		if(StringUtils.isBlank(phoneNo) || theMonth <= 0)
			return SUCCESS;
		
		List<Integer> theMonthList = new LinkedList<Integer>();
		List<BiDataDO> biDataList = biDataService.findAll();//按照theMonth倒序
		int cursor = 0;
		while(biDataList.get(cursor).getTheMonth() > theMonth)
			cursor ++;
		
		int currentCollected = 0;
		while(currentCollected <= RECENT_MONTHS && cursor <= biDataList.size() - 1) {
			theMonthList.add(biDataList.get(cursor).getTheMonth());
			currentCollected ++;
			cursor ++;
		}
		
		for(int month: theMonthList) {
			try{
				ConsumerBillDO consumerDo = consumerBillService.getMonthBill(month, phoneNo, -1);
				if(consumerDo != null)
					recentBills.add(consumerBillService.getMonthBill(month, phoneNo, -1));				
			}catch(Exception e) {
				continue;
			}
		}
		
		return SUCCESS;
	}

	public List<ConsumerBillDO> getRecentBills() {
		return recentBills;
	}

	public void setRecentBills(List<ConsumerBillDO> recentBills) {
		this.recentBills = recentBills;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(int theMonth) {
		this.theMonth = theMonth;
	}
	

	public ConsumerBillDO getConsumerBillDO() {
		return consumerBillDO;
	}

	public void setConsumerBillDO(ConsumerBillDO consumerBillDO) {
		this.consumerBillDO = consumerBillDO;
	}
	
	public List<ConsumerBillDO> getBills() {
		return bills;
	}

	public void setBills(List<ConsumerBillDO> bills) {
		this.bills = bills;
	}
}
