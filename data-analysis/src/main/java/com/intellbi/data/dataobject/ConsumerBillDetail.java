package com.intellbi.data.dataobject;

public class ConsumerBillDetail {
	
	private double income;
	private int callAmount;
	private int callTime;
	private int localCallTime;
	private int longDistCallTime;
	private double gprs;
	private int sms;
	
	private double rental;
	private double localCallFee;
	private double roamCallFee;
	private double longDistCallFee;
	private double valueAddedFee;
	private double otherFee;
	private double grantFee;

	private int roamCallTime;
	private int internalCallTime;
	private int internationalCallTime;
	
	public double getRental() {
		return rental;
	}
	public void setRental(double rental) {
		this.rental = rental;
	}
	public double getLocalCallFee() {
		return localCallFee;
	}
	public void setLocalCallFee(double localCallFee) {
		this.localCallFee = localCallFee;
	}
	public double getRoamCallFee() {
		return roamCallFee;
	}
	public void setRoamCallFee(double roamCallFee) {
		this.roamCallFee = roamCallFee;
	}
	public double getLongDistCallFee() {
		return longDistCallFee;
	}
	public void setLongDistCallFee(double longDistCallFee) {
		this.longDistCallFee = longDistCallFee;
	}
	public double getValueAddedFee() {
		return valueAddedFee;
	}
	public void setValueAddedFee(double valueAddedFee) {
		this.valueAddedFee = valueAddedFee;
	}
	public double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(double otherFee) {
		this.otherFee = otherFee;
	}
	public double getGrantFee() {
		return grantFee;
	}
	public void setGrantFee(double grantFee) {
		this.grantFee = grantFee;
	}
	public int getRoamCallTime() {
		return roamCallTime;
	}
	public void setRoamCallTime(int roamCallTime) {
		this.roamCallTime = roamCallTime;
	}
	public int getInternalCallTime() {
		return internalCallTime;
	}
	public void setInternalCallTime(int internalCallTime) {
		this.internalCallTime = internalCallTime;
	}
	public int getInternationalCallTime() {
		return internationalCallTime;
	}
	public void setInternationalCallTime(int internationalCallTime) {
		this.internationalCallTime = internationalCallTime;
	}
	public int getLocalCallTime() {
		return localCallTime;
	}
	public void setLocalCallTime(int localCallTime) {
		this.localCallTime = localCallTime;
	}
	public int getLongDistCallTime() {
		return longDistCallTime;
	}
	public void setLongDistCallTime(int longDistCallTime) {
		this.longDistCallTime = longDistCallTime;
	}
	public int getSms() {
		return sms;
	}
	public void setSms(int sms) {
		this.sms = sms;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public int getCallAmount() {
		return callAmount;
	}
	public void setCallAmount(int callAmount) {
		this.callAmount = callAmount;
	}
	public int getCallTime() {
		return callTime;
	}
	public void setCallTime(int callTime) {
		this.callTime = callTime;
	}
	public double getGprs() {
		return gprs;
	}
	public void setGprs(double gprs) {
		this.gprs = gprs;
	}
	
}
