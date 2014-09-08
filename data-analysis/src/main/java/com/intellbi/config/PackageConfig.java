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

public class PackageConfig {
	
	private static class PackageConfigSingletonHolder {
		public final static PackageConfig instance = new PackageConfig();
	}

	private Map<String,Package> packages = new HashMap<String,Package>();
	
	public static class Package {
		private String name;
		private double fee;
		private double voice;
		private double gprs;
		private double gprsPrice ;
		private double voicePrice;
		
		public String getName() {
			return name;
		}
		public double getFee() {
			return fee;
		}
		public double getVoice(){
			return voice;
		}
		public double getGprs(){
			return gprs;
		}
		public double getGprsPrice(){
			return gprsPrice;
		}
		public double getVoicePrice(){
			return voicePrice;
		}
		
		public Package(String name, double fee, double voice, double gprs, double voicePrice,double gprsPrice) {
			this.name = name;
			this.fee = fee;
			this.voice = voice;
			this.gprs = gprs;
			this.gprsPrice = gprsPrice;
			this.voicePrice = voicePrice;
		}
		
		public String toString() {
			return name + "," + fee + "," + voice + "," + gprs  + "," + voicePrice + "," + gprsPrice;
		}
	}
	
	private Pattern p = Pattern.compile("(\\d+).*?([a-z]+)", Pattern.CASE_INSENSITIVE);
	
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
			String packageName = row.getCell(0).getStringCellValue();
			double packageFee = Double.parseDouble(row.getCell(1).toString());
			String packageVoiceString = row.getCell(2).toString();
			
			String[] voiceItems = packageVoiceString.split("[^0-9]");
			double packageVoice = Double.parseDouble(voiceItems[0]);
			double packageGprs = Double.parseDouble(row.getCell(3).toString());
			double packageVoicePrice = Double.parseDouble(row.getCell(6).toString());
			double packageGprsPrice = Double.parseDouble(row.getCell(7).toString());
			
			Matcher m = p.matcher(packageName);
			if(m.find()) {
				packageName = (m.group(1) + m.group(2)).toLowerCase();
				Package _package = new Package(packageName, packageFee,
						packageVoice, packageGprs, packageVoicePrice, packageGprsPrice);
				packages.put(packageName, _package);
				System.out.println(_package.toString());
			}
		}
	}
	
	public Map<String, Package> getPackages() {
		return packages;
	}
	
	public static PackageConfig getInstance() {
		return PackageConfigSingletonHolder.instance;
	}
	
	public static void main(String[] args) {
		PackageConfig.getInstance();
	}
}
