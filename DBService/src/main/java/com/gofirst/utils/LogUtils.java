/**
 * @file   LogUtils.java
 * @author WangY
 * @Date  2017年3月10日
 */
package com.gofirst.utils;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 *
 * @author WangY
 * @Date 2017年3月10日
 */
public class LogUtils {

	/**
	 * @param args
	 */
	static Logger log = Logger.getLogger(LogUtils.class.getName());

	
	public static Logger getLogHandle()
	{
		return LogUtils.log;
	}
	
	
	public static void main(String[] args) throws IOException, SQLException {

		log.debug("Hello this is an debug message");
		log.info("Hello this is an info message");
	}

}
