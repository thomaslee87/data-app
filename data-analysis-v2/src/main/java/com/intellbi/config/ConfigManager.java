package com.intellbi.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellbit.v2.exception.PropertyParseException;

public class ConfigManager {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    
    private static ConfigManager instance = null;
	
	private Map<String,String> options = new HashMap<String,String>();
	private Properties properties ;
	
	private boolean isDebug = false;
	private Set<String> exludedMonth = null;
	
	public Set<String> getExludedMonth() {
		return exludedMonth;
	}

	public void setExludedMonth(Set<String> exludedMonth) {
		this.exludedMonth = exludedMonth;
	}

	private ConfigManager() {
		
	}
	
	private void loadProperties(String... config) throws PropertyParseException {
		File[] files = new File[config.length];
		for (int i = 0; i < config.length; i ++) 
			files[i] = new File(config[i]);
		
		properties = PropertyFileUtil.loadProperties(files);
	}
	
	public void put(String k, String v) {
		options.put(k, v);
	}
	
	public String get(String k) {
		return options.get(k);
	}
	
	public String getMysqlConnStatment() {
		return properties.getProperty("jdbc.url", "jdbc:mysql://127.0.0.1:3306/intellbi?useUnicode=true&characterEncoding=UTF-8");
	}
	
	public String getMysqlUsername() {
		return properties.getProperty("jdbc.username", "root");
	}
	
	public String getMysqlPassword() {
		return properties.getProperty("jdbc.password", "root");
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
	
	public static ConfigManager getConfigManager(String... conf) {
		ConfigManager config = new ConfigManager();
	    try {
	    	config.loadProperties(conf);
	    } catch (PropertyParseException e) {
	    	logger.error(e.getMessage(), e);
	    }
	    instance = config;//待重构，兼容老逻辑
	    return config;
	}
	
	public static ConfigManager getInstance(){
	    return instance;
	}
	
}
