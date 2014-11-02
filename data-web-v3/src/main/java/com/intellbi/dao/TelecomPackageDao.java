package com.intellbi.dao;

import java.util.List;

import com.intellbi.dataobject.TelecomPackageDO;

public interface TelecomPackageDao {
		
	TelecomPackageDO findById(int id);
	
	List<TelecomPackageDO> getAll();
	
}
