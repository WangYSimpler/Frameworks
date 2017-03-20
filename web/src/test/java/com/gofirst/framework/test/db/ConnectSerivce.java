package com.gofirst.framework.test.db;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.gofirst.framework.utils.PropertiesUtils;

public class ConnectSerivce {

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;
	
/*	private String dbDriver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";// 简单写法：url
	private String user = "footsys";
	private String password = "footsys";*/

	private String dbDriver = "";
	private String url = "";// 简单写法：url
	private String user = "";
	private String password = "";
	/***
	 * 数据库初始化
	 */
	public ConnectSerivce()
	{
		PropertiesUtils prop = new PropertiesUtils();
		this.dbDriver = prop.getDBDriver();
		this.url      = prop.getDBURL();
		this.user     = prop.getDBUserName();
		this.password = prop.getDBPassword();
	}
	
	private void Init() {
		try {
			Class.forName(dbDriver); 
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载错误");
			e.printStackTrace();// 打印出错详细信息
		}

		try {
			conn = DriverManager.getConnection(url, user, password);
			// newConn = DriverManager.getConnection(urlNew, user, password);
		} catch (SQLException e) {
			System.out.println("数据库链接错误");
			e.printStackTrace();
		}

	}

	/**
	 * 返回 连接
	 * @return Connection
	 */
	public Connection getConnection()
	{
		this.Init();
		return conn;
	}
	
	/**
	 *  
	 * @return 获取Statememt
	 */
	public Statement getStatement() {
		
		try {
			stmt = this.getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
		
	}
	/**
	 * 
	 * @param sql  执行sql
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean exeQuery(String sqlStr) {

		String sql = sqlStr;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);// 执行sql语句
			
			if (rs.next()) {
				System.out.println(rs);
			}

		} catch (SQLException e) {
			System.out.println("数据操作错误");
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param sqlStr
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean haveRecord(String sqlStr) {
		
		String sql = sqlStr;
		boolean haveRecordFlag = false;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);// 执行sql语句
		
			if (rs.next()) {
				haveRecordFlag = true;
			}

		} catch (SQLException e) {
			System.out.println("数据操作错误");
			e.printStackTrace();

		}
		return haveRecordFlag;
	}

	/**
	 * 关闭数据库连接
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean closedb() {
		
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}

		} catch (Exception e) {
			System.out.println("数据库关闭错误");
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 
	 * @param updateOrInsertStatement statement
	 * @param tableNameStr 表名
	 * @param pkStr 主键
	 * @param objColumnsMap map
	 */
	public void updateOrInsertRecords(Statement updateOrInsertStatement,String tableNameStr,String pkStr,Map<String,Object> objColumnsMap)
	{
		//表名
		String tableName = tableNameStr;
		///主键名
		String pk = pkStr;
		Map<String, Object> objColumns = objColumnsMap;
		Statement updateOrInsertstmt = updateOrInsertStatement;
		
		String sqlStr = "";
		sqlStr = this.getUpdateSqlStr(tableName,pk,objColumns);
		///初始化，建立数据库连接
		
		///该操作先执行更新，如果更新数目为0，然后执行Insert语句
		try {
			
			
			int updateCounts = updateOrInsertstmt.executeUpdate(sqlStr);
			
			if ( 0 == updateCounts ) {
				sqlStr = this.getInsertSqlStr(tableName, objColumns);
//				System.out.println("=== 插入sql====");
//				System.out.println( "***:" + sqlStr);
				updateOrInsertstmt.executeUpdate(sqlStr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param tableNameStr 表名
	 * @param objColumnsMap Map
	 * @return 插入表中数据 de sql
	 */
	public String getInsertSqlStr(String tableNameStr,Map<String,Object> objColumnsMap) {
		
		////表名
		String tableName = tableNameStr;
		String insertSql = "";
		Map<String, Object> objColumns = objColumnsMap;
		if (tableName != null && !("").equals(tableName) && (objColumns.size() > 0)) {
			/// 插入语句头
			insertSql = "insert into " + tableName;
			String columnsNameStr = " (";
			String columnsValues = " values (";
			for (Map.Entry<String, Object> entry : objColumns.entrySet()) {
				columnsNameStr += entry.getKey() + ",";
				String valueStr = null;
				
				if (null != entry.getValue() ) {
					/*valueStr = "'"+ entry.getValue() + "'";
				
					if (entry.getKey().matches("(.*)TIME")||entry.getKey().matches("(.*)DATE") || entry.getKey().matches("BIRTHDAY")) {
						valueStr = entry.getValue().toString().replace("T", " ");
						valueStr = "to_date('" + valueStr +"','"+"yyyy-mm-dd hh24:mi:ss"+"')";
					}*/
					
					valueStr = this.getCurrectValueStr(entry.getKey(), entry.getValue().toString());
					
				}
				columnsValues += valueStr +",";
			}
			
			columnsNameStr = columnsNameStr.substring(0, columnsNameStr.length()-1) +" )";
			columnsValues  = columnsValues.substring(0, columnsValues.length()-1)  + " )";
			insertSql += columnsNameStr + columnsValues;		
		}

		return insertSql;
	}
	
	/**
	 * 
	 * @param tableNameStr 表名
	 * @param pkStr 主键
	 * @param objColumnsMap map
	 * @return
	 */
	public String getUpdateSqlStr(String tableNameStr,String pkStr,Map<String,Object> objColumnsMap) {
		
		String tableName = tableNameStr;
		String pk = pkStr;
		Map<String, Object> objColumns = objColumnsMap;
		String updateSql = "";
		if (tableName!= null && (!"".equals(tableName))&& pk!= null && (!"".equals(pk))&&objColumns.size()>0  ) {
			updateSql += "update " + tableName + " set ";
			String columnsSetStr = " ";
			for (Map.Entry<String, Object> entry : objColumns.entrySet()) {
				
				String valueStr = null;
			
				/**
				 * if (null != entry.getValue() ) {
					valueStr = "'"+ entry.getValue() + "'";
				
					if (entry.getKey().matches("(.*)TIME")||entry.getKey().matches("(.*)DATE")) {
						valueStr = entry.getValue().toString().replace("T", " ");
						valueStr = "to_date('" + valueStr +"','"+"yyyy-mm-dd hh24:mi:ss"+"')";
					}
				}*/
				
				if (null != entry.getValue()){
					valueStr = this.getCurrectValueStr(entry.getKey(), entry.getValue().toString());
				}
				
				columnsSetStr +=  entry.getKey()  + "=" +valueStr +",";
			}
			
			columnsSetStr = columnsSetStr.substring(0, columnsSetStr.length()-1) + " where " + pk + "='" + objColumns.get(pk).toString()+"'";
			
			updateSql += columnsSetStr;
		}
		
		return updateSql;
		
	}	
		
	public String getCurrectValueStr(String keyStr,String valueStr)	{
		String key = keyStr,value = valueStr;
		
		String resultStr = null;
		if (null != value ) {
			resultStr = "'"+ value + "'";
			if (key.matches("(.*)TIME") || key.matches("(.*)DATE")|| key.matches("BIRTHDAY")) {
			  resultStr = this.getCurrectDateStr(key, resultStr);
			}
		}
		return resultStr;
	}
	
	/**
	 * 
	 * @param keyStr map 的 key值
	 * @param valueStr key对应的value值
	 * @return
	 */
	public String getCurrectDateStr(String keyStr,String valueStr)
	{
		String key = keyStr,value = valueStr;
		
		String resultStr = null;

		if (key.matches("(.*)TIME") || key.matches("(.*)DATE")) {
			value = value.toString().replace("T", " ");
			resultStr = "to_date(" + value + ",'" + "yyyy-mm-dd hh24:mi:ss" + "')";
		}
			
		return resultStr;
	}
	
	public static void main(String[] args) {

		Map<String, Object> testMap = new HashMap<String, Object>();
		testMap.put("id", "6");
		testMap.put("name", "B");
		ConnectSerivce cs = new ConnectSerivce();
		Statement statement1 = cs.getStatement();
		cs.updateOrInsertRecords(statement1,"T_Test", "id", testMap);
	}

}