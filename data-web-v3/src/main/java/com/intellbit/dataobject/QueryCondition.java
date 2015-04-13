package com.intellbit.dataobject;

import java.util.ArrayList;
import java.util.List;

import javax.management.QueryEval;

import com.intellbit.dataobject.ajax.RequestQueryFilter;

public class QueryCondition {
	
	public static class QueryConditionBuilder {

		private int userId;
		private String phoneNo;
		private String theMonth;
		private boolean hideDone;
		private Page page = new Page();
		private List<String> orderFileds = new ArrayList<String>();
		private RequestQueryFilter filter;
		
		public QueryConditionBuilder setFilter(RequestQueryFilter filter) {
			this.filter = filter;
			return this;
		}
		
		public QueryConditionBuilder setUserId(int userId) {
			this.userId = userId;
			return this;
		}
		
		public QueryConditionBuilder setTheMonth(String theMonth) {
			this.theMonth = theMonth;
			return this;
		}
		
		public QueryConditionBuilder setHideDone(boolean hideDone) {
			this.hideDone = hideDone;
			return this;
		}
		
		public QueryConditionBuilder setPage(int pageStart, int pageSize) {
			this.page.setPageStart(pageStart);
			this.page.setPageSize(pageSize);
			return this;
		}
		
		public QueryConditionBuilder addOrderField(String orderField) {
			this.orderFileds.add(orderField);
			return this;
		}
		
		public QueryConditionBuilder setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
			return this;
		}
		
		public QueryCondition build() {
			return new QueryCondition(this);
		}
	}
	
	public static class Page {
		private int pageSize;
		private int pageStart;
		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public int getPageStart() {
			return pageStart;
		}
		public void setPageStart(int pageStart) {
			this.pageStart = pageStart;
		}
	}
	
	private QueryCondition(QueryConditionBuilder builder) {
		this.setUserId(builder.userId);
		this.setTheMonth(builder.theMonth);
		this.setHideDone(builder.hideDone);
		
		this.setPage(builder.page);
		this.setOrderFileds(builder.orderFileds);
		
		this.setPhoneNo(builder.phoneNo);
		
		this.setFilter(builder.filter);
	}

	private int userId;
	private String phoneNo;
	private String theMonth;
	private boolean hideDone;
	private Page page;
	private List<String> orderFileds;
	
	private RequestQueryFilter filter;
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public RequestQueryFilter getFilter() {
		return filter;
	}
	public void setFilter(RequestQueryFilter filter) {
		this.filter = filter;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
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
	public boolean isHideDone() {
		return hideDone;
	}
	public void setHideDone(boolean hideDone) {
		this.hideDone = hideDone;
	}
	public List<String> getOrderFileds() {
		return orderFileds;
	}
	public void setOrderFileds(List<String> orderFileds) {
		this.orderFileds = orderFileds;
	}
	
}
