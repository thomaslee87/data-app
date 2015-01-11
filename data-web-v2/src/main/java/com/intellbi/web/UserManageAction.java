package com.intellbi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dataobject.UserDO;
import com.intellbi.service.UserService;
import com.opensymphony.xwork2.ActionSupport;

public class UserManageAction extends ActionSupport {

	private static final long serialVersionUID = -3071831805360350487L;

	@Autowired
	private UserService userService;
	
	private List<UserDO> users;

	public List<UserDO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDO> users) {
		this.users = users;
	}
	
	public String getAllUsers() {
		setUsers(userService.getAllUsers());
		return SUCCESS;
	}
	
}
