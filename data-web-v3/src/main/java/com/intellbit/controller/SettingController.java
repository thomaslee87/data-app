package com.intellbit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.intellbit.dataobject.UserDO;
import com.intellbit.service.UserService;
import com.intellbit.utils.Const;

@Controller
@RequestMapping("")
public class SettingController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/userManager.html", method = RequestMethod.GET)
	public ModelAndView openUserManager(
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> data = new HashMap<String,Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("group", String.valueOf(userDO.getGroupId()));
		if (userDO.getGroupId() != 0) {
			data.put("info", Const.INFO_NOT_ALLOWED);
			return new ModelAndView("error", data);
		}
		List<UserDO> users = userService.getAllUsers();
		data.put("users", users);
		return new ModelAndView("userManager", data);
	}
	
	@RequestMapping(value = "/profile.html", method = RequestMethod.GET)
	public ModelAndView openProfile(
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> data = new HashMap<String,Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("group", String.valueOf(userDO.getGroupId()));
		List<UserDO> users = userService.getAllUsers();
		data.put("users", users);
		return new ModelAndView("profile", data);
	}
	
}
