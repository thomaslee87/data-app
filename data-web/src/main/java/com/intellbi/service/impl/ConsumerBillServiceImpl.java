package com.intellbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbi.dao.ConsumerBillDAO;
import com.intellbi.dataobject.BillQueryParameter;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.service.ConsumerBillService;

@Service("consumerBillService")
public class ConsumerBillServiceImpl implements ConsumerBillService {
	
	@Autowired
	ConsumerBillDAO consumerBillDao;

	public ConsumerBillDO getMonthBill(int yearMonth, String phoneNo, int userId) {
		// TODO Auto-generated method stub
		return consumerBillDao.get(new BillQueryParameter(yearMonth, phoneNo, userId));
	}

	public List<ConsumerBillDO> getAllMonthBills(int yearMonth, String phoneNo,
			int userId) {
		return consumerBillDao.findAll(new BillQueryParameter(yearMonth, phoneNo, userId));
	}

}
