/**
 * 
 */
package com.intellbi.data.etl;

/**
 * @author lizheng 20140504
 *
 */
public interface IDataETLJob {
	/**
	 * initialization
	 */
	public void init();
	
	/**
	 * run the ETL job
	 */
	public void run();
}
