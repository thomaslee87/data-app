package com.intellbi.utils;

public class Utils {
	public static double parseDouble(Object str) {
		double res = 0.0;
		if(str != null) {
			try {
				res = Double.parseDouble(str.toString());
			} catch(NumberFormatException e) {
				res = 0.0;
			}
		}
		return res;
	}
	
	public static int parseInteger(Object str) {
		int res = 0;
		if(str != null) {
			try {
				res = Integer.parseInt(str.toString());
			} catch(NumberFormatException e) {
				res = 0;
			}
		}
		return res;
	}
	public static String parseString(Object str) {
		if(str == null)
			return "";
		return str.toString();
	}
}
