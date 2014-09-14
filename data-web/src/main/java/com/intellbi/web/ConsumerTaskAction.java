package com.intellbi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.service.ConsumerBillService;
import com.opensymphony.xwork2.ActionSupport;

public class ConsumerTaskAction extends ActionSupport {

	private static final long serialVersionUID = 6542977997657343468L;
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	private List<ConsumerBillDO> bills;
	
	public String getConsumerBills() {
		setBills(consumerBillService.getAllMonthBills(201301, null, -1));
		return SUCCESS;
	}

	public List<ConsumerBillDO> getBills() {
		return bills;
	}

	public void setBills(List<ConsumerBillDO> bills) {
		this.bills = bills;
	}
	
}
