package com.intellbit.web.dataobject;

public class ListPage {
	
	private int totalNumber;
	private int currPage = 1;
	private int pageSize = 50;
	private int totalPages = 1;

	private void calc(){
		totalPages = totalNumber / pageSize + (totalNumber % pageSize == 0?0:1);
	}
	
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
		calc();
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
		calc();
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
