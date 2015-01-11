package com.intellbi.service;

import java.util.List;

import com.intellbi.dataobject.ConsumerBillDO;

public interface ConsumerBillService {

	ConsumerBillDO getMonthBill(String yearMonth, String phoneNo, int userId,String order) ;
	
	List<ConsumerBillDO> getAllMonthBills(String yearMonth, String phoneNo, int userId, int page, int pageSize,String order,int howlong);
	
	int getTotalCount(String yearMonth, String phoneNo, int userId, int howLong);
	
	int getSingleHighGprsTotalCount(String yearMonth, String phoneNo, int userId);

	List<ConsumerBillDO> getAllSingleHighGprsBill(String yearMonth, String phoneNo, int userId, int page, int pageSize,String order);
}