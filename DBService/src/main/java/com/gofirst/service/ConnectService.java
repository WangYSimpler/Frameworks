package com.gofirst.service;

import java.sql.*;
import java.sql.Statement;
import java.util.*;

import com.gofirst.utils.Bean2Map;
import com.gofirst.utils.PropertiesUtils;

public class ConnectSerivce {

	/* 数据库连接池 */
	private Connection conn = null;
	/* 数据库会话 */
	private Statement stmt = null;
	/* 结果集 */
	private ResultSet rs;

	/* 数据库连接参数 */
	private String dbDriver = "";
	private String url = "";// 简单写法：url
	private String user = "";
	private String password = "";

	
	
	
	
	/***
	 * 构造方法里面读取数据库配置，并且赋值
	 */
	public ConnectSerivce() {
		PropertiesUtils prop = new PropertiesUtils();
		this.dbDriver = prop.getDBDriver();
		this.url = prop.getDBURL();
		this.user = prop.getDBUserName();
		this.password = prop.getDBPassword();
	}

	/**
	 * 建立数据库连接池赋值
	 */
	private void init() {
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
	 * 返回 数据库连接
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		this.init();
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
	 * 执行数据查询保证返回查询对象
	 * 
	 * @param sqlStr
	 * @param T
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T exeQuery(String sqlStr, Class<?> T) {
		return (T) this.exeQueryObjet(sqlStr, T);
	}

	/**
	 * 
	 * @param sql  执行sql
	 *           
	 * @return
	 */
	private Object exeQueryObjet(String sqlStr, Class<?> beanClass) {

		this.init();

		Object obj = null;
		String sql = sqlStr;
		try {
			stmt = this.getStatement();
			rs = stmt.executeQuery(sql);// 执行sql语句

			if (rs.next()) {

				rs = stmt.executeQuery(sql);
				Map<String, Object> rsMap = this.resultSetToMap(rs);
				obj = Bean2Map.mapToObject(rsMap, beanClass);
			}

		} catch (SQLException e) {
			System.out.println("数据操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	
	/**
	 * 查詢 所有結果对象List
	 * @param sqlStr
	 * @param T
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	public <T> List<T> exeQueryAll(String sqlStr, Class T) {

		return Bean2Map.convertListMap2ListBean(this.exeQueryAllObject(sqlStr, T), T);
	}

	public List<Object> exeQueryAllObject(String sqlStr, Class<?> beanClass) {

		this.init();

		List<Object> listObject = new ArrayList<Object>();
		String sql = sqlStr;

		try {

			stmt = this.getStatement();
			rs = stmt.executeQuery(sql);// 执行sql语句
			/// ResultSet resultSet =rs;
			if (rs.next()) {
				ResultSet resultSet = stmt.executeQuery(sql);
				List<Map<String, Object>> rsMap = this.resultSetToListMap(resultSet);
				for (Map<String, Object> map : rsMap) {
					Object obj = Bean2Map.mapToObject(map, beanClass);
					listObject.add(obj);
				}
			}

		} catch (SQLException e) {
			System.out.println("数据操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listObject;
	}

	/**
	 * 结果装换对应Map 
	 * @param resultSet
	 * @return
	 */
	private Map<String, Object> resultSetToMap(ResultSet resultSet) {

		ResultSet rs = resultSet;
		Map<String, Object> rowData = new HashMap<String, Object>();
		try {
			ResultSetMetaData md = rs.getMetaData(); // 获得结果集结构信息,元数据
			int columnCount = md.getColumnCount(); // 获得列数
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rowData;
	}

	/**
	 * 返回ListMap
	 * 
	 * @param resultSet
	 * @return
	 */
	private List<Map<String, Object>> resultSetToListMap(ResultSet resultSet) {

		ResultSet rs = resultSet;
		List<Map<String, Object>> ListData = new ArrayList<Map<String, Object>>();
		try {
			ResultSetMetaData md = rs.getMetaData(); // 获得结果集结构信息,元数据
			int columnCount = md.getColumnCount(); // 获得列数
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				ListData.add(rowData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ListData;
	}

	
	
	/* 执行更新sql， 有个更新条数返回真*/
	public boolean exeUpdate(String sql) {
		this.init();
		boolean flag = false;

		try {
			if (this.getStatement().executeUpdate(sql) > 0) {
				flag = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
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
	 * 
	 * @param updateOrInsertStatement
	 *            statement
	 * @param tableNameStr
	 *            表名
	 * @param pkStr
	 *            主键
	 * @param objColumnsMap
	 *            map
	 */
	public void updateOrInsertRecords(Statement updateOrInsertStatement, String tableNameStr, String pkStr,
			Map<String, Object> objColumnsMap) {
		// 表名
		String tableName = tableNameStr;
		/// 主键名
		String pk = pkStr;
		Map<String, Object> objColumns = objColumnsMap;
		Statement updateOrInsertstmt = updateOrInsertStatement;

		String sqlStr = "";
		sqlStr = this.getUpdateSqlStr(tableName, pk, objColumns);
		/// 初始化，建立数据库连接

		/// 该操作先执行更新，如果更新数目为0，然后执行Insert语句
		try {

			int updateCounts = updateOrInsertstmt.executeUpdate(sqlStr);

			if (0 == updateCounts) {
				sqlStr = this.getInsertSqlStr(tableName, objColumns);
				// System.out.println("=== 插入sql====");
				// System.out.println( "***:" + sqlStr);
				updateOrInsertstmt.executeUpdate(sqlStr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 *  通过Map转换成Insert 语句
	 * @param tableNameStr   表名
	 * @param objColumnsMap     Map
	 * @return 插入表中数据 de sql
	 */
	public String getInsertSqlStr(String tableNameStr, Map<String, Object> objColumnsMap) {

		//// 表名
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

				if (null != entry.getValue()) {
					valueStr = this.getCurrectValueStr(entry.getKey(), entry.getValue().toString());

				}
				columnsValues += valueStr + ",";
			}

			columnsNameStr = columnsNameStr.substring(0, columnsNameStr.length() - 1) + " )";
			columnsValues = columnsValues.substring(0, columnsValues.length() - 1) + " )";
			insertSql += columnsNameStr + columnsValues;
		}

		return insertSql;
	}

	/**
	 *  获取更新SQL 语句
	 * @param tableNameStr      表名
	 * @param pkStr    主键
	 * @param objColumnsMap   map
	 * @return
	 */
	public String getUpdateSqlStr(String tableNameStr, String pkStr, Map<String, Object> objColumnsMap) {

		String tableName = tableNameStr;
		String pk = pkStr;
		Map<String, Object> objColumns = objColumnsMap;
		String updateSql = "";
		if (tableName != null && (!"".equals(tableName)) && pk != null && (!"".equals(pk)) && objColumns.size() > 0) {
			updateSql += "update " + tableName + " set ";
			String columnsSetStr = " ";
			for (Map.Entry<String, Object> entry : objColumns.entrySet()) {

				String valueStr = null;

				if (null != entry.getValue()) {
					valueStr = this.getCurrectValueStr(entry.getKey(), entry.getValue().toString());
				}

				columnsSetStr += entry.getKey() + "=" + valueStr + ",";
			}

			columnsSetStr = columnsSetStr.substring(0, columnsSetStr.length() - 1) + " where " + pk + "='"
					+ objColumns.get(pk).toString() + "'";

			updateSql += columnsSetStr;
		}

		return updateSql;

	}

	/**
	 * 将表中日期的格式进行转换
	 * @param keyStr
	 * @param valueStr
	 * @return
	 */
	public String getCurrectValueStr(String keyStr, String valueStr) {
		String key = keyStr, value = valueStr;

		String resultStr = null;
		if (null != value) {
			resultStr = "'" + value + "'";
			if (key.matches("(.*)TIME") || key.matches("(.*)DATE") || key.matches("BIRTHDAY")) {
				resultStr = this.getDBDateStr(key, resultStr);
			}
		}
		return resultStr;
	}

	/**
	 * 装成成数据日期格式
	 * @param keyStr
	 *            map 的 key值
	 * @param valueStr
	 *            key对应的value值
	 * @return
	 */
	public String getDBDateStr(String keyStr, String valueStr) {
		String key = keyStr, value = valueStr;

		String resultStr = null;

		if (key.matches("(.*)TIME") || key.matches("(.*)DATE")) {
			value = value.toString().replace("T", " ");
			resultStr = "to_date(" + value + ",'" + "yyyy-mm-dd hh24:mi:ss" + "')";
		}

		return resultStr;
	}
	
	/**
	 * 关闭数据库连接
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private void closedb() {

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

	}

	

	

	

}