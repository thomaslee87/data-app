package com.intellbi.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
	
	private static class ConfigManagerSingletonHolder {
		public final static ConfigManager instance = new ConfigManager();
	}
	
	private Properties properties ;
	
	private ConfigManager() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(ClassLoader.getSystemResource(Constants.CONFIG_FILE).getFile()));
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
	
	public String getDataLocation() {
		return properties.getProperty("intellbi.data.location").toString();
	}
	
	public String getVipCustomersFile() {
		return properties.get("intellbi.vip.customers").toString();
	}
	
	public String getPackageFile() {
		return properties.getProperty("intellbi.3g.packages").toString();
	}
	
	public static void main(String[] args) {
		String a = ConfigManager.getInstance().getVipCustomersFile();
		System.out.println(a);
	}
}
