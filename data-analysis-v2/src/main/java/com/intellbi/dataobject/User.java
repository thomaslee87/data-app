package com.intellbi.dataobject;

import java.util.Date;

import com.google.gson.Gson;

public class User {
	private int id;
	private int groupId;
	private String username;
	private String password;
	private String nickname;
	private Date gmtLogin;
	private Date gmtModified;
	
	
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getGroupId() {
		return groupId;
	}


	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public Date getGmtLogin() {
		return gmtLogin;
	}


	public void setGmtLogin(Date gmtLogin) {
		this.gmtLogin = gmtLogin;
	}


	public Date getGmtModified() {
		return gmtModified;
	}


	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
}
