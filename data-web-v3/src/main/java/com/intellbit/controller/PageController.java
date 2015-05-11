package com.intellbit.controller;

import java.text.DecimalFormat;
import java.text.MessageFormat;
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
import org.springframework.web.servlet.ModelAndView;

import com.intellbit.dao.TelecomPackageDao;
import com.intellbit.dataobject.BiDataDO;
import com.intellbit.dataobject.ConsumerBillDO;
import com.intellbit.dataobject.TelecomPackageDO;
import com.intellbit.dataobject.UserDO;
import com.intellbit.service.BiDataService;
import com.intellbit.service.ConsumerBillService;
import com.intellbit.service.ConsumerDataService;
import com.intellbit.service.UserService;
import com.intellbit.utils.Const;
import com.intellbit.utils.MyDateUtils;

@Controller
@RequestMapping("")
public class PageController {
	
	private static final Logger logger = LoggerFactory.getLogger(PageController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private BiDataService biDataService;
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	@Autowired
	private ConsumerDataService consumerDataService;
	
	@Autowired
	private TelecomPackageDao telPakcageDao;
	
	@RequestMapping(value = "maintain.html", method = RequestMethod.GET)
	public ModelAndView showMaintainViewPage(HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("group", String.valueOf(userDO.getGroupId()));
		return new ModelAndView("maintain", data);
	}
	
	@RequestMapping(value = "contract.html", method = RequestMethod.GET)
	public ModelAndView showContractViewPage(HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("group", String.valueOf(userDO.getGroupId()));
		return new ModelAndView("contract", data);
	}
	
	@RequestMapping(value = "bandwidth.html", method = RequestMethod.GET)
	public ModelAndView showBandwidthViewPage(HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("group", String.valueOf(userDO.getGroupId()));
		return new ModelAndView("bandwidth", data);
	}
	
	@RequestMapping(value = "/contract/billdetail.html", method = RequestMethod.GET)
	public ModelAndView getConsumerBillDetail(
			@RequestParam("theBizMonth") int theBizMonth,
			@RequestParam("phoneNo") String phoneNo,
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("phone", phoneNo);
		data.put("theBizMonth", theBizMonth);
		data.put("phoneNo", phoneNo);
		data.put("type", 1);
//		data.put("task", "带宽升级任务");
		
		boolean dateValid = MyDateUtils.checkMonthFormat(String.valueOf(theBizMonth), "yyyyMM");
		if (!dateValid) {
			String info = MessageFormat.format(Const.INFO_DATE_INVALID, userDO.getRealname(), theBizMonth);
			data.put("info", info);
			logger.warn("NOT ALLOWED:" + userDO.getRealname() + " set invalid bizmonth");
			return new ModelAndView("error", data);
		}
		
		if (userDO.getGroupId() != 0) {
			//检查是否是当前用户名下的客户
			UserDO realUserDO = userService.getUserIdOfContractPhone(phoneNo);
			if (realUserDO == null || realUserDO.getId() == 0) {
				String info = MessageFormat.format(Const.INFO_CONSUMER_NOT_ALLOWED, userDO.getRealname(), phoneNo, "续约任务");
				data.put("info", info);
				logger.warn("NOT ALLOWED:" + userDO.getRealname() + " try to see other's consumer ");
				return new ModelAndView("error", data);
			}
		}
		
		//find data-available months, order by date desc
		List<BiDataDO> biDataDOList = biDataService.findAll();
		if (biDataDOList == null || biDataDOList.size() == 0) {
			logger.warn("NOT READY:" + userDO.getRealname() + " try to see data, but not ready.");
			String info = MessageFormat.format(Const.INFO_DATA_NOT_READY, userDO.getRealname(), theBizMonth);
			data.put("info", info);
			return new ModelAndView("error", data);
		}
		
		List<String> theMonthList = new ArrayList<String>(12);
        //使用小于等于选中月份的最新数据
        for(BiDataDO biDataDO: biDataDOList) {
        	String theMonth = biDataDO.getTheMonth();
        	if(theMonth.compareTo(String.valueOf(theBizMonth)) <= 0) {
        		theMonthList.add(theMonth);
        		if(theMonthList.size() >= Const.RECENT_MONTH)
        			break;
        	}
        }
		
		//load package info(cached in map)
        Map<String,String> idPkgMap = new HashMap<String, String>();
        Map<String, TelecomPackageDO> pkgMap = new HashMap<String,TelecomPackageDO>();
        List<TelecomPackageDO> packages = telPakcageDao.getAll();
        for(TelecomPackageDO pkg: packages) {
            idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
            pkgMap.put(pkg.getName(), pkg);
        }
        
        Map<String,String> idPkgMap4G = new HashMap<String, String>();
        Map<String, TelecomPackageDO> pkgMap4G = new HashMap<String,TelecomPackageDO>();
        List<TelecomPackageDO> packages4G = telPakcageDao.getAll4G();
        for(TelecomPackageDO pkg : packages4G) {
        	idPkgMap4G.put(String.valueOf(pkg.getId()), pkg.getName());
        	pkgMap4G.put(pkg.getName(), pkg);
        }
        
        String currPkgName = null;
        String recommend1 = null, recommend2 = null, recommend3 = null;
        String rcmd1pkg = null, rcmd2pkg = null, rcmd3pkg = null;
        double recommendCost1 = 0, recommendCost2 = 0, recommendCost3 = 0;
        double recommendSave1 = 0, recommendSave2 = 0, recommendSave3 = 0;
        
        String recommend1_4G = null, recommend2_4G = null, recommend3_4G = null;
        String rcmd1pkg4G = null, rcmd2pkg4G = null, rcmd3pkg4G = null;
        double recommendCost1_4G = 0, recommendCost2_4G = 0, recommendCost3_4G = 0;
        double recommendSave1_4G = 0, recommendSave2_4G = 0, recommendSave3_4G = 0;
        
        List<ConsumerBillDO> recentBills = consumerDataService.getConsumerMonthBills(phoneNo, theMonthList);
        for(ConsumerBillDO consumerDo: recentBills) {
        	if(consumerDo == null)
        		continue;
        	currPkgName = consumerDo.getPackageName();
        	
        	
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//						consumerDo.setRecommend1(rcmd1);
//						consumerDo.setRecommendCost1(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信（条）:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信（元/条）:"+ pkg.getSmsPrice() +"</li>"+
								"</ul></p>";
					}
			    }
			}
			if(rcmdNumber >= 2){
			    String[] rcmdPackagePair = rcmdPackageIds[1].split(":");
			    if(rcmdPackagePair.length == 2) {
					String rcmd2 = idPkgMap4G.get(rcmdPackagePair[0]);
					if(StringUtils.isNotBlank(rcmd2)) {
//						consumerDo.setRecommend2(rcmd2);
//						consumerDo.setRecommendCost2(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
								"</ul></p>";
					}
			    }
			}
			if(rcmdNumber >= 3){
			    String[] rcmdPackagePair = rcmdPackageIds[2].split(":");
                if(rcmdPackagePair.length == 2) {
					String rcmd3 = idPkgMap4G.get(rcmdPackagePair[0]);
					if(StringUtils.isNotBlank(rcmd3)) {
//						consumerDo.setRecommend3(rcmd3);
//						consumerDo.setRecommendCost3(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
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
			
        }
        
		data.put("currPkgName", currPkgName);
		
		data.put("recentBillList", recentBills);
		
		data.put("recommend1",recommend1);
		data.put("recommend2",recommend2);
		data.put("recommend3",recommend3);
		data.put("rcmd1pkg",rcmd1pkg);
		data.put("rcmd2pkg",rcmd2pkg);
		data.put("rcmd3pkg",rcmd3pkg);
		data.put("recommendCost1",recommendCost1);
		data.put("recommendCost2",recommendCost2);
		data.put("recommendCost3",recommendCost3);
		data.put("recommendSave1",recommendSave1);
		data.put("recommendSave2",recommendSave2);
		data.put("recommendSave3",recommendSave3);
		        
		data.put("recommend1_4G",recommend1_4G);
		data.put("recommend2_4G",recommend2_4G);
		data.put("recommend3_4G",recommend3_4G);
		data.put("rcmd1pkg4G",rcmd1pkg4G);
		data.put("rcmd2pkg4G",rcmd2pkg4G);
		data.put("rcmd3pkg4G",rcmd3pkg4G);
		data.put("recommendCost1_4G",recommendCost1_4G);
		data.put("recommendCost2_4G",recommendCost2_4G);
		data.put("recommendCost3_4G",recommendCost3_4G);
		data.put("recommendSave1_4G",recommendSave1_4G);
		data.put("recommendSave2_4G",recommendSave2_4G);
		data.put("recommendSave3_4G",recommendSave3_4G);
        
		return new ModelAndView("billDetail", data);
	}
	
	
	
	@RequestMapping(value = "/maintain/billdetail.html", method = RequestMethod.GET)
	public ModelAndView getConsumerMaintainBillDetail(
			@RequestParam("theBizMonth") int theBizMonth,
			@RequestParam("phoneNo") String phoneNo,
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("phone", phoneNo);
		data.put("theBizMonth", theBizMonth);
		data.put("phoneNo", phoneNo);
		data.put("type", 0);
//		data.put("task", "带宽升级任务");
		
		boolean dateValid = MyDateUtils.checkMonthFormat(String.valueOf(theBizMonth), "yyyyMM");
		if (!dateValid) {
			String info = MessageFormat.format(Const.INFO_DATE_INVALID, userDO.getRealname(), theBizMonth);
			data.put("info", info);
			logger.warn("NOT ALLOWED:" + userDO.getRealname() + " set invalid bizmonth");
			return new ModelAndView("error", data);
		}
		
		if (userDO.getGroupId() != 0) {
			//检查是否是当前用户名下的客户
			UserDO realUserDO = userService.getUserIdOfContractPhone(phoneNo);
			if (realUserDO == null || realUserDO.getId() == 0) {
				String info = MessageFormat.format(Const.INFO_CONSUMER_NOT_ALLOWED, userDO.getRealname(), phoneNo, "保有任务");
				data.put("info", info);
				logger.warn("NOT ALLOWED:" + userDO.getRealname() + " try to see other's consumer ");
				return new ModelAndView("error", data);
			}
		}
		
		//find data-available months, order by date desc
		List<BiDataDO> biDataDOList = biDataService.findAll();
		if (biDataDOList == null || biDataDOList.size() == 0) {
			logger.warn("NOT READY:" + userDO.getRealname() + " try to see data, but not ready.");
			String info = MessageFormat.format(Const.INFO_DATA_NOT_READY, userDO.getRealname(), theBizMonth);
			data.put("info", info);
			return new ModelAndView("error", data);
		}
		
		List<String> theMonthList = new ArrayList<String>(12);
        //使用小于等于选中月份的最新数据
        for(BiDataDO biDataDO: biDataDOList) {
        	String theMonth = biDataDO.getTheMonth();
        	if(theMonth.compareTo(String.valueOf(theBizMonth)) <= 0) {
        		theMonthList.add(theMonth);
        		if(theMonthList.size() >= Const.RECENT_MONTH)
        			break;
        	}
        }
		
		//load package info(cached in map)
        Map<String,String> idPkgMap = new HashMap<String, String>();
        Map<String, TelecomPackageDO> pkgMap = new HashMap<String,TelecomPackageDO>();
        List<TelecomPackageDO> packages = telPakcageDao.getAll();
        for(TelecomPackageDO pkg: packages) {
            idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
            pkgMap.put(pkg.getName(), pkg);
        }
        
        Map<String,String> idPkgMap4G = new HashMap<String, String>();
        Map<String, TelecomPackageDO> pkgMap4G = new HashMap<String,TelecomPackageDO>();
        List<TelecomPackageDO> packages4G = telPakcageDao.getAll4G();
        for(TelecomPackageDO pkg : packages4G) {
        	idPkgMap4G.put(String.valueOf(pkg.getId()), pkg.getName());
        	pkgMap4G.put(pkg.getName(), pkg);
        }
        
        String currPkgName = null;
        String recommend1 = null, recommend2 = null, recommend3 = null;
        String rcmd1pkg = null, rcmd2pkg = null, rcmd3pkg = null;
        double recommendCost1 = 0, recommendCost2 = 0, recommendCost3 = 0;
        double recommendSave1 = 0, recommendSave2 = 0, recommendSave3 = 0;
        
        String recommend1_4G = null, recommend2_4G = null, recommend3_4G = null;
        String rcmd1pkg4G = null, rcmd2pkg4G = null, rcmd3pkg4G = null;
        double recommendCost1_4G = 0, recommendCost2_4G = 0, recommendCost3_4G = 0;
        double recommendSave1_4G = 0, recommendSave2_4G = 0, recommendSave3_4G = 0;
        
        List<ConsumerBillDO> recentBills = consumerDataService.getConsumerMonthBills(phoneNo, theMonthList);
        for(ConsumerBillDO consumerDo: recentBills) {
        	if(consumerDo == null)
        		continue;
        	currPkgName = consumerDo.getPackageName();
        	
        	
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//						consumerDo.setRecommend1(rcmd1);
//						consumerDo.setRecommendCost1(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信（条）:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信（元/条）:"+ pkg.getSmsPrice() +"</li>"+
								"</ul></p>";
					}
			    }
			}
			if(rcmdNumber >= 2){
			    String[] rcmdPackagePair = rcmdPackageIds[1].split(":");
			    if(rcmdPackagePair.length == 2) {
					String rcmd2 = idPkgMap4G.get(rcmdPackagePair[0]);
					if(StringUtils.isNotBlank(rcmd2)) {
//						consumerDo.setRecommend2(rcmd2);
//						consumerDo.setRecommendCost2(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
								"</ul></p>";
					}
			    }
			}
			if(rcmdNumber >= 3){
			    String[] rcmdPackagePair = rcmdPackageIds[2].split(":");
                if(rcmdPackagePair.length == 2) {
					String rcmd3 = idPkgMap4G.get(rcmdPackagePair[0]);
					if(StringUtils.isNotBlank(rcmd3)) {
//						consumerDo.setRecommend3(rcmd3);
//						consumerDo.setRecommendCost3(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
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
			
        }
        
		data.put("currPkgName", currPkgName);
		
		data.put("recentBillList", recentBills);
		
		data.put("recommend1",recommend1);
		data.put("recommend2",recommend2);
		data.put("recommend3",recommend3);
		data.put("rcmd1pkg",rcmd1pkg);
		data.put("rcmd2pkg",rcmd2pkg);
		data.put("rcmd3pkg",rcmd3pkg);
		data.put("recommendCost1",recommendCost1);
		data.put("recommendCost2",recommendCost2);
		data.put("recommendCost3",recommendCost3);
		data.put("recommendSave1",recommendSave1);
		data.put("recommendSave2",recommendSave2);
		data.put("recommendSave3",recommendSave3);
		        
		data.put("recommend1_4G",recommend1_4G);
		data.put("recommend2_4G",recommend2_4G);
		data.put("recommend3_4G",recommend3_4G);
		data.put("rcmd1pkg4G",rcmd1pkg4G);
		data.put("rcmd2pkg4G",rcmd2pkg4G);
		data.put("rcmd3pkg4G",rcmd3pkg4G);
		data.put("recommendCost1_4G",recommendCost1_4G);
		data.put("recommendCost2_4G",recommendCost2_4G);
		data.put("recommendCost3_4G",recommendCost3_4G);
		data.put("recommendSave1_4G",recommendSave1_4G);
		data.put("recommendSave2_4G",recommendSave2_4G);
		data.put("recommendSave3_4G",recommendSave3_4G);
        
		return new ModelAndView("billDetail", data);
	}
	
	
	@RequestMapping(value = "/bandwidth/billdetail.html", method = RequestMethod.GET)
	public ModelAndView getConsumerBandupBillDetail(
			@RequestParam("theBizMonth") int theBizMonth,
			@RequestParam("phoneNo") String phoneNo,
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		int userId = Integer.valueOf((request.getSession().getAttribute(Const.SS_USER_ID).toString()));
		UserDO userDO = userService.getUser(userId);
		data.put("realname", userDO.getRealname());
		data.put("phone", phoneNo);
		data.put("theBizMonth", theBizMonth);
		data.put("phoneNo", phoneNo);
		data.put("type", 2);
//		data.put("task", "带宽升级任务");
		
		boolean dateValid = MyDateUtils.checkMonthFormat(String.valueOf(theBizMonth), "yyyyMM");
		if (!dateValid) {
			String info = MessageFormat.format(Const.INFO_DATE_INVALID, userDO.getRealname(), theBizMonth);
			data.put("info", info);
			logger.warn("NOT ALLOWED:" + userDO.getRealname() + " set invalid bizmonth");
			return new ModelAndView("error", data);
		}
		
		if (userDO.getGroupId() != 0) {
			//检查是否是当前用户名下的客户
			UserDO realUserDO = userService.getUserIdOfContractPhone(phoneNo);
			if (realUserDO == null || realUserDO.getId() == 0) {
				String info = MessageFormat.format(Const.INFO_CONSUMER_NOT_ALLOWED, userDO.getRealname(), phoneNo, "保有任务");
				data.put("info", info);
				logger.warn("NOT ALLOWED:" + userDO.getRealname() + " try to see other's consumer ");
				return new ModelAndView("error", data);
			}
		}
		
		//find data-available months, order by date desc
		List<BiDataDO> biDataDOList = biDataService.findAll();
		if (biDataDOList == null || biDataDOList.size() == 0) {
			logger.warn("NOT READY:" + userDO.getRealname() + " try to see data, but not ready.");
			String info = MessageFormat.format(Const.INFO_DATA_NOT_READY, userDO.getRealname(), theBizMonth);
			data.put("info", info);
			return new ModelAndView("error", data);
		}
		
		List<String> theMonthList = new ArrayList<String>(12);
        //使用小于等于选中月份的最新数据
        for(BiDataDO biDataDO: biDataDOList) {
        	String theMonth = biDataDO.getTheMonth();
        	if(theMonth.compareTo(String.valueOf(theBizMonth)) <= 0) {
        		theMonthList.add(theMonth);
        		if(theMonthList.size() >= Const.RECENT_MONTH)
        			break;
        	}
        }
		
		//load package info(cached in map)
        Map<String,String> idPkgMap = new HashMap<String, String>();
        Map<String, TelecomPackageDO> pkgMap = new HashMap<String,TelecomPackageDO>();
        List<TelecomPackageDO> packages = telPakcageDao.getAll();
        for(TelecomPackageDO pkg: packages) {
            idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
            pkgMap.put(pkg.getName(), pkg);
        }
        
        Map<String,String> idPkgMap4G = new HashMap<String, String>();
        Map<String, TelecomPackageDO> pkgMap4G = new HashMap<String,TelecomPackageDO>();
        List<TelecomPackageDO> packages4G = telPakcageDao.getAll4G();
        for(TelecomPackageDO pkg : packages4G) {
        	idPkgMap4G.put(String.valueOf(pkg.getId()), pkg.getName());
        	pkgMap4G.put(pkg.getName(), pkg);
        }
        
        String currPkgName = null;
        String recommend1 = null, recommend2 = null, recommend3 = null;
        String rcmd1pkg = null, rcmd2pkg = null, rcmd3pkg = null;
        double recommendCost1 = 0, recommendCost2 = 0, recommendCost3 = 0;
        double recommendSave1 = 0, recommendSave2 = 0, recommendSave3 = 0;
        
        String recommend1_4G = null, recommend2_4G = null, recommend3_4G = null;
        String rcmd1pkg4G = null, rcmd2pkg4G = null, rcmd3pkg4G = null;
        double recommendCost1_4G = 0, recommendCost2_4G = 0, recommendCost3_4G = 0;
        double recommendSave1_4G = 0, recommendSave2_4G = 0, recommendSave3_4G = 0;
        
        List<ConsumerBillDO> recentBills = consumerDataService.getConsumerMonthBills(phoneNo, theMonthList);
        for(ConsumerBillDO consumerDo: recentBills) {
        	if(consumerDo == null)
        		continue;
        	currPkgName = consumerDo.getPackageName();
        	
        	
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
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
//						consumerDo.setRecommend1(rcmd1);
//						consumerDo.setRecommendCost1(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信（条）:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信（元/条）:"+ pkg.getSmsPrice() +"</li>"+
								"</ul></p>";
					}
			    }
			}
			if(rcmdNumber >= 2){
			    String[] rcmdPackagePair = rcmdPackageIds[1].split(":");
			    if(rcmdPackagePair.length == 2) {
					String rcmd2 = idPkgMap4G.get(rcmdPackagePair[0]);
					if(StringUtils.isNotBlank(rcmd2)) {
//						consumerDo.setRecommend2(rcmd2);
//						consumerDo.setRecommendCost2(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
								"</ul></p>";
					}
			    }
			}
			if(rcmdNumber >= 3){
			    String[] rcmdPackagePair = rcmdPackageIds[2].split(":");
                if(rcmdPackagePair.length == 2) {
					String rcmd3 = idPkgMap4G.get(rcmdPackagePair[0]);
					if(StringUtils.isNotBlank(rcmd3)) {
//						consumerDo.setRecommend3(rcmd3);
//						consumerDo.setRecommendCost3(Double.parseDouble(rcmdPackagePair[1]));
						
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
//								"<li>短信:"+ pkg.getSms() +"</li>" +
//								"<li>彩信:"+ pkg.getMms()+"</li>"+
								"</ul></p>" +
						"<p>超套餐计费:<ul>" +
								"<li>国内语音（元/分钟）:" + pkg.getLocalVoicePrice() +"</li>"+
								"<li>国内流量（元/K）:"+ decimalFormat.format(pkg.getGprsPrice()) +"</li>"+
//								"<li>短信:"+ pkg.getSmsPrice() +"</li>"+
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
			
        }
        
		data.put("currPkgName", currPkgName);
		
		data.put("recentBillList", recentBills);
		
		data.put("recommend1",recommend1);
		data.put("recommend2",recommend2);
		data.put("recommend3",recommend3);
		data.put("rcmd1pkg",rcmd1pkg);
		data.put("rcmd2pkg",rcmd2pkg);
		data.put("rcmd3pkg",rcmd3pkg);
		data.put("recommendCost1",recommendCost1);
		data.put("recommendCost2",recommendCost2);
		data.put("recommendCost3",recommendCost3);
		data.put("recommendSave1",recommendSave1);
		data.put("recommendSave2",recommendSave2);
		data.put("recommendSave3",recommendSave3);
		        
		data.put("recommend1_4G",recommend1_4G);
		data.put("recommend2_4G",recommend2_4G);
		data.put("recommend3_4G",recommend3_4G);
		data.put("rcmd1pkg4G",rcmd1pkg4G);
		data.put("rcmd2pkg4G",rcmd2pkg4G);
		data.put("rcmd3pkg4G",rcmd3pkg4G);
		data.put("recommendCost1_4G",recommendCost1_4G);
		data.put("recommendCost2_4G",recommendCost2_4G);
		data.put("recommendCost3_4G",recommendCost3_4G);
		data.put("recommendSave1_4G",recommendSave1_4G);
		data.put("recommendSave2_4G",recommendSave2_4G);
		data.put("recommendSave3_4G",recommendSave3_4G);
        
		return new ModelAndView("bandwidthDetail", data);
	}
}
