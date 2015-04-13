package com.intellbit.dataobject.ajax;

import java.io.Serializable;
import java.util.List;

public class DataGridResponse<T> implements Serializable {
	
	private static final long serialVersionUID = -2591425842370251404L;
	
	private String recordsFiltered;
	private String recordsTotal;
	private String draw;
	private String theDataMonth;
	
	private List<T> data;

	public String getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(String recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public String getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(String recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public String getTheDataMonth() {
		return theDataMonth;
	}

	public void setTheDataMonth(String theDataMonth) {
		this.theDataMonth = theDataMonth;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	
}
