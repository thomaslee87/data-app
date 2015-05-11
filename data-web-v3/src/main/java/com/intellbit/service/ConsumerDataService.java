package com.intellbit.service;

import java.util.List;

import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.ajax.RequestQueryFilter;

// get consumer's bill data of given user
public interface ConsumerDataService {
	
	public int getMaintainConsumersCount(int userId, String theMonth, RequestQueryFilter filter);
	public List<ConsumerBillDO> getMaintainConsumers(int userId, String theMonth, int pageStart, int pageSize, List<String> orderFields, RequestQueryFilter filter);
	
	public int getContractConsumersCount(int userId, String bizmonth, String theMonth, RequestQueryFilter filter);
	public List<ConsumerBillDO> getContractConsumers(int userId, String bizmonth, String theMonth, int pageStart, int pageSize, List<String> orderFields, RequestQueryFilter filter);
	
	public int getBandwidthConsumersCount(int userId, String theMonth, RequestQueryFilter filter);
	public List<ConsumerBillDO> getBandwidthConsumers(int userId, String theMonth, int pageStart, int pageSize, List<String> orderFields, RequestQueryFilter filter);
	
	public ConsumerBillDO getConsumerMonthBill(String phoneNo, String theMonth);
	public List<ConsumerBillDO> getConsumerMonthBills(String phoneNo, List<String> theMonthList);
	
}
