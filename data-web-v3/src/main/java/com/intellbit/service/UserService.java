package com.intellbit.service;

import java.util.List;

import com.intellbit.dataobject.UserDO;

public interface UserService {
	
	public UserDO getUser(String username);
	public UserDO getUser(int id);
	
	public List<UserDO> getAllUsers();
	
	public void update(UserDO userDO);
	
	public UserDO getUserIdOfContractPhone(String phone);
	public UserDO getUserIdOfMaintainPhone(String phone);
	public UserDO getUserIdOfBandwidthPhone(String phone);
}
