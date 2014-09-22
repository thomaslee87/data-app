package com.intellbi.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigManager {
	
	private static class ConfigManagerSingletonHolder {
		public final static ConfigManager instance = new ConfigManager();
	}
	
	private Properties properties ;
	
	private ConfigManager() {
		properties = new Properties();
		try {
//			properties.load(new FileInputStream(ClassLoader.getSystemResource(Constants.CONFIG_FILE).getFile()));
			properties.load(new InputStreamReader(ClassLoader.getSystemResourceAsStream(Constants.CONFIG_FILE), "utf-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ConfigManager getInstance() {
		return ConfigManagerSingletonHolder.instance;
	}
	
	public String getMysqlConnStatment() {
		return properties.getProperty("intellbi.mysql.conn");
	}
	
	public String getMysqlUsername() {
		return properties.getProperty("intellbi.mysql.username");
	}
	
	public String getMysqlPassword() {
		return properties.getProperty("intellbi.mysql.password");
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
	
	public static void main(String[] args) {
		String a = ConfigManager.getInstance().getVipCustomersFile();
		System.out.println(a);
	}
}
