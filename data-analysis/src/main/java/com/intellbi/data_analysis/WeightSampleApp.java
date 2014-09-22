package com.intellbi.data_analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.intellbi.config.ConfigManager;
import com.intellbi.config.PackageConfig;
import com.intellbi.config.VIPManagerConfig;
import com.intellbi.data.dataobject.ConsumerBillDetail;
import com.intellbi.data.dataobject.ConsumerBillDetailWrapper;
import com.intellbi.data.dataobject.ConsumerSessionMap;
import com.intellbi.data.dataobject.TelecomPackage;
import com.intellbi.utils.Utils;

public class WeightSampleApp {
	
	private static Pattern pBase = Pattern.compile("(\\d+)元/月基本套餐([A-C]?)", Pattern.CASE_INSENSITIVE);
	private static Pattern pIphone = Pattern.compile("(iPhone) (\\d+)元/月套餐", Pattern.CASE_INSENSITIVE);
	
	public static Set<String> findVipConsumers(int yearMonth, String mdbDataFile, Set<String> vipCustomers){
		int i =0 ;
		Set<String> _vipConsumers = new HashSet<String>();
		try {
			Database db = DatabaseBuilder.open(new File(mdbDataFile));
			Set<String> tableNames = db.getTableNames();
			Table table = db.getTable((String)tableNames.toArray()[0]);
			for(Row row: table) {
				String phoneNo = row.get("号码").toString();
				Object packageObj = row.get("套餐类型");
				String packageName = "";
				if(packageObj != null)
					packageName = packageObj.toString();
				
				if(vipCustomers.contains(phoneNo)) { // vip cusumers
					Matcher m = pIphone.matcher(packageName);
					if(m.find()) {// && parseDouble(m.group(1)) >= 286) { package type
						packageName = (m.group(2) + m.group(1)).toLowerCase();
					}
					else {
						m = pBase.matcher(packageName);
						if(m.find())
							packageName = (m.group(1) + m.group(2)).toLowerCase();
						else
							continue;
					}
					
					double income  = Utils.parseDouble(row.get("收入").toString());
					double callAmount = Utils.parseDouble(row.get("通话次数").toString());
					
					double localCallTime    = Utils.parseDouble(row.get("非漫游本地计费时长"));
					double roamCallTime     = Utils.parseDouble(row.get("漫游计费时长"));
					double distanceCallTime = Utils.parseDouble(row.get("长途计费时长"));
					double callTime = localCallTime  + roamCallTime + distanceCallTime;
					
					long contractFrom = Utils.parseInteger(row.get("合约计划生效时间"));
					long contractTo   = Utils.parseInteger(row.get("合约计划失效时间"));
					
					double gprs = Utils.parseDouble(row.get("GPRS流量"));
					
					i++;
//					if(contractTo >= 20131200 && contractTo <= 20131231){
					if(contractTo >= 20130701 && contractTo <= 20131231){
//						System.out.println(phoneNo + ","  + packageName + "," + income + "," + callAmount
//								+ "," + localCallTime + "," + roamCallTime + "," + distanceCallTime);
						_vipConsumers.add(phoneNo);
					}
				}
			}
			System.out.println("all vip:" + i);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _vipConsumers;
	}

	public static void main(String[] args) {
		//load vip customers
		VIPManagerConfig vipManagerConfig = VIPManagerConfig.getInstance();
		Set<String> vipCustomers = vipManagerConfig.getCustomerSet();
		
		//load project configuration
		ConfigManager configManager = ConfigManager.getInstance();
		String dataLocation = configManager.getDataLocation();
		
		//load packages
		PackageConfig packageManager = PackageConfig.getInstance();
		Map<String, TelecomPackage> packages = packageManager.getPackages();
		
		ConsumerSessionMap consumerSessionMap = new ConsumerSessionMap();
		
		int i  =0;
		//load billing data
		String dataFile = "移动收入欠费%d.mdb";
		int yearMonth = 201306;
		String mdbDataFile = dataLocation + String.format(dataFile, 201306);
		Set<String> _vipConsumers = findVipConsumers(yearMonth, mdbDataFile, vipCustomers);
		System.out.println(_vipConsumers.size());
		
		for(yearMonth = 201301; yearMonth <=201306; yearMonth++) {
			mdbDataFile = dataLocation + String.format(dataFile, yearMonth);
			try {
				Database db = DatabaseBuilder.open(new File(mdbDataFile));
				Set<String> tableNames = db.getTableNames();
				Table table = db.getTable((String)tableNames.toArray()[0]);
				for(Row row: table) {
					String phoneNo = row.get("号码").toString();
					Object packageObj = row.get("套餐类型");
					String packageName = "";
					if(packageObj != null)
						packageName = packageObj.toString();
					
					if(_vipConsumers.contains(phoneNo)) { // vip cusumers
						i++;
						Matcher m = pIphone.matcher(packageName);
						if(m.find()) {// && parseDouble(m.group(1)) >= 286) { package type
							packageName = (m.group(2) + m.group(1)).toLowerCase();
						}
						else {
							m = pBase.matcher(packageName);
							if(m.find())
								packageName = (m.group(1) + m.group(2)).toLowerCase();
							else
								continue;
						}
						
						double income  = Utils.parseDouble(row.get("收入").toString());
						int callAmount = Utils.parseInteger(row.get("通话次数").toString());
						
						int localCallTime    = Utils.parseInteger(row.get("非漫游本地计费时长"));
						int roamCallTime     = Utils.parseInteger(row.get("漫游计费时长"));
						int distanceCallTime = Utils.parseInteger(row.get("长途计费时长"));
						int callTime = localCallTime  + roamCallTime + distanceCallTime;
						
						long contractFrom = Utils.parseInteger(row.get("合约计划生效时间"));
						long contractTo   = Utils.parseInteger(row.get("合约计划失效时间"));
						
						double gprs = Utils.parseDouble(row.get("GPRS流量"));
						
//						System.out.println(phoneNo + ","  + packageName + "," + income + "," + callAmount
//								+ "," + localCallTime + "," + roamCallTime + "," + distanceCallTime);
						
						TelecomPackage _package = packages.get(packageName);
						
						ConsumerBillDetail detail = new ConsumerBillDetail();
						detail.setIncome(income);
						detail.setCallAmount(callAmount);
						detail.setCallTime(callTime);
						detail.setGprs(gprs);
						
						ConsumerBillDetailWrapper consumerSessionNode = null; 
//								new consumerBillDetailWrapper(yearMonth, _package, contractFrom, contractTo, detail);
						
						consumerSessionMap.add(phoneNo, consumerSessionNode);
						
//						if(_package == null) {
//							i ++;
//							System.out.println(yearMonth + "," + packageName + "," + phoneNo);
//						}
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream("score.csv"));
			for(String phoneNo: consumerSessionMap.getConsumerSession().keySet()) {
				if(consumerSessionMap.getConsumerSession().get(phoneNo).getPackage().getFee() < 286)//过滤286以下
					continue;
				
				consumerSessionMap.getConsumerSession().get(phoneNo).calc();
				System.out.println(phoneNo + "," + consumerSessionMap.getConsumerSession().get(phoneNo).toString(false));
				osw.write(phoneNo + "," + consumerSessionMap.getConsumerSession().get(phoneNo).toString(false) + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				osw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
