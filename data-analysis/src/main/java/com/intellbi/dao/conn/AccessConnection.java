/**
 * 
 */
package com.intellbi.dao.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.intellbi.dao.factory.AbstractDBConfig;

/**
 * @author lizheng 20140503
 * microsoft access connection
 */
public class AccessConnection extends BaseDBConnection {
	
	public AccessConnection(AbstractDBConfig dbConfig) {
		try {
			Class.forName(dbConfig.getDriver());
			connection = DriverManager.getConnection(dbConfig.getConnUrl());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.intellbi.dao.conn.AbstractDBConnection#getConnection()
	 */
	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return connection;
	}

	/* (non-Javadoc)
	 * @see com.intellbi.dao.conn.AbstractDBConnection#closeConnection(java.sql.Connection)
	 */
	@Override
	public void closeConnection(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		super.closeConnection(conn);
	}

}
