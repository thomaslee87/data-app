/**
 * 
 */
package com.intellbi.dao.conn;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lizheng 20140503
 * Abstract base class for db connection
 */
public class BaseDBConnection implements IDBConnection {
	
	protected Connection connection;
	
	/* (non-Javadoc)
	 * @see com.intellbi.dao.conn.IDBConnection#getConnection(java.lang.String[])
	 */
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.intellbi.dao.conn.IDBConnection#closeConnection(java.sql.Connection)
	 */
	public void closeConnection(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		conn.close();
	}

}
