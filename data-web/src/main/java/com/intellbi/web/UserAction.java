package com.intellbi.web;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.UserDao;
import com.intellbi.dataobject.UserDO;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	private static final long serialVersionUID = -1417237614181805435L;
	
    @Autowired
    private UserDao userDao;
    
	private String username;
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 跳转到登录界面
	 * @return
	 */
	public String login_input() {
		return SUCCESS;
	}
	
	/**
	 * 登录
	 * @return
	 */
	public String login() {
		System.out.println("name->" + username);
		System.out.println("password->" + password);
		
		return SUCCESS;
	}
	
	@Override
	public String execute() {
		List<UserDO> o = userDao.findAll();
		System.out.println(o.size());
		
		try {
			AuthenticationToken token = new UsernamePasswordToken(username,password);
			Subject currUser = SecurityUtils.getSubject();
			currUser.login(token);
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
