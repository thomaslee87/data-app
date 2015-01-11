/**
 * 
 */
package com.intellbi.data.etl;

import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordDataset;

/**
 * @author lizheng 20140504
 *
 */
public interface IDataETLTask {

	/**
	 * @param record
	 * @return deal with a single record
	 */
	public IDataRecord dealWithRecord(IDataRecord record);
	
	/**
	 * @param records
	 * @return deal with set of records
	 */
	public RecordDataset dealWithRecords(RecordDataset records);
	
}
