package com.intellbi.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.intellbi.data.dataobject.VipMgrInfo;

public class VIPManagerConfig {
	
	private static class VIPManagerConfigSingletonHolder {
		public final static VIPManagerConfig instance = new VIPManagerConfig();
	}
	
	public static VIPManagerConfig getInstance() {
		return VIPManagerConfigSingletonHolder.instance;
	}
	
	private VIPManagerConfig() {
		try {
			loadVipCustomersFromExcel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Logger logger = Logger.getLogger(VIPManagerConfig.class);
	
	private Pattern p = Pattern.compile("(.+?)\\[(.+)\\]", Pattern.CASE_INSENSITIVE);
	
	private Set<String> vipCustomerSet = new HashSet<String>();
	private Map<String,Set<String>> vipManagerCustomersMap = new HashMap<String,Set<String>>();
	
	private Map<String,VipMgrInfo> vip2MgrMap = new HashMap<String,VipMgrInfo>();
	
	public Map<String,VipMgrInfo> getVip2MgrMap() {
		return vip2MgrMap;
	}
	
	public Set<String> getCustomerSet() {
		return vipCustomerSet;
	}
	public Map<String,Set<String>> getVipManagerCustomerMap() {
		return vipManagerCustomersMap;
	}
	
	private void loadVipCustomersFromExcel() throws FileNotFoundException,IOException {
		loadVipCustomersFromExcel(ConfigManager.getInstance().getVipCustomersFile());
	}

	public void loadVipCustomersFromExcel(String excel) throws FileNotFoundException, IOException {
		Workbook book = null;
		Sheet sheet = null;
		if (excel.endsWith("xls"))
			book = new HSSFWorkbook(ClassLoader.getSystemResourceAsStream(excel));
		else if (excel.endsWith("xlsx"))
			book = new XSSFWorkbook(ClassLoader.getSystemResourceAsStream(excel));
		else
			throw new IOException("vip customer file should be xls or xlsx");
		sheet = book.getSheetAt(0);
		vipCustomerSet.clear();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			
			DecimalFormat df = new DecimalFormat("0");  
			String phoneNo = df.format(row.getCell(0).getNumericCellValue());
			
			if(vipCustomerSet.add(phoneNo)) {
				String vipMgrString = row.getCell(1).toString();
				Matcher m = p.matcher(vipMgrString);
				if(m.find()) {
					VipMgrInfo vipMgr = new VipMgrInfo();
					vipMgr.setUsername(m.group(1).trim());
					vipMgr.setRealname(m.group(2).trim());
					
					Set<String> customerSet = vipManagerCustomersMap.get(vipMgr.getUsername());
					if(customerSet == null) {
						customerSet = new HashSet<String>();
						vipManagerCustomersMap.put(vipMgr.getUsername(), customerSet);
					}
					customerSet.add(phoneNo);
					
					vip2MgrMap.put(phoneNo, vipMgr);
				}
			}
			else {
				logger.warn("phone number " + phoneNo + ": Duplicated row in vip customer excel file.\t" + row.getCell(1).toString() + "\t" + vip2MgrMap.get(phoneNo));
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		VIPManagerConfig vipManager = VIPManagerConfig.getInstance();
		Map<String,Set<String>> map = vipManager.getVipManagerCustomerMap();
		for(String manager: map.keySet()) {
			System.out.println(manager + "  " + map.get(manager).size());
		}
	}

}
