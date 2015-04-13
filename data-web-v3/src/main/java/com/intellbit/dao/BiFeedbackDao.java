package com.intellbit.dao;

import java.util.List;
import java.util.Map;

import com.intellbit.dataobject.BiFeedbackDO;

public interface BiFeedbackDao {
		
	public List<BiFeedbackDO> findConsumerFeedback(Map<String,Object> map);
	
	public void update(BiFeedbackDO biFeedbackDO);
	
}
