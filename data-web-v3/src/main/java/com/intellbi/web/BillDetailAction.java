package com.intellbi.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.TelecomPackageDao;
import com.intellbi.dataobject.BiDataDO;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.dataobject.TelecomPackageDO;
import com.intellbi.dataobject.UserDO;
import com.intellbi.service.BiDataService;
import com.intellbi.service.ConsumerBillService;
import com.intellbi.utils.Constants;
import com.intellbi.utils.MyDateUtils;
import com.opensymphony.xwork2.ActionSupport;


/**
 * @author lizheng
 * 获取用户近期消费详情
 */
public class BillDetailAction extends ActionSupport{

	private static final long serialVersionUID = -1847821641321803952L;

	private Logger logger = Logger.getLogger(ContractTaskDatatablesAction.class);
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	@Autowired
	private TelecomPackageDao telPakcageDao;
	
	@Autowired
	private BiDataService biDataService;
	
	private String phoneNo;
	private String theBizMonth;
	private String currPkgName;
	
	private String theDataMonth;
	
	public String getTheDataMonth() {
		return theDataMonth;
	}

	public void setTheDataMonth(String theDataMonth) {
		this.theDataMonth = theDataMonth;
	}

	private String rcmd1pkg;
	private String rcmd2pkg;
	private String rcmd3pkg;
	
	private String recommend1;
	private String recommend2;
	private String recommend3;
	
	private double recommendCost1;
	private double recommendCost2;
	private double recommendCost3;
	
	private double recommendSave1;
	private double recommendSave2;
	private double recommendSave3;
	
	private String rcmd1pkg4G;
	private String rcmd2pkg4G;
	private String rcmd3pkg4G;
	
	private String recommend1_4G;
	private String recommend2_4G;
	private String recommend3_4G;
	
	private double recommendCost1_4G;
	private double recommendCost2_4G;
	private double recommendCost3_4G;
	
	private double recommendSave1_4G;
	private double recommendSave2_4G;
	private double recommendSave3_4G;
	
	
	public String getRcmd1pkg4G() {
		return rcmd1pkg4G;
	}

	public void setRcmd1pkg4G(String rcmd1pkg4g) {
		rcmd1pkg4G = rcmd1pkg4g;
	}

	public String getRcmd2pkg4G() {
		return rcmd2pkg4G;
	}

	public void setRcmd2pkg4G(String rcmd2pkg4g) {
		rcmd2pkg4G = rcmd2pkg4g;
	}

	public String getRcmd3pkg4G() {
		return rcmd3pkg4G;
	}

	public void setRcmd3pkg4G(String rcmd3pkg4g) {
		rcmd3pkg4G = rcmd3pkg4g;
	}

	public String getRecommend1_4G() {
		return recommend1_4G;
	}

	public void setRecommend1_4G(String recommend1_4g) {
		recommend1_4G = recommend1_4g;
	}

	public String getRecommend2_4G() {
		return recommend2_4G;
	}

	public void setRecommend2_4G(String recommend2_4g) {
		recommend2_4G = recommend2_4g;
	}

	public String getRecommend3_4G() {
		return recommend3_4G;
	}

	public void setRecommend3_4G(String recommend3_4g) {
		recommend3_4G = recommend3_4g;
	}

	public double getRecommendCost1_4G() {
		return recommendCost1_4G;
	}

	public void setRecommendCost1_4G(double recommendCost1_4G) {
		this.recommendCost1_4G = recommendCost1_4G;
	}

	public double getRecommendCost2_4G() {
		return recommendCost2_4G;
	}

	public void setRecommendCost2_4G(double recommendCost2_4G) {
		this.recommendCost2_4G = recommendCost2_4G;
	}

	public double getRecommendCost3_4G() {
		return recommendCost3_4G;
	}

	public void setRecommendCost3_4G(double recommendCost3_4G) {
		this.recommendCost3_4G = recommendCost3_4G;
	}

	public double getRecommendSave1_4G() {
		return recommendSave1_4G;
	}

	public void setRecommendSave1_4G(double recommendSave1_4G) {
		this.recommendSave1_4G = recommendSave1_4G;
	}

	public double getRecommendSave2_4G() {
		return recommendSave2_4G;
	}

	public void setRecommendSave2_4G(double recommendSave2_4G) {
		this.recommendSave2_4G = recommendSave2_4G;
	}

	public double getRecommendSave3_4G() {
		return recommendSave3_4G;
	}

	public void setRecommendSave3_4G(double recommendSave3_4G) {
		this.recommendSave3_4G = recommendSave3_4G;
	}

	public String getRcmd1pkg() {
		return rcmd1pkg;
	}

	public void setRcmd1pkg(String rcmd1pkg) {
		this.rcmd1pkg = rcmd1pkg;
	}

	public String getRcmd2pkg() {
		return rcmd2pkg;
	}

	public void setRcmd2pkg(String rcmd2pkg) {
		this.rcmd2pkg = rcmd2pkg;
	}

	public String getRcmd3pkg() {
		return rcmd3pkg;
	}

	public void setRcmd3pkg(String rcmd3pkg) {
		this.rcmd3pkg = rcmd3pkg;
	}

	public double getRecommendSave1() {
		return recommendSave1;
	}

	public void setRecommendSave1(double recommendSave1) {
		this.recommendSave1 = recommendSave1;
	}

	public double getRecommendSave2() {
		return recommendSave2;
	}

	public void setRecommendSave2(double recommendSave2) {
		this.recommendSave2 = recommendSave2;
	}

	public double getRecommendSave3() {
		return recommendSave3;
	}

	public void setRecommendSave3(double recommendSave3) {
		this.recommendSave3 = recommendSave3;
	}

	private List<ConsumerBillDO> recentBillList;
	
	public String getRecommend1() {
		return recommend1;
	}

	public void setRecommend1(String recommend1) {
		this.recommend1 = recommend1;
	}

	public String getRecommend2() {
		return recommend2;
	}

	public void setRecommend2(String recommend2) {
		this.recommend2 = recommend2;
	}

	public String getRecommend3() {
		return recommend3;
	}

	public void setRecommend3(String recommend3) {
		this.recommend3 = recommend3;
	}

	public double getRecommendCost1() {
		return recommendCost1;
	}

	public void setRecommendCost1(double recommendCost1) {
		this.recommendCost1 = recommendCost1;
	}

	public double getRecommendCost2() {
		return recommendCost2;
	}

	public void setRecommendCost2(double recommendCost2) {
		this.recommendCost2 = recommendCost2;
	}

	public double getRecommendCost3() {
		return recommendCost3;
	}

	public void setRecommendCost3(double recommendCost3) {
		this.recommendCost3 = recommendCost3;
	}

	public List<ConsumerBillDO> getRecentBillList() {
		return recentBillList;
	}

	public void setRecentBillList(List<ConsumerBillDO> recentBillList) {
		this.recentBillList = recentBillList;
	}

	public String getCurrPkgName() {
		return currPkgName;
	}

	public void setCurrPkgName(String currPkgName) {
		this.currPkgName = currPkgName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getTheBizMonth() {
		return theBizMonth;
	}

	public void setTheBizMonth(String theBizMonth) {
		this.theBizMonth = theBizMonth;
	}

	private boolean checkTheMonth(String theMonth){
		return StringUtils.isNotBlank(theMonth)
				&& MyDateUtils.checkMonthFormat(theMonth, "yyyyMM");
	}
	
	public String getBillDetails() {
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated())
			return LOGIN;
		
		//todo:判断是否是当前登陆用户名下的客户
		
		if(!checkTheMonth(theBizMonth) || StringUtils.isBlank(phoneNo))
			return ERROR;
		
        UserDO userDO = (UserDO)subject.getSession().getAttribute("userDO");
        int userId = userDO.getId();
        if(userId == 1) //admin
        	userId = 0;
        
        setRecentBillList(getConsumerRecentBills(phoneNo, theBizMonth));
        
        consumerBillService.updateContractStatus(phoneNo, theDataMonth, 1);
        
		return SUCCESS;
	}
	
	private List<ConsumerBillDO> getConsumerRecentBills(String thePhoneNo, String theBizMonth) {
		List<ConsumerBillDO> recentBills = new ArrayList<ConsumerBillDO>();
		
		List<BiDataDO> biDataDOList = biDataService.findAll();
        if(biDataDOList == null || biDataDOList.size() == 0) {
        	return null;
        }
//      BiDataDO biDataDO = biDataService.getRecentBiDataDO();//最新的数据月份，从该月分表中加载数据
        
        List<String> theMonthList = new ArrayList<String>(12);
        //使用小于等于选中月份的最新数据
        for(BiDataDO biDataDO: biDataDOList) {
        	String theMonth = biDataDO.getTheMonth();
        	if(theMonth.compareTo(theBizMonth) <= 0) {
        		theMonthList.add(theMonth);
        		if(theMonthList.size() >= Constants.RECENT_MONTH)
        			break;
        	}
        }
		
		//读数据库加载一次
		Map<String,String> idPkgMap = new HashMap<String, String>();
		Map<String, TelecomPackageDO> pkgMap = new HashMap<String,TelecomPackageDO>();
		List<TelecomPackageDO> packages = telPakcageDao.getAll();
		for(TelecomPackageDO pkg: packages) {
			idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
			pkgMap.put(pkg.getName(), pkg);
		}
		
		//加载4G套餐信息
		Map<String,String> idPkgMap4G = new HashMap<String,String>();
		Map<String, TelecomPackageDO> pkgMap4G = new HashMap<String,TelecomPackageDO>();
		List<TelecomPackageDO> packages4G = telPakcageDao.getAll4G();
		for(TelecomPackageDO pkg : packages4G) {
			idPkgMap4G.put(String.valueOf(pkg.getId()), pkg.getName());
			pkgMap4G.put(pkg.getName(), pkg);
		}
		
		boolean flag = true;
		
		for(String theMonth: theMonthList) {
			try{
				ConsumerBillDO consumerDo = consumerBillService.getConsumerMonthBill(thePhoneNo, theMonth,-1);
				if(consumerDo != null){
					
					if(flag) {
						setCurrPkgName(consumerDo.getPackageName());
						
						String[] rcmdPackageIds = consumerDo.getRecommend().split(",");
						int rcmdNumber = rcmdPackageIds.length;
						if(rcmdNumber >= 1){
						    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
						    if(rcmdPackagePair.length == 2) {
	    						String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
	    						if(StringUtils.isNotBlank(rcmd1)) {
	    							consumerDo.setRecommend1(rcmd1);
	    							consumerDo.setRecommendCost1(Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							recommend1 = rcmd1;
	    							recommendCost1 = Math.round(Double.parseDouble(rcmdPackagePair[1]));
	    							recommendSave1 = Math.round(consumerDo.getIncome6() - Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							TelecomPackageDO pkg = pkgMap.get(rcmd1);
	    							
	    							String voiceString = String.valueOf((int)pkg.getVoice());
	    							if(pkg.getLocalVoice() > 0.001) 
	    								voiceString = String.valueOf((int)pkg.getLocalVoice()) + "（本地）";
	    							
	    							DecimalFormat decimalFormat = new DecimalFormat("#0.0000");//格式化设置  
	    					        
	    							rcmd1pkg = "<p>套餐费用：" + pkg.getFee() + "元</p>" +
	    							"<p>套餐内赠送:<ul>" +
	    									"<li>语音（分钟）:" + voiceString +"</li>"+
	    									"<li>国内流量（M）:"+ (int)pkg.getGprs() +"</li>"+
	    									"<li>短信（条）:"+ pkg.getSms() +"</li>" +
//	    									"<li>彩信:"+ pkg.getMms()+"</li>"+
	    									"</ul></p>" +
	    							"<p>超套餐计费:<ul>" +
	    									"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
	    									"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
	    									"<li>短信（元/条）:"+ pkg.getSmsPrice() +"</li></ul></p>";
	    						}
						    }
						}
						if(rcmdNumber >= 2){
						    String[] rcmdPackagePair = rcmdPackageIds[1].split(":");
						    if(rcmdPackagePair.length == 2) {
	    						String rcmd2 = idPkgMap.get(rcmdPackagePair[0]);
	    						if(StringUtils.isNotBlank(rcmd2)) {
	    							consumerDo.setRecommend2(rcmd2);
	    							consumerDo.setRecommendCost2(Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							recommend2 = rcmd2;
	    							recommendCost2 = Math.round(Double.parseDouble(rcmdPackagePair[1]));
	    							recommendSave2 = Math.round(consumerDo.getIncome6() - Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							TelecomPackageDO pkg = pkgMap.get(rcmd2);
	    							
	    							String voiceString = String.valueOf((int)pkg.getVoice());
	    							if(pkg.getLocalVoice() > 0.001) 
	    								voiceString = String.valueOf((int)pkg.getLocalVoice()) + "（本地）";
	    							
	    							DecimalFormat decimalFormat = new DecimalFormat("#0.0000");//格式化设置  
	    					        
	    							rcmd2pkg = "<p>套餐费用：" + pkg.getFee() + "元</p>" +
	    							"<p>套餐内赠送:<ul>" +
	    									"<li>语音（分钟）:" + voiceString +"</li>"+
	    									"<li>国内流量（M）:"+ (int)pkg.getGprs() +"</li>"+
	    									"<li>短信:"+ pkg.getSms() +"</li>" +
//	    									"<li>彩信:"+ pkg.getMms()+"</li>"+
	    									"</ul></p>" +
	    							"<p>超套餐计费:<ul>" +
	    									"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
	    									"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
	    									"<li>短信:"+ pkg.getSmsPrice() +"</li></ul></p>";
	    						}
						    }
						}
						if(rcmdNumber >= 3){
						    String[] rcmdPackagePair = rcmdPackageIds[2].split(":");
	                        if(rcmdPackagePair.length == 2) {
	    						String rcmd3 = idPkgMap.get(rcmdPackagePair[0]);
	    						if(StringUtils.isNotBlank(rcmd3)) {
	    							consumerDo.setRecommend3(rcmd3);
	    							consumerDo.setRecommendCost3(Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							recommend3 = rcmd3;
	    							recommendCost3 = Math.round(Double.parseDouble(rcmdPackagePair[1]));
	    							recommendSave3 = Math.round(consumerDo.getIncome6() - Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							TelecomPackageDO pkg = pkgMap.get(rcmd3);
	    							
	    							String voiceString = String.valueOf((int)pkg.getVoice());
	    							if(pkg.getLocalVoice() > 0.001) 
	    								voiceString = String.valueOf((int)pkg.getLocalVoice()) + "（本地）";
	    							
	    							DecimalFormat decimalFormat = new DecimalFormat("#0.0000");//格式化设置  
	    					        
	    							rcmd3pkg = "<p>套餐费用：" + pkg.getFee() + "元</p>" +
	    							"<p>套餐内赠送:<ul>" +
	    									"<li>语音（分钟）:" + voiceString +"</li>"+
	    									"<li>国内流量（M）:"+ (int)pkg.getGprs() +"</li>"+
	    									"<li>短信:"+ pkg.getSms() +"</li>" +
//	    									"<li>彩信:"+ pkg.getMms()+"</li>"+
	    									"</ul></p>" +
	    							"<p>超套餐计费:<ul>" +
	    									"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
	    									"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
	    									"<li>短信:"+ pkg.getSmsPrice() +"</li></ul></p>";
	    						}
	                        }
						}
						
						//4G套餐
						rcmdPackageIds = consumerDo.getRecommend4G().split(",");
						rcmdNumber = rcmdPackageIds.length;
						if(rcmdNumber >= 1){
						    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
						    if(rcmdPackagePair.length == 2) {
	    						String rcmd1 = idPkgMap4G.get(rcmdPackagePair[0]);
	    						if(StringUtils.isNotBlank(rcmd1)) {
//	    							consumerDo.setRecommend1(rcmd1);
//	    							consumerDo.setRecommendCost1(Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							recommend1_4G = rcmd1;
	    							recommendCost1_4G = Math.round(Double.parseDouble(rcmdPackagePair[1]));
	    							recommendSave1_4G = Math.round(consumerDo.getIncome6() - Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							TelecomPackageDO pkg = pkgMap4G.get(rcmd1);
	    							
	    							String voiceString = String.valueOf((int)pkg.getVoice());
	    							if(pkg.getLocalVoice() > 0.001) 
	    								voiceString = String.valueOf((int)pkg.getLocalVoice()) + "（本地）";
	    							
	    							DecimalFormat decimalFormat = new DecimalFormat("#0.0000");//格式化设置  
	    					        
	    							rcmd1pkg4G = "<p>套餐费用：" + pkg.getFee() + "元</p>" +
	    							"<p>套餐内赠送:<ul>" +
	    									"<li>语音（分钟）:" + voiceString +"</li>"+
	    									"<li>国内流量（M）:"+ (int)pkg.getGprs() +"</li>"+
//	    									"<li>短信（条）:"+ pkg.getSms() +"</li>" +
//	    									"<li>彩信:"+ pkg.getMms()+"</li>"+
	    									"</ul></p>" +
	    							"<p>超套餐计费:<ul>" +
	    									"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
	    									"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//	    									"<li>短信（元/条）:"+ pkg.getSmsPrice() +"</li>"+
	    									"</ul></p>";
	    						}
						    }
						}
						if(rcmdNumber >= 2){
						    String[] rcmdPackagePair = rcmdPackageIds[1].split(":");
						    if(rcmdPackagePair.length == 2) {
	    						String rcmd2 = idPkgMap4G.get(rcmdPackagePair[0]);
	    						if(StringUtils.isNotBlank(rcmd2)) {
//	    							consumerDo.setRecommend2(rcmd2);
//	    							consumerDo.setRecommendCost2(Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							recommend2_4G = rcmd2;
	    							recommendCost2_4G = Math.round(Double.parseDouble(rcmdPackagePair[1]));
	    							recommendSave2_4G = Math.round(consumerDo.getIncome6() - Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							TelecomPackageDO pkg = pkgMap4G.get(rcmd2);
	    							
	    							String voiceString = String.valueOf((int)pkg.getVoice());
	    							if(pkg.getLocalVoice() > 0.001) 
	    								voiceString = String.valueOf((int)pkg.getLocalVoice()) + "（本地）";
	    							
	    							DecimalFormat decimalFormat = new DecimalFormat("#0.0000");//格式化设置  
	    					        
	    							rcmd2pkg4G = "<p>套餐费用：" + pkg.getFee() + "元</p>" +
	    							"<p>套餐内赠送:<ul>" +
	    									"<li>语音（分钟）:" + voiceString +"</li>"+
	    									"<li>国内流量（M）:"+ (int)pkg.getGprs() +"</li>"+
//	    									"<li>短信:"+ pkg.getSms() +"</li>" +
//	    									"<li>彩信:"+ pkg.getMms()+"</li>"+
	    									"</ul></p>" +
	    							"<p>超套餐计费:<ul>" +
	    									"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
	    									"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//	    									"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
	    									"</ul></p>";
	    						}
						    }
						}
						if(rcmdNumber >= 3){
						    String[] rcmdPackagePair = rcmdPackageIds[2].split(":");
	                        if(rcmdPackagePair.length == 2) {
	    						String rcmd3 = idPkgMap4G.get(rcmdPackagePair[0]);
	    						if(StringUtils.isNotBlank(rcmd3)) {
//	    							consumerDo.setRecommend3(rcmd3);
//	    							consumerDo.setRecommendCost3(Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							recommend3_4G = rcmd3;
	    							recommendCost3_4G = Math.round(Double.parseDouble(rcmdPackagePair[1]));
	    							recommendSave3_4G = Math.round(consumerDo.getIncome6() - Double.parseDouble(rcmdPackagePair[1]));
	    							
	    							TelecomPackageDO pkg = pkgMap4G.get(rcmd3);
	    							
	    							String voiceString = String.valueOf((int)pkg.getVoice());
	    							if(pkg.getLocalVoice() > 0.001) 
	    								voiceString = String.valueOf((int)pkg.getLocalVoice()) + "（本地）";
	    							
	    							DecimalFormat decimalFormat = new DecimalFormat("#0.0000");//格式化设置  
	    					        
	    							rcmd3pkg4G = "<p>套餐费用：" + pkg.getFee() + "元</p>" +
	    							"<p>套餐内赠送:<ul>" +
	    									"<li>语音（分钟）:" + voiceString +"</li>"+
	    									"<li>国内流量（M）:"+ (int)pkg.getGprs() +"</li>"+
//	    									"<li>短信:"+ pkg.getSms() +"</li>" +
//	    									"<li>彩信:"+ pkg.getMms()+"</li>"+
	    									"</ul></p>" +
	    							"<p>超套餐计费:<ul>" +
	    									"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
	    									"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//	    									"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
	    									"</ul></p>";
	    						}
	                        }
						}
						
						if(consumerDo.getPriority() <= 3)
							consumerDo.setPriorityDesc("高");
						else if(consumerDo.getPriority() >=8 )
							consumerDo.setPriorityDesc("低");
						else
							consumerDo.setPriorityDesc("中");
						
						theDataMonth = theMonth;
						
						flag = false;
					}
					
					recentBills.add(consumerDo);				
				}
			}catch(Exception e) {
				continue;
			}
		}
		return recentBills;
	}

}
