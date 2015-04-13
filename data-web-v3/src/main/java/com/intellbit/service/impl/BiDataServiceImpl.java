package com.intellbit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbit.dao.BiDataDao;
import com.intellbit.dataobject.BiDataDO;
import com.intellbit.service.BiDataService;

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
