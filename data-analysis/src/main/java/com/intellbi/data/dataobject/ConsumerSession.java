package com.intellbi.data.dataobject;

import java.util.ArrayList;
import java.util.List;

import com.intellbi.config.Constants;
import com.intellbi.config.PackageConfig.Package;

public class ConsumerSession{
	
	//  |------|      |------|
	//  |201301| ---> |201302| ---> ... 一个session就是一个用户月计费数据形成的链表
	//  |------|      |------|
	
	private List<ConsumerSessionNode> consumerSession = new ArrayList<ConsumerSessionNode>(12);
	
	public void add(String phoneNo, ConsumerSessionNode consumerSessionNode) {
		consumerSession.add(consumerSessionNode);
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
	
	//先调用calc方法再获取各数据
	public void calc() {
		int i = 0;
		for(ConsumerSessionNode node: consumerSession) {
			income6 += node.getIncome();
			voice6 += node.getCallTime();
			gprs6 += node.getGprs();
			
			if(i >= 3) {
				income3 += node.getIncome();
				voice3 += node.getCallTime();
				gprs3 += node.getGprs();
			}
			
			packageSpill += node.getIncome()/node.getPackage().getFee();//(node.getIncome() - node.getPackage().getFee())/node.getPackage().getFee();
			voiceSpill += node.getCallTime() /node.getPackage().getVoice();
			gprsSpill += node.getGprs() /node.getPackage().getGprs();
			
			i++;
		}
		income6 /= i;
		voice6 /= i;
		gprs6 /= i;
		income3 /= (consumerSession.size() - 3);
		voice3 /= (consumerSession.size() - 3);
		gprs3  /= (consumerSession.size() - 3);
		
		packageSpill /= i;
		voiceSpill /= i;
		gprsSpill /= i;
		
		// 防止除0
		incomeFluctuation = (income3 - income6) / (income6 + 1);
		voiceFluctuation = (voice3 - voice6) / (voice6 + 1);
		gprsFluctuation = (gprs3 - gprs6) / (gprs6 + 1);
		
		double[] weightDoubleHigh = new double[]  {4,5,5,5,5,5};
		double[] weightDoubleLow = new double[]   {2,2,2,2,2,2};
		double[] weightSingleVoice = new double[] {3,2,5,5,2,5};
		double[] weightSingleGprs = new double[]  {3,5,2,5,5,2};
		double[] weightNormal = new double[]      {3,3,3,3,3,3};
		
		UserConsumeType consumeType = getUserConsumeType();
		switch (consumeType) {
		case DOUBLE_HIGH:
			score = calcScore(weightDoubleHigh); break;
		case DOUBLE_LOW:
			score = calcScore(weightDoubleLow);  break;
		case SINGLE_HIGH_GPRS: 
			score = calcScore(weightSingleGprs); break;
		case SINGLE_HIGH_VOICE:
			score = calcScore(weightSingleVoice);break;
		default:
			score = calcScore(weightNormal);     break;
		}
	}
	
	private double calcScore(double[] weight) {
		return weight[0] * getIncomeFluctuation() + weight[1] * getVoiceFluctuation()
				+ weight[2] * getGprsFluctuation() + weight[3] * getPackageSpill()
				+ weight[4] * getVoiceSpill() + weight[5] * getGprsSpill();
	}
	
	public double getScore(){
		return score;
	}
	
	private double sigmoid(double x) {
		return 1/(1 + Math.exp(-1*x));
	}
	
	public double getPackageSpill(){
		return packageSpill;
	}
	public double getVoiceSpill(){
		return voiceSpill;
	}
	public double getGprsSpill(){
		return gprsSpill;
	}
	public double getIncomeFluctuation() {
		return incomeFluctuation;
	}
	public double getVoiceFluctuation() {
		return voiceFluctuation;
	}
	public double getGprsFluctuation() {
		return gprsFluctuation;
	}
	public double getRegularPakcageSpill(){
		return sigmoid(getPackageSpill());
	}
	public double getRegularVoiceSpill(){
		return sigmoid(getVoiceSpill());
	}
	public double getRegularGprsSpill(){
		return sigmoid(getGprsSpill());
	}
	public double getRegularIncomeFluctuation(){
		return sigmoid(getIncomeFluctuation());
	}
	public double getRegularVoiceFluctuation(){
		return sigmoid(getVoiceFluctuation());
	}
	public double getRegularGprsFluctuation(){
		return sigmoid(getGprsFluctuation());
	}
	public double getIncome3(){
		return income3;
	}
	public double getVoice3(){
		return voice3;
	}
	public double getGprs3(){
		return gprs3;
	}
	public double getRegularIncome3(){
		return sigmoid(income3);
	}
	public double getRegularVoice3(){
		return sigmoid(voice3);
	}
	public double getRegularGprs3(){
		return sigmoid(gprs3);
	}
	public UserConsumeType getUserConsumeType(){
		boolean gprsHigh = getGprsSpill() >= Constants.CONSUMER_TYPE_GPRS_THEREHOLD;
		boolean voiceHigh = getVoiceSpill() >= Constants.CONSUMER_TYPE_VOICE_THEREHOLD;
		
		if(gprsHigh && voiceHigh)
			return UserConsumeType.DOUBLE_HIGH;
		if(gprsHigh && !voiceHigh)
			return UserConsumeType.SINGLE_HIGH_GPRS;
		if(!gprsHigh && voiceHigh)
			return UserConsumeType.SINGLE_HIGH_VOICE;
		if(!gprsHigh && !voiceHigh)
			return UserConsumeType.DOUBLE_LOW;
		return UserConsumeType.NORMAL;
	}

	public String toString(){
		return consumerSession.get(consumerSession.size()-1).getPackage().getName()
				+ "," +getUserConsumeType() + "," + getRegularIncomeFluctuation() + "," + getRegularVoiceFluctuation() + ","
				+ getRegularGprsFluctuation() + "," + getRegularPakcageSpill() + ","
				+ getRegularVoiceSpill() + "," + getRegularGprsSpill() + ","
				+ getRegularIncome3() + "," + getRegularVoice3() + ","
				+ getRegularGprs3() + "," + getScore();
	}
	
	public Package getPackage() {
		return consumerSession.get(consumerSession.size()-1).getPackage();
	}
	
	public String toString(boolean regular){
		if(regular)
			return toString();
		return getPackage().getName() + ","
				+ getUserConsumeType() + "," + getIncomeFluctuation() + "," + getVoiceFluctuation() + ","
				+ getGprsFluctuation() + "," + getPackageSpill() + ","
				+ getVoiceSpill() + "," + getGprsSpill() + ","
				+ getIncome3() + "," + getVoice3() + ","
				+ getGprs3() + "," + getScore();
	}
	
}
