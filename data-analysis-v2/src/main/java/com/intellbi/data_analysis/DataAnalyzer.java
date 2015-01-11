package com.intellbi.data_analysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.intellbi.config.ConfigManager;
import com.intellbi.config.UserManager;
import com.intellbi.dao.conn.IDBConnection;
import com.intellbi.dao.conn.MySqlConnection;
import com.intellbi.data.dao.dataobject.ConsumerBillDetailWrapper;
import com.intellbi.data.dao.dataobject.ConsumerSession;
import com.intellbi.data.dao.dataobject.ConsumerSessionMap;
import com.intellbi.data.io.BillReader;
import com.intellbi.data.io.MdbBillReader;
import com.intellbi.data.pojo.ConsumerDO;
import com.intellbi.utils.MyDateUtils;
import com.intellbi.utils.Utils;

public class DataAnalyzer {
	
	private String theMonth;
	
	private final static Logger logger = Logger.getLogger(DataAnalyzer.class);
	
	private ConfigManager config ;
	private UserManager userManager;
	
	public DataAnalyzer(ConfigManager config) {
		this.config = config;
		this.theMonth = config.get("bizmonth");
		this.userManager = new UserManager(config);
	}
	
	public boolean init() {
		return true;
	}
	
	public void run() {
		if(!init()) {
			logger.error("Initialization failure.");
			return ;
		}
		
//		Set<String> vipSet = loadTargetConsumers();
		ConsumerSessionMap consumerSessionMap = loadHistoryBills();
//		writeConsumerSessionFeatures(consumerSessionMap);
		writeConsumerSessionFeaturesToMysql(consumerSessionMap);
		updateRegularScore();
	}
	
	private void updateRegularScore(){
		IDBConnection mysqlConn = new MySqlConnection(config);
		Connection conn = mysqlConn.getConnection();
		Statement stat;
		try {
			stat = conn.createStatement();
			
			String avgSql = "select avg(score) as avg_score,count(1) as cnt from intellbit.bi_bills_" + theMonth;
			ResultSet rs1 = stat.executeQuery(avgSql);
			rs1.next();
			double avgScore = rs1.getDouble("avg_score");
			int count = rs1.getInt("cnt");
			rs1.close();

//			String updateSql = "replace into bi_bills_" + theMonth + " (id,regular_score) values ";
//			String updateArg = "(%d, %f)";
//			StringBuilder sb = new StringBuilder();
			
			String selSql = "select id,score from intellbit.bi_bills_" + theMonth ;
			ResultSet rs = stat.executeQuery(selSql);
			Map<Integer,Double> scoreMap = new HashMap<Integer,Double>();
			while(rs.next()){
				int id = rs.getInt("id");
				double score = rs.getDouble("score");	
				double regularScore = Math.abs(score - avgScore);
				scoreMap.put(id, regularScore);
			}
			rs.close();
			
			for(int id : scoreMap.keySet()) {
				String uSql = "update intellbit.bi_bills_" + theMonth + " set regular_score=" + scoreMap.get(id) + " where id=" +id;
				stat.execute(uSql);
			}
			
			//将结果按照顺序分为10份，可更自由的选择各档涵盖的人数
			List<Set<Integer>> priSet = new ArrayList<Set<Integer>>();
			for(int i = 0; i < 10; i ++) {
				priSet.add(new HashSet<Integer>());
			}
			
			selSql = "select id from intellbit.bi_bills_" + theMonth + " order by regular_score desc";
			rs = stat.executeQuery(selSql);
			int i = 0;
			while(rs.next()){
				int id = rs.getInt("id");
				
				for(int j = 0; j < 10; j ++) {
					if(i <= 0.1 * (j + 1)* count) {
						priSet.get(j).add(id);
						break;
					}
				}
				
				i++;
			}
			rs.close();
			
			String uSqlFormat = "update intellbit.bi_bills_%s set priority=%d where id in (%s)";
			for(i = 0; i < 10; i ++) {
				String ids = priSet.get(i).toString();
				String uSql = String.format(uSqlFormat, theMonth, i+1, ids.substring(1, ids.length()-1)); 
				System.out.println(uSql);
				stat.execute(uSql);
			}
			
//				if(sb.length() > 0)
//					sb.append(",");
//				sb.append(String.format(updateArg, id, regularScore));
//				
//				if(sb.length() > 4096) {
//					System.out.println(updateSql + sb.toString());
//					stat.execute(updateSql + sb.toString());
//					sb.delete(0, sb.length());
//				}
//			}
//			if(sb.length() > 0)
//				stat.execute(updateSql + sb.toString());
			
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private Set<String> loadTargetConsumers() {
//		VIPManagerConfig vipManagerConfig = VIPManagerConfig.getInstance();
//		Map<String,VipMgrInfo> vipCustomersMgrMap = vipManagerConfig.getVip2MgrMap();
//		
//		Set<String> vipSet = new HashSet<String>();
//		
//		String dataLocation = ConfigManager.getInstance().getDataLocation();
//		String dataFileFormat = ConfigManager.getInstance().getDataFileFormat();
//		String dataFile = dataLocation + String.format(dataFileFormat, theMonth);
//		
//		BillReader reader = new MdbBillReader(theMonth, dataFile);
//		if(reader.init()) {
//			while(reader.hasNext()) {
//				ConsumerBillDetailWrapper billDetailWrapper = reader.next();
//				billDetailWrapper.getPhoneNo();
//				if(vipCustomersMgrMap.)
//			}
//			reader.finish();
//		}
//		return vipSet;
//	}
	
	private ConsumerSessionMap loadHistoryBills(){
		Gson gson =new Gson();
		
		Map<String, ConsumerDO> consumers = userManager.getConsumerMap();
		
		ConsumerSessionMap consumerSessionMap = new ConsumerSessionMap();
		String dataLocation = config.getDataLocation();
		String dataFileFormat = config.getDataFileFormat();
		for(int start = 1 - config.getAnalysisLongPeriod(); start <= 0; start ++) {
			String month = MyDateUtils.getMonthByDelta(theMonth, start);
			String dataFile = dataLocation + String.format(dataFileFormat, month);
			logger.info("Begin Processing data file: " + dataFile);
			
			BillReader reader = new MdbBillReader(theMonth, dataFile);
			int i = 0;
			if(reader.init()) {
				while(reader.hasNext()) {
					ConsumerBillDetailWrapper billDetailWrapper = reader.next();
					if(billDetailWrapper != null && consumers.containsKey(billDetailWrapper.getPhoneNo())) {
						consumerSessionMap.add(billDetailWrapper.getPhoneNo(), billDetailWrapper);
						if(config.isDebug())
						    logger.info(gson.toJson(billDetailWrapper));
						i ++;
						if(config.isDebug() && i > 10000)
							break;
					}
				}
				reader.finish();
			}
		}
		return consumerSessionMap;
	}
	
	private void writeConsumerSessionFeaturesToMysql(ConsumerSessionMap consumerSessionMap) {
		
		Map<String,Integer> mgr2IDMap = new HashMap<String,Integer>() {
			private static final long serialVersionUID = -3886866250965685454L;
			{
				put("zhangq65",2);
				put("changl7",3);
				put("chenss71",4);
				put("zhangbc",5);
				put("poyu1",6);
				put("zhangwt26",7);
				put("zhangxw183",8);
				put("lijing758",9);
				put("zhangchi32",10);
				put("zhaog3",11);
				put("yinhang23",12);
				put("zhengyc25",13);
				put("zhaopu3",14);
				put("zhengchao10",15);
			}
		};
		
		String dropSql = "drop table if exists intellbit.bi_bills_" + theMonth;
		String createSql = "create table if not exists intellbit.bi_bills_" + theMonth 
				+ " like intellbit.bi_bills_template";
		
		String sql = "insert into intellbit.bi_bills_"
				+ theMonth
				+ " (`phone_no`, `user_id`, `the_month`, `package_id`,`package_name`, `income`, "
				+ "`monthly_rental`, `local_voice_fee`, `roaming_fee`, `long_distance_voice_fee`,"
				+ " `value_added_fee`, `other_fee`, `grant_fee`, `call_number`, "
				+ "`local_call_duration`, `roam_call_duration`, `long_distance_call_duration`, "
				+ "`internal_call_duration`, `international_call_duration`, `sms`, `gprs`,"
				+ " `is_group_user`, `network`, `status`, `contract_from`, `contract_to`,"
				+ "`consumer_type`,`income6`,`income3`,`voice6`,`voice3`,`gprs6`,`gprs3`,"
				+ "`package_spill`,`voice_spill`,`gprs_spill`,`income_fluctuation`,"
				+ "`voice_fluctuation`,`gprs_fluctuation`,`score`,`recommend`,`value_change`,"
				+ "`recommend_4g`, `value_change_4g`"
				+ ")"
				+ " values " ;
		String sqlArgs = "('%s',%d,%d,%d,'%s',%f,%f,%f,"
				+ "%f,%f,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%f,%d,%d,'%s',%d,%d,"
				+ "%d,%f,%f,%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,'%s',%f,'%s',%f"
				+ ")";
		
		int number = 0;
		double totalScore = 0;
		double avgScore = 0;
		
		MySqlConnection mysqlConn = new MySqlConnection(config);
		Connection conn = mysqlConn.getConnection();
		try {
			Statement stat = conn.createStatement();
			if(config.isDebug()) {
			    logger.info("Drop sql: " + dropSql);
	            logger.info("Create sql: " + createSql);
			} else {
				stat.execute(dropSql);
				stat.execute(createSql);
			}
			
			StringBuilder sb = new StringBuilder();
		
			for(String phoneNo: consumerSessionMap.getConsumerSession().keySet()) {
	//			if(consumerSessionMap.getMap().get(phoneNo).getPackage().getFee() < 286)//过滤286以下
	//				continue;
	
				ConsumerSession consumerSession = consumerSessionMap.getConsumerSession().get(phoneNo);
				consumerSession.calc(); // calculate features
				
				totalScore += consumerSession.getScore();
				number ++;
				
				ConsumerBillDetailWrapper wrapper = consumerSession.getCurrConsumerBillDetailWrapper();			
				
				int mgrId = userManager.getConsumerMap().get(phoneNo).getUserId();
				
				String sqlStat = String.format(sqlArgs, phoneNo, mgrId, Utils.parseInteger(theMonth),
					wrapper.getThePackage().getId(), wrapper.getThePackage().getName(),
					wrapper.getBillDetail().getIncome(),wrapper.getBillDetail().getRental(),
					wrapper.getBillDetail().getLocalCallFee(),wrapper.getBillDetail().getRoamCallFee(),
					wrapper.getBillDetail().getLongDistCallFee(),wrapper.getBillDetail().getValueAddedFee(),
					wrapper.getBillDetail().getOtherFee(), wrapper.getBillDetail().getGrantFee(),
					wrapper.getBillDetail().getCallAmount(), wrapper.getBillDetail().getLocalCallTime(),
					wrapper.getBillDetail().getRoamCallTime(), wrapper.getBillDetail().getLongDistCallTime(),
					wrapper.getBillDetail().getInternalCallTime(), wrapper.getBillDetail().getInternationalCallTime(),
					wrapper.getBillDetail().getSms(), wrapper.getBillDetail().getGprs(),
					wrapper.getIsGroupUser(),wrapper.getNetwork(),wrapper.getStatus(),Utils.parseInteger(wrapper.getContractFrom()),
					Utils.parseInteger(wrapper.getContractTo()),consumerSession.getUserConsumeType().getValue(),consumerSession.getIncome6(),
					consumerSession.getIncome3(),(int)consumerSession.getVoice6(),(int)consumerSession.getVoice3(),
					consumerSession.getGprs6(),consumerSession.getGprs3(),consumerSession.getPackageSpill(),
					consumerSession.getVoiceSpill(),consumerSession.getGprsSpill(),consumerSession.getIncomeFluctuation(),
					consumerSession.getVoiceFluctuation(),consumerSession.getGprsFluctuation(),consumerSession.getScore(),
					consumerSession.getRecommenedString(),consumerSession.getValueChange(),consumerSession.getRecommened4GString(),
					consumerSession.getValueChange4G()
				);
				
				if(sb.length() > 0)
					sb.append(",");
				sb.append(sqlStat);
				
				if(sb.length() >= 4096) {
					if(config.isDebug())
						System.out.println(sql + sb.toString());
					else
						stat.execute(sql + sb.toString());
					sb.delete(0, sb.length());
				}
			}
			
			if(sb.length() > 0){
				if(config.isDebug())
					System.out.println(sql + sb.toString());
				else
					stat.execute(sql + sb.toString());
			}
			
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeConsumerSessionFeatures(ConsumerSessionMap consumerSessionMap) {
		
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream("score.csv"),"gbk");
			for(String phoneNo: consumerSessionMap.getConsumerSession().keySet()) {
//				if(consumerSessionMap.getMap().get(phoneNo).getPackage().getFee() < 286)//过滤286以下
//					continue;
				consumerSessionMap.getConsumerSession().get(phoneNo).calc();
				System.out.println(phoneNo + "," + consumerSessionMap.getConsumerSession().get(phoneNo).toString(false));
				osw.write(phoneNo + "," + consumerSessionMap.getConsumerSession().get(phoneNo).toString(false) + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				osw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getTheMonth() {
		return theMonth;
	}
	
	/*public static void main(String args[]) {
		String month = "201301";
		
		while(Integer.parseInt(month) <= 201312) {
			DataAnalyzer dataAnalyzer = new DataAnalyzer(month);
			dataAnalyzer.run();
			month = MyDateUtils.getMonthByDelta(month, 1);
		}
	}*/

}
