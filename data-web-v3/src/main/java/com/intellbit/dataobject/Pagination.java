package com.intellbit.dataobject;

/**
 * @author lizheng
 * 数据库分页参数
 */
public class Pagination {

	/**
	 * 排序字段名
	 */
	private String orderBy;
	/**
	 * 分页的开始记录序号
	 */
	private int pageBeginSeq;
	/**
	 * 分页的结束记录序号
	 */
	private int pageSize;
	
	public Pagination(int pageBeginSeq, int pageSize, String orderBy) {
		setPageBeginSeq(pageBeginSeq);
		setPageSize(pageSize);
		setOrderBy(orderBy);
	}
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getPageBeginSeq() {
		return pageBeginSeq;
	}
	public void setPageBeginSeq(int pageBeginSeq) {
		this.pageBeginSeq = pageBeginSeq;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
