/**
 * 
 */
package com.intellbi.utils;

/**
 * @author lizheng 20140503
 *
 */
public class FileUtils {
	/**
	 * @return project absolute path
	 */
	public static String getProjectPath () {
		String clzPath = FileUtils.class.getClassLoader().getResource("").getPath();
		return clzPath + "../../";
	}
}
