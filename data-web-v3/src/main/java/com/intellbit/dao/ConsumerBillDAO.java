package com.intellbit.dao;

import java.util.List;

import com.intellbit.dataobject.BillQuery;
import com.intellbit.dataobject.BillQueryParameter;
import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.Pagination;
import com.intellbit.utils.MyDateUtils.Period;

public interface ConsumerBillDAO {
	
	List<ConsumerBillDO> findAll(BillQueryParameter object);
	
	ConsumerBillDO get(BillQueryParameter object);
	
	int getCnt(BillQueryParameter object);
	
	int getSingleHighGprsCnt(BillQueryParameter object);
	
	List<ConsumerBillDO> findAllSingleHighGprs(BillQueryParameter object);
	
	/**
	 * @param billQuery
	 * @return 合约用户数
	 */
	public int getContractCnt(BillQuery billQuery);
	
	/**
	 * @param billQuery
	 * @return 合约用户列表
	 */
	public List<ConsumerBillDO> getContractUsers(BillQuery billQuery);
	
	/**
	 * @return 根据查询条件返回单条用户信息
	 */
	public ConsumerBillDO getConsumerMonthBill(BillQuery billQuery) ;
	
	public List<ConsumerBillDO> getBrandUpConsumers(BillQuery billQuery);
	public int getBrandUpCnt(BillQuery billQuery);
	
	public void updateContractStatus(BillQuery billQuery);
	
}
