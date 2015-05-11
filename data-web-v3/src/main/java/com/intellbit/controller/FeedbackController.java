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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intellbit.dao.BiFeedbackDao;
import com.intellbit.dataobject.BiFeedbackDO;
import com.intellbit.dataobject.RespMsg;
import com.intellbit.dataobject.UserDO;
import com.intellbit.service.UserService;
import com.intellbit.utils.Const;

@Controller
@RequestMapping(value = {"/maintain", "/contract", "/bandwidth"})
public class FeedbackController {
	
	private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private BiFeedbackDao feedbackDao;

	@RequestMapping(value = "/getFeedback", method = RequestMethod.GET)
	public @ResponseBody RespMsg<List<BiFeedbackDO>> getFeedback(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("theMonth") String theMonth,
			@RequestParam("phone") String phone,
			@RequestParam("type") int type) {
		
		    Map<String,Object> param = new HashMap<String,Object>();
		    param.put("theMonth", theMonth);
		    param.put("phone", phone);
		    param.put("type", type);

		    RespMsg<List<BiFeedbackDO>> resp = new RespMsg<>();
		    resp.setData(feedbackDao.findConsumerFeedback(param));
		    
		    return resp;
	}
	
	@RequestMapping(value = "/postFeedback", method = RequestMethod.POST)
	public @ResponseBody RespMsg<String> postFeedback(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("phone") String phone,
			@RequestParam("theMonth") String theMonth,
			@RequestParam("callNum") int callNum,
			@RequestParam("succCallNum") int succCallNum,
			@RequestParam("feedback") int feedback,
			@RequestParam("type") int type,
			@RequestParam("done") int done) {
		
		RespMsg<String> resp = new RespMsg<>();
		if (callNum < succCallNum) {
			resp.setErrCode(Const.ERROR_CODE);
			resp.setErrMsg("callNum should no less than succCallNum");
			resp.setData(Const.FAILED);
			return resp;
		}
		
        BiFeedbackDO biFeedbackDO = new BiFeedbackDO();
        biFeedbackDO.setPhone(phone);
        biFeedbackDO.setTheMonth(theMonth);
        biFeedbackDO.setCallNum(callNum);
        biFeedbackDO.setSuccCallNum(succCallNum);
        biFeedbackDO.setFeedback(feedback);
        biFeedbackDO.setType(type);
        biFeedbackDO.setDone(done);
        
        int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		if (userDO == null) {
			resp.setErrCode(Const.NOTLONGIN_CODE);
			resp.setErrMsg(Const.NOTALLOWED);
			resp.setData(Const.FAILED);
			return resp;
		}
        biFeedbackDO.setCmtUserId(userDO.getId());
        
        try {
        	feedbackDao.update(biFeedbackDO);
        } catch (Exception e) {
        	 resp.setErrCode(Const.ERROR_CODE);
             resp.setErrMsg("insert db error");
             resp.setData(Const.FAILED);
             logger.error(e.getMessage(), e);
             return resp;
        }
        
        resp.setErrCode(Const.SUCCESS_CODE);
        resp.setErrMsg(Const.SUCCESS);
        resp.setData(Const.SUCCESS);
        
        return resp;
	}
	
}
