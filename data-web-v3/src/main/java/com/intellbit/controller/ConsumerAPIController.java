package com.intellbit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.intellbit.dao.TelecomPackageDao;
import com.intellbit.dataobject.BiDataDO;
import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.RespMsg;
import com.intellbit.dataobject.TelecomPackageDO;
import com.intellbit.dataobject.ajax.ContractConsumerInfo.ExtraInfo;
import com.intellbit.dataobject.ajax.DataGridRequest;
import com.intellbit.dataobject.ajax.DataGridResponse;
import com.intellbit.dataobject.ajax.RequestQueryFilter;
import com.intellbit.service.BiDataService;
import com.intellbit.service.BiMapService;
import com.intellbit.service.ConsumerDataService;
import com.intellbit.service.UserService;
import com.intellbit.utils.Const;
import com.intellbit.utils.MyDateUtils;
import com.intellbit.utils.SerializationUtil;

@Controller
@RequestMapping("")
public class ConsumerAPIController {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsumerAPIController.class);

	@Autowired
	private ConsumerDataService consumerDataService;
	
	@Autowired
	private BiMapService biMapService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TelecomPackageDao telPakcageDao;
	
	@Autowired
	private BiDataService biDataService;
	
	@RequestMapping(value = "/api/consumers/maintain", method = RequestMethod.POST)
	public @ResponseBody RespMsg<DataGridResponse<List<Object>>> getMaintainConsumerList(
			@RequestParam("aoData") String aodata,
			@RequestParam("bizdate") String bizdate,
			@RequestParam("orderby") String orderby,
			@RequestParam("hideDone") boolean hideDone,
			@RequestParam("condition") String condition,
			HttpServletRequest request,
			HttpServletResponse response) {
		RespMsg<DataGridResponse<List<Object>>> resp = new RespMsg<DataGridResponse<List<Object>>>();
		DataGridResponse<List<Object>> dgResp = new DataGridResponse<List<Object>>();
		resp.setData(dgResp);
		try {
			//parse request parameters 
			DataGridRequest aoData = SerializationUtil.gson2Object(aodata,
					new TypeToken<DataGridRequest>(){}.getType());
			bizdate = MyDateUtils.transferDateFormat(bizdate, "yyyy-MM-dd", "yyyyMMdd");
			String bizmonth = bizdate.substring(0, 6);
			
			RequestQueryFilter reqQueryCondition = SerializationUtil.gson2Object(condition, 
					new TypeToken<RequestQueryFilter>(){}.getType());
			reqQueryCondition.setHideDone(hideDone);
			
			if (aoData.getiDisplayLength() > 0 && aoData.getiDisplayStart() >= 0) {
				//find current login user
				int userId = Integer.valueOf(request.getSession().getAttribute(Const.SS_USER_ID).toString());
//				UserDO userDO = userService.getUser(userId);
				
				//find data-available months, order by date desc
				List<BiDataDO> biDataDOList = biDataService.findAll();
				if (biDataDOList == null || biDataDOList.size() == 0) {
					resp.setErrCode(Const.ERROR_CODE);
					resp.setErrMsg(Const.MSG_DATA_NOT_FOUND);
					return resp;
				}
				
				//theMonth is thre real month that data we userd
				String theMonth = biDataDOList.get(0).getTheMonth();
				for (BiDataDO biDataDO: biDataDOList) {
					//use the latest month's data that before current month
					if (biDataDO.getTheMonth().compareTo(bizmonth) <= 0) {
						theMonth = biDataDO.getTheMonth();
						break;
					}
				}
				
				dgResp.setDraw(String.valueOf(aoData.getsEcho()));
				
				int iConsumerCnt = consumerDataService.getMaintainConsumersCount(userId, bizmonth,theMonth,reqQueryCondition);
				dgResp.setRecordsTotal(String.valueOf(iConsumerCnt));
				dgResp.setRecordsFiltered(String.valueOf(iConsumerCnt));
				
				//order by
				List<String> orderFields = new ArrayList<String>();
				String[] arrOrderFields = orderby.split(",");
				for(String field: arrOrderFields)
					orderFields.add(field.trim());
				
				//get consumer data list
				List<ConsumerBillDO> detailDataList = consumerDataService.getMaintainConsumers(userId, bizmonth,theMonth, 
						aoData.getiDisplayStart(), aoData.getiDisplayLength(),orderFields,reqQueryCondition);
				
				//load package info(cached in map)
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
		        
				//filter fields we needed
		        //todo: 以下代码待改进
				List<List<Object>> rowList = new ArrayList<List<Object>>();
				for (ConsumerBillDO o: detailDataList) {
					List<Object> row = new ArrayList<Object>();
					row.add(o.getPhoneNo());
					
					if (o.getPriority() <= 3)
						o.setPriorityDesc("高");
					else if (o.getPriority() >= 8)
						o.setPriorityDesc("低");
					else
						o.setPriorityDesc("中");
					
					o.setRecommend1("暂无更合适套餐，可保持原套餐.");
					o.setRecommendCost1(0);
					
					String[] rcmdPackageIds = o.getRecommend().split(",");
	                int rcmdNumber = rcmdPackageIds.length;
	                if(rcmdNumber >= 1){
	                    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
	                    if(rcmdPackagePair.length == 2) {   
	                        String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
	                        if(StringUtils.isNotBlank(rcmd1)) {
	                            double costSave = Math.round(o.getIncome6()) - Math.round(Double.parseDouble(rcmdPackagePair[1]));
	                            if(costSave > 0) {
	                                o.setRecommend1(rcmd1);
	                                o.setRecommendCost1(costSave);
	                            }
	                        }
	                    }
	                }
	                
	                String rmd4g = "";
	                double save4g = 0.0;
	                
	                String[] rmd4GPacageIds = o.getRecommend4G().split(",");
	                if(rmd4GPacageIds.length >= 1) {
	                	String[] rcmd4GPackagePair = rmd4GPacageIds[0].split(":");
	                	if(rcmd4GPackagePair.length == 2) {
	                		String rcmd14G = idPkgMap4G.get(rcmd4GPackagePair[0]);
	                		if(StringUtils.isNotBlank(rcmd14G)) {
	                			double costSave = Math.round(o.getIncome6()) - Math.round(Double.parseDouble(rcmd4GPackagePair[1]));
	                			rmd4g = rcmd14G;
	                			save4g = costSave;
	                		}
	                	}
	                }
					
					row.add(o.getPriorityDesc());
					row.add(o.getContractFrom());
					row.add(o.getContractTo());
					row.add(o.getPackageName());
					
					if(o.getDone() == 0)
						row.add("<span class=\"label label-danger\">未处理</span>");
	                else
	                	row.add("<span class=\"label label-success\">已处理</span>");
					
					row.add(new ExtraInfo()
						.setMonth(bizmonth)
						.setPhone(o.getPhoneNo())
						.setRmd(o.getRecommend1())
						.setSave(String.valueOf(o.getRecommendCost1()))
						.setRmd4g(rmd4g)
						.setSave4g(String.valueOf(save4g)));
					rowList.add(row);
				}
				
				dgResp.setData(rowList);
			}
		} catch (Exception e) {
			resp.setErrCode(Const.ERROR_CODE);
			resp.setErrMsg(e.getMessage());
			logger.warn(e.getMessage(), e);
		}
		return resp;
	}
	
	
	@RequestMapping(value = "/api/consumers/contract", method = RequestMethod.POST)
	public @ResponseBody RespMsg<DataGridResponse<List<Object>>> getContractConsumerList(
			@RequestParam("aoData") String aodata,
			@RequestParam("bizdate") String bizdate,
			@RequestParam("orderby") String orderby,
			@RequestParam("hideDone") boolean hideDone,
			@RequestParam("condition") String condition,
			HttpServletRequest request,
			HttpServletResponse response) {
		RespMsg<DataGridResponse<List<Object>>> resp = new RespMsg<DataGridResponse<List<Object>>>();
		DataGridResponse<List<Object>> dgResp = new DataGridResponse<List<Object>>();
		resp.setData(dgResp);
		try {
			//parse request parameters 
			DataGridRequest aoData = SerializationUtil.gson2Object(aodata,
					new TypeToken<DataGridRequest>(){}.getType());
			bizdate = MyDateUtils.transferDateFormat(bizdate, "yyyy-MM-dd", "yyyyMMdd");
			String bizmonth = bizdate.substring(0, 6);
			
			RequestQueryFilter reqQueryCondition = SerializationUtil.gson2Object(condition, 
					new TypeToken<RequestQueryFilter>(){}.getType());
			reqQueryCondition.setHideDone(hideDone);
			
			if (aoData.getiDisplayLength() > 0 && aoData.getiDisplayStart() >= 0) {
				//find current login user
				int userId = Integer.valueOf(request.getSession().getAttribute(Const.SS_USER_ID).toString());
//				UserDO userDO = userService.getUser(userId);
				
				//find data-available months, order by date desc
				List<BiDataDO> biDataDOList = biDataService.findAll();
				if (biDataDOList == null || biDataDOList.size() == 0) {
					resp.setErrCode(Const.ERROR_CODE);
					resp.setErrMsg(Const.MSG_DATA_NOT_FOUND);
					return resp;
				}
				
				//theMonth is thre real month that data we userd
				String theMonth = biDataDOList.get(0).getTheMonth();
				for (BiDataDO biDataDO: biDataDOList) {
					//use the latest month's data that before current month
					if (biDataDO.getTheMonth().compareTo(bizmonth) <= 0) {
						theMonth = biDataDO.getTheMonth();
						break;
					}
				}
				
				dgResp.setDraw(String.valueOf(aoData.getsEcho()));
				
				int iConsumerCnt = consumerDataService.getContractConsumersCount(userId,
						bizmonth, theMonth,
						reqQueryCondition);
				dgResp.setRecordsTotal(String.valueOf(iConsumerCnt));
				dgResp.setRecordsFiltered(String.valueOf(iConsumerCnt));
				
				//order by
				List<String> orderFields = new ArrayList<String>();
				String[] arrOrderFields = orderby.split(",");
				for(String field: arrOrderFields)
					orderFields.add(field.trim());
				
				//get consumer data list
				List<ConsumerBillDO> detailDataList = consumerDataService.getContractConsumers(userId,
						bizmonth, theMonth, 
						aoData.getiDisplayStart(), aoData.getiDisplayLength(),orderFields,reqQueryCondition);
				
				//load package info(cached in map)
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
		        
				//filter fields we needed
		        //todo: 以下代码待改进
				List<List<Object>> rowList = new ArrayList<List<Object>>();
				for (ConsumerBillDO o: detailDataList) {
					List<Object> row = new ArrayList<Object>();
					row.add(o.getPhoneNo());
					
					if (o.getPriority() <= 3)
						o.setPriorityDesc("高");
					else if (o.getPriority() >= 8)
						o.setPriorityDesc("低");
					else
						o.setPriorityDesc("中");
					
					o.setRecommend1("暂无更合适套餐，可保持原套餐.");
					o.setRecommendCost1(0);
					
					String[] rcmdPackageIds = o.getRecommend().split(",");
	                int rcmdNumber = rcmdPackageIds.length;
	                if(rcmdNumber >= 1){
	                    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
	                    if(rcmdPackagePair.length == 2) {   
	                        String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
	                        if(StringUtils.isNotBlank(rcmd1)) {
	                            double costSave = Math.round(o.getIncome6()) - Math.round(Double.parseDouble(rcmdPackagePair[1]));
	                            if(costSave > 0) {
	                                o.setRecommend1(rcmd1);
	                                o.setRecommendCost1(costSave);
	                            }
	                        }
	                    }
	                }
	                
	                String rmd4g = "";
	                double save4g = 0.0;
	                
	                String[] rmd4GPacageIds = o.getRecommend4G().split(",");
	                if(rmd4GPacageIds.length >= 1) {
	                	String[] rcmd4GPackagePair = rmd4GPacageIds[0].split(":");
	                	if(rcmd4GPackagePair.length == 2) {
	                		String rcmd14G = idPkgMap4G.get(rcmd4GPackagePair[0]);
	                		if(StringUtils.isNotBlank(rcmd14G)) {
	                			double costSave = Math.round(o.getIncome6()) - Math.round(Double.parseDouble(rcmd4GPackagePair[1]));
	                			rmd4g = rcmd14G;
	                			save4g = costSave;
	                		}
	                	}
	                }
					
					row.add(o.getPriorityDesc());
					row.add(o.getContractFrom());
					row.add(o.getContractTo());
					row.add(o.getPackageName());
					
					if(o.getDone() == 0)
						row.add("<span class=\"label label-danger\">未处理</span>");
	                else 
	                	row.add("<span class=\"label label-success\">已处理</span>");
					
					row.add(new ExtraInfo()
						.setMonth(bizmonth)
						.setPhone(o.getPhoneNo())
						.setRmd(o.getRecommend1())
						.setSave(String.valueOf(o.getRecommendCost1()))
						.setRmd4g(rmd4g)
						.setSave4g(String.valueOf(save4g)));
					rowList.add(row);
				}
				
				dgResp.setData(rowList);
			}
		} catch (Exception e) {
			resp.setErrCode(Const.ERROR_CODE);
			resp.setErrMsg(e.getMessage());
			logger.warn(e.getMessage(), e);
		}
		return resp;
	}
	
	
	@RequestMapping(value = "/api/consumers/bandwidth", method = RequestMethod.POST)
	public @ResponseBody RespMsg<DataGridResponse<List<Object>>> getBandwidthConsumerList(
			@RequestParam("aoData") String aodata,
			@RequestParam("bizdate") String bizdate,
			@RequestParam("orderby") String orderby,
			@RequestParam("hideDone") boolean hideDone,
			@RequestParam("condition") String condition,
			HttpServletRequest request,
			HttpServletResponse response) {
		RespMsg<DataGridResponse<List<Object>>> resp = new RespMsg<DataGridResponse<List<Object>>>();
		DataGridResponse<List<Object>> dgResp = new DataGridResponse<List<Object>>();
		resp.setData(dgResp);
		try {
			//parse request parameters 
			DataGridRequest aoData = SerializationUtil.gson2Object(aodata,
					new TypeToken<DataGridRequest>(){}.getType());
			bizdate = MyDateUtils.transferDateFormat(bizdate, "yyyy-MM-dd", "yyyyMMdd");
			String bizmonth = bizdate.substring(0, 6);
			
			RequestQueryFilter reqQueryCondition = SerializationUtil.gson2Object(condition, 
					new TypeToken<RequestQueryFilter>(){}.getType());
			reqQueryCondition.setHideDone(hideDone);
			
			if (aoData.getiDisplayLength() > 0 && aoData.getiDisplayStart() >= 0) {
				//find current login user
				int userId = Integer.valueOf(request.getSession().getAttribute(Const.SS_USER_ID).toString());
//				UserDO userDO = userService.getUser(userId);
				
				//find data-available months, order by date desc
				List<BiDataDO> biDataDOList = biDataService.findAll();
				if (biDataDOList == null || biDataDOList.size() == 0) {
					resp.setErrCode(Const.ERROR_CODE);
					resp.setErrMsg(Const.MSG_DATA_NOT_FOUND);
					return resp;
				}
				
				//theMonth is thre real month that data we userd
				String theMonth = biDataDOList.get(0).getTheMonth();
				for (BiDataDO biDataDO: biDataDOList) {
					//use the latest month's data that before current month
					if (biDataDO.getTheMonth().compareTo(bizmonth) <= 0) {
						theMonth = biDataDO.getTheMonth();
						break;
					}
				}
				
				dgResp.setDraw(String.valueOf(aoData.getsEcho()));
				
				int iConsumerCnt = consumerDataService.getBandwidthConsumersCount(userId,bizmonth, theMonth, reqQueryCondition);
				
				dgResp.setRecordsTotal(String.valueOf(iConsumerCnt));
				dgResp.setRecordsFiltered(String.valueOf(iConsumerCnt));
				
				//order by
				List<String> orderFields = new ArrayList<String>();
				String[] arrOrderFields = orderby.split(",");
				for(String field: arrOrderFields)
					orderFields.add(field.trim());
				
				//get consumer data list
				List<ConsumerBillDO> detailDataList = consumerDataService.getBandwidthConsumers(userId,bizmonth, theMonth, 
						aoData.getiDisplayStart(), aoData.getiDisplayLength(),orderFields,reqQueryCondition);
				
				//load package info(cached in map)
		        Map<String,String> idPkgMap = new HashMap<String, String>();
		        List<TelecomPackageDO> packages = telPakcageDao.getAll4G();
		        for(TelecomPackageDO pkg : packages) {
		        	idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
		        }
		        
				//filter fields we needed
		        //todo: 以下代码待改进
				List<List<Object>> rowList = new ArrayList<List<Object>>();
				for (ConsumerBillDO o: detailDataList) {
					List<Object> row = new ArrayList<Object>();
					row.add(o.getPhoneNo());
					
					if (o.getValueChange4G() > 10)
						o.setValueChangeDesc("节省");
					else if (o.getValueChange4G() < -10)
						o.setValueChangeDesc("增加");
					else
						o.setValueChangeDesc("持平");
					
					o.setRecommend1("暂无更合适套餐，可保持原套餐.");
					o.setRecommendCost1(0);
					
					String[] rcmdPackageIds = o.getRecommend4G().split(",");
	                int rcmdNumber = rcmdPackageIds.length;
	                if(rcmdNumber >= 1){
	                    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
	                    if(rcmdPackagePair.length == 2) {   
	                        String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
	                        if(StringUtils.isNotBlank(rcmd1)) {
	                            double costSave = Math.round(o.getIncome6()) - Math.round(Double.parseDouble(rcmdPackagePair[1]));
//	                            if(costSave > 0) {
	                                o.setRecommend1(rcmd1);
	                                o.setRecommendCost1(costSave);
//	                            }
	                        }
	                    }
	                }
	                row.add(o.getValueChangeDesc());
	                row.add(o.getIncome());
	                row.add(o.getIncome6());
	                row.add(o.getGprs());
	                row.add(o.getGprs6());
	                row.add(o.getPackageName());
//	                row.add(o.getIsContractConsumer());
					
					if(o.getDone() == 0)
						row.add("<span class=\"label label-danger\">未处理</span>");
	                else
	                	row.add("<span class=\"label label-success\">已处理</span>");
					
					row.add(new ExtraInfo()
						.setMonth(bizmonth)
						.setPhone(o.getPhoneNo())
						.setRmd("（4G）" + o.getRecommend1())
						.setSave(String.valueOf(o.getRecommendCost1())));
					rowList.add(row);
				}
				
				dgResp.setData(rowList);
			}
		} catch (Exception e) {
			resp.setErrCode(Const.ERROR_CODE);
			resp.setErrMsg(e.getMessage());
			logger.warn(e.getMessage(), e);
		}
		return resp;
	}
	
	
}
