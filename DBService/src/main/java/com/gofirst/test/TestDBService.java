package com.gofirst.test;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.gofirst.service.ConnectService;

public class TestDBService {

	public static void main(String[] args) {
		ConnectService cs = new ConnectService();
		Statement statement  =  cs.getStatement();
		ResultSet result = null;
		try {
			result = statement.executeQuery("select * from sys_user t");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 1;
		 try {
			while(result.next()){
				 ResultSetMetaData rsmd = result.getMetaData();
				 //获取表中列名字
				System.out.println("1: " +": " + rsmd.getColumnName(i++));
				
		
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
