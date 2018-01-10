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
public class MybatisInsertSqlHandler extends SqlHandler {

    public MybatisInsertSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String mybatisInsertSql = "<insert id=\"\" >\r\ninsert into " + tableName.toLowerCase() + "\r\n";
        String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();

            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
        }
        mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and "
                    + property + "!=''\">\r\n "
                    + (columnTypeName.equals("DATE") ? "str_to_date(#{" + property + "},'%Y-%m-%d')," : "#{" + property + "},")
                    + "\r\n</if>\r\n";
        }
        mybatisInsertSql += mybatisInsertSqlPlus + "</trim>\r\n</insert>";
        return mybatisInsertSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        String mybatisInsertSql = "<insert id=\"\" >\r\ninsert into " + tableName.toLowerCase() + "\r\n";
        String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
        }
        mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and "
                    + property + "!=''\">\r\n "
                    + (columnTypeName.equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')," : "#{" + property + "},")
                    + "\r\n</if>\r\n";
        }
        mybatisInsertSql += mybatisInsertSqlPlus + "</trim>\r\n</insert>";
        return mybatisInsertSql;
    }

}
