package com.intellbit.service;

import java.util.List;

public interface BiMapService {
	
	public int getMaintainConsumersCount(int userId);
	public List<String> getMaintainConsumers();
	
	public int getContractConsumersCount(int userId);
	public List<String> getContractConsumers();
	
	public int getBrandupConsumersCount(int userId);
	public List<String> getBrandupConsumers();
	
}
