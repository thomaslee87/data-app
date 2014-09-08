/**
 * 
 */
package com.intellbi.data.record;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lizheng 20140511
 *
 */
public class RecordDataset implements Iterable<IDataRecord> {
	
	private Attributes m_Attr;
	private List<IDataRecord> m_Dataset;
	
	public RecordDataset(Attributes attrs) {
		m_Attr = attrs;
		m_Dataset = new ArrayList<IDataRecord>();
	}
	
	/**
	 * @param record
	 * @return success or not
	 */
	public boolean add(IDataRecord record) {
		if(record == null || m_Attr.size() != record.size())
			return false;
		
		record.setRecordDataset(this);
		m_Dataset.add(record);
		return true;
	}
	
	/**
	 * @param records  add another dataset
	 * @return
	 */
	public boolean add(RecordDataset records) {
		if( records == null || m_Attr.size() != records.getAttributes().size())
			return false;
		
		for(IDataRecord record: records) {
			record.setRecordDataset(this);
			add(record);
		}
		
		return true;
	}
	
	public Attributes getAttributes() {
		return m_Attr;
	}
	
	public AttributeEntity getAttributeAt(int i) {
		if(i < 0 || i >= m_Attr.size()) 
			return null;
		
		return m_Attr.getAttributeAt(i);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public java.util.Iterator<IDataRecord> iterator() {
		// TODO Auto-generated method stub
		return m_Dataset.iterator();
	}
	
	public boolean addAttr(AttributeEntity attr) {
		return m_Attr.add(attr);
	}
	
	public String getAttrString(String delimiter) {
		StringBuilder sb = new StringBuilder();
		if(m_Attr != null) {
			for(AttributeEntity attr: m_Attr) {
				if(sb.length() > 0)
					sb.append(delimiter);
				sb.append(attr.getName());
			}
			return sb.toString();
		}
		return null;
	}
	
}
