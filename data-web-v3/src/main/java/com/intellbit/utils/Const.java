package com.intellbit.utils;

public class Const {

	public static final int NOTLONGIN_CODE = 2;
	public static final int ERROR_CODE = 1;
	public static final int SUCCESS_CODE = 0;
	
	public static final int ADMIN_GROUP_ID = 0;
	
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	public static final String NOTALLOWED = "not allowed";
	
	public static final String SS_USER_ID = "user_id";
	public static final String SS_USER_NAME = "username";
	public static final String SS_PASSWORD = "password";
	
	public static final String MSG_LOGIN_FAILED = "登录失败，请输入正确的用户名和密码";
	public static final String MSG_DATA_NOT_FOUND = "系统尚未收集到可用数据";
	public static final String MSG_LOGOUT_FAILED = "登出失败，请确认已登录，并刷新页面重试";
	
	
	public static final String INFO_CONSUMER_NOT_ALLOWED = 
			"{0} 您好：请确认 {1} 是您的 {2} 客户";
	public static final String INFO_DATA_NOT_READY = "{0} 您好：{1} 的数据尚未就绪，暂时无法查看";
	public static final String INFO_DATE_INVALID = "{0} 您好： {1} 不是合法的月份日期";
	public static final String INFO_NOT_ALLOWED = "抱歉，您没有权限";
	
	public static final int CONTRACT_DAYS = 190; //关注180天(6个月)到期的用户
	
	public static final String TASK_VIEW_DAY = "day";
	public static final String TASK_VIEW_WEEK = "week";
	public static final String TASK_VIEW_MONTH = "month";
	
	public static final String ORDER_BY_REGULAR_SCORE = "regular_score";//根据续约得分排序
	public static final String ORDER_BY_VALUD_CHANGE = "value_change";//根据推荐套餐的价值变化
	public static final String ORDER_BY_GPRS6 = "gprs6";
	public static final String ORDER_BY_VALUD_CHANGE_4G = "value_change_4g";//根据推荐套餐的价值变化
	
	public static final int RECENT_MONTH = 6; //消费详情可展示最近1年的信息
	
}
