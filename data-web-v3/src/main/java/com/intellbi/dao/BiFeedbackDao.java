package com.intellbi.dao;

import java.util.List;
import java.util.Map;

import com.intellbi.dataobject.BiFeedbackDO;

public interface BiFeedbackDao {
		
	public List<BiFeedbackDO> findConsumerFeedback(Map<String,Object> map);
	
	public void update(BiFeedbackDO biFeedbackDO);
	
}
