package com.intellbi.config;

import java.util.regex.Pattern;

public class Constants {

	public static final String CONFIG_FILE = "config.properties";
	
	public static final double CONSUMER_TYPE_VOICE_THEREHOLD = 0.8;
	public static final double CONSUMER_TYPE_GPRS_THEREHOLD = 0.8;
	
	public static final Pattern PATTERN_PHONE_NO = Pattern.compile("^\\d+$");
	
}
