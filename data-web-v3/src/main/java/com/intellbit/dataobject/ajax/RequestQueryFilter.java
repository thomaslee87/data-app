package com.intellbit.dataobject.ajax;

import java.io.Serializable;

public class RequestQueryFilter implements Serializable {
	
	private static final long serialVersionUID = -6292191209866317513L;
	
	private String phone;
	private String fromStart;
	private String fromEnd;
	private String toStart;
	private String toEnd;
	private int price;
	private boolean hideDone;
	
	public boolean isHideDone() {
		return hideDone;
	}
	public void setHideDone(boolean hideDone) {
		this.hideDone = hideDone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFromStart() {
		return fromStart;
	}
	public void setFromStart(String fromStart) {
		this.fromStart = fromStart;
	}
	public String getFromEnd() {
		return fromEnd;
	}
	public void setFromEnd(String fromEnd) {
		this.fromEnd = fromEnd;
	}
	public String getToStart() {
		return toStart;
	}
	public void setToStart(String toStart) {
		this.toStart = toStart;
	}
	public String getToEnd() {
		return toEnd;
	}
	public void setToEnd(String toEnd) {
		this.toEnd = toEnd;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
