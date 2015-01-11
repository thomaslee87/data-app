package com.intellbi.utils;

import org.apache.commons.lang3.StringUtils;

public class Tools {

	public static int parseInt(String s) {
		if(StringUtils.isBlank(s))
			return 0;
		int ret = 0 ;
		try {
			ret = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
		return ret;
	}
}
