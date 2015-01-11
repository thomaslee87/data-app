package com.intellbi.data.dao.dataobject;

public enum UserConsumeType {
	NORMAL(0), //普通
	DOUBLE_HIGH(1), //双高
	SINGLE_HIGH_GPRS(2), //单高流量
	SINGLE_HIGH_VOICE(3), //单高语音
	DOUBLE_LOW(4); //双低
	
	private UserConsumeType(int value) {
		this.value = value;
	}
	
	private int value;
	
	public int getValue() {
		return value;
	}
	
}
