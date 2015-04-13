package com.intellbit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.intellbit.service.BiMapService;

@Service("biMapService")
public class BiMapServiceImpl implements BiMapService {

	@Override
	public int getMaintainConsumersCount(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getMaintainConsumers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getContractConsumersCount(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getContractConsumers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBrandupConsumersCount(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getBrandupConsumers() {
		// TODO Auto-generated method stub
		return null;
	}

}
