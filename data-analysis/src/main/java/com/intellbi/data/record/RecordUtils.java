/**
 * 
 */
package com.intellbi.data.record;

import org.apache.commons.lang.StringUtils;

import com.intellbi.utils.Constants;

/**
 * @author lizheng 20140531
 *
 */
public class RecordUtils {
	
	public static String getJoinNetTime(IDataRecord rec) {
		Object joinNetTime = rec.get(rec.getAttrIdxByName(Constants.COL_JOIN_NET_TIME));
		return joinNetTime.toString();
	}
	
	public static boolean isGroupUser(IDataRecord rec) {
		Object isGroupUserObj = rec.get(rec.getAttrIdxByName(Constants.COL_IS_GROUP_CUSTOMER));
		if(isGroupUserObj == null)
			return false;
		return isGroupUserObj.equals("是");
	}
	
	public static String getContractFrom(IDataRecord rec) {
		Object contractFrom = rec.get(rec.getAttrIdxByName(Constants.COL_CONTRACT_FROM));
		if(contractFrom == null)
			return null;
		return contractFrom.toString();
	}
	
	public static String getContractTo(IDataRecord rec) {
		Object contractTo = rec.get(rec.getAttrIdxByName(Constants.COL_CONTRACT_TO));
		if(contractTo == null)
			return null;
		return contractTo.toString();
	}
	
	public static boolean isContractUser(IDataRecord rec) {
		String contractTo = getContractTo(rec);
		return StringUtils.isNotBlank(contractTo);
	}
	
	public static String getPhoneNo(IDataRecord rec) {
		Object phoneNo = rec.get(rec.getAttrIdxByName(Constants.COL_PHONE_NO));
		return phoneNo.toString();
	}
	
	public static boolean is3GUser(IDataRecord rec) {
		int networkIdx = (int)rec.getAttrIdxByName(Constants.COL_NETWORK);
		if(rec.get(networkIdx).toString().equals("3G"))
			return true;
		return false;
	}
	
	public static int getYearMonth(IDataRecord rec) {
		int yearMonthIdx = (int) rec.getAttrIdxByName(Constants.COL_YEAR_MONTH);
		return Integer.parseInt(rec.get(yearMonthIdx).toString());
	}
	
	public static int getIncome(IDataRecord rec) {
		if(rec == null)
			return 0;
		int incomeIdx = (int)rec.getAttrIdxByName(Constants.COL_INCOME);
		return (int)Double.parseDouble(rec.get(incomeIdx).toString());
	}
	
	public static int getCallNumber(IDataRecord rec) {
		if(rec == null)
			return 0;
		int callNumIdx = (int)rec.getAttrIdxByName(Constants.COL_CALL_NUMBER);
		return Integer.parseInt(rec.get(callNumIdx).toString());
	}
	
	public static int getSms(IDataRecord rec) {
		if(rec == null)
			return 0;
		int smsIdx =(int)rec.getAttrIdxByName(Constants.COL_SMS);
		return Integer.parseInt(rec.get(smsIdx).toString());
	}
	
	public static int getGprs(IDataRecord rec) {
		if(rec == null)
			return 0;
		int gprsIdx = (int)rec.getAttrIdxByName(Constants.COL_GPRS);
		return (int)Double.parseDouble(rec.get(gprsIdx).toString());
	}
}
