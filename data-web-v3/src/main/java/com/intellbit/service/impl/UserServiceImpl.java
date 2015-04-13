package com.intellbit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intellbit.dao.UserDao;
import com.intellbit.dataobject.UserDO;
import com.intellbit.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	public UserDO getUser(String username) {
		// TODO Auto-generated method stub
		return userDao.findByUser(username);
	}
	
	public List<UserDO> getAllUsers() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}
	
	public void update(UserDO userDO){
		// TODO Auto-generated method stub
		userDao.update(userDO);
	}

	@Override
	public UserDO getUser(int id) {
		// TODO Auto-generated method stub
		return userDao.get(id);
	}

	@Override
	public UserDO getUserIdOfContractPhone(String phone) {
		// TODO Auto-generated method stub
		return userDao.getUserIdOfContractPhone(phone);
	}

	@Override
	public UserDO getUserIdOfMaintainPhone(String phone) {
		// TODO Auto-generated method stub
		return userDao.getUserIdOfMaintainPhone(phone);
	}

	@Override
	public UserDO getUserIdOfBandwidthPhone(String phone) {
		// TODO Auto-generated method stub
		return userDao.getUserIdOfBandwidthPhone(phone);
	}
	
	
	
}
