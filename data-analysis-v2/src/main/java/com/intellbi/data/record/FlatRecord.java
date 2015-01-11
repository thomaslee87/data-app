/**
 * 
 */
package com.intellbi.data.record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.intellbi.utils.ProjectConfiguration;


/**
 * @author lizheng 20140504
 *
 */
public class FlatRecord implements IDataRecord {
	
	// dataset the record belongs to
	private RecordDataset m_Dataset;
	
	private List<Object> m_Values;
	
	public FlatRecord(String line, String delimter) {
		m_Values = new ArrayList<Object>();
		if(line != null && delimter != null) {
			Object[] values = line.split(delimter, -1);
			for(Object o: values)
				m_Values.add(o);
		}
	}
	
	public FlatRecord(RecordDataset dataset, String line, String delimiter) {
		this(line, delimiter);
		setRecordDataset(dataset);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return toString(ProjectConfiguration.DEFAULT_DELIMITER);
	}


	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#size()
	 */
	public int size() {
		// TODO Auto-generated method stub
		int ret = 0;
		if(m_Values != null)
			return m_Values.size();
		return ret;
	}


	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#get(int)
	 */
	public Object get(int i) {
		// TODO Auto-generated method stub
		if(m_Values == null)
			return null;
		return m_Values.get(i);
	}


	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#set(int, java.lang.Object)
	 */
	public void set(int i, Object value) {
		// TODO Auto-generated method stub
		if(m_Values != null && i >= 0 && i < m_Values.size())
			m_Values.set(i, value);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#getDataset()
	 */
	public RecordDataset getDataset() {
		// TODO Auto-generated method stub
		return m_Dataset;
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#setRecordDataset(com.intellbi.data.record.RecordDataset)
	 */
	public void setRecordDataset(RecordDataset dataset) {
		// TODO Auto-generated method stub
		m_Dataset = dataset;
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#addColumn(java.lang.String, java.lang.Object)
	 */
	public void addColumn(AttributeEntity attr, Object value) {
		// TODO Auto-generated method stub
		if(m_Dataset.addAttr(attr) )
			m_Values.add(value);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#toString(java.lang.String)
	 */
	public String toString(String delimiter) {
		// TODO Auto-generated method stub
		return StringUtils.join(m_Values, delimiter);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#getAttrIdxByName(java.lang.String)
	 */
	public int getAttrIdxByName(String name) {
		// TODO Auto-generated method stub
		return getDataset().getAttributes().getAttrIdxByName(name);
	}

}
