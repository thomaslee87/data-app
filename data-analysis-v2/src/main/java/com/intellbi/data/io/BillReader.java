/**
 * 
 */
package com.intellbi.data.io;

import com.intellbi.data.dao.dataobject.ConsumerBillDetailWrapper;

public interface BillReader {
	
	public boolean init();
	
	public boolean hasNext();
	
	public ConsumerBillDetailWrapper next();
	
	public void    finish();
	
}
