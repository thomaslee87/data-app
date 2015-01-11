package com.intellbi.web;

import java.util.Date;

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
import com.intellbi.dataobject.SysMsg;
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
	
	private String errorMessage;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String doLogin() {
		
		String method = ServletActionContext.getRequest().getMethod();
//		if(!"POST".equalsIgnoreCase(method))
//			return ERROR;
		
//		UserDO userDO = null;
//		if(StringUtils.isNotBlank(username))
//			userDO = userDao.findByUser(username);
//		
//		if(userDO == null) {
//			return ERROR;
//		}
		
		log.info("loginName:" + username + ";loginPassword:" + password);
		
		AuthenticationToken token = new UsernamePasswordToken(username,password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return SUCCESS;
		} catch (UnknownAccountException uae) {
			errorMessage = "用户不存在或者密码错误";
			log.info(errorMessage);
		} catch (IncorrectCredentialsException ice) {
			errorMessage = "用户不存在或者密码错误";
			log.info(errorMessage);
		} catch (LockedAccountException lae) {
			errorMessage = "用户锁定";
			log.info(errorMessage);
		} catch (AuthenticationException e) {
			errorMessage = "登录失败错误信息：" + e;
			log.error(errorMessage);
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public String logout(){  
        Subject subject = SecurityUtils.getSubject();  
        if(subject.isAuthenticated()) {
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
	            return ERROR;
	        }  
        }
        return LOGIN;
    }
	
	private SysMsg sysMsg;
	
	public SysMsg getSysMsg() {
		return sysMsg;
	}

	public void setSysMsg(SysMsg sysMsg) {
		this.sysMsg = sysMsg;
	}

	private String oldpassword;
	
	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String setPasswd(){
		sysMsg = new SysMsg();
		sysMsg.setMsgCode(SysMsg.NOTLONGIN);
		sysMsg.setMsgText("请登录后再操作！");
		Subject subject = SecurityUtils.getSubject();  
        if(subject.isAuthenticated()) {
        	sysMsg.setMsgCode(SysMsg.ERROR);
        	sysMsg.setMsgText("请确认用户存在且密码不为空！");
    		String method = ServletActionContext.getRequest().getMethod();
    		if(!"POST".equalsIgnoreCase(method)){
    			sysMsg.setMsgText("错误请求，请确认您在合法操作！");
    			return SUCCESS;
    		}
    		
        	String username = (String) subject.getSession().getAttribute("username");
        	UserDO userDO = userDao.findByUser(username);
        	if(userDO != null) {
        		if(!userDO.getPassword().equals(oldpassword))
        			sysMsg.setMsgText("原密码不正确！");
        		else if(StringUtils.isNotBlank(password)) {
        			userDO.setPassword(password);
        			userDO.setGmtModified(new Date());
        			userDao.update(userDO);
        			sysMsg.setMsgCode(SysMsg.SUCCESS);
        			sysMsg.setMsgText("密码修改成功!");
        		}
        	}
        }
        return SUCCESS;
	}
	
}
