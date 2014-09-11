package com.intellbi.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.UserDao;
import com.intellbi.dataobject.UserDO;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	private static final long serialVersionUID = -1417237614181805435L;
	
	private Logger log = Logger.getLogger(UserAction.class); 
	
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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String login() {
		
		String method = ServletActionContext.getRequest().getMethod();
		if(!"POST".equalsIgnoreCase(method))
			return ERROR;
		
		UserDO userDO = null;
		if(StringUtils.isNotBlank(username))
			userDO = userDao.findByUser(username);
		
		if(userDO == null) {
			return ERROR;
		}
		
		log.info("loginName:" + username + ";loginPassword:" + password);
		String errorMessage = "";
		
		AuthenticationToken token = new UsernamePasswordToken(username,password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return SUCCESS;
		} catch (UnknownAccountException uae) {
			errorMessage = "用户认证失败：" + "username wasn't in the system.";
			log.info(errorMessage);
		} catch (IncorrectCredentialsException ice) {
			errorMessage = "用户认证失败：" + "password didn't match.";
			log.info(errorMessage);
		} catch (LockedAccountException lae) {
			errorMessage = "用户认证失败：" + "account for that username is locked - can't login.";
			log.info(errorMessage);
		} catch (AuthenticationException e) {
			errorMessage = "登录失败错误信息：" + e;
			log.error(errorMessage);
			e.printStackTrace();
		}
		subject.getSession().setAttribute("errorMessage", errorMessage);
		return ERROR;
	}
	
	public String logout(){  
        Subject subject = SecurityUtils.getSubject();  
		String username = (String) subject.getSession().getAttribute("username");
        try{  
            //退出登录（移除当前用户的信息）  
            subject.logout();  
            javax.servlet.http.HttpSession session = ServletActionContext.getRequest().getSession();  
            session.invalidate();  
            log.info("user " + username + " logout.");
        }catch(Exception e){  
            log.error("logout异常：", e);  
            e.printStackTrace();  
        }  
        return SUCCESS;  
    }  
	
}
