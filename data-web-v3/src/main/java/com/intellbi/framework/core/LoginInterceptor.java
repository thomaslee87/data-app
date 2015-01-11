package com.intellbi.framework.core;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LoginInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;

    @Override
    public String doIntercept(ActionInvocation invocation) throws Exception {
    	
    	 // 取得请求相关的ActionContext实例  
        ActionContext ctx = invocation.getInvocationContext();

        Subject subject = SecurityUtils.getSubject();
        if(subject != null && subject.isAuthenticated()) {
            return invocation.invoke();  
        }  
  
        ctx.put("tip", "请登录后重试");  
        return Action.LOGIN;  
    }

}
