/**
 * 
 */
package com.intellbi.data.record;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.healthmarketscience.jackcess.Row;
import com.intellbi.utils.ProjectConfiguration;

/**
 * @author lizheng 20140504
 * data record for mdb file
 */
public class MdbRecord implements IDataRecord {
	
	private RecordDataset m_Dataset;
	private List<Object> m_Values;
	
	public MdbRecord(RecordDataset dataset, Row row) {
		setRecordDataset(dataset);
		m_Values = new ArrayList<Object>();
		for(AttributeEntity attr: dataset.getAttributes()) {
			String attrNameCN = attr.getNameCN();
			if(!attrNameCN.equals("数据日期")) {
				Object attrValue = row.get(attr.getNameCN());
				if(attrValue == null) {
					if(attr.getType().equalsIgnoreCase("double") || attr.getType().equalsIgnoreCase("integer"))
						attrValue = "0";
					else
						attrValue = "";
				}
				m_Values.add(attrValue.toString().replaceAll(",", ""));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return toString(ProjectConfiguration.DEFAULT_DELIMITER);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#get(int)
	 */
	public Object get(int i) {
		// TODO Auto-generated method stub
		return m_Values.get(i);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#set(int, java.lang.Object)
	 */
	public void set(int index, Object value) {
		// TODO Auto-generated method stub
		if(index >= 0 && index < m_Dataset.getAttributes().size()) {
			m_Values.set(index, value);	
		}
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
	 * @see com.intellbi.data.record.IDataRecord#size()
	 */
	public int size() {
		// TODO Auto-generated method stub
		return m_Values.size();
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#toString(java.lang.String)
	 */
	public String toString(String delimiter) {
		// TODO Auto-generated method stub
		return StringUtils.join(m_Values, delimiter);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#addColumn(com.intellbi.data.record.AttributeEntity, java.lang.Object)
	 */
	public void addColumn(AttributeEntity attr, Object value) {
		// TODO Auto-generated method stub
		if(m_Dataset.addAttr(attr))
			m_Values.add(value);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.record.IDataRecord#getAttrIdxByName(java.lang.String)
	 */
	public int getAttrIdxByName(String name) {
		// TODO Auto-generated method stub
		return getDataset().getAttributes().getAttrIdxByName(name);
	}
}
