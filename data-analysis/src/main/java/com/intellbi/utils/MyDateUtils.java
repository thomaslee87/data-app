/**
 * 
 */
package com.intellbi.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lizheng
 *
 */
public class MyDateUtils {

	private static Pattern p = Pattern.compile("^(\\d{6})($|\\d{2}$)");
	
	public static boolean checkMonthFormat(String theMonth, String format){
		boolean ret = true;
		DateFormat ff = new SimpleDateFormat(format);
		try {
			ff.parse(theMonth);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			ret = false;
		}
		return ret;
	}
	
	public static String getMonthByDelta(String theMonth, int delta) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dateObj = null;
		try {
			dateObj = format.parse(theMonth + "01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dateObj);
		c.add(Calendar.MONTH, delta);
		return format.format(c.getTime()).substring(0, 6);
	}
	
	/**
	 * @param date1 起始日期 YYYYMM
	 * @param date2 结束日期 YYYYMM
	 * @return 日期相差的月数
	 */
	public static int getMonthsBetween(String date1, String date2) {
		Matcher m1 = p.matcher(date1);
		Matcher m2 = p.matcher(date2);
		if(!m1.find() || !m2.find())
			return Integer.MAX_VALUE;  //用来代表非法值
		int d1 = Integer.parseInt(m1.group(1));
		int d2 = Integer.parseInt(m2.group(1));
		int year1  = d1 / 100;
		int month1 = d1 % 100; 
		int year2  = d2 / 100;
		int month2 = d2 % 100;
		
		return year2 * 12 + month2 - (year1 * 12 + month1);
	}
	
	public static int getMonthsBetween(int d1, int d2) {
		int bits1 = getBitsOfNumber(d1);
		int bits2 = getBitsOfNumber(d2);
		
		int year1, month1, year2, month2;
		
		if(bits1 == 6) {
			year1  = d1 / 100;
			month1 = d1 % 100; 
		}
		else if(bits1 == 8) {
			year1  = d1 / 10000;
			month1 = (d1 / 100) % 100; 
		}
		else {
			return Integer.MAX_VALUE; //用来代表非法值
		}
		
		if(bits2 == 6) {
			year2  = d2 / 100;
			month2 = d2 % 100;
		}
		else if(bits2 == 8) {
			year2  = d2 / 100;
			month2 = (d2 / 100) % 100;
		}
		else {
			return Integer.MAX_VALUE; //用来代表非法值
		}
		
		return year2 * 12 + month2 - (year1 * 12 + month1);
	}
	
	private static int getBitsOfNumber(int number) {
		int bits = 0;
		while(number != 0) {
			number /= 10;
			bits ++;
		}
		return bits;
	}
	
}
