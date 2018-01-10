/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.dao.impl;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.loquat.autocreatesql.dao.AbstractDao;

/**
 *
 * @author Administrator
 */
public  class MysqlDao extends AbstractDao {

    public MysqlDao(String driver, String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        super(driver, url, user, pwd);
    }
    public MysqlDao( String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        super("com.mysql.jdbc.Driver", url, user, pwd);
    }

    @Override
   public String[][] getTableValues( String table,Connection conn) {
        try {
            String[][] tableVales;
            DatabaseMetaData rt = conn.getMetaData();
            ResultSet rs = rt.getColumns(null, null, table.toUpperCase(), null);
            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME").toLowerCase();
                String columnTypeName = rs.getString("TYPE_NAME");
                String comments = rs.getString("REMARKS");
                String[] strs1 = {columnName, columnTypeName, comments, "", ""};
                list.add(strs1);
            }
            int length = list.size();
            tableVales = new String[list.size()][]; //数据
            int i = 0;
            for (String[] strs : list) {
                tableVales[i] = strs;
                i++;
            }
            return tableVales;
        } catch (SQLException ex) {
            Logger.getLogger(MysqlDao.class.getName()).log(Level.SEVERE, null, ex);
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
