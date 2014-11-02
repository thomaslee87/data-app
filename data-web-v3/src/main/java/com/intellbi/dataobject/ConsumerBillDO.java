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
	
	private int contractRemain;
	
	public int getContractRemain() {
        return contractRemain;
    }
    public void setContractRemain(int contractRemain) {
        this.contractRemain = contractRemain;
    }
    private String packageName;
	
	private int consumerType;
	
	private double regularScore;
	private String recommend;
	private double priority;
	
	private String priorityDesc;
	private String valueChangeDesc;
	
	private double valueChange;
	
	private String recommend1;
	private String recommend2;
	private String recommend3;
	
	private double recommendCost1;
	private double recommendCost2;
	private double recommendCost3;
	
	private int isContractConsumer;
	
	public int getIsContractConsumer() {
		return isContractConsumer;
	}
	public void setIsContractConsumer(int isContractConsumer) {
		this.isContractConsumer = isContractConsumer;
	}
	public double getRecommendCost1() {
        return recommendCost1;
    }
    public void setRecommendCost1(double recommendCost1) {
        this.recommendCost1 = recommendCost1;
    }
    public double getRecommendCost2() {
        return recommendCost2;
    }
    public void setRecommendCost2(double recommendCost2) {
        this.recommendCost2 = recommendCost2;
    }
    public double getRecommendCost3() {
        return recommendCost3;
    }
    public void setRecommendCost3(double recommendCost3) {
        this.recommendCost3 = recommendCost3;
    }
    public double getValueChange() {
		return valueChange;
	}
	public void setValueChange(double valueChange) {
		this.valueChange = valueChange;
	}
	public String getValueChangeDesc() {
		return valueChangeDesc;
	}
	public void setValueChangeDesc(String valueChangeDesc) {
		this.valueChangeDesc = valueChangeDesc;
	}
	public String getPriorityDesc() {
		return priorityDesc;
	}
	public void setPriorityDesc(String priorityDesc) {
		this.priorityDesc = priorityDesc;
	}
	public double getPriority() {
		return priority;
	}
	public void setPriority(double priority) {
		this.priority = priority;
	}
	public String getRecommend1() {
		return recommend1;
	}
	public void setRecommend1(String recommend1) {
		this.recommend1 = recommend1;
	}
	public String getRecommend2() {
		return recommend2;
	}
	public void setRecommend2(String recommend2) {
		this.recommend2 = recommend2;
	}
	public String getRecommend3() {
		return recommend3;
	}
	public void setRecommend3(String recommend3) {
		this.recommend3 = recommend3;
	}
	public double getRegularScore() {
		return regularScore;
	}
	public void setRegularScore(double regularScore) {
		this.regularScore = regularScore;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public int getConsumerType() {
		return consumerType;
	}
	public void setConsumerType(int consumerType) {
		this.consumerType = consumerType;
	}
	private double income6 = 0;
	private double income3 = 0;
	private double voice6 = 0;
	private double voice3 = 0;
	private double gprs6 = 0;
	private double gprs3 = 0;
	private double packageSpill = 0;
	private double voiceSpill = 0;
	private double gprsSpill = 0;
	
	private double incomeFluctuation ;
	private double voiceFluctuation ;
	private double gprsFluctuation ;
	
	private double score;
	
	public double getIncome6() {
		return income6;
	}
	public void setIncome6(double income6) {
		this.income6 = income6;
	}
	public double getIncome3() {
		return income3;
	}
	public void setIncome3(double income3) {
		this.income3 = income3;
	}
	public double getVoice6() {
		return voice6;
	}
	public void setVoice6(double voice6) {
		this.voice6 = voice6;
	}
	public double getVoice3() {
		return voice3;
	}
	public void setVoice3(double voice3) {
		this.voice3 = voice3;
	}
	public double getGprs6() {
		return gprs6;
	}
	public void setGprs6(double gprs6) {
		this.gprs6 = gprs6;
	}
	public double getGprs3() {
		return gprs3;
	}
	public void setGprs3(double gprs3) {
		this.gprs3 = gprs3;
	}
	public double getPackageSpill() {
		return packageSpill;
	}
	public void setPackageSpill(double packageSpill) {
		this.packageSpill = packageSpill;
	}
	public double getVoiceSpill() {
		return voiceSpill;
	}
	public void setVoiceSpill(double voiceSpill) {
		this.voiceSpill = voiceSpill;
	}
	public double getGprsSpill() {
		return gprsSpill;
	}
	public void setGprsSpill(double gprsSpill) {
		this.gprsSpill = gprsSpill;
	}
	public double getIncomeFluctuation() {
		return incomeFluctuation;
	}
	public void setIncomeFluctuation(double incomeFluctuation) {
		this.incomeFluctuation = incomeFluctuation;
	}
	public double getVoiceFluctuation() {
		return voiceFluctuation;
	}
	public void setVoiceFluctuation(double voiceFluctuation) {
		this.voiceFluctuation = voiceFluctuation;
	}
	public double getGprsFluctuation() {
		return gprsFluctuation;
	}
	public void setGprsFluctuation(double gprsFluctuation) {
		this.gprsFluctuation = gprsFluctuation;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	public String getPackageName() {
		return packageName;
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
	
}
