package com.intellbit.dataobject.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContractConsumerInfo implements Serializable {
	
	private static final long serialVersionUID = -2390657652171521399L;
	
	private List<Object> dataList = new ArrayList<Object>();
	
	public List<Object> getDataList() {
		return dataList;
	}
	
	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
	}
	
	public ContractConsumerInfo addDataObject(Object o ){
		this.dataList.add(o);
		return this;
	}
	
	public static class ExtraInfo {
		private String month;
		private String phone;
		private String save;
		private String rmd;//recommend desc
		private String rmd4g;
		private String save4g;
		
		public String getRmd4g() {
			return rmd4g;
		}
		public ExtraInfo setRmd4g(String rmd4g) {
			this.rmd4g = rmd4g;
			return this;
		}
		public String getSave4g() {
			return save4g;
		}
		public ExtraInfo setSave4g(String save4g) {
			this.save4g = save4g;
			return this;
		}
		public String getMonth() {
			return month;
		}
		public ExtraInfo setMonth(String month) {
			this.month = month;
			return this;
		}
		public String getPhone() {
			return phone;
		}
		public ExtraInfo setPhone(String phone) {
			this.phone = phone;
			return this;
		}
		public String getSave() {
			return save;
		}
		public ExtraInfo setSave(String save) {
			this.save = save;
			return this;
		}
		public String getRmd() {
			return rmd;
		}
		public ExtraInfo setRmd(String rmd) {
			this.rmd = rmd;
			return this;
		}
	}
	
}
