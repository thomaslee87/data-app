package com.intellbit.v2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellbi.config.ConfigManager;
import com.intellbi.dao.conn.IDBConnection;
import com.intellbi.dao.conn.MySqlConnection;
import com.intellbit.v2.data.importer.BiConsumer;
import com.intellbit.v2.data.importer.BiUser;

public class DBOperator {

	private static final Logger logger = LoggerFactory.getLogger(DBOperator.class);
	
	private Connection conn = null;
	
	private DBOperator(Connection conn) {
		this.conn = conn;
	}
	
	public static DBOperator getDBOperator(String conf) {
		IDBConnection mysqlConn = new MySqlConnection(ConfigManager.getConfigManager(conf));
        Connection conn = mysqlConn.getConnection();
        return new DBOperator(conn);
	}
	
	public void beginTrancation() throws SQLException {
		conn.setAutoCommit(false);
	}
	
	public void endTrancation() throws SQLException {
		conn.commit();
	}
	
	public void rollback() throws SQLException {
		conn.rollback();
	}
	
	public void insertBiUsers(List<BiUser> users) throws SQLException{
		String sql = "insert into bi_user (group_id, username, password, realname, status, gmt_create) values %s";
		String tpl = "(%d, '%s', '%s', '%s', %d, now()),";
		StringBuilder sb = new StringBuilder();
		for (BiUser user: users) {
			sb.append(
				String.format(tpl, 
					user.getGroupId(),
					user.getUsername(),
					user.getPassword(),
					user.getRealname(),
					user.getStatus()
			));
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);//删除多余的,号
			sql = String.format(sql, sb.toString());
				
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
	}
	
	public void updateBiUsersByRealname(BiUser user) throws SQLException{
		String sql = "UPDATE bi_user SET username=?,group_id=?,password=?,status=?,gmt_modified=now() WHERE realname=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, user.getUsername());
		stmt.setInt(2, user.getGroupId());
		stmt.setString(3, user.getPassword());
		stmt.setInt(4, user.getStatus());
		stmt.setString(5, user.getRealname());
		stmt.executeUpdate();
	}
	
	public List<BiUser> selectAllBiUsers() {
		String sql = "select id,username,password,realname,status from bi_user";
		List<BiUser> userList = new LinkedList<BiUser>();
		ResultSet rs = null;
		try {
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String realname = rs.getString("realname");
				String password = rs.getString("password");
				int status = rs.getInt("status");

				BiUser biUser = BiUser.builder()
						.id(id)
						.username(username)
						.realname(realname)
						.password(password)
						.status(status).build();
				userList.add(biUser);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
        return userList;
	}
	
	public List<BiConsumer> selectAllConsumers() {
		String sql = "select id,phone,name,user_id from bi_consumer";
		List<BiConsumer> consumers = new LinkedList<BiConsumer>();
		
		ResultSet rs = null;
		try {
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String phone = rs.getString("phone");
				String name = rs.getString("name");
				int userId = rs.getInt("user_id");

				BiConsumer biConsumer = BiConsumer.builder()
						.id(id)
						.phone(phone)
						.name(name)
						.managerId(userId)
						.build();
				consumers.add(biConsumer);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
        return consumers;
	}
	public void insertBiConsumers(List<BiConsumer> consumers) throws SQLException {
		String sql = "INSERT INTO bi_consumer (phone,name,identity,identity_type,gmt_create,vip_level) VALUES %s";
		String tpl = "('%s','%s','%s','%s',now(),'%s'),";
		StringBuilder sb = new StringBuilder();
		for (BiConsumer consumer: consumers) {
			sb.append(String.format(tpl, 
					consumer.getPhone(),
					consumer.getName(),
					consumer.getIdentity(),
					consumer.getIdentityType(),
					consumer.getVipLevel()
				));
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);//删除多余的,号
			sql = String.format(sql, sb.toString());
				
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
	}
	public void updateBiConsumerByPhone(BiConsumer consumer) throws SQLException {
		String sql = "UPDATE bi_consumer SET name=?,identity=?,identity_type=?,vip_level=?,gmt_modified=now() WHERE phone=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, consumer.getName());
		stmt.setString(2, consumer.getIdentity());
		stmt.setString(3, consumer.getIdentityType());
		stmt.setString(4, consumer.getVipLevel());
		stmt.setString(5, consumer.getPhone());
		stmt.executeUpdate();
	}
	
	public Map<String, Integer> selectContractMap() {
		Map<String, Integer> phones = new HashMap<String, Integer>();
		String sql = "SELECT id, user_id, phone from bi_contract_map";
		ResultSet rs = null;
		try {
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				/*int id = rs.getInt("id");*/
				int userId = rs.getInt("user_id");
				String phone = rs.getString("phone");
				phones.put(phone, userId);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
		return phones;
	}
	public void insertContractMap(Map<String, Integer> phoneUserIdMap) throws SQLException {
		String sql = "INSERT INTO bi_contract_map (user_id, phone, gmt_create) values %s";
		String tpl = "(%d, '%s', now()),";
		StringBuilder sb = new StringBuilder();
		for(Entry<String, Integer> entry : phoneUserIdMap.entrySet()) {
			String phone = entry.getKey();
			int userId   = entry.getValue().intValue();
			sb.append(String.format(tpl, userId, phone));
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sql = String.format(sql, sb.toString());
			
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
	}
	public void updateContractMap(String phone, int userId) throws SQLException {
		String sql = "UPDATE bi_contract_map SET user_id=?,gmt_modified=now() WHERE phone=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, userId);
		stmt.setString(2, phone);
		stmt.executeUpdate();
	}
	
	public Map<String, Integer> selectMaintainMap() {
		Map<String, Integer> phones = new HashMap<>();
		String sql = "SELECT id, user_id, phone from bi_maintain_map";
		ResultSet rs = null;
		try {
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				/*int id = rs.getInt("id");*/
				int userId = rs.getInt("user_id");
				String phone = rs.getString("phone");
				phones.put(phone, userId);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
		return phones;
	}
	public void insertMaintainMap(Map<String, Integer> phoneUserIdMap) throws SQLException {
		String sql = "INSERT INTO bi_maintain_map (user_id, phone, gmt_create) values %s";
		String tpl = "(%d, '%s', now()),";
		StringBuilder sb = new StringBuilder();
		for(Entry<String, Integer> entry : phoneUserIdMap.entrySet()) {
			String phone = entry.getKey();
			int userId   = entry.getValue().intValue();
			sb.append(String.format(tpl, userId, phone));
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sql = String.format(sql, sb.toString());
			
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
	}
	public void updateMantainMap(String phone, int userId) throws SQLException {
		String sql = "UPDATE bi_maintain_map SET user_id=?,gmt_modified=now() WHERE phone=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, userId);
		stmt.setString(2, phone);
		stmt.executeUpdate();
	}
	
	public Map<String, Integer> selectBandwidthMap() {
		Map<String, Integer> phones = new HashMap<>();
		String sql = "SELECT id, user_id, phone from bi_bandwidth_map";
		ResultSet rs = null;
		try {
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				/*int id = rs.getInt("id");*/
				int userId = rs.getInt("user_id");
				String phone = rs.getString("phone");
				phones.put(phone, userId);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
		return phones;
	}
	public void insertBandwidthMap(Map<String, Integer> phoneUserIdMap) throws SQLException {
		String sql = "INSERT INTO bi_bandwidth_map (user_id, phone, gmt_create) values %s";
		String tpl = "(%d, '%s', now()),";
		StringBuilder sb = new StringBuilder();
		for(Entry<String, Integer> entry : phoneUserIdMap.entrySet()) {
			String phone = entry.getKey();
			int userId   = entry.getValue().intValue();
			sb.append(String.format(tpl, userId, phone));
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sql = String.format(sql, sb.toString());
			
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
	}
	public void updateBandwidthMap(String phone, int userId) throws SQLException {
		String sql = "UPDATE bi_bandwidth_map SET user_id=?,gmt_modified=now() WHERE phone=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, userId);
		stmt.setString(2, phone);
		stmt.executeUpdate();
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.warn(e.getMessage(), e);
		}
	}
	
}
