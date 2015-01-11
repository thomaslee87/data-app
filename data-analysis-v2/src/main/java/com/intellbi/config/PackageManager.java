package com.intellbi.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.intellbi.dao.conn.IDBConnection;
import com.intellbi.dao.conn.MySqlConnection;
import com.intellbi.data.dao.dataobject.TelecomPackage;

public class PackageManager {
    
    private static PackageManager instance = null;
    
    private final static Logger logger = Logger.getLogger(PackageManager.class);
	
	private Map<String,TelecomPackage> packages = new HashMap<String,TelecomPackage>();
	
	private ConfigManager config;
	
	private static Pattern pBase = Pattern.compile("(\\d+)元/月基本套餐([A-C]?)",Pattern.CASE_INSENSITIVE);
	private static Pattern pIphone = Pattern.compile("(iPhone) (\\d+)元/月套餐", Pattern.CASE_INSENSITIVE);
	
	public TelecomPackage getPackageByName(String packageName){
		Matcher m = pIphone.matcher(packageName);
		if (m.find()) {
			packageName = (m.group(1) + m.group(2)).toLowerCase();
		} else {
			m = pBase.matcher(packageName);
			if (m.find())
				packageName = (m.group(1) + m.group(2)).toLowerCase();
		}
		return packages.get(packageName);
	}
	
//	private Pattern p = Pattern.compile("(\\d+).*?([a-z]+)", Pattern.CASE_INSENSITIVE);
	
	private void loadDBData(){
	    IDBConnection mysqlConn = null;
        try {
            mysqlConn = new MySqlConnection(config);
            Connection conn = mysqlConn.getConnection();
            Statement stat;
            stat = conn.createStatement();

            String sql = "select id,name,fee,voice,local_voice,long_dist_voice,gprs,sms,mms,local_voice_price,long_dist_voice_price,gprs_price,sms_price,feature from bi_package";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                TelecomPackage telecomPackage = new TelecomPackage();
                telecomPackage.setId(rs.getInt("id"));
                
                telecomPackage.setName(rs.getString("name"));
                telecomPackage.setFee(rs.getDouble("fee"));
                telecomPackage.setVoice(rs.getDouble("voice"));
                
                telecomPackage.setLocalVoice(rs.getDouble("local_voice"));
                telecomPackage.setLongDistVoice(rs.getDouble("long_dist_voice"));
                telecomPackage.setGprs(rs.getDouble("gprs"));
                telecomPackage.setSms(rs.getInt("sms"));
                
                telecomPackage.setLocalVoicePrice(rs.getDouble("local_voice_price"));
                telecomPackage.setLongDistVoicePrice(rs.getDouble("long_dist_voice_price"));
                telecomPackage.setGprsPrice(rs.getDouble("gprs_price"));
                telecomPackage.setSmsPrice(rs.getDouble("sms_price"));
                
                telecomPackage.setFeature(rs.getString("feature"));
                
                packages.put(telecomPackage.getFeature(), telecomPackage);
            }
            
            rs.close();
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
	
	public int getIdByFeature(String feature){
	    return packages.get(feature).getId();
	}
	
	public PackageManager(ConfigManager config) {
	    this.config = config;
	    loadDBData();
	}
	
	public static PackageManager getInstance() {
	    if(instance == null)
	        instance = new PackageManager(ConfigManager.getInstance());
	    return instance;
    }
	
	public Map<String, TelecomPackage> getPackages() {
		return packages;
	}
	
}
