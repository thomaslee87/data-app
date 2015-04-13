package com.intellbit.service;

import java.util.List;

import com.intellbit.dataobject.BiDataDO;

public interface BiDataService {
	
	public BiDataDO getRecentBiDataDO();
	
	public List<BiDataDO> findAll();
	
}
