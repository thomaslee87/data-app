package com.intellbi.dao;

import java.util.List;

import com.intellbi.dataobject.BillQuery;
import com.intellbi.dataobject.BillQueryParameter;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.dataobject.Pagination;
import com.intellbi.utils.MyDateUtils.Period;

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
	
}
