package com.intellbi.dao;

import java.util.List;

import com.intellbi.dataobject.BillQueryParameter;
import com.intellbi.dataobject.ConsumerBillDO;

public interface ConsumerBillDAO {
	
	List<ConsumerBillDO> findAll(BillQueryParameter object);
	
	ConsumerBillDO get(BillQueryParameter object);
	
	int getCnt(BillQueryParameter object);
	
	int getSingleHighGprsCnt(BillQueryParameter object);
	
	List<ConsumerBillDO> findAllSingleHighGprs(BillQueryParameter object);
}
