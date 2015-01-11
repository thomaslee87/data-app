package com.intellbi.dataobject;

public class TelecomPackageDO {
	
	private int id;

	private String name;
	
	//package contains:
	private double fee;
	private double voice;
	private double localVoice;
	private double longDistVoice;
	private double gprs;
	private int sms;
	private int mms;
	//price when over budget:
	private double gprsPrice ;
	private double localVoicePrice;    //local 
	private double longDistVoicePrice; //long distance
	private double smsPrice;
	
	private String feature;
	
	private boolean isStandard = true;
	
	public boolean isStandard() {
		return isStandard;
	}


	public void setStandard(boolean isStandard) {
		this.isStandard = isStandard;
	}


	public int getMms() {
		return mms;
	}


	public void setMms(int mms) {
		this.mms = mms;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFeature() {
		return feature;
	}


	public void setFeature(String feature) {
		this.feature = feature;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getFee() {
		return fee;
	}


	public void setFee(double fee) {
		this.fee = fee;
	}


	public double getVoice() {
		return voice;
	}


	public void setVoice(double voice) {
		this.voice = voice;
	}


	public double getLocalVoice() {
		return localVoice;
	}


	public void setLocalVoice(double localVoice) {
		this.localVoice = localVoice;
	}


	public double getLongDistVoice() {
		return longDistVoice;
	}


	public void setLongDistVoice(double longDistVoice) {
		this.longDistVoice = longDistVoice;
	}


	public double getGprs() {
		return gprs;
	}


	public void setGprs(double gprs) {
		this.gprs = gprs;
	}


	public int getSms() {
		return sms;
	}


	public void setSms(int sms) {
		this.sms = sms;
	}


	public double getGprsPrice() {
		return gprsPrice;
	}


	public void setGprsPrice(double gprsPrice) {
		this.gprsPrice = gprsPrice;
	}

	public double getLocalVoicePrice() {
		return localVoicePrice;
	}


	public void setLocalVoicePrice(double localVoicePrice) {
		this.localVoicePrice = localVoicePrice;
	}


	public double getLongDistVoicePrice() {
		return longDistVoicePrice;
	}


	public void setLongDistVoicePrice(double longDistVoicePrice) {
		this.longDistVoicePrice = longDistVoicePrice;
	}


	public double getSmsPrice() {
		return smsPrice;
	}


	public void setSmsPrice(double smsPrice) {
		this.smsPrice = smsPrice;
	}

}
