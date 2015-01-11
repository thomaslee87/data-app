package com.intellbi.web;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.BiNoticeDao;
import com.intellbi.dao.UserDao;
import com.opensymphony.xwork2.ActionSupport;

public class NoticeAction extends ActionSupport {
	
	private static final long serialVersionUID = 6197472332651675389L;

	private Logger log = Logger.getLogger(NoticeAction.class); 
	
	@Autowired
    private UserDao userDao;
	
	@Autowired
	private BiNoticeDao biNoticeDao;
	
	public String listAllNotices() {
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			if(subject.getSession().getAttribute("group").equals("0")) {
				
				return SUCCESS;
			}
			return ERROR;
		}
		return LOGIN;
	}
	
}
