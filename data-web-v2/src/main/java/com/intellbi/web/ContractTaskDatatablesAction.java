package com.intellbi.web;

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
import com.intellbi.utils.MyDateUtils;
import com.opensymphony.xwork2.ActionSupport;

//响应datatables ajax请求
public class ContractTaskDatatablesAction extends ActionSupport{

	private static final long serialVersionUID = 1430175981188769414L;
	
	private Logger logger = Logger.getLogger(ContractTaskDatatablesAction.class);
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	@Autowired
	private TelecomPackageDao telPakcageDao;
	
	@Autowired
	private BiDataService biDataService;
	
	private String aoData;
	
	public String getAoData() {
		return aoData;
	}

	public void setAoData(String aoData) {
		this.aoData = aoData;
	}
	
	private Map<String,Object> dataMap;

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public String getContractConsumerList(){
		dataMap = new HashMap<String, Object>();
		
		System.out.println(aoData);
		
		dataMap.put("draw",6);
		dataMap.put("recordsTotal",57);
		dataMap.put("recordsFiltered",57);
		
		return SUCCESS;
	}

	private int userId = -1;
	private String theMonth;
	private String phoneNo;
	
	private String ordertype;
	
	//分页
	private int totalNumber;
	private int currPage = 1;
	private int pageSize = 20;
	private int totalPages = 1;
	private int prevPage = 0;
	private int nextPage = 2;
	
	private String theMonthString;
	private String calenderString;
	
	private ConsumerBillDO consumerBillDO;
	
	private List<ConsumerBillDO> bills;
	
	private List<ConsumerBillDO> recentBills;
	
	public String getAllContractConsumerBills(){
	    Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated() && pageSize > 0 && currPage > 0){
            UserDO userDO = (UserDO)subject.getSession().getAttribute("userDO");
            userId = userDO.getId();
            if(userId == 1) //admin
                userId = 0;
            BiDataDO biDataDO = biDataService.getRecentBiDataDO();
            if(StringUtils.isBlank(theMonth))
                theMonth = biDataDO.getTheMonth();

            if(!checkTheMonth())
                return ERROR;
            
            try {
                if(StringUtils.isBlank(ordertype))
                    ordertype = "regular_score";
                    
                setBills(consumerBillService.getAllMonthBills(theMonth.substring(0,6), phoneNo, userId, currPage, pageSize,ordertype,-1));
                for(ConsumerBillDO consumerBillDO: bills) {
                    int contractTo = (int)(consumerBillDO.getContractTo());
                    consumerBillDO.setContractRemain(MyDateUtils.getMonthsBetween(Integer.parseInt(theMonth.substring(0,6)), contractTo));
                        
                    if(consumerBillDO.getPriority() <= 3)
                      consumerBillDO.setPriorityDesc("高");
                    else if(consumerBillDO.getPriority() >=8 )
                      consumerBillDO.setPriorityDesc("低");
                    else
                      consumerBillDO.setPriorityDesc("中");
                    
                    if(consumerBillDO.getValueChange() > 10)
                      consumerBillDO.setValueChangeDesc("升值");
                    else if(consumerBillDO.getValueChange() < -10)
                      consumerBillDO.setValueChangeDesc("贬值");
                    else
                      consumerBillDO.setValueChangeDesc("持平");
                }
                
                totalNumber = consumerBillService.getTotalCount(theMonth.substring(0,6),phoneNo,userId,-1);
                totalPages = totalNumber / pageSize + (totalNumber % pageSize == 0?0:1);
                prevPage = currPage - 1;
                if(prevPage <= 0)
                    prevPage = 0;
                nextPage = currPage + 1;
                if(nextPage > totalPages)
                    nextPage = 0;
            } catch(Exception e) {
                setBills(null);
            }
        }
        return SUCCESS;
	}
	
	public String getContractConsumerBills() {
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated() && pageSize > 0 && currPage > 0){
			UserDO userDO = (UserDO)subject.getSession().getAttribute("userDO");
			userId = userDO.getId();
			if(userId == 1) //admin
				userId = 0;
			BiDataDO biDataDO = biDataService.getRecentBiDataDO();
			if(StringUtils.isBlank(theMonth))
				theMonth = biDataDO.getTheMonth();

			if(!checkTheMonth())
				return ERROR;
			
			try {
				if(StringUtils.isBlank(ordertype))
					ordertype = "regular_score";
				
				//读数据库加载一次
		        Map<String,String> idPkgMap = new HashMap<String, String>();
		        List<TelecomPackageDO> packages = telPakcageDao.getAll();
		        for(TelecomPackageDO pkg: packages) {
		            idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
		        }
					
				setBills(consumerBillService.getAllMonthBills(theMonth.substring(0,6), phoneNo, userId, currPage, pageSize,ordertype,6));
				for(ConsumerBillDO consumerBillDO: bills) {
				    int contractTo = (int)(consumerBillDO.getContractTo());
				    consumerBillDO.setContractRemain(MyDateUtils.getMonthsBetween(Integer.parseInt(theMonth.substring(0,6)), contractTo));
				        
			        if(consumerBillDO.getPriority() <= 3)
                      consumerBillDO.setPriorityDesc("高");
	                else if(consumerBillDO.getPriority() >=8 )
	                  consumerBillDO.setPriorityDesc("低");
	                else
	                  consumerBillDO.setPriorityDesc("中");
	                
	                if(consumerBillDO.getValueChange() > 10)
	                  consumerBillDO.setValueChangeDesc("升值");
	                else if(consumerBillDO.getValueChange() < -10)
	                  consumerBillDO.setValueChangeDesc("贬值");
	                else
	                  consumerBillDO.setValueChangeDesc("持平");
	                
	                consumerBillDO.setRecommend1("暂无更合适套餐推荐.");
                    consumerBillDO.setRecommendCost1(0);
	                String[] rcmdPackageIds = consumerBillDO.getRecommend().split(",");
                    int rcmdNumber = rcmdPackageIds.length;
                    if(rcmdNumber >= 1){
                        String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
                        if(rcmdPackagePair.length == 2) {   
                            String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
                            if(StringUtils.isNotBlank(rcmd1)) {
                                double costSave = Math.round(consumerBillDO.getIncome6()) - Math.round(Double.parseDouble(rcmdPackagePair[1]));
                                if(costSave > 0) {
                                    consumerBillDO.setRecommend1(rcmd1);
                                    consumerBillDO.setRecommendCost1(costSave);
                                }
                            }
                        }
                    }
				}
				
//				setBills(consumerBillService.getAllMonthBills(theMonth.substring(0,6), phoneNo, userId, currPage, pageSize,ordertype));
//				for(ConsumerBillDO consumerBillDO : bills) {
//					if(consumerBillDO.getPriority() <= 3)
//						consumerBillDO.setPriorityDesc("高");
//					else if(consumerBillDO.getPriority() >=8 )
//						consumerBillDO.setPriorityDesc("低");
//					else
//						consumerBillDO.setPriorityDesc("中");
//					
//					if(consumerBillDO.getValueChange() > 10)
//						consumerBillDO.setValueChangeDesc("升值");
//					else if(consumerBillDO.getValueChange() < -10)
//						consumerBillDO.setValueChangeDesc("贬值");
//					else
//						consumerBillDO.setValueChangeDesc("持平");
//				}
				
				totalNumber = consumerBillService.getTotalCount(theMonth.substring(0,6),phoneNo,userId,6);
				totalPages = totalNumber / pageSize + (totalNumber % pageSize == 0?0:1);
				prevPage = currPage - 1;
				if(prevPage <= 0)
					prevPage = 0;
				nextPage = currPage + 1;
				if(nextPage > totalPages)
					nextPage = 0;
			} catch(Exception e) {
				setBills(null);
			}
		}
		return SUCCESS;
	}
	
	public String getSingleHighGprsConsumerBills() {
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated() && pageSize > 0 && currPage > 0){
			UserDO userDO = (UserDO)subject.getSession().getAttribute("userDO");
			userId = userDO.getId();
			if(userId == 1) //admin
				userId = 0;
			BiDataDO biDataDO = biDataService.getRecentBiDataDO();
			if(StringUtils.isBlank(theMonth))
				theMonth = biDataDO.getTheMonth();

			if(!checkTheMonth())
				return ERROR;
			
			try {
				if(StringUtils.isBlank(ordertype))
					ordertype = "regular_score";
					
				setBills(consumerBillService.getAllSingleHighGprsBill(theMonth.substring(0,6), phoneNo, userId, currPage, pageSize,null));
				
				totalNumber = consumerBillService.getSingleHighGprsTotalCount(theMonth.substring(0,6),phoneNo,userId);
				totalPages = totalNumber / pageSize + (totalNumber % pageSize == 0?0:1);
				prevPage = currPage - 1;
				if(prevPage <= 0)
					prevPage = 0;
				nextPage = currPage + 1;
				if(nextPage > totalPages)
					nextPage = 0;
			} catch(Exception e) {
				setBills(null);
			}
		}
		return SUCCESS;
	}
	
	public String getTheMonthString() {
		return theMonthString;
	}

	public void setTheMonthString(String theMonthString) {
		this.theMonthString = theMonthString;
	}

	public String getCalenderString() {
		return calenderString;
	}

	public void setCalenderString(String calenderString) {
		this.calenderString = calenderString;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	private boolean checkTheMonth(){
		if(MyDateUtils.checkMonthFormat(theMonth, "yyyyMMdd")){
			theMonthString = theMonth.substring(0,4) + "年" + theMonth.substring(4,6) + "月";		
			calenderString = theMonth.substring(0,4) + "-" + theMonth.substring(4,6) + "-" + theMonth.substring(6,8);
		} else if(MyDateUtils.checkMonthFormat(theMonth, "yyyyMM")){
			theMonthString = theMonth.substring(0,4) + "年" + theMonth.substring(4,6) + "月";		
			calenderString = theMonth.substring(0,4) + "-" + theMonth.substring(4,6) + "-01";
		} else {
			return false;
		}
		return true;
	}
	
	public String getConsumerBillsJson() {
		if(!checkTheMonth()) {
			setConsumerBillDO(new ConsumerBillDO());
		}
		else {
			try {
				setConsumerBillDO(consumerBillService.getMonthBill(theMonth.substring(0,6), phoneNo, userId,ordertype));
			}catch (Exception e) {
				setConsumerBillDO(new ConsumerBillDO());
			}
		}
		return SUCCESS;
	}
	
	public String getRecentConsumerBillsJson() {
		int RECENT_MONTHS = 6;
		
		recentBills.clear();
		if(StringUtils.isBlank(phoneNo) || StringUtils.isBlank(theMonth))
			return SUCCESS;
		
		List<String> theMonthList = new LinkedList<String>();
		List<BiDataDO> biDataList = biDataService.findAll();//按照theMonth倒序
		int cursor = 0;
		while(Integer.parseInt(biDataList.get(cursor).getTheMonth()) > Integer.parseInt(theMonth))
			cursor ++;
		
		int currentCollected = 0;
		while(currentCollected < RECENT_MONTHS && cursor <= biDataList.size() - 1) {
			theMonthList.add(biDataList.get(cursor).getTheMonth());
			currentCollected ++;
			cursor ++;
		}
		
		//读数据库加载一次
		Map<String,String> idPkgMap = new HashMap<String, String>();
		List<TelecomPackageDO> packages = telPakcageDao.getAll();
		for(TelecomPackageDO pkg: packages) {
			idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
		}
		
		for(String month: theMonthList) {
			try{
				ConsumerBillDO consumerDo = consumerBillService.getMonthBill(month, phoneNo, -1,ordertype);
				if(consumerDo != null){
					String[] rcmdPackageIds = consumerDo.getRecommend().split(",");
					int rcmdNumber = rcmdPackageIds.length;
					if(rcmdNumber >= 1){
					    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
					    if(rcmdPackagePair.length == 2) {
    						String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
    						if(StringUtils.isNotBlank(rcmd1)) {
    							consumerDo.setRecommend1(rcmd1);
    							consumerDo.setRecommendCost1(Double.parseDouble(rcmdPackagePair[1]));
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
    						}
                        }
					}
					
					if(consumerDo.getPriority() <= 3)
						consumerDo.setPriorityDesc("高");
					else if(consumerDo.getPriority() >=8 )
						consumerDo.setPriorityDesc("低");
					else
						consumerDo.setPriorityDesc("中");
					
					recentBills.add(consumerDo);				
				}
			}catch(Exception e) {
				continue;
			}
		}
		
		return SUCCESS;
	}

	public List<ConsumerBillDO> getRecentBills() {
		return recentBills;
	}

	public void setRecentBills(List<ConsumerBillDO> recentBills) {
		this.recentBills = recentBills;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(String theMonth) {
		this.theMonth = theMonth;
	}
	

	public ConsumerBillDO getConsumerBillDO() {
		return consumerBillDO;
	}

	public void setConsumerBillDO(ConsumerBillDO consumerBillDO) {
		this.consumerBillDO = consumerBillDO;
	}
	
	public List<ConsumerBillDO> getBills() {
		return bills;
	}

	public void setBills(List<ConsumerBillDO> bills) {
		this.bills = bills;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

}
