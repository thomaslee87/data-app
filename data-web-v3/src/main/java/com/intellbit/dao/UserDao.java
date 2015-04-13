package com.intellbit.dao;

import com.intellbit.dataobject.UserDO;

public interface UserDao extends Dao<UserDO, Integer>{
	
	UserDO findByUser(String username);
	
	public UserDO getUserIdOfContractPhone(String phone);
	public UserDO getUserIdOfMaintainPhone(String phone);
	public UserDO getUserIdOfBandwidthPhone(String phone);
	
}
