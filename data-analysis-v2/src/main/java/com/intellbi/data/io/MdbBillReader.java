package com.intellbi.data.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.intellbi.config.PackageManager;
import com.intellbi.data.dao.dataobject.ConsumerBillDetail;
import com.intellbi.data.dao.dataobject.ConsumerBillDetailWrapper;
import com.intellbi.data.dao.dataobject.TelecomPackage;
import com.intellbi.utils.Utils;

public class MdbBillReader implements BillReader {
	
	private String theMonth;
	private String filename;
	private Database db;
	private Table table;
	private Iterator<Row> iter;
	
	private static final String MDBCOL_PHONENO = "号码"; 
	private static final String MDBCOL_STATUS = "目前状态";
	private static final String MDBCOL_INCOME = "收入";
	private static final String MDBCOL_PACKAGE = "套餐类型";
	private static final String MDBCOL_CALLAMOUNT = "通话次数";
	private static final String MDBCOL_LOCAL_CALLTIME = "非漫游本地计费时长";
	private static final String MDBCOL_ROAM_CALLTIME = "漫游计费时长";
	private static final String MDBCOL_LONGDISTANCE_CALLTIME = "长途计费时长";
	private static final String MDBCOL_CONTRACT_FROM = "合约计划生效时间";
	private static final String MDBCOL_CONTRACT_TO = "合约计划失效时间";
	private static final String MDBCOL_SMS = "点对点短信条数";
	private static final String MDBCOL_GPRS = "GPRS流量";
	
	private static final String MDBCOL_GROUP = "是否集团用户";
	private static final String MDBCOL_NETWORK = "移动标识";
	
	private static final String MDBCOL_RENTAL = "月租";
	private static final String MDBCOL_LOCAL_VOICE_FEE = "本地通话费";
	private static final String MDBCOL_ROAM_FEE = "漫游费"; 
	private static final String MDBCOL_LONG_DIST_FEE = "长途通话费";
	private static final String MDBCOL_VALUE_ADDED_FEE = "增值费";
	private static final String MDBCOL_OTHER_FEE = "其他费";
	private static final String MDBCOL_GRANT_FEE = "赠款金额";
	private static final String MDBCOL_INTERNAL_CALL_TIME = "国内长途计费时长";
	private static final String MDBCOL_INTERNATIONAL_CALL_TIME = "国际长途计费时长";
	
	Logger logger = Logger.getLogger(MdbBillReader.class);
	
	public MdbBillReader(String theMonth, String filename) {
		this.theMonth = theMonth;
		this.filename = filename;
	}

	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		boolean ret = false;
		do {
			if(StringUtils.isBlank(filename))
				break;
			
			try {
				db = DatabaseBuilder.open(new File(filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				break;
			}
			Set<String> tableNames;
			try {
				tableNames = db.getTableNames();
				table = db.getTable((String) tableNames.toArray()[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				break;
			}
			iter = table.iterator();
			
			ret = true;
		} while (false);
		
		return ret;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return iter.hasNext();
	}

	@Override
	public ConsumerBillDetailWrapper next() {
		// TODO Auto-generated method stub
		return parseRow(iter.next());
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		try {
			db.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Pattern pp = Pattern.compile("(\\d+)元");

	private ConsumerBillDetailWrapper parseRow(Row row) {
		String phoneNo = Utils.parseString(row.get(MDBCOL_PHONENO));
		if(StringUtils.isBlank(phoneNo))
			return null;
		String status  = Utils.parseString(row.get(MDBCOL_STATUS));
		String packageName = Utils.parseString(row.get(MDBCOL_PACKAGE));
		
		TelecomPackage telecomPackage = PackageManager.getInstance().getPackageByName(packageName);
		if(telecomPackage == null) {//not standard telecom package
			telecomPackage = new TelecomPackage();
			telecomPackage.setName(packageName);
			Matcher m = pp.matcher(packageName);
			if(m.find()){
				telecomPackage.setFee(Utils.parseDouble(m.group(1)));
			}
			telecomPackage.setStandard(false);
		}
		
		double income  = Utils.parseDouble(row.get(MDBCOL_INCOME).toString());
		int callAmount = Utils.parseInteger(row.get(MDBCOL_CALLAMOUNT).toString());
		
		int localCallTime    = Utils.parseInteger(row.get(MDBCOL_LOCAL_CALLTIME));
		int roamCallTime     = Utils.parseInteger(row.get(MDBCOL_ROAM_CALLTIME));
		int longDistCallTime = Utils.parseInteger(row.get(MDBCOL_LONGDISTANCE_CALLTIME));
		int callTime = localCallTime  + roamCallTime + longDistCallTime;
		
		String contractFrom = Utils.parseString(row.get(MDBCOL_CONTRACT_FROM));
		String contractTo   = Utils.parseString(row.get(MDBCOL_CONTRACT_TO));
		
		int sms = Utils.parseInteger(row.get(MDBCOL_SMS));
		double gprs = Utils.parseDouble(row.get(MDBCOL_GPRS));
		
		int isGroupUser = 0;
		String groupUser = Utils.parseString(row.get(MDBCOL_GROUP));
		if(StringUtils.isNotBlank(groupUser) && groupUser.equals("是"))
			isGroupUser = 1;
		String networkStr = Utils.parseString(row.get(MDBCOL_NETWORK));
		int network = 0;
		if(networkStr.equalsIgnoreCase("2G"))
			network = 2;
		else if(networkStr.equalsIgnoreCase("3G"))
			network = 3;
		
		ConsumerBillDetailWrapper consumerBillDetailWrapper = new ConsumerBillDetailWrapper();
		consumerBillDetailWrapper.setPhoneNo(phoneNo);
		consumerBillDetailWrapper.setTheMonth(this.theMonth);
		consumerBillDetailWrapper.setThePackage(telecomPackage);
		consumerBillDetailWrapper.setContractFrom(contractFrom);
		consumerBillDetailWrapper.setContractTo(contractTo);
		consumerBillDetailWrapper.setStatus(status);
		consumerBillDetailWrapper.setIsGroupUser(isGroupUser);
		consumerBillDetailWrapper.setNetwork(network);
		
		ConsumerBillDetail consumerBillDetail = new ConsumerBillDetail();
		consumerBillDetail.setIncome(income);
		consumerBillDetail.setCallAmount(callAmount);
		consumerBillDetail.setLocalCallTime(localCallTime);
		consumerBillDetail.setLongDistCallTime(longDistCallTime);
		consumerBillDetail.setCallTime(callTime);
		consumerBillDetail.setSms(sms);
		consumerBillDetail.setGprs(gprs);
		
		consumerBillDetail.setRental(Utils.parseDouble(row.get(MDBCOL_RENTAL)));
		consumerBillDetail.setLocalCallFee(Utils.parseDouble(row.get(MDBCOL_LOCAL_VOICE_FEE)));
		consumerBillDetail.setRoamCallFee(Utils.parseDouble(row.get(MDBCOL_ROAM_FEE)));
		consumerBillDetail.setLongDistCallFee(Utils.parseDouble(row.get(MDBCOL_LONG_DIST_FEE)));
		consumerBillDetail.setValueAddedFee(Utils.parseDouble(row.get(MDBCOL_VALUE_ADDED_FEE)));
		consumerBillDetail.setGrantFee(Utils.parseDouble(row.get(MDBCOL_GRANT_FEE)));
		consumerBillDetail.setOtherFee(Utils.parseDouble(row.get(MDBCOL_OTHER_FEE)));
		consumerBillDetail.setInternalCallTime(Utils.parseInteger(row.get(MDBCOL_INTERNAL_CALL_TIME)));
		consumerBillDetail.setInternationalCallTime(Utils.parseInteger(row.get(MDBCOL_INTERNATIONAL_CALL_TIME)));
		consumerBillDetail.setRoamCallTime(roamCallTime);
		
		
		consumerBillDetailWrapper.setBillDetail(consumerBillDetail);
		
//		System.out.println(consumerBillDetailWrapper.toString());
//		logger.debug(consumerBillDetailWrapper.toString());
		
		return consumerBillDetailWrapper;
	}
}
