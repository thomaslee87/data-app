/**
 * 
 */
package com.intellbi.data.etl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.intellbi.data.io.GzipTxtDataRecordWriter;
import com.intellbi.data.io.IDataRecordWriter;
import com.intellbi.data.io.IFileIterator;
import com.intellbi.data.io.MdbFileIterator;
import com.intellbi.data.record.Attributes;
import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordDataset;
import com.intellbi.utils.Constants;
import com.intellbi.utils.ProjectConfiguration;

/**
 * @author lizheng 20140504
 * load user's monthly billing data
 */
public class PartitionCustomerMonthlyBillingJob implements IDataETLJob {
	
	private String m_ETLConf = ProjectConfiguration.CONF_DIR + "etl.xml";
	
	private int m_SplitFileNumber = 10;
	private List<IDataRecordWriter> m_Writers = new ArrayList<IDataRecordWriter>();

	private Attributes m_ReservedAttributes;
	private String m_DataDir ;
	private String m_OutputDir;
	
	public PartitionCustomerMonthlyBillingJob(String dataDir, String outputDir) {
		m_DataDir = dataDir;
		m_OutputDir = outputDir;
		init();
	}
	
	public PartitionCustomerMonthlyBillingJob(String dataDir, String outputDir, String conf_file, int split_num) {
		m_DataDir = dataDir;
		m_OutputDir = outputDir;
		m_ETLConf = conf_file;
		m_SplitFileNumber = split_num;
		init();
	}
	
	/* (non-Javadoc)
	 * @see com.intellbi.data.etl.IDataETLJob#init()
	 */
	public void init() {
		// TODO Auto-generated method stub
		m_ReservedAttributes = new Attributes();
		m_ReservedAttributes.loadFromXML(m_ETLConf , "reserved_fields");
		initWriter();
	}
	
	public void initWriter() {
		for(int i = 0; i < m_SplitFileNumber ; i ++ ) {
			IDataRecordWriter writer = new GzipTxtDataRecordWriter(m_OutputDir + "\\" + i + ".log.gz");
			writer.open();
			System.out.println(m_OutputDir + "\\" + i + ".tar.gz");
			m_Writers.add(writer);
		}
	}

	/* (non-Javadoc)
	 * @see com.intellbi.data.etl.IDataETLJob#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		File f = new File(m_DataDir);
		if(f.isDirectory()) {
			File[] files = new File(m_DataDir).listFiles();
			for(File file: files) {
				String filePathName = file.toString();
				// Traverse all mdb/accdb files
				if(filePathName.endsWith(".mdb") || filePathName.endsWith(".accdb"))
					sb.append(filePathName).append("\n");
			}
		}
		else {
			sb.append(m_DataDir);
		}
		
		Pattern p = Pattern.compile("(\\d{6})");
		String[] mdbPathItems = sb.toString().split("\n");
		for(String mdbPath: mdbPathItems) {
			System.out.println(mdbPath);
			Matcher m = p.matcher(mdbPath);
			if(m.find()) {
				RecordDataset dataset = new RecordDataset(m_ReservedAttributes);
				int phoneNoIdx         = m_ReservedAttributes.getAttrIdxByName(Constants.COL_PHONE_NO);
				int joinNetTimeIdx     = m_ReservedAttributes.getAttrIdxByName(Constants.COL_JOIN_NET_TIME);
				int isGroupCustomerIdx = m_ReservedAttributes.getAttrIdxByName(Constants.COL_IS_GROUP_CUSTOMER);
				
				IFileIterator reader = new MdbFileIterator(dataset, mdbPath);
			
				IDataETLTask task = new CustomerMonthlyBillingFieldsFilter(dataset, m.group(1));
				IDataRecord record = null;
				while((record = reader.next()) != null) {
					long phoneNo = Long.parseLong((String) record.get(phoneNoIdx));
					String joinNetTime = (String)record.get(joinNetTimeIdx);
					boolean canBeNull = m_ReservedAttributes.getAttributeAt(joinNetTimeIdx).canBeNull();
					if(!canBeNull && joinNetTime.equals("")) {
						System.out.println("==========================");
						System.out.println(m.group(1));
						System.out.println(record.toString());
						System.out.println("==========================");
						System.exit(0);
					}
						
					int writerNo = (int)(phoneNo % m_SplitFileNumber);
					String filter = m_ReservedAttributes.getAttributeAt(isGroupCustomerIdx).getValueFilter();
					if(StringUtils.isBlank(filter) || record.get(isGroupCustomerIdx).equals(filter)) {
						IDataRecordWriter writer = m_Writers.get(writerNo);
//						System.out.println(record.toString());
						writer.writeRecord(task.dealWithRecord(record));
					}
//					dataset.add(record);
				}
//				task.dealWithRecords(dataset);
//				m_Dataset.add(dataset);
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for(IDataRecordWriter writer: m_Writers) {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
