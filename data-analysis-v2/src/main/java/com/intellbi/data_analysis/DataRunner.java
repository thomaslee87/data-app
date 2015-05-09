package com.intellbi.data_analysis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.intellbi.config.ConfigManager;
import com.intellbi.utils.MyDateUtils;

public class DataRunner {
	
	public static final String APP_NAME = "DataRunner";
	
	private static String conf;
	
	public static ConfigManager parseArgs(String[] args) {
		Options options = new Options();
		options.addOption("h", "help", false, "[optional]Show help infomation");
		options.addOption("c", "config", true, "[required]Project config file");
		options.addOption("m", "month", true, "[required]Business data month");
		options.addOption("d", "debug", false, "[optional]Run in debug mode");
		options.addOption("e", "exclude", false, "[optional]Skip month-data in analysis");
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
			conf = cmdArg.getOptionValue("c");
		    config = ConfigManager.getConfigManager(cmdArg.getOptionValue("c"));
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
		if(cmdArg.hasOption("e")) {
			if(config != null) {
				Set<String> excluedMonths = new HashSet<String>();
				String eMonths = cmdArg.getOptionValue("e");
				if(StringUtils.isNotBlank(eMonths)) {
					String[] eMons = eMonths.split(",");
					for(String m: eMons) 
						excluedMonths.add(m.trim());
				}
				config.setExludedMonth(excluedMonths);
			}
		}
		return config;
	}
	
	private static final Logger logger=  Logger.getLogger(DataRunner.class);
	
	public static void main(String[] args) throws IOException {
	    /*BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("log4j.properties"), "utf-8"));
	    String line ;
	    while( (line =br.readLine()) != null) {
	    	System.out.println(line);
	    }
	    System.exit(0);*/
	    
		ConfigManager config = parseArgs(args);
		PropertyConfigurator.configure(conf + "\\log4j.properties");
		System.out.println("log level: " + logger.getLevel());
		logger.setLevel(Level.DEBUG);
		logger.info(StringUtils.join(args, " "));
		if(config != null){
			DataAnalyzer dataAnalyzer = new DataAnalyzer(config);
			dataAnalyzer.run();
		} else {
			System.exit(1);
		}
	}
	
}
