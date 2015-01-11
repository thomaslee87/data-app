package com.intellbi.dao;

import java.util.List;

import com.intellbi.dataobject.BiNoticeDO;

public interface BiNoticeDao {
		
	public List<BiNoticeDO> findAll();
	
	public BiNoticeDO findById(int id);
	
	public void save(BiNoticeDO biNoticeDO);
	
	public void update(BiNoticeDO biNoticeDO);
	
}
