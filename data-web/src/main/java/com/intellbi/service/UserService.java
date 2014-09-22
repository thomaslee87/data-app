package com.intellbi.service;

import java.util.List;

import com.intellbi.dataobject.UserDO;

public interface UserService {
	
	public UserDO getUser(String username);
	
	public List<UserDO> getAllUsers();
	
	public void update(UserDO userDO);
}
