package com.intellbi.dataobject;

import java.io.Serializable;

public class BiFeedbackDO implements Serializable {
	
    private static final long serialVersionUID = 1067563670027768793L;
    
    private int id;
    private String phone;
	private String theMonth;
	private int cmtUserId;
	private int callNum;
	private int succCallNum;
	private int feedback;
	private int type;
	private int done;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getTheMonth() {
        return theMonth;
    }
    public void setTheMonth(String theMonth) {
        this.theMonth = theMonth;
    }
    public int getCmtUserId() {
        return cmtUserId;
    }
    public void setCmtUserId(int cmtUserId) {
        this.cmtUserId = cmtUserId;
    }
    public int getCallNum() {
        return callNum;
    }
    public void setCallNum(int callNum) {
        this.callNum = callNum;
    }
    public int getSuccCallNum() {
        return succCallNum;
    }
    public void setSuccCallNum(int succCallNum) {
        this.succCallNum = succCallNum;
    }
    public int getFeedback() {
        return feedback;
    }
    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getDone() {
        return done;
    }
    public void setDone(int done) {
        this.done = done;
    }
}
