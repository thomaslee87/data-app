package com.intellbit.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.intellbit.utils.Const;

public class LoginFilter implements Filter {
	
	private FilterConfig config;
	private Set<String> excludedUrlSet = new HashSet<String>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.config = filterConfig;
		String excludedUrls = config.getInitParameter("excludedUrls");
		if (null != excludedUrls) {
			String[] tmps = excludedUrls.split(",");
			for (String tmp : tmps) {
				tmp = tmp.trim();
				if (tmp.length() > 0) {
					this.excludedUrlSet.add(tmp.toLowerCase());
				}
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest hreq = (HttpServletRequest) request;
		
		String requestURI = hreq.getRequestURI().toLowerCase();
		if (!requestURI.equals("/")) {
			if (this.excludedUrlSet.contains(requestURI)
					|| (!requestURI.endsWith(".html") && !requestURI.startsWith("/api/")) ) {
				chain.doFilter(request, response);
				return ;
			}
		}
		
		Object oUserId = hreq.getSession().getAttribute(Const.SS_USER_ID);
		if (oUserId == null) { // not login
            HttpServletResponse hres = (HttpServletResponse) response;
            hres.sendRedirect("/login.html");
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
