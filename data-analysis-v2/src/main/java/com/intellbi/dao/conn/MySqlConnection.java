package com.intellbi.dao.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intellbi.config.ConfigManager;

public class MySqlConnection implements IDBConnection {
	
	private static Logger logger = Logger.getLogger(MySqlConnection.class);
	
	private Connection conn = null;
	
	private ConfigManager config;
	
	public MySqlConnection(ConfigManager config) {
		this.config = config;
	}
	
	@Override
	public Connection getConnection() {
		if(conn != null)
			return conn;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					config.getMysqlConnStatment(),
					config.getMysqlUsername(),
					config.getMysqlPassword());
		} catch (Exception e) {
			logger.error("failed to connect to mysql." + "\t" + e.getMessage());
		}
		return conn;
	}

	@Override
	public void closeConnection() throws SQLException {
		// TODO Auto-generated method stub
		if(conn != null){
			conn.close();
		}
	}
	
	public void insert(String sql) {
		if(conn == null || StringUtils.isBlank(sql))
			return ;
		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error("Exception when execute sql: " + sql + "\t" + e.getMessage());
		}
	}

}
