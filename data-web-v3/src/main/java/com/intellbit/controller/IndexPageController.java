package com.intellbit.controller;

import java.util.HashMap;
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
@RequestMapping("/index.html")
public class IndexPageController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexPageController.class);

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView showOverview(
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> data = new HashMap<String,Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("group", String.valueOf(userDO.getGroupId()));
		return new ModelAndView("index", data);
	}
	
}
