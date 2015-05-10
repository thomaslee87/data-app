package com.intellbit.v2.data.importer;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellbit.v2.data.importer.BiConsumer.BiConsumerBuilder;
import com.intellbit.v2.data.importer.BiUser.BiUserBuilder;
import com.intellbit.v2.database.DBOperator;
import com.intellbit.v2.exception.ConsumerImportException;
import com.intellbit.v2.exception.UserImportException;

public class UserImporter {
	
	private static final Logger logger = LoggerFactory.getLogger(UserImporter.class);

	private static final String HEADER_PHONE = "用户号码";
	private static final String HEADER_NAME  = "用户姓名";
	private static final String HEADER_MANAGER = "客户经理";
	private static final String HEADER_IDCARD = "证件号码";
	private static final String HEADER_IDCARD_TYPE = "证件类型";
	private static final String HEADER_VIPLEVEL = "vip级别";
	
	private String jdbcConfig;
	private List<String> head;
	
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	
	private UserImporter(String jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
		biUsers = new LinkedList<BiUser>();
		biConsumers = new LinkedList<BiConsumer>();
		biConsumerUserMap = new HashMap<String, String>();
	}
	
	private Map<String, BiUser> transferBiUserList2Map(List<BiUser> users) {
		Map<String, BiUser> dbUserMap = new HashMap<String, BiUser>();
		for(BiUser user: users) {
			dbUserMap.put(user.getRealname(), user);
		}
		return dbUserMap;
	}
	private Map<String, BiConsumer> transferBiConsumerList2Map(List<BiConsumer> consumers) {
		Map<String, BiConsumer> dbConsumerMap = new HashMap<String, BiConsumer>();
		for(BiConsumer consumer: consumers) {
			dbConsumerMap.put(consumer.getPhone(), consumer);
		}
		return dbConsumerMap;
	}
	
	private void importUsersToDB() throws UserImportException {
		DBOperator db = DBOperator.getDBOperator(jdbcConfig);
		List<BiUser> dbUsers = db.selectAllBiUsers();
		Map<String, BiUser> dbUserMap = this.transferBiUserList2Map(dbUsers);
		List<BiUser> newUsers = new ArrayList<BiUser>();
		try {
			db.beginTrancation();
			for(BiUser user: biUsers) {
				if(dbUserMap.containsKey(user.getRealname())) {
					//没有更新的必要
//					db.updateBiUsersByRealname(user);
				} else {
					newUsers.add(user);
				}
			}
			if(newUsers.size() > 0)
				db.insertBiUsers(newUsers);
			db.endTrancation();
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
			try {
				db.rollback();
			} catch (SQLException ex) {
				logger.error(e.getMessage(), e);
				throw new UserImportException();
			}
			throw new UserImportException();
		}
	}
	private void importConsumersToDB() throws ConsumerImportException {
		DBOperator db = DBOperator.getDBOperator(jdbcConfig);
		List<BiConsumer> dbConsumers = db.selectAllConsumers();
		Map<String, BiConsumer> dbConsumerMap = this.transferBiConsumerList2Map(dbConsumers);
		
		List<BiConsumer> newConsumers = new LinkedList<BiConsumer>();
		try {
			db.beginTrancation();
			for(BiConsumer consumer : biConsumers) {
				if (!dbConsumerMap.containsKey(consumer.getPhone()))
					newConsumers.add(consumer);
				else 
					db.updateBiConsumerByPhone(consumer);
			}
			if (newConsumers.size() > 0)
				db.insertBiConsumers(newConsumers);
			db.endTrancation();
		} catch(SQLException e) {
			logger.warn(e.getMessage(), e);
			try {
				db.rollback();
			} catch (SQLException ex) {
				logger.error(e.getMessage(), e);
				throw new ConsumerImportException();
			}
			throw new ConsumerImportException();
		}
	}
	
	/**
	 * 导入续约任务的“用户-客户”对应表
	 */
	private void importContractMap() {
		Map<String, Integer> phoneUserMap = new TreeMap<String, Integer>();
		
		DBOperator db = DBOperator.getDBOperator(jdbcConfig);
		Set<String> dbPhones = db.selectContractMap();
		List<BiUser> biUserList = db.selectAllBiUsers();
		Map<String, BiUser> biUserMap = this.transferBiUserList2Map(biUserList);
		
		try {
			db.beginTrancation();
			for(Entry<String, String> entry : biConsumerUserMap.entrySet()) {
				String phone = entry.getKey();
				String userName = entry.getValue();
				BiUser biUser = biUserMap.get(userName);
				if (biUser != null) {
					int userId = biUser.getId();
					if (dbPhones.contains(phone)) {
						db.updateContractMap(phone, userId);
					} else {
						phoneUserMap.put(phone, userId);
					}
				}
			}
			if (phoneUserMap.size() > 0)
				db.insertContractMap(phoneUserMap);
			db.endTrancation();
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
			try {
				db.rollback();
			} catch (SQLException e1) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 导入保有任务的“用户-客户”对应表，目前和续约任务是同一个列表，在使用时按照合约到期时间区分
	 */
	private void importMaintainMap() {
		Map<String, Integer> phoneUserMap = new TreeMap<String, Integer>();
		
		DBOperator db = DBOperator.getDBOperator(jdbcConfig);
		Set<String> dbPhones = db.selectMaintainMap();
		List<BiUser> biUserList = db.selectAllBiUsers();
		Map<String, BiUser> biUserMap = this.transferBiUserList2Map(biUserList);
		
		try {
			db.beginTrancation();
			for(Entry<String, String> entry : biConsumerUserMap.entrySet()) {
				String phone = entry.getKey();
				String userName = entry.getValue();
				BiUser biUser = biUserMap.get(userName);
				if (biUser != null) {
					int userId = biUser.getId();
					if (dbPhones.contains(phone)) {
						db.updateMantainMap(phone, userId);
					} else {
						phoneUserMap.put(phone, userId);
					}
				}
			}
			if (phoneUserMap.size() > 0)
				db.insertMaintainMap(phoneUserMap);
			db.endTrancation();
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
			try {
				db.rollback();
			} catch (SQLException e1) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 导入带宽升级任务的“用户-客户”对应表
	 */
	private void importBandwidthMap() {
		Map<String, Integer> phoneUserMap = new TreeMap<String, Integer>();
		
		DBOperator db = DBOperator.getDBOperator(jdbcConfig);
		Set<String> dbPhones = db.selectBandwidthMap();
		List<BiUser> biUserList = db.selectAllBiUsers();
		Map<String, BiUser> biUserMap = this.transferBiUserList2Map(biUserList);
		
		try {
			db.beginTrancation();
			for(Entry<String, String> entry : biConsumerUserMap.entrySet()) {
				String phone = entry.getKey();
				String userName = entry.getValue();
				BiUser biUser = biUserMap.get(userName);
				if (biUser != null) {
					int userId = biUser.getId();
					if (dbPhones.contains(phone)) {
						db.updateBandwidthMap(phone, userId);
					} else {
						phoneUserMap.put(phone, userId);
					}
				}
			}
			if (phoneUserMap.size() > 0)
				db.insertBandwidthMap(phoneUserMap);
			db.endTrancation();
		} catch (SQLException e) {
			logger.warn(e.getMessage(), e);
			try {
				db.rollback();
			} catch (SQLException e1) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public static UserImporter getImporter(String config) {
		return new UserImporter(config);
	}
	
	public void doImport(String excel) {
		try {
			readUserExcel(excel);
			importUsersToDB(); 		//导入用户列表（vip经理）
			importConsumersToDB();  //导入客户列表
			importContractMap();	//导入续约任务对应关系
			importMaintainMap();	//导入保有任务对应关系
			importBandwidthMap();   //导入带宽对应关系（三个map相同）
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private String readCellString(Cell cell) {
		if (cell == null){// || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("0");
			return df.format(cell.getNumericCellValue());
		} else {
			return cell.getStringCellValue();
		}
	}
	
	private List<BiUser> biUsers; 
	private List<BiConsumer> biConsumers;
	private Map<String, String> biConsumerUserMap; //phone->user name
	
	private void readUserExcel(String excel) throws IOException {
		Workbook book = null;
		if (excel.endsWith("xls")) {
			book = new HSSFWorkbook(new FileInputStream(excel));
		} else if (excel.endsWith("xlsx")) {
			book = new XSSFWorkbook(new FileInputStream(excel));
		} else {
			throw new IOException("用户文件支持xls和xlsx格式");
		}
		
		Set<String> managerSet = new HashSet<String>();
		
		Sheet sheet = book.getSheetAt(0);
		Row header = sheet.getRow(0);
		for (int i = 0; i < header.getLastCellNum(); i++) {
			Cell cell = header.getCell(i);
			String colHeader = cell.getStringCellValue();
			headerMap.put(Integer.valueOf(i), colHeader);
		}
		
		for (int r = 1; r < sheet.getLastRowNum(); r ++) {
			
			BiUserBuilder builder = BiUser.builder()
					.groupId(1).password("abc123").status(1);
			BiConsumerBuilder consumerBuilder = BiConsumer.builder();
			
			String phone = null, managerName = null;
			String identity = "", name = "", vipLevel = "", identityType = "";
			Row row = sheet.getRow(r);
			for(int i = 0; i < row.getLastCellNum(); i ++) {
				Cell cell = row.getCell(i);
				
				String headerName = headerMap.get(i);
				if(StringUtils.isBlank(headerName))
					continue;
				
				if (headerName.equals(HEADER_PHONE) ) {
					phone = readCellString(cell);
				} else if (headerName.equals(HEADER_MANAGER) ) {
					managerName = readCellString(cell);
				} else if (headerName.equals(HEADER_NAME)) {
					name = readCellString(cell);
				} else if (headerName.equals(HEADER_IDCARD)) {
					identity = readCellString(cell);
				} else if (headerName.equalsIgnoreCase(HEADER_VIPLEVEL)) {
					vipLevel = cell.getStringCellValue();
				} else if (headerName.equals(HEADER_IDCARD_TYPE)) {
					identityType = readCellString(cell);
				}
			}	
			
			if(StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(managerName)) {
				BiUser biUser = builder
						.realname(managerName)
						.username(managerName)
						.build();
				
				BiConsumer biConsumer = consumerBuilder
						.phone(phone)
						.manager(managerName)
						.identity(identity)
						.identityType(identityType)
						.name(name)
						.vipLevel(vipLevel)
						.build();
				
				if(!managerSet.contains(managerName)) {
					biUsers.add(biUser);
					managerSet.add(managerName);
				}
				biConsumers.add(biConsumer);
				biConsumerUserMap.put(phone, managerName);
			}
			
		}
	}
}
