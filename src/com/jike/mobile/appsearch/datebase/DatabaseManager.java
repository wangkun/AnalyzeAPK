package com.jike.mobile.appsearch.datebase;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	private static final Logger logger = LogManager
			.getLogger(DatabaseManager.class);
	private String m_szDriverName = null;
	private String m_szConnectionString = null;
	private String m_szUserName = null;
	private String m_szPassword = null;
	private Connection m_pConnection = null;
	private Statement m_pStatement = null;

	public String getPassword() {
		return m_szPassword;
	}

	public void setPassword(String passWord) {
		m_szPassword = passWord;
	}

	public String getUserName() {
		return m_szUserName;
	}

	public void setUserName(String userName) {
		m_szUserName = userName;
	}

	public String getConnectionString() {
		return m_szConnectionString;
	}

	public void setConnectionString(String connectionString) {
		m_szConnectionString = connectionString;
	}

	public String getDriverName() {
		return m_szDriverName;
	}

	public void setDriverName(final String driverName) {
		m_szDriverName = driverName;
	}

	public int connectDatabase() {
		try {
			Class.forName(m_szDriverName);
			m_pConnection = DriverManager.getConnection(m_szConnectionString,
					m_szUserName, m_szPassword);
			m_pStatement = m_pConnection.createStatement();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException :" + e.getMessage());
			logger.error("ClassNotFoundException :" + e.getMessage());
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("SQLException :" + e.getMessage());
			logger.error("SQLException :" + e.getMessage());
			return -1;
		}

		return 1;

	}

	public Connection getConnection() {
        return m_pConnection;
    }

    public void setConnection(Connection m_pConnection) {
        this.m_pConnection = m_pConnection;
    }

    public int checkTableExist(String tableName) {
		if (tableName == null) {
			System.err.println("Parameter is error in function execuetQuery");
			logger.error("Parameter is error in function execuetQuery");
			return -1;
		}

		if (m_pStatement == null) {
			System.err
					.println("Database Statement is NULL,can not execute Query");
			logger.error("Database Statement is NULL,can not execute Query");
			return -1;
		}

		try {
			m_pStatement.executeQuery("select count(*) from " + tableName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
		}
		return 1;
	}

	public ResultSet executeQuery(String queryString) {
		if (queryString == null) {
			System.err.println("Parameter is error in function execuetQuery");
			logger.error("Parameter is error in function execuetQuery");
			return null;
		}

		if (m_pStatement == null) {
			System.err
					.println("Database Statement is NULL,can not execute Query");
			logger.error("Database Statement is NULL,can not execute Query");
			return null;
		}

		ResultSet rs = null;
		try {
			rs = m_pStatement.executeQuery(queryString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("SQLException :" + e.getMessage());
			logger.error("SQLException :" + e.getMessage());
			return null;
		}
		return rs;
	}


	public int execute(String executeString) {
		if (executeString == null) {
			System.err.println("Parameter is error in function execute");
			logger.error("Parameter is error in function execute");
			return -1;
		}

		if (m_pStatement == null) {
			System.err
					.println("Database Statement is NULL,can not execute Handle");
			logger.error("Database Statement is NULL,can not execute Handle");
			return -1;
		}

		try {
			return m_pStatement.execute(executeString) == true ? 1 : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("SQLException :" + e.getMessage());
			logger.error("SQLException :" + e.getMessage());
			return -1;
		}
	}

	public int closeDatabase() {
		try {
			if (m_pStatement != null)
				m_pStatement.close();

			if (m_pConnection != null)
				m_pConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("SQLException :" + e.getMessage());
			logger.error("SQLException :" + e.getMessage());
			return -1;
		}
		return 1;
	}

}
