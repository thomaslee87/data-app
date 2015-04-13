package com.intellbit.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbit.dao.ConsumerBillDAO;
import com.intellbit.dataobject.BillQuery;
import com.intellbit.dataobject.BillQueryParameter;
import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.Pagination;
import com.intellbit.service.ConsumerBillService;
import com.intellbit.utils.MyDateUtils;
import com.intellbit.utils.MyDateUtils.Period;

@Service("consumerBillService")
public class ConsumerBillServiceImpl implements ConsumerBillService {

	@Autowired
	ConsumerBillDAO consumerBillDao;
	
	
	@Override
	public int getBrandUpCnt(int userId, String theMonth, Period cQueryWindow, int contractState, int hideDone,String theBizMonth) {
		// TODO Auto-generated method stub
		BillQuery billQuery = new BillQuery(userId, null, theMonth, cQueryWindow, null, contractState,hideDone,theBizMonth);
		return consumerBillDao.getBrandUpCnt(billQuery);
	}

	@Override
	public List<ConsumerBillDO> getBrandUpConsumers(int userId,
			String theMonth, Period cQueryWindow, Pagination pagination, int contractState,int hideDone,String theBizMonth) {
		// TODO Auto-generated method stub
		BillQuery billQuery = new BillQuery(userId, null, theMonth, cQueryWindow, pagination, contractState,hideDone,theBizMonth);
		return consumerBillDao.getBrandUpConsumers(billQuery);
	}

	@Override
	public ConsumerBillDO getConsumerMonthBill(String phoneNo, String theMonth, int contractState) {
		// TODO Auto-generated method stub
		BillQuery query = new BillQuery(-1, phoneNo, theMonth, null, null,contractState, 0,null);
		return consumerBillDao.getConsumerMonthBill(query);
	}

	@Override
	public int getContractCnt(int userId, String theMonth, Period cQueryWindow,  int contractState, int hideDone,String theBizMonth) {
		// TODO Auto-generated method stub
		BillQuery billQuery = new BillQuery(userId, null, theMonth, cQueryWindow, null,contractState, hideDone,theBizMonth);
		return consumerBillDao.getContractCnt(billQuery);
	}
	
	@Override
	public List<ConsumerBillDO> getContractConsumers(int userId,
			String theMonth, Period cQueryWindow, Pagination pagination, int contractState, int hideDone,String theBizMonth) {
		// TODO Auto-generated method stub
		BillQuery billQuery = new BillQuery(userId, null, theMonth, cQueryWindow, pagination,contractState, hideDone,theBizMonth);
		return consumerBillDao.getContractUsers(billQuery);
	}


	public ConsumerBillDO getMonthBill(String yearMonth, String phoneNo,
			int userId, String order) {
		// TODO Auto-generated method stub
		return consumerBillDao.get(new BillQueryParameter(yearMonth, phoneNo,
				userId, order));
	}

	public List<ConsumerBillDO> getAllMonthBills(String yearMonth,
			String phoneNo, int userId, int page, int pageSize, String order,
			int howLong) {
		BillQueryParameter param = new BillQueryParameter(yearMonth, phoneNo,
				userId, order);
		param.setPageBegin((page - 1) * pageSize);
		param.setPageSize(pageSize);
		param.setMinContract(-1);
		param.setMaxContract(-1);
		if (howLong > 0) {
			param.setMinContract(Integer.parseInt(yearMonth + "00"));
			param.setMaxContract(Integer.parseInt(MyDateUtils.getMonthByDelta(
					yearMonth, howLong) + "99"));
		}
		return consumerBillDao.findAll(param);
	}

	public int getTotalCount(String yearMonth, String phoneNo, int userId,
			int howLong) {
		BillQueryParameter param = new BillQueryParameter(yearMonth, phoneNo,
				userId, null);
		param.setMinContract(-1);
		param.setMaxContract(-1);
		if (howLong > 0) {
			param.setMinContract(Integer.parseInt(yearMonth + "00"));
			param.setMaxContract(Integer.parseInt(MyDateUtils.getMonthByDelta(
					yearMonth, howLong) + "99"));
		}
		return consumerBillDao.getCnt(param);
	}

	public int getSingleHighGprsTotalCount(String yearMonth, String phoneNo,
			int userId) {
		// TODO Auto-generated method stub
		return consumerBillDao.getSingleHighGprsCnt(new BillQueryParameter(
				yearMonth, phoneNo, userId, null));
	}

	public List<ConsumerBillDO> getAllSingleHighGprsBill(String yearMonth,
			String phoneNo, int userId, int page, int pageSize, String order) {
		// TODO Auto-generated method stub
		BillQueryParameter param = new BillQueryParameter(yearMonth, phoneNo,
				userId, null);
		param.setPageBegin((page - 1) * pageSize);
		param.setPageSize(pageSize);
		return consumerBillDao.findAllSingleHighGprs(param);
	}

	@Override
	public void updateContractStatus(String phoneNo, String theMonth,
			int contractState) {
		// TODO Auto-generated method stub
		BillQuery billQuery = new BillQuery(0, phoneNo, theMonth, null, null, contractState, 0,null);
		billQuery.setViewDate(new Date());
		consumerBillDao.updateContractStatus(billQuery);
	}

}
