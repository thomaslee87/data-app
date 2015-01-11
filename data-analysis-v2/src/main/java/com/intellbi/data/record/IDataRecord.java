/**
 * 
 */
package com.intellbi.data.record;

/**
 * @author lizheng 20140504
 */
public interface IDataRecord {
	
	public Object get(int i);
	
	public void set(int index, Object value);
	
	public void addColumn(AttributeEntity attr, Object value);
	
	public RecordDataset getDataset();
	
	public void setRecordDataset(RecordDataset dataset);
	
	public int size();
	
	public String toString(String delimiter);
	
	public int getAttrIdxByName(String name);
	
}