package com.std.framework.model.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Luox ���ݿ��������conn������
 */

public class TransactionHolder {

	private static ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
	private static ThreadLocal<Integer> transStatus = new ThreadLocal<Integer>();
	
	private TransactionHolder() {
	}

	public static void beginConnection() throws SQLException {
		Connection connection = ConnectionsPool.instance().getConnection();
		connection.setAutoCommit(false);
		threadConnection.set(connection);
		transStatus.set(txRunning);
	}

	public static void commitConnection() throws SQLException {
		Connection connection = threadConnection.get();
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				connection.rollback();
			} finally {
				ConnectionsPool.instance().returnConnection(connection);
			}
			threadConnection.remove();
			transStatus.set(txFinished);
		}
	}

	/**
	 * ��ȡ���ݿ����ӳ��е�conn,����autoCommit���Ͳ����뵽ThreadLocal��
	 */
	public static Connection getConn() throws Exception {
		Connection conn = threadConnection.get();
		if (conn == null) {
			conn = ConnectionsPool.instance().getConnection();
			conn.setAutoCommit(true);
		}
		return conn;
	}
	
	/**
	 * �ͷ����ݿ����ӣ������Ƿ�������ִ�е���
	 */
	public static void returnConnection(Connection connection){
		if(transStatus.get() == null || transStatus.get() == txFinished){
			ConnectionsPool.instance().returnConnection(connection);
		}
	}
	
	private static final Integer txRunning = 1;
	private static final Integer txFinished = 0;

}
