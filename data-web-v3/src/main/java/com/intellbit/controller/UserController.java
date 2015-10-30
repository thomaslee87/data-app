package com.intellbit.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.intellbit.dataobject.RespMsg;
import com.intellbit.dataobject.UserDO;
import com.intellbit.service.UserService;
import com.intellbit.utils.Const;

@Controller
@RequestMapping("")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public ModelAndView openLoginHtml(
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("msg", "");
		ModelAndView mav = new ModelAndView("login", data);
		return mav;
	}
	
	@RequestMapping(value = "/logout.html", method = RequestMethod.GET)
	public ModelAndView doLogout(HttpServletRequest request) {
		request.getSession().removeAttribute(Const.SS_USER_ID);
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView doLogin(
			@RequestParam(Const.SS_USER_NAME) String username,
			@RequestParam(Const.SS_PASSWORD) String password,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			username = new String(username.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn(e.getMessage(), e);
		}
		Object oUserId = request.getSession().getAttribute(Const.SS_USER_ID);
		if (oUserId == null) {
			UserDO userDO = userService.getUser(username);
			if (userDO == null || !userDO.getPassword().equals(password)) {
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("msg", Const.MSG_LOGIN_FAILED);
				logger.info("[LOGIN FAILED]username:" + username);
				return new ModelAndView("login", data);
			}
			request.getSession().setAttribute(Const.SS_USER_ID, userDO.getId());
			userDO.setGmtLogin(new Date());
			userService.update(userDO);
		}
		logger.info("[LOGIN SUCCESS]username:" + username);
		return new ModelAndView(new RedirectView("index.html"));
	}
	
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public ModelAndView doLogin(
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("login");
		
		Object oUserId = request.getSession().getAttribute(Const.SS_USER_ID);
		if (oUserId != null) {
			UserDO userDO = userService.getUser(Integer.valueOf((String) oUserId));
			request.getSession().removeAttribute(Const.SS_USER_ID);
			logger.info("[LOGOUT]username:" + userDO.getUsername());
		}
		return mav;
	}
	
	@RequestMapping(value = "/api/setPassword", method = RequestMethod.POST)
	public @ResponseBody RespMsg<String> resetPassword(
			@RequestParam(Const.SS_PASSWORD) String password,
			@RequestParam("oldpassword") String oldpasswd,
			HttpServletRequest request,
			HttpServletResponse response){
		RespMsg<String> respMsg = new RespMsg<String>();
		
		Object oUserId = request.getSession().getAttribute(Const.SS_USER_ID);
		if (oUserId == null) {
			respMsg.setErrCode(Const.NOTLONGIN_CODE);
			respMsg.setErrMsg(Const.NOTALLOWED);
			respMsg.setData("请登录后再操作！");
		} else {
			UserDO userDO = userService.getUser(Integer.valueOf(oUserId.toString()));
			if (userDO != null) {
				if (!userDO.getPassword().equals(oldpasswd)) {
					respMsg.setErrCode(Const.ERROR_CODE);
					respMsg.setErrMsg(Const.NOTALLOWED);
					respMsg.setData("原密码不正确");
				} else if (StringUtils.isNotBlank(password)) {
					userDO.setPassword(password);
        			userDO.setGmtModified(new Date());
        			userService.update(userDO);
        			respMsg.setErrCode(Const.SUCCESS_CODE);
        			respMsg.setErrMsg(Const.SUCCESS);
        			respMsg.setData("密码修改成功!");
				}
			}
		}
		return respMsg;
	}
	
	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public @ResponseBody RespMsg<List<UserDO>> getAllUsers(
			HttpServletRequest request,
			HttpServletResponse response){
		
		RespMsg<List<UserDO>> respMsg = new RespMsg<List<UserDO>>();

		int userId = Integer.valueOf(request.getSession().getAttribute(Const.SS_USER_ID).toString());
		UserDO userDO = userService.getUser(userId);
		if (userDO.getGroupId() == Const.ADMIN_GROUP_ID) {
			respMsg.setErrCode(Const.SUCCESS_CODE);
			respMsg.setErrMsg(Const.SUCCESS);
			respMsg.setData(userService.getAllUsers());
		} else {
			respMsg.setErrCode(Const.ERROR_CODE);
			respMsg.setErrMsg(Const.FAILED);
		}
		return respMsg;
	}
	
}
