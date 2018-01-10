/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.loquat.autocreatesql.utils.ConnectionManager;

/**
 *
 * @author Administrator
 */
public abstract class AbstractDao {
    private final Connection conn;
    
    public AbstractDao(String driver, String url,
            String user, String pwd) throws ClassNotFoundException, SQLException {
        conn =  ConnectionManager.getConnection(driver, url, user, pwd);
    }

    /**
     *
     * @return
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException{
        return conn;
    }
    
    
    
    /**
     *
     * @param table
     * @param conn
     * @return
     */
   public abstract String[][] getTableValues(String table,Connection conn);
    
}
