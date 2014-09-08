/**
 * 
 */
package com.intellbi.data.record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author lizheng 20140525
 *
 */
public class Attributes implements Iterable<AttributeEntity>{
	
	private static Logger logger = Logger.getLogger(Attributes.class);
	
	//jdom element node's content
	private static final String FIELDS_ID = "id";
	private static final String FILED_TYPE = "type";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_NAME_CN = "name_cn";
	private static final String FIELD_VALUE_FILLTER = "filter";
	private static final String FILED_CAN_BE_NULL = "null";
	
	//jdom element attribute
	private static final String FIELD_INDEX = "no";

	private List<AttributeEntity> m_Attributes;
	private Map<String, Integer>  m_AttrIdxMap;
	
	public Attributes() {
		m_Attributes = new ArrayList<AttributeEntity>();
		m_AttrIdxMap = new HashMap<String, Integer>();
	}
	
	public void loadFromXML(String xml, String fieldsID) {
		if(StringUtils.isBlank(xml) || StringUtils.isBlank(fieldsID))
			return ;
		
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document doc = builder.build(xml);
			Element root  = doc.getRootElement();
			List<Element> options = root.getChildren();
			for(Element option: options) {
				if(option.getAttributeValue(FIELDS_ID).equals(fieldsID)) {
					List<Element> fields = option.getChildren();
					for(Element field: fields) {
						String fieldType = field.getChild(FILED_TYPE).getText();
						String fieldName = field.getChild(FIELD_NAME).getText();
						String fieldNameCN = field.getChild(FIELD_NAME_CN).getText();
						
						String fieldValueFilter = null;
						Element valueFilterElement = field.getChild(FIELD_VALUE_FILLTER);
						if(valueFilterElement != null)
							fieldValueFilter = valueFilterElement.getText();
						
						Element fieldCanBeNullElement =  field.getChild(FILED_CAN_BE_NULL);
						boolean fieldCanBeNull = true;
						if(fieldCanBeNullElement != null) {
							String sFieldCanBeNull = fieldCanBeNullElement.getText();
							if(sFieldCanBeNull.equals("false")) 
								fieldCanBeNull = false;
						}
						
						int fieldIndex = -1; 
						try {
							fieldIndex = Integer.parseInt(field.getAttributeValue(FIELD_INDEX));
						} catch (Exception e) {
							fieldIndex = -1;
						}
						if(fieldIndex < 0)
							logger.fatal("field index is not valid in xml configuration");
						else if(fieldIndex < m_Attributes.size())
							logger.fatal("field index [ " + fieldIndex + " ] should be unique.");
						
						AttributeEntity entity = new AttributeEntity(fieldType, fieldName, fieldNameCN, fieldValueFilter, fieldCanBeNull, fieldIndex);
						if(m_AttrIdxMap.put(fieldName, fieldIndex) != null) 
							logger.fatal("field name [ " + fieldName + " ] should be unique.");
						
						m_Attributes.add(entity);
					}
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getAttrIdxByName(String name) {
		Integer idx = m_AttrIdxMap.get(name);
		if(idx == null)
			return -1;
		return idx;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<AttributeEntity> iterator() {
		// TODO Auto-generated method stub
		return m_Attributes.iterator();
	}
	
	public int size() {
		return m_Attributes.size();
	}
	
	public AttributeEntity getAttributeAt(int index) {
		return m_Attributes.get(index);
	}
	
	public boolean add(AttributeEntity attr) {
		if(attr.getName().equals(m_Attributes.get(m_Attributes.size() - 1).getName()))
			return true;
//		if(attr.getIndex() < m_Attributes.size() || m_AttrIdxMap.get(attr.getName()) != null) 
//			return false;
		m_Attributes.add(attr);
		m_AttrIdxMap.put(attr.getName(), attr.getIndex());
		return true;
	}
}
