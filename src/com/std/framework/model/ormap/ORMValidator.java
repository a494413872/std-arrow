package com.std.framework.model.ormap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.std.framework.model.connection.ConnectionsPool;

/**
 * @author Luox ORMMAPINGУ��������֤�����У��ֶ��Ƿ������ݿ��д���
 */
public class ORMValidator {

	private Map<String, String> tabMap = null;
	private Map<String, String> colMap = null;
	private Map<String, String> seqMap = null;

	private void initMaps(Obj2TabContainer obj2Tab) {
		this.tabMap = obj2Tab.getTabMap();
		this.seqMap = obj2Tab.getSeqMap();
		this.colMap = obj2Tab.getColMap();
	}

	/**
	 * ��ͨ���ݿ��USER_TABLES,USER_SEQUENCES,USER_TAB_COLUMNS���жϱ����� ���кͱ����������Ƿ���ȷ��
	 */
	public boolean validOrmmap(Obj2TabContainer obj2Tab) throws Exception {
		try {
			initMaps(obj2Tab);
			validTab(tabMap);
			validSeq(seqMap);
			validCol(tabMap, colMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void validTab(Map<String, String> tabMap) throws Exception {
		Connection conn = ConnectionsPool.instance().getConnection();
		// �齨����Sql
		StringBuilder testSql = new StringBuilder();
		testSql.append(" SELECT * FROM USER_TABLES T WHERE T.TABLE_NAME = '");
		Iterator<String> tabName = tabMap.values().iterator();
		String tableName = "";
		if (tabName.hasNext()) {
			tableName = tabName.next();
			testSql.append(tableName);
		} else {
			throw new Exception("�ñ���ORMMAPINGӳ���ϵʧ��!");
		}
		testSql.append("'");
		// ִ�в���Sql
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(testSql.toString());
			if (!rs.next()) {
				throw new Exception("��" + tableName + "��������!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
		}
		ConnectionsPool.instance().returnConnection(conn);
	}

	private void validSeq(Map<String, String> seqMap) throws Exception {
		Connection conn = ConnectionsPool.instance().getConnection();
		// �齨����Sql
		StringBuilder testSql = new StringBuilder();
		testSql.append(" SELECT * FROM USER_SEQUENCES T WHERE T.SEQUENCE_NAME = '");
		Iterator<String> seqName = seqMap.values().iterator();
		String sequenceName = "";
		if (seqName.hasNext()) {
			sequenceName = seqName.next();
			testSql.append(sequenceName);
		} else {
			throw new Exception("�ñ���ORMMAPINGӳ���ϵʧ��!");
		}
		testSql.append("'");
		// ִ�в���Sql
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(testSql.toString());
			if (!rs.next()) {
				throw new Exception("����" + sequenceName + "��������!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
		}
		ConnectionsPool.instance().returnConnection(conn);
	}

	private void validCol(Map<String, String> tabMap, Map<String, String> colMap) throws Exception {
		Connection conn = ConnectionsPool.instance().getConnection();
		// �齨����Sql
		StringBuilder testSql = new StringBuilder();
		testSql.append(" SELECT * FROM USER_TAB_COLUMNS T WHERE T.TABLE_NAME = '");
		Iterator<String> tabName = tabMap.values().iterator();
		String tableName = "";
		if (tabName.hasNext()) {
			tableName = tabName.next();
			testSql.append(tableName);
		} else {
			throw new Exception("�ñ���ORMMAPINGӳ���ϵʧ��!");
		}
		testSql.append("'");
		// ִ�в���Sql
		List<String> colArray = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(testSql.toString());
			while (rs.next()) {
				colArray.add(rs.getString("COLUMN_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
		}

		Iterator<String> colIterator = colMap.values().iterator();
		while (colIterator.hasNext()) {
			String colName = colIterator.next();
			if (!colArray.contains(colName)) {
				throw new Exception("��" + tableName + "��û���ҵ�����Ϊ" + colName + "����!");
			}
		}
		ConnectionsPool.instance().returnConnection(conn);
	}

}
