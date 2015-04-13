package com.intellbit.dataobject;

import java.io.Serializable;
import java.util.Date;

public class BiMapDO implements Serializable {
	
	private static final long serialVersionUID = -3542104997747994123L;
	
	private int id;
	private int userId;
	private String phone;
	private Date ctime;
	private Date utime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public Date getUtime() {
		return utime;
	}
	public void setUtime(Date utime) {
		this.utime = utime;
	}
	
}
