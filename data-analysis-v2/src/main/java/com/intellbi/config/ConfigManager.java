package com.intellbi.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {
    
    private static ConfigManager instance = null;
	
	private Map<String,String> options = new HashMap<String,String>();
	private Properties properties ;
	
	private boolean isDebug = false;
	
	private ConfigManager(String config) {
		properties = new Properties();
		try {
			properties.load(new InputStreamReader(new FileInputStream(config), "utf-8"));
//			properties.load(new FileInputStream(ClassLoader.getSystemResource(Constants.CONFIG_FILE).getFile()));
//			properties.load(new InputStreamReader(ClassLoader.getSystemResourceAsStream(Constants.CONFIG_FILE), "utf-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void put(String k, String v) {
		options.put(k, v);
	}
	
	public String get(String k) {
		return options.get(k);
	}
	
	public String getMysqlConnStatment() {
		return properties.getProperty("intellbi.mysql.conn", "jdbc:mysql://127.0.0.1:3306/intellbi?useUnicode=true&characterEncoding=UTF-8");
	}
	
	public String getMysqlUsername() {
		return properties.getProperty("intellbi.mysql.username", "root");
	}
	
	public String getMysqlPassword() {
		return properties.getProperty("intellbi.mysql.password", "root");
	}
	
	public String getDataLocation() {
		return properties.getProperty("intellbi.data.location");
	}
	
	public String getVipCustomersFile() {
		return properties.getProperty("intellbi.vip.customers");
	}
	
	public String getPackageFile() {
		return properties.getProperty("intellbi.3g.packages");
	}
	
	public String getDataFileFormat(){
		return properties.getProperty("intellbi.data.fileformat").toLowerCase();
	}
	
	public int getAnalysisLongPeriod(){
		return Integer.parseInt(properties.getProperty("intellbi.data.analysis.long","6"));
	}
	
	public int getAnalysisRecentPeriod(){
		return Integer.parseInt(properties.getProperty("intellbi.data.analysis.recent", "3"));
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	public static ConfigManager getInstance(String config){
	    if(instance == null)
	        instance = new ConfigManager(config);
	    return instance;
	}
	
	public static ConfigManager getInstance(){
	    return instance;
	}
	
}
