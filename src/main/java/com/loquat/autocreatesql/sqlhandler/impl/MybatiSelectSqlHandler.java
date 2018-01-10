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
public class MybatiSelectSqlHandler extends SqlHandler {

    public MybatiSelectSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String mybatiSelectSql = "<select id=\"\" parameterType=\"\">\r\n";
        String selectSql = "select ";
        String mybatisWhereSqlPlus = "<where>\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            selectSql += (i + 1 == (list.size()) ? columnName : columnName + ", ");
            mybatisWhereSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName.equals("DATE") ? "str_to_date(#{" + property + "},'%Y-%m-%d')" : "#{" + property + "}") + "\r\n</if>\r\n";
        }
        mybatiSelectSql += selectSql + "\r\n";
        mybatiSelectSql += mybatisWhereSqlPlus + "</where>\r\n</select>";
        return mybatiSelectSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        String mybatiSelectSql = "<select id=\"\" parameterType=\"\">\r\n";
        String selectSql = "select ";
        String mybatisWhereSqlPlus = "<where>\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            selectSql += (i + 1 == (list.size()) ? columnName : columnName + ", ");
            mybatisWhereSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName.equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')" : "#{" + property + "}") + "\r\n</if>\r\n";
        }
        mybatiSelectSql += selectSql + "\r\n";
        mybatiSelectSql += mybatisWhereSqlPlus + "</where>\r\n</select>";
        return mybatiSelectSql;
    }

}
