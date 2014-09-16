package com.intellbi.dataobject;

import java.io.Serializable;

public class ConsumerBillDO implements Serializable {

	private static final long serialVersionUID = -6385540613995771154L;

	private long id;
	private String phoneNo;
	private int yearMonth;
	private int packageId;
	private double income;
	private double monthlyRental;
	private double localVoiceFee;
	private double roamingFee;
	private double longDistanceVoiceFee;
	private double valueAddedFee;
	private double otherFee;
	private double grantFee;
	private double callNumber;
	private int localCallDuration;
	private int roamCallDuration;
	private int longDistanceCallDuration;
	private int internalCallDuration;
	private int internationlCallDuration;
	private int sms;
	private double gprs;
	private int isGroupUser;
	private int network;
	private String status;
	private double contractFrom;
	private double contractTo;
	
	private String packageName;
	
	public String getPackageName() {
		return "套餐";
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getContractTo() {
		return contractTo;
	}
	public void setContractTo(double contractTo) {
		this.contractTo = contractTo;
	}
	public double getContractFrom() {
		return contractFrom;
	}
	public void setContractFrom(double contractFrom) {
		this.contractFrom = contractFrom;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public int getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(int yearMonth) {
		this.yearMonth = yearMonth;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public double getMonthlyRental() {
		return monthlyRental;
	}
	public void setMonthlyRental(double monthlyRental) {
		this.monthlyRental = monthlyRental;
	}
	public double getLocalVoiceFee() {
		return localVoiceFee;
	}
	public void setLocalVoiceFee(double localVoiceFee) {
		this.localVoiceFee = localVoiceFee;
	}
	public double getRoamingFee() {
		return roamingFee;
	}
	public void setRoamingFee(double roamingFee) {
		this.roamingFee = roamingFee;
	}
	public double getLongDistanceVoiceFee() {
		return longDistanceVoiceFee;
	}
	public void setLongDistanceVoiceFee(double longDistanceVoiceFee) {
		this.longDistanceVoiceFee = longDistanceVoiceFee;
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
	public double getCallNumber() {
		return callNumber;
	}
	public void setCallNumber(double callNumber) {
		this.callNumber = callNumber;
	}
	public int getLocalCallDuration() {
		return localCallDuration;
	}
	public void setLocalCallDuration(int localCallDuration) {
		this.localCallDuration = localCallDuration;
	}
	public int getRoamCallDuration() {
		return roamCallDuration;
	}
	public void setRoamCallDuration(int roamCallDuration) {
		this.roamCallDuration = roamCallDuration;
	}
	public int getLongDistanceCallDuration() {
		return longDistanceCallDuration;
	}
	public void setLongDistanceCallDuration(int longDistanceCallDuration) {
		this.longDistanceCallDuration = longDistanceCallDuration;
	}
	public int getInternalCallDuration() {
		return internalCallDuration;
	}
	public void setInternalCallDuration(int internalCallDuration) {
		this.internalCallDuration = internalCallDuration;
	}
	public int getInternationlCallDuration() {
		return internationlCallDuration;
	}
	public void setInternationlCallDuration(int internationlCallDuration) {
		this.internationlCallDuration = internationlCallDuration;
	}
	public int getSms() {
		return sms;
	}
	public void setSms(int sms) {
		this.sms = sms;
	}
	public double getGprs() {
		return gprs;
	}
	public void setGprs(double gprs) {
		this.gprs = gprs;
	}
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
