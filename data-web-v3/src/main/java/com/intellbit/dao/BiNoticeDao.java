package com.intellbit.dao;

import java.util.List;

import com.intellbit.dataobject.BiNoticeDO;

public interface BiNoticeDao {
		
	public List<BiNoticeDO> findAll();
	
	public BiNoticeDO findById(int id);
	
	public void save(BiNoticeDO biNoticeDO);
	
	public void update(BiNoticeDO biNoticeDO);
	
}
