package com.intellbi.data_analysis;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.intellbi.config.ConfigManager;
import com.intellbi.config.PackageConfig;
import com.intellbi.config.VIPManagerConfig;
import com.intellbi.dao.conn.MySqlConnection;
import com.intellbi.data.dataobject.ConsumerSessionMap;
import com.intellbi.data.dataobject.TelecomPackage;

public class DataLoader {
	private static double parseDouble(Object str) {
		double res = 0.0;
		if (str != null) {
			try {
				res = Double.parseDouble(str.toString());
			} catch (NumberFormatException e) {
				res = 0.0;
			}
		}
		return res;
	}

	private static long parseLong(Object str) {
		long res = 0;
		if (str != null) {
			try {
				res = Long.parseLong(str.toString());
			} catch (NumberFormatException e) {
				res = 0;
			}
		}
		return res;
	}

	private static Pattern pBase = Pattern.compile("(\\d+)元/月基本套餐([A-C]?)",Pattern.CASE_INSENSITIVE);
	private static Pattern pIphone = Pattern.compile("(iPhone) (\\d+)元/月套餐", Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) {
		
		Options options = new Options();
		options.addOption("h", "help", false, "Show help.");
		options.addOption("m", "month", true, "Set the month(yyyyMM) to analyze.");
		options.addOption("v", "vipfile", true, "Set vip consumer file under each manager.");
		options.addOption("p", "packagefile",true,"Set package definition file.");
		options.addOption("d", "dir", true, "set input data directory");
		
		BasicParser parser = new BasicParser();
		CommandLine cl = null;
		try{
			cl = parser.parse(options, args);
			if(cl.getOptions().length > 0) {
				if(cl.hasOption("h")) {
					HelpFormatter hf = new HelpFormatter();
					hf.printHelp("Options", options);
				}
			}
			else {
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		int theMonth = 201302;
		String dataDir = null;
		
		// load vip customers
		VIPManagerConfig vipManagerConfig = VIPManagerConfig.getInstance();
		Set<String> vipCustomers = vipManagerConfig.getCustomerSet();

		// load project configuration
		ConfigManager configManager = ConfigManager.getInstance();
		
		String dataLocation = dataDir;
		if(StringUtils.isBlank(dataLocation))
			dataLocation = configManager.getDataLocation();

		// load packages
		PackageConfig packageManager = PackageConfig.getInstance();
		Map<String, TelecomPackage> packages = packageManager.getPackages();

		ConsumerSessionMap consumerSessionMap = new ConsumerSessionMap();

		int i = 0;
		// load billing data
		String dataFile = configManager.getDataFileFormat();
		String mdbDataFile = dataLocation + String.format(dataFile, theMonth);
		
		MySqlConnection mysqlConn = new MySqlConnection();
		Connection conn = mysqlConn.getConnection();
		
		String sql = "insert into intellbi.bi_bills_"
				+ theMonth
				+ " (`phone_no`, `user_id`, `the_month`, `package_id`, `income`, "
				+ "`monthly_rental`, `local_voice_fee`, `roaming_fee`, `long_distance_voice_fee`,"
				+ " `value_added_fee`, `other_fee`, `grant_fee`, `call_number`, "
				+ "`local_call_duration`, `roam_call_duration`, `long_distance_call_duration`, "
				+ "`internal_call_duration`, `international_call_duration`, `sms`, `gprs`,"
				+ " `is_group_user`, `network`, `status`, `contract_from`, `contract_to`)"
				+ " values  ('%s',%d,%d,%d,%f,%f,%f,"
				+ "%f,%f,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%f,%d,%d,'%s',%d,%d)";
		
		mdbDataFile = dataLocation + String.format(dataFile, theMonth);
		try {
			Database db = DatabaseBuilder.open(new File(mdbDataFile));
			Set<String> tableNames = db.getTableNames();
			Table table = db.getTable((String) tableNames.toArray()[0]);
			for (Row row : table) {
				if(row.get("号码")==null)
					continue;
				String phoneNo = row.get("号码").toString();
				Object packageObj = row.get("套餐类型");
				String packageName = "";
				if (packageObj != null)
					packageName = packageObj.toString();

				i++;
				Matcher m = pIphone.matcher(packageName);
				if (m.find()) {// && parseDouble(m.group(1)) >= 286) { package
								// type
					packageName = (m.group(2) + m.group(1)).toLowerCase();
				} else {
					m = pBase.matcher(packageName);
					if (m.find())
						packageName = (m.group(1) + m.group(2)).toLowerCase();
					else
						continue;
				}

				double income = parseDouble(row.get("收入"));
				long callAmount = parseLong(row.get("通话次数"));

				long localCallTime = parseLong(row.get("非漫游本地计费时长"));
				long roamCallTime = parseLong(row.get("漫游计费时长"));
				long distanceCallTime = parseLong(row.get("长途计费时长"));
				double callTime = localCallTime + roamCallTime
						+ distanceCallTime;

				long contractFrom = parseLong(row.get("合约计划生效时间"));
				long contractTo = parseLong(row.get("合约计划失效时间"));

				double gprs = parseDouble(row.get("GPRS流量"));
				
				int is_group = 0;
				Object isGroupUser = row.get("是否集团用户");
				if(isGroupUser != null && isGroupUser == "是")
					is_group = 1;
				
				Object network = row.get("移动标识");
				int networkType = 0;
				if(network != null) {
					if(network.toString().equalsIgnoreCase("2G")) networkType = 2;
					else if(network.toString().equalsIgnoreCase("3G")) networkType = 3;
				}
				
				
//				System.out.println(
//						phoneNo + "\t" + 0.0 + "\t" + yearMonth+"\t"+0.0+"\t"+income+"\t"+
//						parseDouble(row.get("月租"))+ "	" +parseDouble(row.get("本地通话费"))+ "	" +
//						parseDouble(row.get("漫游费"))+ "	" + parseDouble(row.get("长途通话费"))+ "	" +
//						parseDouble(row.get("增值费"))+ "	" + parseDouble(row.get("其他费"))+ "	" +
//						parseDouble(row.get("赠款金额")) + "	" + callAmount+ "	" +localCallTime+ "	" +
//						roamCallTime+ "	" +distanceCallTime+ "	" +parseDouble(row.get("国内长途计费时长"))+ "	" +
//						parseDouble(row.get("国际长途计费时长"))+ "	" +parseDouble(row.get("点对点短信条数"))+ "	" +
//						gprs+ "	" +is_group+ "	" +networkType+ "	" +row.get("目前状态")+ "	" +contractFrom+ "	" +contractTo);
//				String sql2 = "insert into intellbi.bi_bills_"
//						+ yearMonth
//						+ " (phone_no,user_id,year_month,package_id,income,monthly_rental,local_voice_fee,"
//						+ "roaming_fee,long_distance_voice_fee,value_added_fee,other_fee,grant_fee,call_number,"
//						+ "local_call_duration,roam_call_duration,long_distance_call_duration,"
//						+ "internal_call_duration,international_call_duration,sms,gprs,is_group_user,"
//						+ "network,status,contract_from,contract_to) values ('%s',%d,%d,%d,%f,%f,%f,"
//						+ "%f,%f,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%f,%d,%d,'%s',%d,%d)";				
				String sqlStat = String.format(sql,phoneNo,0,theMonth,0,income,
						parseDouble(row.get("月租")),parseDouble(row.get("本地通话费")),
						parseDouble(row.get("漫游费")), parseDouble(row.get("长途通话费")),
						parseDouble(row.get("增值费")), parseDouble(row.get("其他费")),
						parseDouble(row.get("赠款金额")) , callAmount,localCallTime,
						roamCallTime,distanceCallTime,parseLong(row.get("国内长途计费时长")),
						parseLong(row.get("国际长途计费时长")),parseLong(row.get("点对点短信条数")),
						gprs,is_group,networkType,row.get("目前状态").toString(),contractFrom,contractTo);

				TelecomPackage _package = packages
						.get(packageName);
				
				System.out.println(_package.getName());
				System.out.println(sqlStat);
				
				Statement stat = conn.createStatement();
				stat.execute(sqlStat);

//				List<Double> values = new ArrayList<Double>();
//				values.add(income);
//				values.add(callAmount);
//				values.add(callTime);
//				values.add(gprs);

//				ConsumerSessionNode consumerSessionNode = new ConsumerSessionNode(
//						yearMonth, _package, contractFrom, contractTo, values);

//				consumerSessionMap.add(phoneNo, consumerSessionNode);

				// if(_package == null) {
				// i ++;
				// System.out.println(yearMonth + "," + packageName + "," +
				// phoneNo);
				// }
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		OutputStreamWriter osw = null;
//		try {
//			osw = new OutputStreamWriter(new FileOutputStream("score.csv"));
//			for (String phoneNo : consumerSessionMap.getMap().keySet()) {
//				if (consumerSessionMap.getMap().get(phoneNo).getPackage()
//						.getFee() < 286)// 过滤286以下
//					continue;
//
//				consumerSessionMap.getMap().get(phoneNo).calc();
//				System.out.println(phoneNo
//						+ ","
//						+ consumerSessionMap.getMap().get(phoneNo)
//								.toString(false));
//				osw.write(phoneNo
//						+ ","
//						+ consumerSessionMap.getMap().get(phoneNo)
//								.toString(false) + "\n");
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				osw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}
}
