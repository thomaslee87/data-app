/**
 * 
 */
package com.intellbi.utils;

/**
 * @author lizheng 20140503
 * 
 */
public class ProjectConfiguration {
	private static ProjectConfiguration _instance ;
	static {
		
	}
	
	public static final String CONF_DIR = "conf/";
	
	public static final String DEFAULT_CSV_ENCODING = "utf-8";
	public static final String DEFAULT_DELIMITER = ",";
	
	
	public static final String DB_PROP_DRIVER    = "driver";
	public static final String DB_PROP_URLPREFIX = "url_prefix";
	public static final String DB_PROP_HOST      = "host";
	public static final String DB_PROP_PORT      = "port";
	public static final String DB_PROP_USER      = "user";
	public static final String DB_PROP_PASSWD    = "password";
	public static final String DB_ACCESS_CONF    = "db_access.properties";
	
	public static final String CONF_ETL_FIELDS   = "etl.properties";
	
	
}
