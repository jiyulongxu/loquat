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
public class MybatisBatchInsertSqlHandler extends SqlHandler {

    public MybatisBatchInsertSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String mybatisBatchInsertSql = "<insert id=\"\" parameterType=\"java.util.List\">\r\ninsert into " + tableName.toLowerCase() + "(\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();

            mybatisBatchInsertSql += (i + 1 == (list.size()) ? columnName + ")" : columnName + ", ");
        }
        mybatisBatchInsertSql += "\r\n<foreach item=\"item\" index=\"index\" collection=\"list\" "
                + "open=\"values\" separator=\",\" close=\"\">\r\n(\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();

            if (j == (list.size() - 1)) {
                mybatisBatchInsertSql += columnTypeName.equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')" : "#{item." + property + "}";
            } else {
                mybatisBatchInsertSql += columnTypeName.equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')," : "#{item." + property + "},";
            }
        }
        mybatisBatchInsertSql += "\r\n)\r\n</foreach>\r\n</insert>";
        return mybatisBatchInsertSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        String mybatisBatchInsertSql = "<insert id=\"\" parameterType=\"java.util.List\">\r\ninsert into " + tableName.toLowerCase() + "(\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisBatchInsertSql += (i + 1 == (list.size()) ? columnName + ")" : columnName + ", ");
        }
        mybatisBatchInsertSql += "\r\n<foreach item=\"item\" index=\"index\" collection=\"list\" "
                + "open=\"(\" separator=\"union all\" close=\")\">\r\nselect\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            if (j == (list.size() - 1)) {
                mybatisBatchInsertSql += columnTypeName.equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')" : "#{item." + property + "}";
            } else {
                mybatisBatchInsertSql += columnTypeName.equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},";
            }
        }
        mybatisBatchInsertSql += "\r\nfrom dual\r\n</foreach>\r\n</insert>";
        return mybatisBatchInsertSql;
    }

}
