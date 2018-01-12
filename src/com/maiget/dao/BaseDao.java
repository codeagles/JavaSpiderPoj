/**
 * 
 */
package com.maiget.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author codeagles
 * 数据库连接基础层
 */
public class BaseDao {
	protected static Connection conn = null;
	protected static Statement stmt = null;
	String url = "jdbc:mysql://123.59.100.119:3306/mfu_toutiao?user=houcn&password=houcn@2017)()&characterEncoding=UTF-8";
//	String url = "jdbc:mysql://127.0.0.1:3306/newspsider?user=root&password=root&characterEncoding=UTF-8";

	public BaseDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=getConnection();
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public Connection getConnection(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	// 关闭连接
	public static void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn = null;
		}
	}
}
