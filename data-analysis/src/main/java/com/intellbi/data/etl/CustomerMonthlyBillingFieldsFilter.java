/**
 * 
 */
package com.intellbi.data.etl;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.intellbi.data.record.AttributeEntity;
import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordDataset;
import com.intellbi.utils.Constants;


/**
 * @author lizheng 20140504
 * drop un-used fields
 */
public class CustomerMonthlyBillingFieldsFilter implements IDataETLTask {
	
	private Logger logger = Logger.getLogger(CustomerMonthlyBillingFieldsFilter.class);
	
	private RecordDataset m_Dataset;
	private String m_YearMonth;
	
	private List<IDataETLTask> fieldFilters;
	
	public CustomerMonthlyBillingFieldsFilter(RecordDataset dataset, String yearMonth) {
		this.m_Dataset = dataset;
		this.m_YearMonth = yearMonth;
		initOperations();
	}
	
	/**
	 * initialize field operations
	 */
	private void initOperations() {
		fieldFilters = new LinkedList<IDataETLTask>();
		//add date month fields
		fieldFilters.add(new IDataETLTask() {
			
			public IDataRecord dealWithRecord(IDataRecord record) {
				// TODO Auto-generated method stub
				int size = record.getDataset().getAttributes().size();
				AttributeEntity yearMonthAttr = new AttributeEntity("integer", Constants.COL_YEAR_MONTH, "数据日期", "", false, size);
				record.addColumn(yearMonthAttr, m_YearMonth);
				return record;
			}

			public RecordDataset dealWithRecords(RecordDataset records) {
				// TODO Auto-generated method stub
				for(IDataRecord record: records) {
					dealWithRecord(record);
				}
				return records;
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.etl.IDataETLTask#dealWithRecord(com.intellbi.data.record.IDataRecord)
	 */
	public IDataRecord dealWithRecord(IDataRecord record) {
		// TODO Auto-generated method stub
		for(IDataETLTask task : fieldFilters)
			task.dealWithRecord(record);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.etl.IDataETLTask#dealWithRecords(com.intellbi.data.record.RecordDataset)
	 */
	public RecordDataset dealWithRecords(RecordDataset records) {
		// TODO Auto-generated method stub
		for(IDataETLTask task : fieldFilters)
			task.dealWithRecords(records);
		return records;
	}
	
}
