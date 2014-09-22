package com.intellbi.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.intellbi.data.dataobject.TelecomPackage;
import com.intellbi.utils.Utils;

public class PackageConfig {
	
	private static class PackageConfigSingletonHolder {
		public final static PackageConfig instance = new PackageConfig();
	}

	private Map<String,TelecomPackage> packages = new HashMap<String,TelecomPackage>();
	
	private static Pattern pBase = Pattern.compile("(\\d+)元/月基本套餐([A-C]?)",Pattern.CASE_INSENSITIVE);
	private static Pattern pIphone = Pattern.compile("(iPhone) (\\d+)元/月套餐", Pattern.CASE_INSENSITIVE);
	
	private static Map<String, Integer> package2IDMap = new HashMap<String, Integer>() {
		{
			put("36a",1);
			put("36b",2);
			put("46a",3);
			put("46b",4);
			put("46c",5);
			put("66a",6);
			put("66b",7);
			put("66c",8);
			put("iphone66",9);
			put("96a",10);
			put("96b",11);
			put("96c",12);
			put("iphone96",13);
			put("126a",14);
			put("126b",15);
			put("iphone126",16);
			put("156a",17);
			put("156b",18);
			put("iphone156",19);
			put("186a",20);
			put("186b",21);
			put("iphone186",22);
			put("226a",23);
			put("iphone226",24);
			put("286a",25);
			put("iphone286",26);
			put("386a",27);
			put("iphone386",28);
			put("586a",29);
			put("iphone586",30);
			put("886a",31);
			put("iphone886",32);
		}
	};
	
	public int getIdByFeature(String feature) {
		return package2IDMap.get(feature);
	}
	
	public TelecomPackage getPackageByName(String packageName){
		Matcher m = pIphone.matcher(packageName);
		if (m.find()) {
			packageName = (m.group(1) + m.group(2)).toLowerCase();
		} else {
			m = pBase.matcher(packageName);
			if (m.find())
				packageName = (m.group(1) + m.group(2)).toLowerCase();
		}
		return packages.get(packageName);
	}
	
//	private Pattern p = Pattern.compile("(\\d+).*?([a-z]+)", Pattern.CASE_INSENSITIVE);
	
	private PackageConfig() {
		String packageExcel = ConfigManager.getInstance().getPackageFile();
		
		Workbook book = null;
		Sheet sheet = null;
		try {
			if (packageExcel.endsWith("xls"))
				book = new HSSFWorkbook(ClassLoader.getSystemResourceAsStream(packageExcel));
			else if (packageExcel.endsWith("xlsx"))
				book = new XSSFWorkbook(ClassLoader.getSystemResourceAsStream(packageExcel));
			else
				throw new IOException("3g package file should be xls or xlsx");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet = book.getSheetAt(0);
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			String name = row.getCell(0).getStringCellValue();
			double fee  = Utils.parseDouble(row.getCell(1).toString());

			String[] voiceItems = row.getCell(2).toString().split("[^0-9\\.]",-1);
			String voice = "0";
			String localVoice = "0";
			String longDistVoice = "0";
			if(voiceItems.length == 1)
				voice = voiceItems[0];
			else {
				localVoice = voiceItems[0];
			}
			
			double gprs = Utils.parseDouble(row.getCell(3).toString());
			int sms  = Utils.parseInteger(row.getCell(4).toString());
			
			String[] voicePriceItems = row.getCell(6).toString().split("/");
			double localVoicePrice = Utils.parseDouble(voicePriceItems[0]);
			double longDistVoicePrice = localVoicePrice;
			
			if(voicePriceItems.length == 2) {
				longDistVoicePrice = Utils.parseDouble(voicePriceItems[1]);;
			}
			
			double gprsPrice = Utils.parseDouble(row.getCell(7).toString());
			double smsPrice  = Utils.parseDouble(row.getCell(8).toString());
			
			String featureName = row.getCell(9).toString();
			
			TelecomPackage telecomPackage = new TelecomPackage();
			telecomPackage.setName(name);
			telecomPackage.setFee(fee);
			telecomPackage.setVoice(Utils.parseDouble(voice));
			telecomPackage.setLocalVoice(Utils.parseDouble(localVoice));
			telecomPackage.setLongDistVoice(Utils.parseDouble(longDistVoice));
			telecomPackage.setGprs(gprs);
			telecomPackage.setSms(sms);
			
			telecomPackage.setLocalVoicePrice(localVoicePrice);
			telecomPackage.setLongDistVoicePrice(longDistVoicePrice);
			telecomPackage.setGprsPrice(gprsPrice);
			telecomPackage.setSmsPrice(smsPrice);
			
			telecomPackage.setFeature(featureName);
			
			telecomPackage.setId(getIdByFeature(featureName));
			
			System.out.println(featureName + ": " + telecomPackage);
			packages.put(featureName.toLowerCase(), telecomPackage);
			
//			String packageName = row.getCell(0).getStringCellValue();
//			double packageFee = Double.parseDouble(row.getCell(1).toString());
//			String packageVoiceString = row.getCell(2).toString();
//			
//			String[] voiceItems = packageVoiceString.split("[^0-9]");
//			double packageVoice = Double.parseDouble(voiceItems[0]);
//			double packageGprs = Double.parseDouble(row.getCell(3).toString());
//			double packageVoicePrice = Double.parseDouble(row.getCell(6).toString());
//			double packageGprsPrice = Double.parseDouble(row.getCell(7).toString());
//			
//			Matcher m = p.matcher(packageName);
//			if(m.find()) {
//				packageName = (m.group(1) + m.group(2)).toLowerCase();
//				Package _package = new Package(packageName, packageFee,
//						packageVoice, packageGprs, packageVoicePrice, packageGprsPrice);
//				packages.put(packageName, _package);
//				System.out.println(_package.toString());
//			}
		}
	}
	
	public Map<String, TelecomPackage> getPackages() {
		return packages;
	}
	
	public static PackageConfig getInstance() {
		return PackageConfigSingletonHolder.instance;
	}
	
	public static void main(String[] args) {
		PackageConfig.getInstance();
	}
}
