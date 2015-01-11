package com.intellbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbi.dao.ConsumerBillDAO;
import com.intellbi.dataobject.BillQueryParameter;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.service.ConsumerBillService;
import com.intellbi.utils.MyDateUtils;

@Service("consumerBillService")
public class ConsumerBillServiceImpl implements ConsumerBillService {
	
	@Autowired
	ConsumerBillDAO consumerBillDao;

	public ConsumerBillDO getMonthBill(String yearMonth, String phoneNo, int userId,String order) {
		// TODO Auto-generated method stub
		return consumerBillDao.get(new BillQueryParameter(yearMonth, phoneNo, userId,order));
	}

	public List<ConsumerBillDO> getAllMonthBills(String yearMonth, String phoneNo,
			int userId, int page, int pageSize,String order,int howLong) {
		BillQueryParameter param = new BillQueryParameter(yearMonth, phoneNo, userId,order);
		param.setPageBegin((page-1)*pageSize);
		param.setPageSize(pageSize);
        param.setMinContract(-1);
        param.setMaxContract(-1);
        if(howLong > 0) {
            param.setMinContract(Integer.parseInt(yearMonth + "00"));
		    param.setMaxContract(Integer.parseInt(MyDateUtils.getMonthByDelta(yearMonth, howLong) + "99"));
        }
		return consumerBillDao.findAll(param);
	}
	
	public int getTotalCount(String yearMonth, String phoneNo, int userId,int howLong){
	    BillQueryParameter param = new BillQueryParameter(yearMonth, phoneNo, userId,null);
	    param.setMinContract(-1);
        param.setMaxContract(-1);
        if(howLong > 0) {
            param.setMinContract(Integer.parseInt(yearMonth + "00"));
            param.setMaxContract(Integer.parseInt(MyDateUtils.getMonthByDelta(yearMonth, howLong) + "99"));
        }
		return consumerBillDao.getCnt(param);
	}

	public int getSingleHighGprsTotalCount(String yearMonth, String phoneNo,
			int userId) {
		// TODO Auto-generated method stub
		return consumerBillDao.getSingleHighGprsCnt(new BillQueryParameter(yearMonth, phoneNo, userId, null));
	}

	public List<ConsumerBillDO> getAllSingleHighGprsBill(String yearMonth,
			String phoneNo, int userId, int page, int pageSize, String order) {
		// TODO Auto-generated method stub
		BillQueryParameter param = new BillQueryParameter(yearMonth, phoneNo, userId,null);
		param.setPageBegin((page-1)*pageSize);
		param.setPageSize(pageSize);
		return consumerBillDao.findAllSingleHighGprs(param);
	}

}
