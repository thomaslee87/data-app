package com.intellbit.data.importer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.intellbi.data.pojo.ConsumerDO;
import com.intellbi.data.pojo.UserDO;
import com.mysql.jdbc.Statement;

public class UserImporter {

	private static final String HEADER_PHONE = "用户号码";
	private static final String HEADER_NAME  = "用户姓名";
	private static final String HEADER_MANAGER = "客户经理";
	
	private String conf;
	private List<String> head;
	
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	
	private UserImporter(String conf) {
		this.conf = conf;
	}
	
	public static UserImporter getImporter(String conf) {
		return new UserImporter(conf);
	}
	
	public void readUserExcel(String excel) throws IOException {
		Workbook book = null;
		if (excel.endsWith("xls")) {
			book = new HSSFWorkbook(new FileInputStream(excel));
		} else if (excel.endsWith("xlsx")) {
			book = new XSSFWorkbook(new FileInputStream(excel));
		} else {
			throw new IOException("用户文件支持xls和xlsx格式");
		}
		Sheet sheet = book.getSheetAt(0);
		Row header = sheet.getRow(0);
		for (int i = 0; i < header.getLastCellNum(); i++) {
			Cell cell = header.getCell(i);
			String colHeader = cell.getStringCellValue();
			headerMap.put(Integer.valueOf(i), colHeader);
		}
			
		header.getCell(arg0)
		
	}
	 private void loadVipCustomersFromExcel(String excel)
	            throws FileNotFoundException, IOException {
	            
	            pstmt = conn.prepareStatement("update bi_consumer set status=0");
	            pstmt.executeUpdate();
	            
	            sheet = book.getSheetAt(0);
	            
	            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	                
	                Row row = sheet.getRow(i);
	    
	                DecimalFormat df = new DecimalFormat("0");
	                String phoneNo = df.format(row.getCell(0).getNumericCellValue());
	                
	                String userString = row.getCell(1).toString();
	                Matcher m = p.matcher(userString);
	                if (m.find()) {
	                    String username = m.group(1).trim();
	                    String realname = m.group(2).trim();
	                    UserDO user = userMap.get(username);
	                    if(user == null) {//新增的vip经理
	                        user = new UserDO();
	                        user.setUsername(username);
	                        user.setRealname(realname);
	                        
	                        pstmt.setString(1, username);
	                        pstmt.setString(2, realname);
	                        pstmt.executeUpdate();
	                        ResultSet rs = pstmt.getGeneratedKeys();
	                        if(rs.next()) {
	                            int id = rs.getInt(1);
	                            user.setId(id);
	                        }
	                        userMap.put(username, user);
	                    }
	                    
	                    ConsumerDO consumer = consumerMap.get(phoneNo);
	                    validConsumer.add(phoneNo);
	                    if(consumer == null) {//插入新的客户
	                        pstmt = conn.prepareStatement(insertConsumerSql, Statement.RETURN_GENERATED_KEYS);
	                        pstmt.setString(1, phoneNo);
	                        pstmt.setInt(2, user.getId());
	                        pstmt.executeUpdate();
	                    } else { //更新老客户
	                        if(consumer.getUserId() != user.getId()) {
	                            pstmt = conn.prepareStatement(updateConsumerSql);
	                            pstmt.setInt(1, user.getId());
	                            pstmt.setString(3, phoneNo);
	                            pstmt.executeUpdate();
	                        }
	                    }
	                }
	    
	                    /*Set<String> customerSet = user2ConsumerMap.get(user.getUsername());
	                    if (customerSet == null) {
	                        customerSet = new HashSet<String>();
	                        user2ConsumerMap.put(user.getUsername(), customerSet);
	                    }
	                    customerSet.add(phoneNo);
	    
	                    consumer2UserMap.put(phoneNo, user);*/
	            }
	            
	            /*String validConsumerStr = validConsumer.toString();
	            validConsumerStr.substring(1, validConsumerStr.length()-1)*/
	            
	            
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            logger.error(e.toString());
	            e.printStackTrace();
	        } finally {
	            try {
	                mysqlConn.closeConnection();
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                logger.error(e.toString());
	                e.printStackTrace();
	                
	            }
	        }
	    }
	
}
