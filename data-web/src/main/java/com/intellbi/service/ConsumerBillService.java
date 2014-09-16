package com.intellbi.service;

import java.util.List;

import com.intellbi.dataobject.ConsumerBillDO;

public interface ConsumerBillService {

	ConsumerBillDO getMonthBill(int yearMonth, String phoneNo, int userId) ;
	
	List<ConsumerBillDO> getAllMonthBills(int yearMonth, String phoneNo, int userId, int page, int pageSize);
}
