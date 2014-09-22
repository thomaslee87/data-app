package com.intellbi.data_analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ConverterUtils;

import com.intellbi.data.etl.PartitionCustomerMonthlyBillingJob;
import com.intellbi.data.etl.UserBills;
import com.intellbi.data.etl.UserBills.SingleUserBillList;
import com.intellbi.data.io.GzipFileIterator;
import com.intellbi.data.io.IFileIterator;
import com.intellbi.data.record.AttributeEntity;
import com.intellbi.data.record.Attributes;
import com.intellbi.data.record.IDataRecord;
import com.intellbi.data.record.RecordDataset;
import com.intellbi.utils.ColumnConstants;
import com.intellbi.utils.ProjectConfiguration;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static String m_ETLConf = ProjectConfiguration.CONF_DIR + "etl.xml";
	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
//		System.out.println(Runtime.getRuntime().maxMemory());
//		System.out.println(Runtime.getRuntime().totalMemory());
//		System.out.println(Runtime.getRuntime().freeMemory());

//		readRecordsFromMDB();
//		flatUserBills();
		
		Pattern p = Pattern.compile("(\\d+)_(\\d?)");
		Matcher m = p.matcher("123_2");
		if(m.find())
			System.out.println(m.group(2));
		
		
		try {
//			c45Tree();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void c45Tree() throws Exception {

		String file = "D:\\z-proj\\data\\python_scripts\\vip_data.arff";
//		String file = "D:\\z-proj\\data\\python_scripts\\normal_data.arff";
		AbstractFileLoader loader = ConverterUtils.getLoaderForFile(file);
		loader.setFile(new File(file));
		Instances inst = loader.getDataSet();
		inst.setClassIndex(inst.numAttributes() - 1);
		
		Classifier classifier = new J48();
		classifier.setOptions(new String[]{"-M","50","-C","0.25"});
		Instances ins = new Instances(inst);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		System.out.println(inst.instance(0).toString());
		
		classifier.buildClassifier(inst);
		System.out.println(classifier.toString());
		
//		FileOutputStream os = new FileOutputStream(new File("normal_users.csv"));
		FileOutputStream os = new FileOutputStream(new File("vip_users.csv"));
		for(int i = 0; i<inst.numInstances(); i ++) {
//			System.out.println(
					os.write(
							(ins.instance(i).toString()
//					);
//			System.out.println(
					+ "," + classifier.classifyInstance(inst.instance(i))
					+ "," + 
					Math.round(100-classifier.distributionForInstance(inst.instance(i))[0]*100) + "%"
					+ "\n").getBytes()
					);
//			System.out.println("------------------------------------------------");
		}
		
		os.close();
	}
	
	public static void flatUserBills() {
		
		StringBuilder sb = new StringBuilder();
		File[] files = new File("D:\\z-proj\\data\\all_files").listFiles();
		for(File file: files) {
			String filePathName = file.toString();
			// Traverse all mdb/accdb files
			if(filePathName.endsWith(".log.gz"))
				sb.append(filePathName).append("\n");
		}
		int i = 0;
		
		String[] fileItems = sb.toString().split("\n");
		for(String filePath: fileItems) {
			
			String[] filePathItems = filePath.split("[\\\\/]");
			String fileName = filePathItems[filePathItems.length - 1];
			
			UserBills userBills = new UserBills();
			
			Attributes attributes = new Attributes();
			attributes.loadFromXML(m_ETLConf , "reserved_fields");
			AttributeEntity yearMonthAttr = new AttributeEntity("integer", ColumnConstants.COL_YEAR_MONTH, "�������", "", false, attributes.size());
			attributes.add(yearMonthAttr);
			
			RecordDataset dataset = new RecordDataset(attributes);
			
			IFileIterator reader = new GzipFileIterator(dataset, filePath);
			IDataRecord record = null;
			while((record = reader.next()) != null) {
				userBills.add(record);
			}

			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(
						new GZIPOutputStream(new FileOutputStream("D:\\z-proj\\data\\contract\\" + fileName)), 
						ProjectConfiguration.DEFAULT_CSV_ENCODING));
				Map<Long, SingleUserBillList> userBillsMap = userBills.getUserBills();
				for(Long phone: userBillsMap.keySet()) {
					SingleUserBillList billList = userBillsMap.get(phone);
					for(IDataRecord rec: billList.getList()) {
						bw.write(rec.toString() + "\n");
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static void readRecordsFromMDB() {//23min
		Date date = new Date();
		Long time1 = date.getTime() / 1000;
		System.out.println(date.toString());
		PartitionCustomerMonthlyBillingJob o = new PartitionCustomerMonthlyBillingJob("D:\\z-proj\\data\\files\\�ƶ�����Ƿ��201312.mdb", "D:\\z-proj\\data\\201312_files\\");
		o.run();
		date = new Date();
		System.out.println(date.toString());
		Long time2 = date.getTime() / 1000;
		System.out.println(time2 - time1);
	}
}
