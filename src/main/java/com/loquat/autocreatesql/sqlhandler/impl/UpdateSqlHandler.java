/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.sqlhandler.impl;

import java.util.List;

import com.loquat.autocreatesql.entity.SqlEntity;
import com.loquat.autocreatesql.sqlhandler.SqlHandler;

/**
 *
 * @author Administrator
 */
public class UpdateSqlHandler extends SqlHandler{

    public UpdateSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }
    
    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
       String updateSql = "update " + tableName.toLowerCase() + " set ";
       for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            updateSql += (i+1 == (list.size()) ? columnName + "=?" : columnName + "=?, ");
       }
        updateSql += " where 1=1 ";
        return updateSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        return  createSqlByMysql(tableName , list) ;
    }
    
}
