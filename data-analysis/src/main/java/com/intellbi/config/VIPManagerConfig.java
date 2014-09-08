package com.intellbi.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.intellbi.config.ConfigManager;

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
	
	private Set<String> vipCustomerSet = new HashSet<String>();
	private Map<String,Set<String>> vipManagerCustomersMap = new HashMap<String,Set<String>>();
	
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
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(0);
			DecimalFormat df = new DecimalFormat("0");  
			String phoneNo = df.format(cell.getNumericCellValue());
			
			if(vipCustomerSet.add(phoneNo)) {
				cell = row.getCell(1);
				String vipManagerName = cell.getStringCellValue();
				
				Set<String> customerSet = vipManagerCustomersMap.get(vipManagerName);
				if(customerSet == null) {
					customerSet = new HashSet<String>();
					vipManagerCustomersMap.put(vipManagerName, customerSet);
				}
				customerSet.add(phoneNo);
			}
			else
				throw new IOException("phone number " + phoneNo + "。 Duplicated row in vip customer excel file。");
		}
	}
	
	public static void main(String[] args) {
		VIPManagerConfig vipManager = VIPManagerConfig.getInstance();
		try {
			vipManager.loadVipCustomersFromExcel();
			Map<String,Set<String>> map = vipManager.getVipManagerCustomerMap();
			for(String manager: map.keySet()) {
				System.out.println(manager + "  " + map.get(manager).size());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
