package com.intellbi.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordDataset;
import com.intellbi.utils.ProjectConfiguration;

public class GzipTxtDataRecordWriter implements IDataRecordWriter {
	private String txtFile;
	private String delimiter;
//	private OutputStreamWriter osw;
	private GZIPOutputStream gzipOs;
	
	public GzipTxtDataRecordWriter(String txtFile) {
		this(txtFile, ProjectConfiguration.DEFAULT_DELIMITER);
	}
	
	public GzipTxtDataRecordWriter(String txtFile, String delimiter) {
		this.txtFile = txtFile;
		this.delimiter = delimiter;
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.io.IDataRecordWriter#open(java.lang.String)
	 */
	public void open() {
		// TODO Auto-generated method stub
//		try {
//			osw = new OutputStreamWriter(new FileOutputStream(new File(txtFile)), ProjectConfiguration.DEFAULT_CSV_ENCODING);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			gzipOs = new GZIPOutputStream(new FileOutputStream(new File(txtFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.io.IDataRecordWriter#writeRecord(com.intellbi.data.record.IDataRecord)
	 */
	public void writeRecord(IDataRecord record) {
		// TODO Auto-generated method stub
		if(gzipOs != null) {
			try {
				gzipOs.write((record.toString(this.delimiter) + "\n").getBytes(ProjectConfiguration.DEFAULT_CSV_ENCODING));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.io.IDataRecordWriter#close()
	 */
	public void close() throws IOException {
		// TODO Auto-generated method stub
		if(gzipOs != null) {
			gzipOs.finish();
			gzipOs.flush();
			gzipOs.close();
			gzipOs = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.io.IDataRecordWriter#writeTitle(com.intellbi.data.record.IDataRecord)
	 */
	public void writeTitle(IDataRecord record) {
		// TODO Auto-generated method stub
		RecordDataset dataset = record.getDataset();
		if(dataset != null) {
			String titleString = dataset.getAttrString(this.delimiter);
			if(titleString != null) {
				try {
					gzipOs.write((titleString + "\n").getBytes(ProjectConfiguration.DEFAULT_CSV_ENCODING));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
