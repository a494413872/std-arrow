package com.std.framework.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.std.framework.model.connection.TransactionHolder;
import com.std.framework.model.ormap.MappingRule;

/**
 * @author Luox SQL�Զ������������
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ModelHelper {
	
	/**
	 * ��Sql���ҵĽ����ŵ���T�У�ӳ���ϵ��Ormmap�д洢��Ϊ׼
	 */
	public static <T> List<T> findDIYClassListBySql(T t, MappingRule maprule ,String sql, List parameters) {
		Class<T> c = (Class<T>) t.getClass();
		ResultSet rs = null;
		try {
			rs = excuteQuerySql(sql, parameters);
			return ModelBuilder.buildClassResult(rs, c, maprule);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.getStatement().close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList();
	}
	
	/**
	 * ��Sql���ҵĽ����ŵ���T�У�ӳ���ϵ��Ormmap�д洢��Ϊ׼
	 */
	public static <T> List<T> findORMClassListBySql(T t, String sql, List parameters) {
		Class<T> c = (Class<T>) t.getClass();
		ModelBuilder.checkOrm(c);
		ResultSet rs = null;
		try {
			rs = excuteQuerySql(sql, parameters);
			return ModelBuilder.buildClassResult(rs, c);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.getStatement().close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList();
	}

	/**
	 * ��Sql���ҵĽ����ŵ�List<Map>�У�
	 */
	public static List findMapListBySql(String sql, List parameters) {
		ResultSet rs = null;
		try {
			rs = excuteQuerySql(sql, parameters);
			return ModelBuilder.buildMapResult(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.getStatement().close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList();
	}

	/**
	 * ִ��DML���
	 */
	public static <T> boolean excuteDMLSql(String sql, List parameters) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = TransactionHolder.getConn();
			pstmt = conn.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					pstmt.setObject(i + 1, parameters.get(i));
				}
			}
			return pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			pstmt.close();
			TransactionHolder.returnConnection(conn);
		}
		return true;
	}

	private static ResultSet excuteQuerySql(String sql, List parameters) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = TransactionHolder.getConn();
			pstmt = conn.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					pstmt.setObject(i + 1, parameters.get(i));
				}
			}
			rs = pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		}finally{
			TransactionHolder.returnConnection(conn);
		}
		return rs;
	}

}
