package com.intellbit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbit.dao.ConsumerDataDao;
import com.intellbit.dao.TelecomPackageDao;
import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.QueryCondition.QueryConditionBuilder;
import com.intellbit.dataobject.ajax.RequestQueryFilter;
import com.intellbit.service.ConsumerDataService;
import com.intellbit.utils.MyDateUtils;

// get consumer's bill data of given user
@Service("consumerDataService")
public class ConsumerDataServiceImpl implements ConsumerDataService {

	@Autowired
	private ConsumerDataDao consumerDataDao;

	@Autowired
	private TelecomPackageDao telPakcageDao;

	@Override
	public int getMaintainConsumersCount(int userId, String bizmonth, String theMonth,
			RequestQueryFilter filter) {
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setBizmonth(bizmonth).setFilter(filter);
		return consumerDataDao.getMaintainConsumersCount(builder.build());
	}

	@Override
	public List<ConsumerBillDO> getMaintainConsumers(int userId,String bizmonth, 
			String theMonth, int pageStart, int pageSize,
			List<String> orderFields, RequestQueryFilter filter) {

		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setBizmonth(bizmonth)
				.setPage(pageStart, pageSize).setFilter(filter);

		if (orderFields != null) {
			for (String field : orderFields)
				builder.addOrderField(field);
		}

		return consumerDataDao.getMaintainConsumers(builder.build());
	}

	@Override
	public int getBandwidthConsumersCount(int userId, String bizmonth, String theMonth,
			RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setBizmonth(bizmonth).setFilter(filter);
		return consumerDataDao.getBandwidthConsumersCount(builder.build());
	}

	@Override
	public List<ConsumerBillDO> getBandwidthConsumers(int userId,String bizmonth, 
			String theMonth, int pageStart, int pageSize,
			List<String> orderFields, RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setBizmonth(bizmonth)
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
	public int getContractConsumersCount(int userId, String bizmonth,  String theMonth,
			RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		// 续约任务看6个月内合约到期的用户，这里和过滤条件中的合约结束期比较，取较小的值
		String end = MyDateUtils.getMonthByDelta(bizmonth, 6);
		end += "32";
		if (StringUtils.isBlank(filter.getToEnd()) || end.compareTo(filter.getToEnd()) < 0) {
			filter.setToEnd(end);
		}
		if (StringUtils.isBlank(filter.getToStart()) || bizmonth.compareTo(filter.getToStart()) > 0) {
			filter.setToStart(bizmonth + "00");
		}
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setBizmonth(bizmonth).setFilter(filter);
		return consumerDataDao.getContractConsumersCount(builder.build());
	}

	@Override
	public List<ConsumerBillDO> getContractConsumers(int userId,String bizmonth, 
			String theMonth, int pageStart, int pageSize,
			List<String> orderFields, RequestQueryFilter filter) {
		// TODO Auto-generated method stub
		// 续约任务看6个月内合约到期的用户，这里和过滤条件中的合约结束期比较，取较小的值
		String end = MyDateUtils.getMonthByDelta(bizmonth, 6);
		end += "32";
		if (StringUtils.isBlank(filter.getToEnd()) || end.compareTo(filter.getToEnd()) < 0) {
			filter.setToEnd(end);
		}
		if (StringUtils.isBlank(filter.getToStart()) || bizmonth.compareTo(filter.getToStart()) > 0) {
			filter.setToStart(bizmonth + "00");
		}
		QueryConditionBuilder builder = new QueryConditionBuilder()
				.setUserId(userId).setTheMonth(theMonth).setBizmonth(bizmonth)
				.setPage(pageStart, pageSize).setFilter(filter);

		if (orderFields != null) {
			for (String field : orderFields)
				builder.addOrderField(field);
		}

		return consumerDataDao.getContractConsumers(builder.build());
	}

}
