package com.intellbit.dao;

import java.util.List;

import com.intellbit.dataobject.TelecomPackageDO;

public interface TelecomPackageDao {
		
	TelecomPackageDO findById(int id);
	
	List<TelecomPackageDO> getAll();
	
	List<TelecomPackageDO> getAll4G();
	
}
