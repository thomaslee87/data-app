/**
 * 
 */
package com.intellbi.dao.conn;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lizheng 20140503
 * Database connection interface
 */
public interface IDBConnection {
	/**
	 * @param possible used arguments
	 * @return database connection
	 */
	public Connection getConnection();
	
	/**
	 * @param database connection
	 * @throws SQLException 
	 */
	public void closeConnection() throws SQLException;
}
