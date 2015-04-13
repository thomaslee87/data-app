package com.intellbit.dao;

import java.util.List;

import com.intellbit.dataobject.BiDataDO;

public interface BiDataDao {
		
	public List<BiDataDO> findAll();
	
	public BiDataDO findRecent();
	
}
