/**
 * 
 */
package com.intellbi.dao.factory;

import com.intellbi.dao.conn.AccessConnection;
import com.intellbi.dao.conn.IDBConnection;
import com.intellbi.utils.FileUtils;
import com.intellbi.utils.ProjectConfiguration;

/**
 * @author lizheng 20140503
 * concrete DB connection factory, used to create access connection object.
 */
public class AccessConnectionFactory extends AbstractDBConnectionFactory {
	
	public static class AccessConfig extends AbstractDBConfig {
		
		/**
		 * construct with default properties files
		 */
		public AccessConfig() { 
			loadDefaultConfigFromProp(
					FileUtils.getProjectPath() + 
					ProjectConfiguration.CONF_DIR + 
					ProjectConfiguration.DB_ACCESS_CONF);
		}
		
		/**
		 * @param propFile
		 * construct with properties file
		 */
		public AccessConfig(String propFile) { 
			loadDefaultConfigFromProp(FileUtils.getProjectPath() + propFile);
		}
		
		/**
		 * @param host
		 * @param port
		 * @param user
		 * @param password
		 * construct with given arguments
		 */
		public AccessConfig(String host, String port, String dbName, String user, String password) {
			loadDefaultConfigFromProp(
					FileUtils.getProjectPath() + 
					ProjectConfiguration.CONF_DIR + 
					ProjectConfiguration.DB_ACCESS_CONF);
			setHost(host);
			setPort(port);
			setDbName(dbName);
			setUser(user);
			setPassword(password);
		}
		
		/* (non-Javadoc)
		 * @see com.intellbi.dao.factory.AbstractDBConfig#initConnUrl()
		 */
		@Override
		public String getConnUrl() {
			// TODO Auto-generated method stub
			return getUrlPrefix() + getDbName();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.intellbi.dao.factory.AbstractDBConnectionFactory#createDBConnection()
	 */
	public IDBConnection createDBConnection() {
		// TODO Auto-generated method stub
		return createDBConnection(new AccessConfig());
	}
	
	public IDBConnection createDBConnection(String propFile) {
		// TODO Auto-generated method stub
		return createDBConnection(new AccessConfig(propFile));
	}
	
	/* (non-Javadoc)
	 * @see com.intellbi.dao.factory.AbstractDBConnectionFactory#createDBConnection()
	 */
	public IDBConnection createDBConnection(String host, String port, String dbName, String user, String password) {
		// TODO Auto-generated method stub
		return new AccessConnection(new AccessConfig(host, port, dbName, user, password));
	}

}
