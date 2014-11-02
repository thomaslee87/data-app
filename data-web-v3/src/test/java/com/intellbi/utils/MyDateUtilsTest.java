package com.intellbi.utils;

import org.junit.Assert;
import org.junit.Test;

public class MyDateUtilsTest {
	
	@Test
    public void testGetDateByDeltaDays1() {
		String date = MyDateUtils.getDateByDeltaDays("20140901", 30);
		Assert.assertEquals(date, "20141001");
    }
	
	@Test
    public void testGetDateByDeltaDays2() {
		String date = MyDateUtils.getDateByDeltaDays("20141001", 30);
		Assert.assertEquals(date, "20141031");
    }
	
	@Test
    public void testGetDateByDeltaDays3() {
		String date = MyDateUtils.getDateByDeltaDays("20141001", 90);
		Assert.assertEquals(date, "20141230");
    }
	
	@Test
	public void testGetWeekOfDate1() {
		String date = "20141101";
		String start = MyDateUtils.getWeekOfDate(date).getBegin();
		String end = MyDateUtils.getWeekOfDate(date).getEnd();
		Assert.assertEquals(start, "20141027");
		Assert.assertEquals(end, "20141102");
	}
	
	@Test
	public void testGetWeekOfDate2() {
		String date = "20140302";
		String start = MyDateUtils.getWeekOfDate(date).getBegin();
		String end = MyDateUtils.getWeekOfDate(date).getEnd();
		Assert.assertEquals(start, "20140224");
		Assert.assertEquals(end, "20140302");
	}
	
	@Test
	public void testGetMonthOfDate1() {
		String date = "20140212";
		String start = MyDateUtils.getMonthOfDate(date).getBegin();
		String end = MyDateUtils.getMonthOfDate(date).getEnd();
		Assert.assertEquals(start, "20140201");
		Assert.assertEquals(end, "20140228");
	}
	
}