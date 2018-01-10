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
public class SelectSqlHandler extends SqlHandler {

    public SelectSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String selectSql = "select ";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            selectSql += (i + 1 == (list.size()) ? columnName : columnName + ", ");
        }
        selectSql += " from " + tableName.toLowerCase() + " where 1=1 ";
        return selectSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        return  createSqlByMysql(tableName , list) ;
    }

}
