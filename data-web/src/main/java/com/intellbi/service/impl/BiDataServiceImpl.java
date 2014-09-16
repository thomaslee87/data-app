package com.intellbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbi.dao.BiDataDao;
import com.intellbi.dataobject.BiDataDO;
import com.intellbi.service.BiDataService;

@Service("biDataService")
public class BiDataServiceImpl implements BiDataService {
	
	@Autowired
	private BiDataDao biDataDao;

	public BiDataDO getRecentBiDataDO() {
		// TODO Auto-generated method stub
		return biDataDao.findRecent();
	}

	public List<BiDataDO> findAll() {
		// TODO Auto-generated method stub
		return biDataDao.findAll();
	}
	
	
}
