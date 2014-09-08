/**
 * 
 */
package com.intellbi.dao.factory;

import com.intellbi.dao.conn.AccessConnection;
import com.intellbi.dao.conn.IDBConnection;

/**
 * @author lizheng 20140503
 * DB connection factory, used to create db connection object.
 */
public abstract class AbstractDBConnectionFactory {
	
	/**
	 * @return database connection
	 * create database connection with default settings
	 */
	abstract public IDBConnection createDBConnection() ;
	
	/**
	 * @param propFile
	 * @return database connection
	 * create database connection with properties file
	 */
	abstract public IDBConnection createDBConnection(String propFile) ;
	
	/**
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @return database connection
	 * create database connection with given arguments
	 */
	abstract public IDBConnection createDBConnection(String host, String port, String dbName, String user, String password) ;
	
	/**
	 * @param  database configuration
	 * @return database connection
	 * create database connection with db configration
	 */
	public IDBConnection createDBConnection(AbstractDBConfig dbConfig) {
		if(dbConfig == null)
			return null;
		return new AccessConnection(dbConfig);
	}
}
