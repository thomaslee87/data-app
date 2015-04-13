package com.intellbit.dao;

import java.util.List;

//get user <-> consumer map
public interface BiMapDao {
		
	public int getMaintainConsumersCount(int userId, String bizmonth);
	public List<String> getMaintainConsumers(int userId, String bizmonth);
	
	public int getContractConsumersCount(int userId);
	public List<String> getContractConsumers();
	
	public int getBrandupConsumersCount(int userId);
	public List<String> getBrandupConsumers();
	
}
