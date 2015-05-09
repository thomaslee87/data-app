package com.intellbi.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.intellbi.dao.conn.IDBConnection;
import com.intellbi.dao.conn.MySqlConnection;
import com.intellbi.data.pojo.ConsumerDO;
import com.intellbi.data.pojo.UserDO;
import com.intellbi.dataobject.User;

public class UserManager {
    
    private static Logger logger = Logger.getLogger(UserManager.class);

    private ConfigManager config;

    public UserManager(ConfigManager config) {
        this.config = config;
        loadDBData();
    }

    private UserManager(ConfigManager config, String excel, String type) throws Exception {
        this.config = config;
        loadDBData();
        if (type.equalsIgnoreCase("MAP")) {
             loadVipCustomersFromExcel(excel);
        } else if (type.equalsIgnoreCase("PHOTO")) {
            loadPhotoDataFromExcel(excel);
        } else if (type.equalsIgnoreCase("PHOTO_4G")) {
            loadPhoto4GDataFromExcel(excel);
        } else {
            logger.error("Unsupported argument: type=" + type);
            throw new Exception("Unsupported argument: type=" + type);
        }
    }

    public boolean isVipConsumer(String phone) {
        return consumerMap.containsKey(phone);
    }

    public boolean isRegisterUser(String username) {
        if (userMap.containsKey(username))
            return true;
        return false;
    }

    private Map<String, UserDO> userMap = new HashMap<String, UserDO>();
    
    private Map<String, ConsumerDO> consumerMap = new HashMap<String, ConsumerDO>();

    private Map<String, Set<String>> user2ConsumerMap = new HashMap<String, Set<String>>();
    private Map<String, User> consumer2UserMap = new HashMap<String, User>();

    private void loadDBData() {
        IDBConnection mysqlConn = null;
        try {
            mysqlConn = new MySqlConnection(config);
            Connection conn = mysqlConn.getConnection();
            Statement stat;
            stat = conn.createStatement();

            String sql = "select id,phone,user_id,is_photo,is_photo_4g,status from bi_consumer";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String phone = rs.getString("phone");
                int userId = rs.getInt("user_id");
                boolean isPhoto = rs.getBoolean("is_photo");
                boolean isPhoto4G = rs.getBoolean("is_photo_4g");
                ConsumerDO consumer = new ConsumerDO();
                consumer.setId(id);
                consumer.setUserId(userId);
                consumer.setPhone(phone);
                consumer.setPhoto(isPhoto);
                consumer.setPhoto4G(isPhoto4G);
                
                consumerMap.put(phone, consumer);
            }
            rs.close();

            sql = "select id,group_id,username,realname from bi_user";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                int groupId = rs.getInt("group_id");
                String username = rs.getString("username");
                String realname = rs.getString("realname");
                UserDO user = new UserDO();
                user.setId(id);
                user.setGroupId(groupId);
                user.setUsername(username);
                user.setRealname(realname);
                user.setGroupId(groupId);
                
                userMap.put(username, user);
            }
            rs.close();
            
            /*sql = "select id,user_id,consumer_id from bi_user_consuemr_map";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int consumerId = rs.getInt("consumer_id");
                consumerMap.get(consumerId).setUserId(userId);
            }
            rs.close();*/

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

    private Pattern p = Pattern.compile("(.+?)\\[(.+)\\]", Pattern.CASE_INSENSITIVE);

    public Map<String, User> getVip2MgrMap() {
        return consumer2UserMap;
    }

    public Map<String, Set<String>> getVipManagerCustomerMap() {
        return user2ConsumerMap;
    }

    private void loadVipCustomersFromExcel(String excel)
            throws FileNotFoundException, IOException {
        Workbook book = null;
        Sheet sheet = null;
        if (excel.endsWith("xls"))
            book = new HSSFWorkbook(new FileInputStream(excel));
//                    ClassLoader.getSystemResourceAsStream(excel));
        else if (excel.endsWith("xlsx"))
            book = new XSSFWorkbook(new FileInputStream(excel));
//                    ClassLoader.getSystemResourceAsStream(excel));
        else
            throw new IOException("vip customer file should be xls or xlsx");
        
        String sql = "insert into bi_user (`username`,`nickname`) values (?, ?)";
        String insertConsumerSql = "insert into bi_consumer (`phone`,`user_id`) values (?,?)";
        String updateConsumerSql = "update bi_consumer set user_id=?,status=? where phone=?";
        
        Set<String> validConsumer = new HashSet<String>();
        
        IDBConnection mysqlConn = null;
        try {
            mysqlConn = new MySqlConnection(config);
            Connection conn = mysqlConn.getConnection();
            PreparedStatement pstmt;
            
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
    
    private void loadPhotoDataFromExcel(String excel) throws Exception {
        Workbook book = null;
        Sheet sheet = null;
        if (excel.endsWith("xls"))
            book = new HSSFWorkbook(new FileInputStream(excel));
        else if (excel.endsWith("xlsx"))
            book = new XSSFWorkbook(new FileInputStream(excel));
        else {
            logger.error("photo file should be xls or xlsx");
            throw new IOException("photo file should be xls or xlsx");
        }
        
        Set<Integer> photoConsumers = new HashSet<Integer>();
        
        /*//读头信息
        Map<String, Integer> header = new HashMap<String,Integer>();
        Row headerRow = sheet.getRow(0);
        for(int i = 0; i < headerRow.getLastCellNum();i ++) {
            String content = headerRow.getCell(i).toString();
            if(content.equals("手机号码") || content.equals("号码") || content.indexOf("到期合约计") == 0)
                header.put("手机号码", i);
            else
                header.put(content, i);
        }*/
        sheet = book.getSheetAt(0);
        Row headerRow = sheet.getRow(0);
        int phoneCol = -1;
        for(int i = 0; i < headerRow.getLastCellNum();i ++) {
            String content = headerRow.getCell(i).toString();
            if(content.equals("手机号码") || content.equals("号码") || content.indexOf("到期合约计") == 0)
                phoneCol = i;
        }
        
        if(phoneCol < 0) {
            logger.error("Bad excel format: no phone no column contained!");
            throw new Exception("Bad excel format: no phone no column contained!");
        }
        
        int count = 0;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            DecimalFormat df = new DecimalFormat("0");
            String phone = df.format(row.getCell(phoneCol).getNumericCellValue());
            if(phone.matches("^\\d{11}$")) {//11位数字
                ConsumerDO consumer = consumerMap.get(phone);
                if(consumer != null) {
                    photoConsumers.add(consumer.getId());
                } else {
                    logger.error("Photo phone not found: " + phone);
                    count ++;
                }
            }
        }
        if(count > 0)
            logger.error("Total: " + count);
        String phones = photoConsumers.toString();
        phones = phones.substring(1, phones.length() - 1);
        
        String sql = String.format("update bi_consumer set is_photo=1 where id in (%s)", phones);
        String unsql = String.format("update bi_consumer set is_photo=0 where id not in (%s)", phones);
        
        IDBConnection mysqlConn = null;
        try {
            mysqlConn = new MySqlConnection(config);
            Connection conn = mysqlConn.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(unsql);
            pstmt.executeUpdate();
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
    
    private void loadPhoto4GDataFromExcel(String excel) throws Exception {
        Set<Integer> photoConsumers = new HashSet<Integer>();
        int count = 0;
        
        if(excel.endsWith(".csv")) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(excel),"gbk");
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();//header row
            String[] header = line.split(",", -1);
            int phoneCol = -1;
            for(int i = 0;i < header.length;i++){
                String content = header[i];
                if(content.equals("手机号码") || content.equals("号码") || content.indexOf("到期合约计") == 0)
                    phoneCol = i;
            }
            if(phoneCol < 0){
                logger.error("Bad excel format: no phone no column contained!");
                throw new Exception("Bad excel format: no phone no column contained!");
            }
            
            while((line = br.readLine()) != null) {
                String[] row = line.split(",", -1);
                String phone = row[phoneCol];
                if(phone.matches("^\\d{11}$")) {//11位数字
                    ConsumerDO consumer = consumerMap.get(phone);
                    if(consumer != null) {
                        photoConsumers.add(consumer.getId());
                    } else {
                        logger.error("Photo phone not found: " + phone);
                        count ++;
                    }
                }
            }
            br.close();
            if(count > 0)
                logger.error("Total: " + count);
        } else {
            Workbook book = null;
            Sheet sheet = null;
            if (excel.endsWith("xls"))
                book = new HSSFWorkbook(new FileInputStream(excel));
            else if (excel.endsWith("xlsx"))
                book = new XSSFWorkbook(new FileInputStream(excel));
            else{
                logger.error("photo file should be xls or xlsx or csv");
                throw new IOException("photo file should be xls or xlsx or csv");
            }
        
            /*//读头信息
            Map<String, Integer> header = new HashMap<String,Integer>();
            Row headerRow = sheet.getRow(0);
            for(int i = 0; i < headerRow.getLastCellNum();i ++) {
                String content = headerRow.getCell(i).toString();
                if(content.equals("手机号码") || content.equals("号码") || content.indexOf("到期合约计") == 0)
                    header.put("手机号码", i);
                else
                    header.put(content, i);
            }*/
            sheet = book.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            int phoneCol = -1;
            for(int i = 0; i < headerRow.getLastCellNum();i ++) {
                String content = headerRow.getCell(i).toString();
                if(content.equals("手机号码") || content.equals("号码") || content.indexOf("到期合约计") == 0)
                    phoneCol = i;
            }
            
            if(phoneCol < 0) 
                throw new Exception("Bad excel format: no phone no column contained!");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                DecimalFormat df = new DecimalFormat("0");
                String phone = df.format(row.getCell(phoneCol).getNumericCellValue());
                if(phone.matches("^\\d{11}$")) {//11位数字
                    ConsumerDO consumer = consumerMap.get(phone);
                    if(consumer != null) {
                        photoConsumers.add(consumer.getId());
                    } else {
                        logger.error("Photo phone not found: " + phone);
                        count ++;
                    }
                }
            }
            if(count > 0)
                logger.error("Total: " + count);
        }
        
        String phones = photoConsumers.toString();
        phones = phones.substring(1, phones.length() - 1);
        
        String sql = String.format("update bi_consumer set is_photo_4g=1 where id in (%s)", phones);
        String unsql = String.format("update bi_consumer set is_photo_4g=0 where id not in (%s)", phones);
        
        IDBConnection mysqlConn = null;
        try {
            mysqlConn = new MySqlConnection(config);
            Connection conn = mysqlConn.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(unsql);
            pstmt.executeUpdate();
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
    
    // 更新用户-客户对应信息
    public static void main(String[] args)  {

        String config = System.getProperty("config");
        String file = System.getProperty("file");
        String type = System.getProperty("type");
        if (!type.equalsIgnoreCase("MAP") && !type.equalsIgnoreCase("PHOTO") && !type.equalsIgnoreCase("PHOTO_4G")) {
            logger.error("Invalid Parameter, type should be in map/photo/photo_4g");
            System.exit(1);
        }
        
        logger.info("Config file is: " + config);
        logger.info("Data file is: " + file);

        ConfigManager cfg = ConfigManager.getConfigManager(config);//new ConfigManager(config);
        UserManager userManager = null;
        try {
            userManager = new UserManager(cfg, file, type);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Map<String, Set<String>> map = userManager.getVipManagerCustomerMap();
        for (String manager : map.keySet()) {
            System.out.println(manager + "  " + map.get(manager).size());
        }
    }

    public Map<String, UserDO> getUserMap() {
        return userMap;
    }

    public Map<String, ConsumerDO> getConsumerMap() {
        return consumerMap;
    }
}
