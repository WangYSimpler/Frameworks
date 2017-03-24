/**
 * @file   PropertiesUtils.java
 * @author WangY
 * @Date  2017年3月8日
 */
package com.gofirst.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



/**
 *
 *@author  WangY
 *@Date    2017年3月8日 
 */
public class PropertiesUtils {
	
		public Properties getProperties()
		{
			//String sourcePathStr = new File(this.getClass().getResource("/").getPath().toString()).getParentFile().toString() +"/bin/config.properties";
			String sourcePathStr = new File(this.getClass().getResource("/").getPath().toString()).toString() +"/config.properties";
			////Test 王勇 20170309
			//System.out.println(sourcePathStr);
			Properties properties = new Properties();
			
			try {
				properties.load(new FileInputStream(sourcePathStr));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      /*  Enumeration<?> enum1 = properties.propertyNames();//得到配置文件的名字
	        while(enum1.hasMoreElements()) {
	            String strKey = (String) enum1.nextElement();
	            String strValue = properties.getProperty(strKey);
	            System.out.println(strKey + "=" + strValue);
	        }*/
		
	       /// System.out.println(properties.getProperty("FOOTSYS.jdbc.driverClassName"));
			return properties;
		}
		
		
		
		public String getPropertiesValue(Properties properties,String propertyNameStr )
		{
			Properties prop = properties;
			String propertyName =  propertyNameStr;
			
			return prop.getProperty(propertyName);
		}
	
		public String getDBDriver()
		{
			return this.getPropertiesValue(this.getProperties(), "jdbc.driverClassName");
		}
		
		
		public String getDBURL()
		{
			return this.getPropertiesValue(this.getProperties(), "jdbc.url");
		}
	
		
		public String getDBUserName()
		{
			return this.getPropertiesValue(this.getProperties(), "jdbc.username");
		}
		
		
		public String getDBPassword()
		{
			return this.getPropertiesValue(this.getProperties(), "jdbc.password");
		}
		
		public String getBaseURL()
		{
			return this.getPropertiesValue(this.getProperties(), "baseUrl");
		}
		
		public String getBaseConfigInfo()
		{
			return this.getPropertiesValue(this.getProperties(), "configInfo");
		}
		
		public String getBaseSyncTimeInterval ()
		{
			return this.getPropertiesValue(this.getProperties(), "syncTimeInterval");
		}
		
		
		
	    public static void main(String[] args) throws FileNotFoundException, IOException {
	    	PropertiesUtils propertiesUtils = new PropertiesUtils() ;
	    	System.out.println(propertiesUtils.getDBDriver()+":" +propertiesUtils.getDBDriver() +":" +propertiesUtils.getDBUserName()+":"+ propertiesUtils.getDBPassword());
	    	System.out.println(propertiesUtils.getBaseURL()+":" +propertiesUtils.getBaseConfigInfo());
	    	
	     
	    }



		/**
		 * 
		 */
		
	
}
