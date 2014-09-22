package com.intellbi.data.dataobject;

import com.google.gson.Gson;

public class VipMgrInfo {

	private String username;
	private String realname;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
