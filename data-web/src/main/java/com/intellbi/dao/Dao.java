package com.intellbi.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, ID extends Serializable> {
	
	/**
	 * 保存一个T的一个新对象 
	 * @param entity
	 * @return 添加记录值
	 */
	int save(T entity);
	
	/**
	 * 更新对象
	 * @param entity
	 * @return 
	 */
	int update(T entity);
	
	/**
	 * 根据主键ID，删除对象
	 * @param id
	 * @return  删除记录数
	 */
	int delete(ID id);
	
	/**
	 * 根据主键，查询对象
	 * @param id
	 * @return
	 */
	T get(ID id);
	
	/**
	 * 查找所有记录
	 * @return  返回所有记录
	 */
	List<T> findAll();
	
	boolean exists(ID id);
	
	
}
