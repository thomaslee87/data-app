package com.intellbi.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.TelecomPackageDao;
import com.intellbi.dataobject.BiDataDO;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.dataobject.Pagination;
import com.intellbi.dataobject.TelecomPackageDO;
import com.intellbi.dataobject.UserDO;
import com.intellbi.service.BiDataService;
import com.intellbi.service.ConsumerBillService;
import com.intellbi.utils.Constants;
import com.intellbi.utils.MyDateUtils;
import com.intellbi.utils.MyDateUtils.Period;
import com.intellbi.utils.Tools;
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
	
	private int userId = -1;
	private String phoneNo;
	private String ordertype;
	
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
	
	private boolean checkTheMonth(String theMonth){
		if(MyDateUtils.checkMonthFormat(theMonth, "yyyyMMdd")){
		} else if(MyDateUtils.checkMonthFormat(theMonth, "yyyyMM")){
		} else {
			return false;
		}
		return true;
	}
	
	/** 
	 * 获取当前登陆vip经理名下的所有合约用户，action传入参数包括：
	 * 工作周期(day,week,month)、选择日期(yyyy-mm-dd)、datatables控件带的参数(起始记录，每页记录数)
	 */
	public String getContractConsumerList(){
		String errMsg = "";
		JSONObject json = JSONObject.fromObject(aoData);
		if(json == null) {
			errMsg = "抱歉！暂时无法获取您需要的数据，请确认您已经登陆系统。";
			dataMap.put("errMsg", errMsg);
			return SUCCESS;
		}
		
		logger.info(aoData);

		dataMap = new HashMap<String, Object>();
		
		try {
			String sEcho = json.getString("sEcho");
			String iColumns = json.getString("iColumns");
			int iDisplayStart = Tools.parseInt(json.getString("iDisplayStart"));
			int iDisplayLength = Tools.parseInt(json.getString("iDisplayLength"));
			
			String bizdate = json.getString("bizdate");
			String taskview = json.getString("view");
			
			String orderby = json.getString("orderby");
			
			if(StringUtils.isBlank(bizdate)) {
				errMsg = "请先选择一个日期，用于获取合约即将到期的客户。";
				dataMap.put("errMsg", errMsg);
				return SUCCESS;
			}
			if(StringUtils.isBlank(taskview)) {
				errMsg = "请先选择任务类型：日/月/周任务";
				dataMap.put("errMsg", errMsg);
				return SUCCESS;
			}
			bizdate = MyDateUtils.transferDateFormat(bizdate, "yyyy-mm-dd", "yyyymmdd");
			if(StringUtils.isBlank(bizdate)) {
				errMsg = "系统捕捉到非法请求，请确认选择的日期为YYYY-MM-DD的格式。";
				dataMap.put("errMsg", errMsg);
				return SUCCESS;
			}
			
			Subject subject = SecurityUtils.getSubject();
	        if(subject.isAuthenticated() && iDisplayLength > 0 && iDisplayStart >= 0){
	            UserDO userDO = (UserDO)subject.getSession().getAttribute("userDO");
	            userId = userDO.getId();
	            if(userId == 1) //admin
	                userId = 0;
	            List<BiDataDO> biDataDOList = biDataService.findAll();
	            if(biDataDOList == null || biDataDOList.size() == 0) {
	            	errMsg = "系统尚未收集到可用数据。";
	    			dataMap.put("errMsg", errMsg);
	            	return SUCCESS;
	            }
	//          BiDataDO biDataDO = biDataService.getRecentBiDataDO();//最新的数据月份，从该月分表中加载数据
	            Period taskTimeWindow = getTaskTimeWindow(bizdate, taskview);
	            
	            String theBizMonth = MyDateUtils.getTheMonthOfBizDate(taskTimeWindow.getEnd(), "yyyymmdd");
	            String theMonth = biDataDOList.get(0).getTheMonth();
	            //使用小于等于选中月份的最新数据
	            for(BiDataDO biDataDO: biDataDOList) {
	            	if(biDataDO.getTheMonth().compareTo(theBizMonth) <= 0) {
	            		theMonth = biDataDO.getTheMonth();
	            		break;
	            	}
	            }
	            
	            Period cQueryWindow = getQueryWindow(taskTimeWindow, taskview);
	            
	//            int recordsTotal = consumerBillService.getTotalCount(theMonth, phoneNo,userId,-1);
	            
	            dataMap.put("draw", sEcho);
	            
	            
	            if(taskview.equals(Constants.TASK_VIEW_MONTH)){
	            	int cConsumerCnt = consumerBillService.getContractCnt(userId, theMonth, cQueryWindow, -1);
		            dataMap.put("recordsTotal", String.valueOf(cConsumerCnt));
		            dataMap.put("recordsFiltered", String.valueOf(cConsumerCnt));
	            	dataMap.put("data", getDataList(userId,-1, bizdate,theMonth,cQueryWindow,orderby,iDisplayStart,iDisplayLength));
	            }
	            //打个补丁，日/周视图推荐
	            else if(taskview.equals(Constants.TASK_VIEW_DAY)) {
	            	int cConsumerCnt = consumerBillService.getContractCnt(userId, theMonth, cQueryWindow, 0);
	            	
	            	int cnt = cConsumerCnt > 5 ? 5 : cConsumerCnt;
	            	
	            	dataMap.put("recordsTotal", String.valueOf(cnt));
		            dataMap.put("recordsFiltered", String.valueOf(cnt));
		            
		            dataMap.put("data", getDataList(userId, 0, bizdate,theMonth,cQueryWindow,orderby,0,5));
		            
	            }
	            else if(taskview.equals(Constants.TASK_VIEW_WEEK)) {
	            	int cConsumerCnt = consumerBillService.getContractCnt(userId, theMonth, cQueryWindow, 0);
	            	
	            	int cnt = cConsumerCnt > 20 ? 20 : cConsumerCnt;
	            	
	            	dataMap.put("recordsTotal", String.valueOf(cnt));
		            dataMap.put("recordsFiltered", String.valueOf(cnt));
		            
		            dataMap.put("data", getDataList(userId, 0, bizdate,theMonth,cQueryWindow,orderby,iDisplayStart,iDisplayLength));
	            }
	            	
	            return SUCCESS;
	        }
		} catch (Exception e) {
			errMsg = "系统捕捉到非法请求，请确认您已经登陆系统，或者重新登录后尝试。";
			dataMap.put("errMsg", errMsg);
			return SUCCESS;
		}
        return ERROR;
	}
	
	/**
	 * @return 返回查询时间窗（6月内）
	 * 由于合约期是按月生效，按天或者按周一般都没有数据（除非180天后的窗口正好包含了月末那天）
	 * 想保留按日和按周分配任务的思路，因此做一个额外处理，
	 * 天任务是从月任务中按续约和保有顺序各推荐5个用户，周各推荐20个
	 */
	private Period getQueryWindow(Period taskTimeWindow, String view) {
		//contract的query window，task的window平移180天
		Period cQueryWindow = taskTimeWindow.moveByDays(Constants.CONTRACT_DAYS);
		cQueryWindow.setBegin(taskTimeWindow.getBegin());
		return cQueryWindow;
	}
	
	/**
	 * @param theDate
	 * @param view
	 * 根据任务视图，获取任务时间窗
	 */
	private Period getTaskTimeWindow(String bizDate, String view) {
		if(StringUtils.isNotBlank(view)) { 
			if(view.equals(Constants.TASK_VIEW_DAY)){
				return new Period(bizDate, bizDate);
			} else if(view.equals(Constants.TASK_VIEW_WEEK)) {
				return MyDateUtils.getWeekOfDate(bizDate);
			} else if(view.equals(Constants.TASK_VIEW_MONTH)) {
				return MyDateUtils.getMonthOfDate(bizDate);
			}
		}
		return null;

/*		if(StringUtils.isBlank(view)) //参数不合法，返回的query会让得到的列表为空
			return new Period("0", "-1");
		if(view.equals(Constants.TASK_VIEW_DAY)) {
			int date = Tools.parseInt(bizDate);
			return new ContractQuery(date, date);
		} else if(view.equals(Constants.TASK_VIEW_WEEK)) {
			Period week = MyDateUtils.getWeekOfDate(bizDate);
			return new ContractQuery(Tools.parseInt(week.getBegin()),Tools.parseInt(week.getEnd()));
		} else if(view.equals(Constants.TASK_VIEW_MONTH)) {
			Period month = MyDateUtils.getMonthOfDate(bizDate);
			return new ContractQuery(Tools.parseInt(month.getBegin()),Tools.parseInt(month.getEnd()));
		} else {
			return new ContractQuery(0, -1);
		}
*/	}
	
	private List<List<Object>> getDataList(int userId, int contractTaskState, String bizdate,String theMonth, Period cQueryWindow, String orderby, int pageStart, int pageSize){
		List<List<Object>> dataList = new ArrayList<List<Object>>();
        try {
        	//读数据库加载一次
	        Map<String,String> idPkgMap = new HashMap<String, String>();
	        List<TelecomPackageDO> packages = telPakcageDao.getAll();
	        for(TelecomPackageDO pkg: packages) {
	            idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
	        }
	        
	        Map<String,String> idPkgMap4G = new HashMap<String, String>();
	        List<TelecomPackageDO> packages4G = telPakcageDao.getAll4G();
	        for(TelecomPackageDO pkg : packages4G) {
	        	idPkgMap4G.put(String.valueOf(pkg.getId()), pkg.getName());
	        }
	        
	        
            if(StringUtils.isBlank(orderby) || (!orderby.equals(Constants.ORDER_BY_REGULAR_SCORE) && !orderby.equals(Constants.ORDER_BY_VALUD_CHANGE)))
                orderby = Constants.ORDER_BY_REGULAR_SCORE;
            
            Pagination pagination = new Pagination(pageStart, pageSize, orderby);
            List<ConsumerBillDO> bills = consumerBillService.getContractConsumers(userId, theMonth, cQueryWindow, pagination, contractTaskState);
            
//            List<ConsumerBillDO> bills = null = consumerBillService.getAllMonthBills(theMonth.substring(0,6), phoneNo, userId, currPage, pageSize,ordertype,-1);
            
            for(ConsumerBillDO consumerBillDO: bills) {
                int contractTo = (int)(consumerBillDO.getContractTo());
//                consumerBillDO.setContractRemain(MyDateUtils.getMonthsBetween(Integer.parseInt(theMonth.substring(0,6)), contractTo));
                    
                if(consumerBillDO.getPriority() <= 3)
                  consumerBillDO.setPriorityDesc("高");
                else if(consumerBillDO.getPriority() >=8 )
                  consumerBillDO.setPriorityDesc("低");
                else
                  consumerBillDO.setPriorityDesc("中");
                
                if(consumerBillDO.getValueChange() > 10)
                  consumerBillDO.setValueChangeDesc("节省");
                else if(consumerBillDO.getValueChange() < -10)
                  consumerBillDO.setValueChangeDesc("增加");
                else
                  consumerBillDO.setValueChangeDesc("持平");
                
                consumerBillDO.setRecommend1("暂无更合适套餐，可保持原套餐.");
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
                
                String rmd4g = "";
                double save4g = 0.0;
                
                String[] rmd4GPacageIds = consumerBillDO.getRecommend4G().split(",");
                if(rmd4GPacageIds.length >= 1) {
                	String[] rcmd4GPackagePair = rmd4GPacageIds[0].split(":");
                	if(rcmd4GPackagePair.length == 2) {
                		String rcmd14G = idPkgMap4G.get(rcmd4GPackagePair[0]);
                		if(StringUtils.isNotBlank(rcmd14G)) {
                			double costSave = Math.round(consumerBillDO.getIncome6()) - Math.round(Double.parseDouble(rcmd4GPackagePair[1]));
                			rmd4g = rcmd14G;
                			save4g = costSave;
                		}
                	}
                }
                
                List<Object> _dtList = new ArrayList<Object>();
                _dtList.add(consumerBillDO.getPhoneNo());
                if(orderby.equals(Constants.ORDER_BY_REGULAR_SCORE))
                	_dtList.add(consumerBillDO.getPriorityDesc());
                else
                	_dtList.add(consumerBillDO.getValueChangeDesc());	
                _dtList.add(consumerBillDO.getContractFrom());
                _dtList.add(consumerBillDO.getContractTo());
//                _dtList.add(consumerBillDO.getContractRemain());
                _dtList.add(consumerBillDO.getPackageName());
                
                if(consumerBillDO.getTaskStateContract() == 0)
                	_dtList.add("<span class=\"label label-danger\">未处理</span>");
//                	_dtList.add("<span id=\"btn" + consumerBillDO.getPhoneNo() + "\" class=\"btn btn-warning btn-xs\" value=\""+consumerBillDO.getPhoneNo()+"\">未处理</span>");
                else
                	_dtList.add("<span class=\"label label-success\">已查看</span>");
//                	_dtList.add("<span id=\"btn" + consumerBillDO.getPhoneNo() + "\" class=\"btn btn-warning btn-xs\" value=\""+consumerBillDO.getPhoneNo()+"\">已处理</span>");
                
                Map<String,String> _map = new HashMap<String,String>();
                _map.put("month", bizdate.substring(0,6));
                _map.put("phone", consumerBillDO.getPhoneNo());
                _map.put("save", consumerBillDO.getRecommendCost1()+"");
                _map.put("rmd", consumerBillDO.getRecommend1());
                _map.put("rmd4g", rmd4g);
                _map.put("save4g",String.valueOf(save4g));
                _dtList.add(_map);
                dataList.add(_dtList);
            }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return dataList;
	}

}
