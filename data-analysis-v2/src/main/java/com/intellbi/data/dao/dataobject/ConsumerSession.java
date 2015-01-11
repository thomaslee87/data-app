package com.intellbi.data.dao.dataobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.intellbi.config.Constants;
import com.intellbi.config.PackageManager;

public class ConsumerSession{
	
	//  |------|      |------|
	//  |201301| ---> |201302| ---> ... 一个session就是一个用户月计费数据形成的链表
	//  |------|      |------|
	
	Logger logger = Logger.getLogger(ConsumerSession.class);
	
	private List<ConsumerBillDetailWrapper> consumerSession = new ArrayList<ConsumerBillDetailWrapper>(12);
	
	public ConsumerBillDetailWrapper getCurrConsumerBillDetailWrapper() {
		if(consumerSession.size() > 0)
			return this.consumerSession.get(consumerSession.size() - 1);
		return null;
	}
	
	private static final int RECOMMENDED_NUMBER = 3;
	
	public void add(String phoneNo, ConsumerBillDetailWrapper consumerBillDetailWrapper) {
		this.phoneNo = phoneNo;
		consumerSession.add(consumerBillDetailWrapper);
	}
	private String phoneNo ;
	private double income6 = 0;
	private double income3 = 0;
	private double voice6 = 0;
	private double voice3 = 0;
	private double gprs6 = 0;
	private double gprs3 = 0;
	private double packageSpill = 0;
	private double voiceSpill = 0;
	private double gprsSpill = 0;
	
	private double valueAdded3 = 0;
	private double valueAdded6 = 0;
	
	private double localVoiceCost6 = 0;
	private double longDistVoiceCost6= 0;
	private double roamVoiceCost6 = 0;
	
	private double incomeFluctuation ;
	private double voiceFluctuation ;
	private double gprsFluctuation ;
	
	private double localVoice6=0;
	private double longDistVoice6=0;
	private double roamVoice6=0;
	
	private double score;
	
	private String recommenedString;
	private String recommened4GString;
	private double valueChange;
	private double valueChange4G;
	
	
	public String getRecommened4GString() {
		return recommened4GString;
	}

	public double getValueChange4G() {
		return valueChange4G;
	}

	private StringBuilder sb = new StringBuilder(); 
	
	private boolean isStandardPackage ;
	
	private TelecomPackage telecomPackage;//当前套餐
	
	//先调用calc方法再获取各数据
	public void calc() {
		int i = 0;
		
		telecomPackage = consumerSession.get(consumerSession.size() - 1).getThePackage();
		isStandardPackage = telecomPackage != null && telecomPackage.isStandard();
		
		if(!isStandardPackage) {
//			System.out.println("consumer [" + phoneNo + "] 's package not match");
			logger.warn("consumer [" + phoneNo + "] 's package not match");
		}
		
		for(ConsumerBillDetailWrapper node: consumerSession) {
			income6 += node.getBillDetail().getIncome();
			voice6 += node.getBillDetail().getCallTime();
			gprs6 += node.getBillDetail().getGprs();
			
			localVoice6 += node.getBillDetail().getLocalCallTime();
			longDistVoice6 += node.getBillDetail().getLongDistCallTime();
			roamVoice6 += node.getBillDetail().getRoamCallTime();
			
			valueAdded6 += node.getBillDetail().getValueAddedFee();
			
			//用于计算实际的平均语音消费（由消费根据单价反算时长）
			localVoiceCost6 += node.getBillDetail().getLocalCallFee();
			longDistVoiceCost6 += node.getBillDetail().getLongDistCallFee();
			roamVoiceCost6 += node.getBillDetail().getRoamCallFee();
			
//			if(i >= 3) {
//				income3 += node.getBillDetail().getIncome();
//				voice3 += node.getBillDetail().getCallTime();
//				gprs3 += node.getBillDetail().getGprs();
//			}
			
			if(isStandardPackage) {
				packageSpill += node.getBillDetail().getIncome()/telecomPackage.getFee();//(node.getIncome() - node.getPackage().getFee())/node.getPackage().getFee();
				
				if(telecomPackage.getVoice() < 0.0001){//local
					voiceSpill += node.getBillDetail().getLocalCallTime()/telecomPackage.getLocalVoice();
//					if(Double.isInfinite(voiceSpill)) {
//						System.out.println(telecomPackage.toString());
//						System.out.println(telecomPackage.getLocalVoice());
//					}
				}
				else {
					voiceSpill += node.getBillDetail().getCallTime()/telecomPackage.getVoice();
//					if(Double.isInfinite(voiceSpill)) {
//						System.out.println("-------------");
//						System.out.println(telecomPackage.toString());
//						System.out.println(telecomPackage.getVoice());
//					}
				}
				gprsSpill += node.getBillDetail().getGprs() /telecomPackage.getGprs();
			}
			else {
				packageSpill = 0;
				voiceSpill = 0;
				gprsSpill = 0;
			}
			
			i++;
		}
		
		int j = 1;
		for(; j <= consumerSession.size(); j++){
			if(j > 3) break;
			int idx = consumerSession.size() - j;
			income3 += consumerSession.get(idx).getBillDetail().getIncome();
			voice3 += consumerSession.get(idx).getBillDetail().getCallTime();
			gprs3 += consumerSession.get(idx).getBillDetail().getGprs();
			
			valueAdded3 += consumerSession.get(idx).getBillDetail().getValueAddedFee();
		}
		
		income6 /= i;
		voice6 /= i;
		gprs6 /= i;
		income3 /= (j-1);
		voice3 /= (j-1);
		gprs3  /= (j-1);
		
		valueAdded6 /= i;
		valueAdded3 /= (j-1);
		
		//通话时长均值
		localVoice6 /= i;
		longDistVoice6 /= i;
		roamVoice6 /= i;
		
		//通话费用均值
		localVoiceCost6 /= i;
		longDistVoiceCost6 /= i;
		roamVoiceCost6 /= i;
		
//		income3 /= (consumerSession.size() - 3);
//		voice3 /= (consumerSession.size() - 3);
//		gprs3  /= (consumerSession.size() - 3);
		
		packageSpill /= i;
		voiceSpill /= i;
		gprsSpill /= i;
		
		incomeFluctuation = 0;
		voiceFluctuation = 0;
		gprsFluctuation = 0;
		if(income6 != 0)
			incomeFluctuation = (income3 - income6) / income6 ;
		if(voice6 != 0)
			voiceFluctuation = (voice3 - voice6) / voice6;
		if(gprs6 != 0)
			gprsFluctuation = (gprs3 - gprs6) / gprs6 ;
		
		double[] weightDoubleHigh = new double[]  {2,5,5,5,5,5};
		double[] weightDoubleLow = new double[]   {2,2,2,2,2,2};
		double[] weightSingleVoice = new double[] {2,2,5,5,2,5};
		double[] weightSingleGprs = new double[]  {2,5,2,5,5,2};
		double[] weightNormal = new double[]      {2,3,3,3,3,3};
		
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
		
		sb.delete(0, sb.length());
		List<PackageCost> sortedPackageList = getRecommendPackage();
		for(int k = 0; k < RECOMMENDED_NUMBER; k++) {
		    PackageCost packageCost = sortedPackageList.get(k);
			if(sb.length() > 0)
				sb.append(",");
			sb.append(PackageManager.getInstance().getIdByFeature(packageCost.telecomPackage.getFeature()))
			    .append(":").append(String.format("%.2f", packageCost.realCost));
		}
		recommenedString = sb.toString();
//		valueChange = sortedPackageList.get(0).telecomPackage.getFee() - telecomPackage.getFee();
		valueChange = income6 - sortedPackageList.get(0).realCost ;//- telecomPackage.getFee();
		
		//4G推荐
		sb.delete(0, sb.length());
		List<Package4GCost> sorted4GPackList = getRecommend4GPackage();
		for(int k = 0; k < RECOMMENDED_NUMBER; k++) {
		    Package4GCost packageCost = sorted4GPackList.get(k);
			if(sb.length() > 0)
				sb.append(",");
			
			sb.append(packageCost.telecomPackage.getId())
			    .append(":").append(String.format("%.2f", packageCost.realCost));
		}
		recommened4GString = sb.toString();
		valueChange4G = income6 - sorted4GPackList.get(0).realCost;//telecomPackage.getFee();
	}
	
	private void transferScoreToPriority(){
		
	}

	private static class PackageCost{
		public double realCost;
		public TelecomPackage telecomPackage;
		
		public PackageCost(double realCost, TelecomPackage telecomPackage){
			this.realCost = realCost;
			this.telecomPackage = telecomPackage;
		}
	}
	
	private class ComparatorPackageCost implements Comparator<PackageCost>{
		@Override
		public int compare(PackageCost arg0, PackageCost arg1) {
			if(arg0.realCost > arg1.realCost + 0.001)
				return 1;
			else if(arg0.realCost < arg1.realCost - 0.001)
				return -1;
			return 0;
		}
	}
	
	private List<PackageCost> getRecommendPackage(){
		List<PackageCost> selPackages = new ArrayList<ConsumerSession.PackageCost>();
		Map<String, TelecomPackage> packages = PackageManager.getInstance().getPackages();
		for(Entry<String, TelecomPackage> entry: packages.entrySet()) {
			String selPackageName = entry.getKey();
			TelecomPackage selPackageEntity = entry.getValue();
			double selCost = getCostInPackage(selPackageEntity);
			selPackages.add(new PackageCost(selCost, selPackageEntity));
		}
		Collections.sort(selPackages, new ComparatorPackageCost());
		return selPackages;
	}
	
	
	private static class Package4GCost{
		public double realCost;
		public TelecomPackage4G telecomPackage;
		
		public Package4GCost(double realCost, TelecomPackage4G telecomPackage){
			this.realCost = realCost;
			this.telecomPackage = telecomPackage;
		}
	}
	
	private class ComparatorPackageCost4G implements Comparator<Package4GCost>{
		@Override
		public int compare(Package4GCost arg0, Package4GCost arg1) {
			if(arg0.realCost > arg1.realCost + 0.001)
				return 1;
			else if(arg0.realCost < arg1.realCost - 0.001)
				return -1;
			return 0;
		}
	}
	
	private List<Package4GCost> getRecommend4GPackage(){
		
        // 采用收入反算出的通话时长
        double realLocalVoice6 = localVoice6;
        double realLongDistVoice6 = longDistVoice6;
        double realRoamVoice6 = roamVoice6; 
        
        double voicePrice = 0.15;//默认取4G套餐的单价
        
        //反算
        if(isStandardPackage) {
        	double curVoicePrice = telecomPackage.getLocalVoicePrice();
        	
            realLocalVoice6 = localVoiceCost6 / curVoicePrice;
            realLongDistVoice6 = longDistVoiceCost6 / curVoicePrice;
            realRoamVoice6 = roamVoiceCost6 / curVoicePrice;
            
        }
        else {
	        realLocalVoice6 = localVoiceCost6 / voicePrice;
	        realLongDistVoice6 = longDistVoiceCost6 / voicePrice;
	        realRoamVoice6 = roamVoiceCost6 / voicePrice;
        }
        
        double voiceUsed = realLocalVoice6 + realLongDistVoice6 + realRoamVoice6 + telecomPackage.getVoice();

        // 也改成用增值费反算 20141102
    	double gprsPrice = 0.0003;//所有套餐单价都一样
        double gprsUsed = valueAdded6 / (gprsPrice * 1000) + telecomPackage.getGprs();
        
		List<Package4GCost> selPackages = new ArrayList<ConsumerSession.Package4GCost>();
		Map<String, TelecomPackage> packages = PackageManager.getInstance().getPackages();
		for(Entry<Integer, TelecomPackage4G> entry: pkg4GMap.entrySet()) {
			TelecomPackage4G selPackageEntity = entry.getValue();
			double selCost = getCostIn4GPackage(selPackageEntity,voiceUsed,gprsUsed);
			selPackages.add(new Package4GCost(selCost, selPackageEntity));
		}
		Collections.sort(selPackages, new ComparatorPackageCost4G());
		return selPackages;
	}
	
    // 按照长市漫一体化计费 http://3g.10010.com/
	private double getCostInPackage(TelecomPackage selPackageEntity) {
        double voiceCost = 0;
        double gprsCost = 0;

        // 采用收入反算出的通话时长
        double realLocalVoice6 = localVoice6;
        double realLongDistVoice6 = longDistVoice6;
        double realRoamVoice6 = roamVoice6; 
        
        // 按照长市漫一体化计费，各种单价其实是一样的，这里取本地价格吧，都一样
        // 事实上，手头的套餐数据中，套餐对超出赠送的语音单价也确实只有一个，没有区分
        double voicePrice = selPackageEntity.getLocalVoicePrice();
        
        /* 标准套餐是有单价信息的，可以用来根据消费反算时长
           主要是目前消费的计算方法不明确，根据原始数据的时长与单价计算出的消费与数据中的相差过大
           非标准套餐，由于手头没有套餐数据，不知道单价，所以还是按照原来的计算方法，直接使用原始数据中的时长
           这种反算的方式，其实也是在不明确如何由原始数据中的计费时长得到消费值的前提下，想到的一种近似处理方法
        */
        if(isStandardPackage) {
        	double curVoicePrice = telecomPackage.getLocalVoicePrice();
        	
            realLocalVoice6 = localVoiceCost6 / curVoicePrice;
            realLongDistVoice6 = longDistVoiceCost6 / curVoicePrice;
            realRoamVoice6 = roamVoiceCost6 / curVoicePrice;
            
        }
        else {
	        realLocalVoice6 = localVoiceCost6 / voicePrice;
	        realLongDistVoice6 = longDistVoiceCost6 / voicePrice;
	        realRoamVoice6 = roamVoiceCost6 / voicePrice;
        }

        /*// gprs流量
        double realGprs = gprs6 - selPackageEntity.getGprs();
        if (realGprs > 0)
            gprsCost = realGprs * selPackageEntity.getGprsPrice() * 1024;
*/
        // 也改成用增值费反算 20141102
//        double realGprs6 = gprs6;
//        if(isStandardPackage) {
        	double gprsPrice = 0.0003;//telecomPackage.getGprsPrice();
            double realGprs6 = valueAdded6 / (gprsPrice * 1000);
//        }
        
        double realGprs = realGprs6 - selPackageEntity.getGprs() + telecomPackage.getGprs();
        if (realGprs > 0)
            gprsCost = realGprs * selPackageEntity.getGprsPrice() * 1024;
        
        if(selPackageEntity.getLocalVoice() > 0) {// 实际上，C类套餐是赠送本地通话，这类需特殊处理
        	//还要加上原套餐赠送的部分，非标准套餐由于没数据，按默认0处理了
        	realLocalVoice6 += telecomPackage.getLocalVoice();
        	
        	double realLocal = realLocalVoice6 - selPackageEntity.getLocalVoice();
            if(realLocal < 0) 
                realLocal = 0;
            
            double realLongDist = realLongDistVoice6;
            double realRoam = realRoamVoice6;
            
            voiceCost = (realLocal + realLongDist + realRoam) * voicePrice;
//            voiceCost = realLocal * selPackageEntity.getLocalVoicePrice()
//                    + realLongDist * selPackageEntity.getLongDistVoicePrice()
//                    + realRoam * selPackageEntity.getLongDistVoicePrice();
        }
        else {//其他套餐都是赠送不区分本地长途的语音
            double realVoiceTime = realLocalVoice6 + realLongDistVoice6 + realRoamVoice6 - selPackageEntity.getVoice(); 
            //还要加上原套餐赠送部分
            realVoiceTime += telecomPackage.getVoice(); 
            voiceCost = realVoiceTime > 0 ? realVoiceTime * voicePrice : 0; 
        }
        
        /* 下边是以前采用的一种通用的匹配方式，将赠送分为三部分“本地”，“长途”，“不区分本地长途”
           然后将本地和长途的分别扣除后，再将仍超出的部分用不区分本地长途的部分去匹配，比较复杂
           实际业务其实只有“本地”和“不区分本地长途”两类，而且不重叠，所以改为上边的算法
        */
        
        /*
        // 语音分本地和长途（因为有些套餐赠送的是本地通话），先减去额度得到真实需要付费的语音
        // 长途比本地通话贵，所以如果长途增费不够用时，优先用不区分本地长途的额度去匹配长途
        // 再用剩余的去匹配本地通话
        // 这里有点复杂了，实际上目前的套餐要么赠送本地，要么赠送不区分本地长途，兼容之
        double realLocal = realLocalVoice6 - selPackageEntity.getLocalVoice();
        if (realLocal < 0)
            realLocal = 0;
        double realLongDist = realLongDistVoice6 - selPackageEntity.getLongDistVoice();
        if (realLongDist < 0) {
            realLongDist = 0;
            if (realLocal > 0) {
                realLocal = realLocal - selPackageEntity.getVoice();
                if (realLocal < 0)
                    realLocal = 0;
            }
        } else {
            // long distance expensive than local
            realLongDist = realLongDist - selPackageEntity.getVoice();
            if (realLongDist < 0) {
                realLongDist = 0;
                if (realLocal > 0) {
                    realLocal = realLocal - (selPackageEntity.getVoice() - realLongDist);
                    if (realLocal < 0)
                        realLocal = 0;
                }
            }
        }
        voiceCost = realLocal * selPackageEntity.getLocalVoicePrice()
                + realLongDist * selPackageEntity.getLongDistVoicePrice();
        */

        // 流量和语音超出套餐的付费，加上套餐本身的价值，算作用户使用该套餐的实际支出
        return gprsCost + voiceCost + selPackageEntity.getFee();
	}
	
	private static Map<Integer,TelecomPackage4G> pkg4GMap;
	static {
		pkg4GMap = new HashMap<Integer, TelecomPackage4G>();
		pkg4GMap.put(1, new TelecomPackage4G(1,76,"76元套餐",200,400));
		pkg4GMap.put(2, new TelecomPackage4G(2,106,"106元套餐",300,800));
		pkg4GMap.put(3, new TelecomPackage4G(3,136,"136元套餐",500,1000));
		pkg4GMap.put(4, new TelecomPackage4G(4,166,"166元套餐",500,2000));
		pkg4GMap.put(5, new TelecomPackage4G(5,196,"196元套餐",500,3000));
		pkg4GMap.put(6, new TelecomPackage4G(6,296,"296元套餐",1000,4000));
		pkg4GMap.put(7, new TelecomPackage4G(7,396,"396元套餐",2000,6000));
		pkg4GMap.put(8, new TelecomPackage4G(8,596,"596元套餐",3000,11000));
	}
	private double getCostIn4GPackage(TelecomPackage4G selPackageEntity, double voiceUsed, double gprsUsed) {
		return selPackageEntity.getFee() +
				selPackageEntity.calcVoiceCost(voiceUsed) +
				selPackageEntity.calcGprsCost(gprsUsed);
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
		return consumerSession.get(consumerSession.size()-1).getThePackage().getName()
				+ "," +getUserConsumeType() + "," + getRegularIncomeFluctuation() + "," + getRegularVoiceFluctuation() + ","
				+ getRegularGprsFluctuation() + "," + getRegularPakcageSpill() + ","
				+ getRegularVoiceSpill() + "," + getRegularGprsSpill() + ","
				+ getRegularIncome3() + "," + getRegularVoice3() + ","
				+ getRegularGprs3() + "," + getScore();
	}
	
	public TelecomPackage getPackage() {
		return consumerSession.get(consumerSession.size()-1).getThePackage();
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

	public double getIncome6() {
		return income6;
	}

	public void setIncome6(double income6) {
		this.income6 = income6;
	}

	public double getVoice6() {
		return voice6;
	}

	public void setVoice6(double voice6) {
		this.voice6 = voice6;
	}

	public double getGprs6() {
		return gprs6;
	}

	public void setGprs6(double gprs6) {
		this.gprs6 = gprs6;
	}

	public String getRecommenedString() {
		return recommenedString;
	}

	public double getValueChange() {
		return valueChange;
	}

//	public String toSql() {
//		String sql = "insert into intellbi.bi_bills_" + yearMonth + 
//				" (phone_no,user_id,year_month,package_id,income,monthly_rental,local_voice_fee,"
//				+ "roaming_fee,long_distance_voice_fee,value_added_fee,other_fee,grant_fee,call_number,"
//				+ "local_call_duration,roam_call_duration,long_distance_call_duration,"
//				+ "internal_call_duration,international_call_duration,sms,gprs,is_group_user,"
//				+ "network,status,contract_from,contract_to) values (%s,%d,%d,%d,%f,%f,%f,"
//				+ "%f,%f,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%f,%d,%d,%s,%d,%d)"; 
//	}
//	
//	public String toSql(boolean regular) {
//		if(regular)
//			return toSql();
//		String sql = "insert into intellbi.bi_bills_" + this.yearMonth + 
//				" (phone_no,user_id,year_month,package_id,income,monthly_rental,local_voice_fee,"
//				+ "roaming_fee,long_distance_voice_fee,value_added_fee,other_fee,grant_fee,call_number,"
//				+ "local_call_duration,roam_call_duration,long_distance_call_duration,"
//				+ "internal_call_duration,international_call_duration,sms,gprs,is_group_user,"
//				+ "network,status,contract_from,contract_to) values (%s,%d,%d,%d,%f,%f,%f,"
//				+ "%f,%f,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%f,%d,%d,%s,%d,%d)"; 
//	}
	
	public static void main(String[] args) {
	    ConsumerSession session = new ConsumerSession();
	    session.telecomPackage = new TelecomPackage();
	    session.income6 = 386;
	    session.gprs6 = 124;
	    session.localVoiceCost6 = (57.81+58.64+58.64+57.76+58.11+56.98)/6;
	    session.longDistVoiceCost6 = (30.15+29.75+29.75+28.96+29.14+30.47)/6;
	    session.roamVoiceCost6 = (26.74+26.31+26.31+27.98+27.45+27.24)/6;
        session.isStandardPackage = false;
        List<PackageCost> list = session.getRecommendPackage();
        for(PackageCost p:list) {
            System.out.println(p.telecomPackage.getName() + "\t" + p.realCost);
        }
	}
}
