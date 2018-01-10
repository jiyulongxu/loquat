/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.dao;

import java.sql.SQLException;

import com.loquat.autocreatesql.dao.impl.MysqlDao;
import com.loquat.autocreatesql.dao.impl.OracleDao;


/**
 *
 * @author Administrator
 */
public class DaoFactory {
    private DaoFactory(){}
    private static String flag = "";
    
    public static String getFlag(){
        return flag;
    }
    
    public static AbstractDao getDao(String driver, String url,
            String user, String pwd) throws ClassNotFoundException, SQLException{
         if (driver == null) {
            return null;
        }
        if (driver.equalsIgnoreCase("com.mysql.jdbc.Driver")) {
            DaoFactory.flag="mysql";
            return new MysqlDao(url,user,pwd);
        } else if (driver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
            DaoFactory.flag="oracle";
            return new OracleDao(url,user,pwd);
        }
        return null;
    }
    
    
}
