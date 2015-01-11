/**
 * 
 */
package com.intellbi.data.io;

import java.io.IOException;

import com.intellbi.data.record.IDataRecord;

/**
 * @author lizheng 20140504
 * load data record from file 
 */
public interface IFileIterator {
	
	/**
	 * @return data record 
	 */
	public IDataRecord next();
	
	/**
	 * close iterator
	 * @throws IOException 
	 */
	public void close() throws IOException;

}
