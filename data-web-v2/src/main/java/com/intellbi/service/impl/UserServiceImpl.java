package com.intellbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbi.dao.UserDao;
import com.intellbi.dataobject.UserDO;
import com.intellbi.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	public UserDO getUser(String username) {
		return userDao.findByUser(username);
	}
	
	public List<UserDO> getAllUsers() {
		return userDao.findAll();
	}
	
	public void update(UserDO userDO){
		userDao.update(userDO);
	}
	
}
