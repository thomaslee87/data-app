package com.intellbi.data.dataobject;

import java.util.ArrayList;
import java.util.List;

public class ConsumerSessionNode {
	private int yearMonth;
	private long contractFrom;
	private long contractTo;
	private com.intellbi.config.PackageConfig.Package consumerPackage;

	private List<Double> values = new ArrayList<Double>();

	public ConsumerSessionNode(int yearMonth, com.intellbi.config.PackageConfig.Package consumerPackage, long contractFrom,
			long contractTo, List<Double> values) {
		this.yearMonth = yearMonth;
		this.contractFrom = contractFrom;
		this.contractTo = contractTo;
		this.values = values;
		this.consumerPackage = consumerPackage;
	}
	
	public com.intellbi.config.PackageConfig.Package getPackage(){
		return consumerPackage;
	}

	public int getYearMonth() {
		return yearMonth;
	}

	public double getIncome() {
		return values.get(0);
	}

	public double getCallAmount() {
		return values.get(1);
	}

	public double getCallTime() {
		return values.get(2);
	}

	public double getGprs() {
		return values.get(3);
	}

	public long getContractFrom() {
		return contractFrom;
	}

	public long getContractTo() {
		return contractTo;
	}
}
