/**
 * 
 */
package com.intellbi.data.record;

/**
 * @author lizheng 20140513
 *
 */
public class AttributeEntity {
	private String  m_Type ;
	private String  m_Name;
	private String  m_Name_CN;
	private String  m_ValidFilter;
	private boolean m_CanBeNull;
	private int     m_Index;
	
	public AttributeEntity(String type, String name, String name_CN, String valueFilter, boolean canBeNull, int index) {
		m_Type = type;
		m_Name = name;
		m_ValidFilter = valueFilter;
		m_CanBeNull = canBeNull;
		m_Name_CN = name_CN;
		m_Index = index;
	}

	public String getType() {
		return m_Type;
	}
	
	public String getName() {
		return m_Name;
	}
	
	public String getValueFilter() {
		return m_ValidFilter;
	}

	public boolean canBeNull() {
		return m_CanBeNull;
	}

	public int getIndex() {
		return this.m_Index;
	}
	
	public String getNameCN() {
		return m_Name_CN;
	}
}