package com.intellbi.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Properties;

import com.intellbit.v2.exception.PropertyParseException;

public class PropertyFileUtil {
	
	public static Properties loadProperties(File... locations) throws PropertyParseException {
		Properties props = new Properties();
		loadProperties(props, locations);
		return props;
	}

	private static void loadProperties(Properties props, File... locations) throws PropertyParseException {
		try {
			for (File location : locations) {
				Properties prop = new Properties();
				if (location.isDirectory()) {
					File[] files = location.listFiles();
					loadProperties(props, files);
				} else {
					prop.load(new InputStreamReader(new FileInputStream(location),"utf-8"));	
				}
				for (Object key : prop.keySet()) {
					if(key == null || props.containsKey(key.toString()))
						throw new PropertyParseException(MessageFormat.format("Property Key Duplication: {0}", key));
					props.put(key, prop.get(key));
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
		}
	}
}
