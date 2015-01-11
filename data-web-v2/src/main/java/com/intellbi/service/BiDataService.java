package com.intellbi.service;

import java.util.List;

import com.intellbi.dataobject.BiDataDO;

public interface BiDataService {
	
	public BiDataDO getRecentBiDataDO();
	
	public List<BiDataDO> findAll();
	
}
