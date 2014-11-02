package com.intellbi.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
public class CustomerAction extends ActionSupport{


	private static final long serialVersionUID = -6503036606728817102L;

	private Logger logger = Logger.getLogger(CustomerAction.class);
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	@Autowired
	private TelecomPackageDao telPakcageDao;
	
	@Autowired
	private BiDataService biDataService;
	
	private String phoneNo;
	private String theBizMonth;
	private String currPkgName;
	
	private String recommend1;
	private String recommend2;
	private String recommend3;
	
	private double recommendCost1;
	private double recommendCost2;
	private double recommendCost3;
	
	private double recommendSave1;
	private double recommendSave2;
	private double recommendSave3;
	
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
		List<TelecomPackageDO> packages = telPakcageDao.getAll();
		for(TelecomPackageDO pkg: packages) {
			idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
		}
		
		boolean flag = true;
		
		for(String theMonth: theMonthList) {
			try{
				ConsumerBillDO consumerDo = consumerBillService.getConsumerMonthBill(thePhoneNo, theMonth);
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
	    						}
	                        }
						}
						
						if(consumerDo.getPriority() <= 3)
							consumerDo.setPriorityDesc("高");
						else if(consumerDo.getPriority() >=8 )
							consumerDo.setPriorityDesc("低");
						else
							consumerDo.setPriorityDesc("中");
						
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
