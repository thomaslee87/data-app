package com.intellbi.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.BiFeedbackDao;
import com.intellbi.dataobject.BiFeedbackDO;
import com.intellbi.dataobject.UserDO;
import com.opensymphony.xwork2.ActionSupport;

public class FeedbackAction extends ActionSupport {
	
    private static final long serialVersionUID = 4819353960724164655L;

    private Logger log = LoggerFactory.getLogger(FeedbackAction.class);
	
	@Autowired
    private BiFeedbackDao feedbackDao;
	
	private String theMonth;
	private String phone;
	private int type;
	private int done;
	
	private String callNum;
	private String succCallNum;
	private int feedback;
	
	private List<BiFeedbackDO> feedbackList;
	
	public String doGetPhoneFeedback() {
	    if(StringUtils.isBlank(theMonth) || StringUtils.isBlank(phone) ) 
	        return ERROR;
	    
	    Map<String,Object> param = new HashMap<String,Object>();
	    param.put("theMonth", theMonth);
	    param.put("phone", phone);
	    param.put("type", type);
	    param.put("done", done);
	    
	    setFeedbackList(feedbackDao.findConsumerFeedback(param));
	    
	    return SUCCESS;
	}
	
	public String doUpdatePhoneFeedback() {
	    String method = ServletActionContext.getRequest().getMethod();
        if (!"POST".equalsIgnoreCase(method))
            return ERROR;
	    if(StringUtils.isBlank(theMonth) || StringUtils.isBlank(phone)) 
            return ERROR;
	    
	    if(Integer.parseInt(callNum) < Integer.parseInt(succCallNum))
	        return ERROR;
        
        BiFeedbackDO biFeedbackDO = new BiFeedbackDO();
        biFeedbackDO.setPhone(phone);
        biFeedbackDO.setTheMonth(theMonth);
        biFeedbackDO.setCallNum(Integer.parseInt(callNum));
        biFeedbackDO.setSuccCallNum(Integer.parseInt(succCallNum));
        biFeedbackDO.setFeedback(feedback);
        biFeedbackDO.setType(type);
        biFeedbackDO.setDone(done);
        
        Session session =  SecurityUtils.getSubject().getSession();
        UserDO userDO = (UserDO)session.getAttribute("userDO");
        biFeedbackDO.setCmtUserId(userDO.getId());
        
        feedbackDao.update(biFeedbackDO);
        
        return SUCCESS;
	}
	
    public String getTheMonth() {
        return theMonth;
    }

    public void setTheMonth(String theMonth) {
        this.theMonth = theMonth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<BiFeedbackDO> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<BiFeedbackDO> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public String getCallNum() {
        return callNum;
    }

    public void setCallNum(String callNum) {
        this.callNum = callNum;
    }

    public String getSuccCallNum() {
        return succCallNum;
    }

    public void setSuccCallNum(String succCallNum) {
        this.succCallNum = succCallNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

	
}
