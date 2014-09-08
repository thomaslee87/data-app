/**
 * 
 */
package com.intellbi.data.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.MdbRecord;
import com.intellbi.data.record.RecordDataset;

/**
 * @author lizheng 20140504
 * load data from mdb file. We use 3rd package jackess here, since mdb file is 
 * usually read in Windows, and odbc datasouce should be set. Db connection 
 * for mdb is not available in Linux.
 */
public class MdbFileIterator implements IFileIterator {
	
	private Database db;
	private Table table;
	private Iterator<Row> iter;
	
	private RecordDataset m_Dataset;
	
	/**
	 * @param mdbFile
	 * @param tblName
	 * construct with mdb file and table name
	 */
	public MdbFileIterator(RecordDataset dataset, String mdbFile, String tblName)  {
		m_Dataset = dataset;
		try {
			db = DatabaseBuilder.open(new File(mdbFile));
			this.useTable(tblName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MdbFileIterator(RecordDataset dataset, String mdbFile)  {
		m_Dataset = dataset;
		try {
			db = DatabaseBuilder.open(new File(mdbFile));
			Set<String> tableNames = db.getTableNames();
			this.useTable((String)tableNames.toArray()[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param tblName
	 * @return mdb Table object
	 * switch table in database
	 */
	private Table useTable(String tblName)  {
		if(db == null)
			return null;
		try {
			table = db.getTable(tblName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iter  = table.iterator(); 
		return table;
	}
	
	/* (non-Javadoc)
	 * @see com.intellbi.data.IKeyValueFileIterator#next()
	 */
	public IDataRecord next() {
		// TODO Auto-generated method stub
		if(db == null || table == null || iter == null || !iter.hasNext())
			return null;
		return new MdbRecord(m_Dataset, iter.next());
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.IKeyValueFileIterator#close()
	 */
	public void close() throws IOException {
		// TODO Auto-generated method stub
		db.close();
	}
}
