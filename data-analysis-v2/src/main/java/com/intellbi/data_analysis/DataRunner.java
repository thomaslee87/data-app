package com.intellbi.data_analysis;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.PropertyConfigurator;

import com.intellbi.config.ConfigManager;
import com.intellbi.utils.MyDateUtils;

public class DataRunner {
	
	public static final String APP_NAME = "DataRunner";
	
	public static ConfigManager parseArgs(String[] args) {
		Options options = new Options();
		options.addOption("h", "help", false, "[optional]Show help infomation");
		options.addOption("c", "config", true, "[required]Project config file");
		options.addOption("m", "month", true, "[required]Business data month");
		options.addOption("d", "debug", false, "[optional]Run in debug mode");
		CommandLineParser parser = new PosixParser();
		CommandLine cmdArg = null;
		ConfigManager config = null;
		try {
			cmdArg = parser.parse(options, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cmdArg.hasOption("h") || !cmdArg.hasOption("c") || !cmdArg.hasOption("m")) {
			HelpFormatter helpFmt = new HelpFormatter();
			helpFmt.printHelp(APP_NAME, options);
		}
		if(cmdArg.hasOption("c")) {
			// 加载配置文件
		    config = ConfigManager.getInstance(cmdArg.getOptionValue("c"));
//			config = new ConfigManager(cmdArg.getOptionValue("c"));
		}
		if(cmdArg.hasOption("m")) {
			String bizmonth = cmdArg.getOptionValue("m");
			if(!MyDateUtils.checkMonthFormat(bizmonth, "yyyyMM")) 
				config = null;
			if(config != null)
				config.put("bizmonth", cmdArg.getOptionValue("m"));
		}
		if(cmdArg.hasOption("d")) {
			if(config != null)
				config.setDebug(true);
		}
		return config;
	}

	public static void main(String[] args) {
	    
	    PropertyConfigurator.configure(ClassLoader.getSystemResource("src/main/resources/log4j.properties"));
	    
		ConfigManager config = parseArgs(args);
		if(config != null){
			DataAnalyzer dataAnalyzer = new DataAnalyzer(config);
			dataAnalyzer.run();
		} else {
			System.exit(1);
		}
	}
	
}
