/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.sqlhandler;

import java.sql.SQLException;
import java.util.List;

import com.loquat.autocreatesql.dao.DaoFactory;
import com.loquat.autocreatesql.entity.SqlEntity;

/**
 *
 * @author Administrator
 */
public abstract class SqlHandler {

    private String table;

    public abstract String createSqlByMysql(String tableName, List<SqlEntity> list);
    
    public abstract String createSqlByOracle(String tableName, List<SqlEntity> list);
    
    private List<SqlEntity> list = null;

    public SqlHandler(String table, List<SqlEntity> list) {
        this.table = table;
        this.list = list;
    }

    public SqlHandler() {

    }
    //模板

    public final String generate() throws SQLException {
        String sql = "";
        if ("mysql".equals(DaoFactory.getFlag())) {
            sql = createSqlByMysql(table, list);
        }else if ("oracle".equals(DaoFactory.getFlag())) {
            sql = createSqlByOracle(table, list);
        }
        return sql;
    }

}
