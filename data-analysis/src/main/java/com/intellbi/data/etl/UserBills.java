/**
 * 
 */
package com.intellbi.data.etl;

import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.intellbi.data.record.AttributeEntity;
import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordUtils;
import com.intellbi.utils.Constants;
import com.intellbi.utils.MyDateUtils;

/**
 * @author lizheng 20140518
 *
 */
public class UserBills  {
	
//	private boolean flag = false;
//	private int id ;
//	private int yearMonth_idx ;
//	
//	private int phone_no_idx;
//	private int status_idx;
//	private int join_net_time_idx;
//	private int brand_idx;
//	private int package_type_idx;
//	private int income_idx;
//	private int local_call_charges_idx;
//	private int roam_charges_idx;			
//	private int distance_call_charges_idx;
//	private int additional_fees_idx;
//	private int other_charges_idx;
//	private int call_number_idx;
//	private int local_billing_time_idx;
//	private int roam_billing_time_idx;
//	private int distance_billing_time_idx;
//	private int internal_billing_time_idx;
//	private int international_billing_time_idx;
//	private int sms_idx;
//	private int gprs_idx;
//	private int is_group_customer_idx;
//	private int network_idx;
//	private int contract_from_idx;
//	private int contract_to_idx;
//	private int customer_income_level_idx;
	
	public static final String COL_NET_TIME = "net_time";
	
	private Map<Long, SingleUserBillList> m_UserBills;
	
	public Map<Long, SingleUserBillList> getUserBills() {
		return m_UserBills;
	}
	
	public UserBills() {
		m_UserBills = new HashMap<Long, SingleUserBillList>();
	}
	
	public static class SingleUserBillList {
		private int m_ContractFrom;
		private int m_ContractTo;
		private boolean m_IsContractUser;
		private boolean m_IsGroupUser;
		private boolean m_Is3GUser;
		private List<IDataRecord> m_RecList;
		
		public SingleUserBillList() {
			m_ContractFrom = -1;
			m_ContractTo   = -1;
			m_IsContractUser = false;
			m_RecList = new ArrayList<IDataRecord>(24);
		}
		
		public void setIs3GUser(boolean is3GUser) {
			m_Is3GUser = is3GUser;
		}
		
		public boolean getIs3GUser() {
			return m_Is3GUser;
		}
		
		public void setIsGroupUser(boolean isGroupUser) {
			m_IsGroupUser = isGroupUser;
		}
		
		public boolean getIsGroupUser() {
			return m_IsGroupUser;
		}
		
		//获取用户当前是否是合约用户的状态
		public boolean isContractUser() {
			return m_IsContractUser;
		}
		
		public void setContractStatus(String contractFrom, String contractTo) {
			if(StringUtils.isBlank(contractTo)) // keep the previous contract status 
				return ;
			try {
				m_ContractFrom = Integer.parseInt(contractFrom);
				m_ContractTo   = Integer.parseInt(contractTo);
			} catch (NumberFormatException e) {
				//to do
			}
			m_IsContractUser = true;
		}
		
		public int getContractTo() {
			return m_ContractTo;
		}
		
		public List<IDataRecord> getList() {
			return m_RecList;
		}
		
		public IDataRecord get(int i) {
			return m_RecList.get(i);
		}
		
		public void add(IDataRecord rec) {
			m_RecList.add(rec);
//			contract_to_idx
		}
		
		public int size() {
			return m_RecList.size();
		}
	}

	public void add(IDataRecord record) {
//		if(!flag) {
//			id = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_PHONE_NO);
//			yearMonth_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_YEAR_MONTH);
//			
//			status_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_STATUS);
//			join_net_time_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_JOIN_NET_TIME);
//			brand_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_BRAND);
//			package_type_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_PACKAGE_TYPE);
//			income_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_INCOME);
//			local_call_charges_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_LOCAL_CALL_CHARGES);
//			roam_charges_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_ROAM_CHARGES);			
//			distance_call_charges_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_DISTANCE_CALL_CHARGES);
//			additional_fees_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_ADDITIONAL_FEES);
//			other_charges_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_OTHER_CHARGES);
//			call_number_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_CALL_NUMBER);
//			local_billing_time_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_LOCAL_BILLING_TIME);
//			roam_billing_time_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_ROAM_BILLING_TIME);
//			distance_billing_time_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_DISTANCE_BILLING_TIME);
//			internal_billing_time_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_INTERNAL_BILLING_TIME);
//			international_billing_time_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_INTERNATIONAL_BILLING_TIME);
//			sms_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_SMS);
//			gprs_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_GPRS);
//			is_group_customer_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_IS_GROUP_CUSTOMER);
//			network_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_NETWORK);
//			contract_from_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_CONTRACT_FROM);
//			contract_to_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_CONTRACT_TO);
//			customer_income_level_idx = record.getDataset().getAttributes().getAttrIdxByName(Constants.COL_CUSTOMER_INCOME_LEVEL);
//		}
		// 只考虑3G用户
		if(!RecordUtils.is3GUser(record) || RecordUtils.isGroupUser(record))
			return ;
				
//		int netTime = getMonthsBetween(record.get(join_net_time_idx).toString(), record.get(yearMonth_idx).toString());
		
		int avgArpuIdx = record.size();
		int cursor = avgArpuIdx;
//		int avgArpuIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "avg_arpu", "前三月的收入均值", "", false, avgArpuIdx), 0);
		int avgCallsIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "avg_call", "前三月的通话次数均值", "", false, avgCallsIdx), 0);
		int avgSmsIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "avg_sms", "前三月的短信数均值", "", false, avgSmsIdx), 0);
		int avgGprsIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "avg_gprs", "前三月的gprs流量均值", "", false, avgGprsIdx), 0);
		int changeArpuIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "change_arpu", "前三月的收入波动均值", "", false, changeArpuIdx), 0);
		int changeCallsIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "change_calls", "前三月的通话次数波动均值", "", false, changeCallsIdx), 0);
		int changeSmsIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "change_sms", "前三月的sms波动均值", "", false, changeSmsIdx), 0);
		int changeGprsIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "change_gprs", "前三月的gprs波动均值", "", false, changeGprsIdx), 0);
		int netTimeIdx = ++cursor;
		record.addColumn(new AttributeEntity("integer", "net_time", "入网时长", "", false, netTimeIdx), -1);
		
		int renewContractIdx = ++cursor;
		//默认值给-1，关注的是合约未到期和到期一个月内续约（1）的用户，以及合约到期一个月内未续约（0）的用户
		record.addColumn(new AttributeEntity("integer", "renew_contract", "是否合约到期1月内续约", "", false, renewContractIdx), -1);
				
		
		long phoneNo = Long.parseLong(RecordUtils.getPhoneNo(record));
		SingleUserBillList userBills = m_UserBills.get(phoneNo);
		if(userBills == null) {
			userBills = new SingleUserBillList();
			m_UserBills.put(phoneNo, userBills);
		}
		userBills.add(record);
		
		int nowIdx = userBills.size() - 1;
//		if(nowIdx < 3) 
//			return ;
		
		IDataRecord rec1 = userBills.get(nowIdx);
		IDataRecord rec2 = null;
		if(nowIdx >= 1)		
			rec2 = userBills.get(nowIdx - 1);
		IDataRecord rec3 = null;
		if(nowIdx >= 2)
			rec3 = userBills.get(nowIdx - 2);
			
		int avgArpu = getAvg_3(RecordUtils.getIncome(rec1), RecordUtils.getIncome(rec2), RecordUtils.getIncome(rec3));
		record.set(avgArpuIdx, avgArpu);
		int avgCallsNumber = getAvg_3(RecordUtils.getCallNumber(rec1), RecordUtils.getCallNumber(rec2), RecordUtils.getCallNumber(rec3));
		record.set(avgCallsIdx, avgCallsNumber);
		int avgSms = getAvg_3(RecordUtils.getSms(rec1), RecordUtils.getSms(rec2), RecordUtils.getSms(rec3));
		record.set(avgSmsIdx, avgSms);
		int avgGprs = getAvg_3(RecordUtils.getGprs(rec1), RecordUtils.getGprs(rec2), RecordUtils.getGprs(rec3));
		record.set(avgGprsIdx, avgGprs);
		double changeArpu = getChange_3(RecordUtils.getIncome(rec1), RecordUtils.getIncome(rec2), RecordUtils.getIncome(rec3));
		record.set(changeArpuIdx, changeArpu);
		double changeCalls = getChange_3(RecordUtils.getCallNumber(rec1), RecordUtils.getCallNumber(rec2), RecordUtils.getCallNumber(rec3));
		record.set(changeCallsIdx, changeCalls);
		double changeSms = getChange_3(RecordUtils.getSms(rec1), RecordUtils.getSms(rec2), RecordUtils.getSms(rec3));
		record.set(changeSmsIdx, changeSms);
		double changeGprs = getChange_3(RecordUtils.getGprs(rec1), RecordUtils.getGprs(rec2), RecordUtils.getGprs(rec3));
		record.set(changeGprsIdx, changeGprs);
//		userBills.setIsGroupUser(RecordUtils.isGroupUser(record));
		
		int yearMonth = RecordUtils.getYearMonth(record);//当前处理的月份
		record.set(netTimeIdx, getMonthsBetween(RecordUtils.getJoinNetTime(record), "" + yearMonth));
		
		// 数据很不规范，不排除用户在某些月份会有字段的缺失
		// 这里需要处理合约用户在合约期某些月份可能会没有 "合约结束日期" 的字段，如果字段缺失则取已经保存的用户状态
		// 即只认为合约结束日期字段存在且与前不同，才表示续约
//		boolean isContractUser = RecordUtils.isContractUser(record) || userBills.isContractUser() ;
		String contractTo = RecordUtils.getContractTo(record);
		
		if(userBills.isContractUser()) {
			int monthDelta = 0;
			if(StringUtils.isNotBlank(contractTo))
				monthDelta = MyDateUtils.getMonthsBetween(contractTo, ""+yearMonth);
			else
				monthDelta = MyDateUtils.getMonthsBetween(userBills.getContractTo(), yearMonth);
			if(monthDelta >= 0 && monthDelta <= 1)
				userBills.get(nowIdx - 1).set(renewContractIdx, 0);
			
			//合约结束期变大，表示发生了续约，业务上，关注的是：
			//合约未到期或者到期一个月内发生的续约 (将续约标记计到上个月，该标记表示下个月是否续约)
			if(StringUtils.isNotBlank(contractTo)) {
				if(userBills.getContractTo() < Integer.parseInt(contractTo) && monthDelta <= 1) 
					if(nowIdx >= 1)
						userBills.get(nowIdx - 1).set(renewContractIdx, 1);
			}
		}
		userBills.setContractStatus(RecordUtils.getContractFrom(record), contractTo);
		
		
	}
	
	private int getMonthsBetween(String from, String to) {
		if(from.length() > 6)
			from = from.substring(0, 6);
		if(to.length() > 6)
			to = to.substring(0, 6);
		int iFrom = Integer.parseInt(from);
		int iTo   = Integer.parseInt(to);
		return (iTo / 100 * 12 + iTo % 100) - (iFrom / 100 * 12 + iFrom % 100);
	}
	
	private int getAvg_3(int num1, int num2, int num3) {
		return (num1 + num2 + num3)/3;
	}
	
	private DecimalFormat df = new DecimalFormat("#.00");
	
	private double getChange_3(int num1, int num2, int num3) {
		if(num1 >= 0 && num2 >= 0) {
			double gap1 = num2 - num1 ;
			double gap2 = num3 - num2 ;
			double ret = (gap1/(num1 + 1) + gap2/(num2 + 1))/2; //加1，防止出现NaN
			return Double.parseDouble(df.format(ret));//保留两位小数
		}
		return 0;
	}
	
}
