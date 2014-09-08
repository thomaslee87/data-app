/**
 * 
 */
package com.intellbi.data.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.intellbi.data.record.FlatRecord;
import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordDataset;
import com.intellbi.utils.ProjectConfiguration;

/**
 * @author lizheng 20140531
 */
public class GzipFileIterator implements IFileIterator {
	
	private BufferedReader br;
	private RecordDataset m_Dataset;
	
	public GzipFileIterator(RecordDataset dataset, String gzipFile)  {
		try {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipFile)), ProjectConfiguration.DEFAULT_CSV_ENCODING));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		m_Dataset = dataset;
	}
	
	/* (non-Javadoc)
	 * @see com.intellbi.data.IKeyValueFileIterator#next()
	 */
	public IDataRecord next() {
		// TODO Auto-generated method stub
		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(line == null)
			return null;
		return new FlatRecord(m_Dataset, line, ProjectConfiguration.DEFAULT_DELIMITER);
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.IKeyValueFileIterator#close()
	 */
	public void close() throws IOException {
		// TODO Auto-generated method stub
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
