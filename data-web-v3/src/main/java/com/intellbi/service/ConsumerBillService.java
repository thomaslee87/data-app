package com.intellbi.service;

import java.util.List;

import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.dataobject.Pagination;
import com.intellbi.utils.MyDateUtils.Period;

public interface ConsumerBillService {

	ConsumerBillDO getMonthBill(String yearMonth, String phoneNo, int userId, String order) ;
	
	List<ConsumerBillDO> getAllMonthBills(String yearMonth, String phoneNo, int userId, int page, int pageSize,String order,int howlong);
	
	int getTotalCount(String yearMonth, String phoneNo, int userId, int howLong);
	
	int getSingleHighGprsTotalCount(String yearMonth, String phoneNo, int userId);

	List<ConsumerBillDO> getAllSingleHighGprsBill(String yearMonth, String phoneNo, int userId, int page, int pageSize,String order);
	
	/*合约用户相关*/
	/**
	 * @param userId
	 * @param theMonth
	 * @param cQueryWindow
	 * @return 合约用户个数
	 */
	public int getContractCnt(int userId, String theMonth, Period cQueryWindow, int contractState, int hideDone,String theBizMonth);
	
	/**
	 * @param userId
	 * @param theMonth
	 * @param contractFrom
	 * @param contractTo
	 * @return 合约用户列表
	 */
	public List<ConsumerBillDO> getContractConsumers(int userId, String theMonth, Period cQueryWindow, Pagination pagination, int contractState, int hideDone,String theBizMonth);
	
	/**
	 * @param phoneNo
	 * @param yearMonth
	 * @return 从指定月表中取用户的消费信息
	 */
	ConsumerBillDO getConsumerMonthBill(String phoneNo, String theMonth, int contractState) ;
	
//	List<ConsumerBillDO> getAllConsumers(int userId, String theMonth);
	
	/**
	 * @param userId
	 * @param theMonth
	 * @param cQueryWindow
	 * @param pagination
	 * @return 返回除了合约期6个月到期的其他用户
	 */
	List<ConsumerBillDO> getBrandUpConsumers(int userId, String theMonth,Period cQueryWindow, Pagination pagination, int contractState,int hideDone,String theBizMonth);
	
	int getBrandUpCnt(int userId, String theMonth,Period cQueryWindow, int contractState,int hideDone,String theBizMonth);
	
	
	void updateContractStatus(String phoneNo, String theMonth, int contractState);
}
