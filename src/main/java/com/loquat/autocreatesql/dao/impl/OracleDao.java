/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.loquat.autocreatesql.dao.AbstractDao;

/**
 *
 * @author Administrator
 */
public class OracleDao extends AbstractDao {

    public OracleDao(String driver, String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        super(driver, url, user, pwd);
    }

    public OracleDao(String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        super("oracle.jdbc.driver.OracleDriver", url, user, pwd);
    }

    @Override
    public String[][] getTableValues(String table,Connection conn) {
        try {
            String sql = "select * from " + table;
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            ResultSetMetaData r = rs.getMetaData();
            int length = r.getColumnCount();
            String[][] tableVales = new String[length][]; //数据
            //找到comment
            PreparedStatement ps = conn.prepareStatement("SELECT t1.COLUMN_NAME, t2.COMMENTS  FROM user_tab_columns t1,"
                    + " user_col_comments t2 WHERE t1.TABLE_NAME = ? "
                    + "   AND t1.TABLE_NAME = t2.TABLE_NAME "
                    + "   AND t1.COLUMN_NAME = t2.COLUMN_NAME ");
            ps.setString(1, table.trim().toUpperCase());
            rs = ps.executeQuery();
            Map map = new HashMap();
            while (rs.next()) {
                map.put(rs.getString("COLUMN_NAME").toLowerCase(), rs.getString("COMMENTS"));
            }
            for (int i = 1; i <= length; i++) {
                String columnName = r.getColumnName(i).toLowerCase();
                String columnTypeName = r.getColumnTypeName(i);
                String comments = r.getColumnLabel(i);
                String[] strs1 = {columnName, columnTypeName, (String) map.get(columnName), "", ""};
                tableVales[i - 1] = strs1;
            }
            return tableVales;
        } catch (SQLException ex) {
            Logger.getLogger(OracleDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(OracleDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
