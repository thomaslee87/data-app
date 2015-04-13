package com.intellbit.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbit.dao.ConsumerDataDao;
import com.intellbit.dao.TelecomPackageDao;
import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.QueryCondition.QueryConditionBuilder;
import com.intellbit.dataobject.TelecomPackageDO;
import com.intellbit.dataobject.ajax.RequestQueryFilter;
import com.intellbit.service.ConsumerDataService;

// get consumer's bill data of given user
@Service("consumerDataService")
public class ConsumerDataServiceImpl implements ConsumerDataService {

	@Autowired
	private ConsumerDataDao consumerDataDao;

	@Autowired
	private TelecomPackageDao telPakcageDao;

	@Override
	public int getMaintainConsumersCount(int userId, String theMonth,
			RequestQueryFilter filter) {
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setFilter(filter);
		return consumerDataDao.getMaintainConsumersCount(builder.build());
	}

	@Override
	public List<ConsumerBillDO> getMaintainConsumers(int userId,
			String theMonth, int pageStart, int pageSize,
			List<String> orderFields, RequestQueryFilter filter) {

		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth)
				.setPage(pageStart, pageSize).setFilter(filter);

		if (orderFields != null) {
			for (String field : orderFields)
				builder.addOrderField(field);
		}

		return consumerDataDao.getMaintainConsumers(builder.build());
	}

	@Override
	public int getBandwidthConsumersCount(int userId, String theMonth,
			RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setFilter(filter);
		return consumerDataDao.getBandwidthConsumersCount(builder.build());
	}

	@Override
	public List<ConsumerBillDO> getBandwidthConsumers(int userId,
			String theMonth, int pageStart, int pageSize,
			List<String> orderFields, RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth)
				.setPage(pageStart, pageSize).setFilter(filter);

		if (orderFields != null) {
			for (String field : orderFields)
				builder.addOrderField(field);
		}

		return consumerDataDao.getBandwidthConsumers(builder.build());
	}

	public ConsumerBillDO getConsumerMonthBill(String phoneNo, String theMonth) {
		QueryConditionBuilder builder = new QueryConditionBuilder().setPhoneNo(
				phoneNo).setTheMonth(theMonth);
		return consumerDataDao.getConsumerMonthBill(builder.build());
	}

	public List<ConsumerBillDO> getConsumerMonthBills(String phoneNo,
			List<String> theMonthList) {
		List<ConsumerBillDO> consumerBills = new ArrayList<ConsumerBillDO>();
		for (String theMonth : theMonthList) {
			ConsumerBillDO consumerDo = getConsumerMonthBill(phoneNo, theMonth);
			consumerBills.add(consumerDo);
		}
		return consumerBills;
	}

	@Override
	public int getContractConsumersCount(int userId, String theMonth,
			RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setFilter(filter);
		return consumerDataDao.getContractConsumersCount(builder.build());
	}

	@Override
	public List<ConsumerBillDO> getContractConsumers(int userId,
			String theMonth, int pageStart, int pageSize,
			List<String> orderFields, RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth)
				.setPage(pageStart, pageSize).setFilter(filter);

		if (orderFields != null) {
			for (String field : orderFields)
				builder.addOrderField(field);
		}

		return consumerDataDao.getContractConsumers(builder.build());
	}

}
