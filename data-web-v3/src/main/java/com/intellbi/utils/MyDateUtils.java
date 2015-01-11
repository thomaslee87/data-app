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

import org.apache.commons.lang3.StringUtils;

/**
 * @author lizheng
 *
 */
public class MyDateUtils {

	private static Pattern p = Pattern.compile("^(\\d{6})($|\\d{2}$)");
	
	public static String getTheMonthOfBizDate(String theDate, String format) {
		return transferDateFormat(theDate, format, "yyyyMM");
	}
	
	/**
	 * @param theDate
	 * @param fromFormat
	 * @param toFormat
	 * @return 将给定格式的日期转化为指定格式
	 */
	public static String transferDateFormat(String theDate, String fromFormat, String toFormat) {
		if(StringUtils.isBlank(theDate))
			return null;
		DateFormat ff = new SimpleDateFormat(fromFormat);
		ff.setLenient(false);
		Date dd = null;
		try {
			dd = ff.parse(theDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
		ff = new SimpleDateFormat(toFormat);
		return ff.format(dd.getTime());
	}
	
	/**
	 * @param theMonth
	 * @param format
	 * @return 检查日期字符串是否符合给定格式
	 */
	public static boolean checkMonthFormat(String theMonth, String format){
		boolean ret = true;
		DateFormat ff = new SimpleDateFormat(format);
		ff.setLenient(false);
		try {
			Date dd = ff.parse(theMonth);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			ret = false;
		}
		return ret;
	}
	
	/**
	 * @param theMonth
	 * @param delta
	 * @return 根据相隔几个月来计算月份字符串
	 */
	public static String getMonthByDelta(String theMonth, int delta) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dateObj = null;
		try {
		    if(theMonth.length() == 6)
		        dateObj = format.parse(theMonth + "01");
		    else if(theMonth.length() == 8)
		        dateObj = format.parse(theMonth);
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
	 * @param theDay
	 * @param delta
	 * @return 给定日期（yyyymmdd格式），计算相距delta天的日期
	 */
	public static String getDateByDeltaDays(String theDay, int delta) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dateObj = null;
		try {
			dateObj = format.parse(theDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dateObj);
		c.add(Calendar.DATE, delta);
		return format.format(c.getTime());
	}
	
	public static class Period {
		private String begin;
		private String end;
		
		public Period(String begin, String end) {
			this.begin = begin;
			this.end = end;
		}

		public String getBegin() {
			return begin;
		}
		public String getEnd() {
			return end;
		}
		public void setBegin(String begin){
			this.begin = begin;
		}
		public void setEnd(String end){
			this.end = end;
		}
		
		public Period moveByDays(int days) {
			return new Period(MyDateUtils.getDateByDeltaDays(begin, days), 
					MyDateUtils.getDateByDeltaDays(end, days));
		}
	}
	
	public static Period getWeekOfDate(String theDate) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = format.parse(theDate);
		} catch (ParseException e) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String begin = format.format(c.getTime());
		
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String end = format.format(c.getTime());
		return new Period(begin, end);
		
//		c.setTime(date);
//		c.add(Calendar.DATE, (c.DAY_OF_WEEK - 1)%7);
//		String begin = format.format(c.getTime());
//		
//		c.setTime(date);
//		c.add(Calendar.DATE, (7 - c.DAY_OF_WEEK)%7);
//		String end = format.format(c.getTime());
//		return new Period(begin, end);
	}
	
	public static Period getMonthOfDate(String theDate) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = format.parse(theDate);
		} catch (ParseException e) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		String begin = format.format(c.getTime());
		
		c.setTime(date);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DATE, -1);
		String end = format.format(c.getTime());
		return new Period(begin, end);
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
			year2  = d2 / 10000;
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
