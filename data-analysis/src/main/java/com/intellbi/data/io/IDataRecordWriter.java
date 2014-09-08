/**
 * 
 */
package com.intellbi.data.io;

import java.io.IOException;

import com.intellbi.data.record.IDataRecord;

/**
 * @author lizheng 20140513
 *
 */
public interface IDataRecordWriter {

	public void open();
	
	public void writeRecord(IDataRecord record) ;
		
	public void close() throws IOException;
	
	public void writeTitle(IDataRecord record);

}
