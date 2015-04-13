package com.intellbit.dao;

import java.util.List;

import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.QueryCondition;

public interface ConsumerDataDao {
	
	public int getMaintainConsumersCount(QueryCondition qCond);
	public List<ConsumerBillDO> getMaintainConsumers(QueryCondition qCond);
	
    public int getContractConsumersCount(QueryCondition qCond);
	public List<ConsumerBillDO> getContractConsumers(QueryCondition qCond);
	
	public int getBandwidthConsumersCount(QueryCondition qCond);
	public List<ConsumerBillDO> getBandwidthConsumers(QueryCondition qCond);
	
	public ConsumerBillDO getConsumerMonthBill(QueryCondition qCond);
	
}
