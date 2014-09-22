package com.intellbi.utils;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void test() {
		String d = MyDateUtils.getMonthByDelta("201401", -1);
		assert(d.equals("20131201"));
	}

}
