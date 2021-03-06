package com.std.framework.model.orm.dialact.oracle;

import com.std.framework.model.connection.ConnectionsPool;
import com.std.framework.model.orm.ORMValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/28.
 */
public class OracleValidator extends ORMValidator {

    public boolean validTab(String tableName) throws Exception {
        Connection conn = ConnectionsPool.instance().applyConnection();
        // 组建测试Sql
        StringBuilder testSql = new StringBuilder();
        testSql.append(" SELECT * FROM USER_TABLES T WHERE T.TABLE_NAME = '");
        testSql.append(tableName);
        testSql.append("'");
        // 执行测试Sql
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(testSql.toString());
            if (!rs.next()) {
                throw new Exception("表" + tableName + "并不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
        }
        ConnectionsPool.instance().releaseConnection(conn);
        return true;
    }

    public boolean validSeq(Map<String, String> seqMap) throws Exception {
        Connection conn = ConnectionsPool.instance().applyConnection();
        // 组建测试Sql
        StringBuilder testSql = new StringBuilder();
        testSql.append(" SELECT * FROM USER_SEQUENCES T WHERE T.SEQUENCE_NAME = '");
        Iterator<String> seqName = seqMap.values().iterator();
        String sequenceName = "";
        if (seqName.hasNext()) {
            sequenceName = seqName.next();
            testSql.append(sequenceName);
        }
        testSql.append("'");
        // 执行测试Sql
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(testSql.toString());
            if (!rs.next()) {
                throw new Exception("序列" + sequenceName + "并不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
        }
        ConnectionsPool.instance().releaseConnection(conn);
        return true;
    }

    public boolean validCol(String tableName, Map<String, String> colMap) throws Exception {
        Connection conn = ConnectionsPool.instance().applyConnection();
        // 组建测试Sql
        StringBuilder testSql = new StringBuilder();
        testSql.append(" SELECT * FROM USER_TAB_COLUMNS T WHERE T.TABLE_NAME = '");
        testSql.append(tableName);
        testSql.append("'");
        // 执行测试Sql
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
                throw new Exception("表" + tableName + "中没有找到列名为" + colName + "的列!");
            }
        }
        ConnectionsPool.instance().releaseConnection(conn);
        return true;
    }
}
