/**
 * 
 */
package com.intellbi.dao.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.intellbi.utils.ProjectConfiguration;

/**
 * @author lizheng 20140503
 * abstract database configuration, cannot be instanced
 */
public abstract class AbstractDBConfig {
	private String driver;
	private String urlPrefix;
	private String host;
	private String port;
	private String dbName;
	private String user;
	private String password;
	
	/**
	 * @param prop
	 * set connection attributes from prop
	 */
	public void setAttributes(Properties prop) {
		setDriver(prop.getProperty(ProjectConfiguration.DB_PROP_DRIVER));
		setUrlPrefix(prop.getProperty(ProjectConfiguration.DB_PROP_URLPREFIX));
		setHost(prop.getProperty(ProjectConfiguration.DB_PROP_DRIVER));
		setPort(prop.getProperty(ProjectConfiguration.DB_PROP_PORT));
		setUser(prop.getProperty(ProjectConfiguration.DB_PROP_USER));
		setPassword(prop.getProperty(ProjectConfiguration.DB_PROP_DRIVER));
	}
	
	/**
	 * @param defaultPropFile
	 */
	protected void loadDefaultConfigFromProp(String defaultPropFile) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(defaultPropFile)));
			setAttributes(prop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * initialize db connection url 
	 */
	public abstract String getConnUrl(); 
	
	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	/**
	 * @param the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}
	/**
	 * @return the url
	 */
	public String getUrlPrefix() {
		return urlPrefix;
	}
	/**
	 * @param the url to set
	 */
	protected void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * @param the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
