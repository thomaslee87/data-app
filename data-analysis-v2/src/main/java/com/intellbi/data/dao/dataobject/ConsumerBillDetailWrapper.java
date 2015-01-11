package com.intellbi.data.dao.dataobject;

import com.google.gson.Gson;

public class ConsumerBillDetailWrapper {

	private String theMonth;
	private String phoneNo;
	private TelecomPackage thePackage;
	
	private String status;
	private String contractFrom ;
	private String contractTo;
	
	private int isGroupUser;
	private int network;
	
	private ConsumerBillDetail billDetail;
	
	public int getIsGroupUser() {
		return isGroupUser;
	}

	public void setIsGroupUser(int isGroupUser) {
		this.isGroupUser = isGroupUser;
	}

	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(String theMonth) {
		this.theMonth = theMonth;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getContractFrom() {
		return contractFrom;
	}

	public void setContractFrom(String contractFrom) {
		this.contractFrom = contractFrom;
	}

	public String getContractTo() {
		return contractTo;
	}

	public void setContractTo(String contractTo) {
		this.contractTo = contractTo;
	}

	public ConsumerBillDetail getBillDetail() {
		return billDetail;
	}

	public void setBillDetail(ConsumerBillDetail billDetail) {
		this.billDetail = billDetail;
	}

	public TelecomPackage getThePackage() {
		return thePackage;
	}

	public void setThePackage(TelecomPackage thePackage) {
		this.thePackage = thePackage;
	}
	
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
