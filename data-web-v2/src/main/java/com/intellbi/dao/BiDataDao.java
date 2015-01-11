package com.intellbi.dao;

import java.util.List;

import com.intellbi.dataobject.BiDataDO;

public interface BiDataDao {
		
	public List<BiDataDO> findAll();
	
	public BiDataDO findRecent();
	
}
