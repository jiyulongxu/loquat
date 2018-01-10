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
public class MybatisBatchUpdateSqlHandler extends SqlHandler {

    public MybatisBatchUpdateSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String mybatisBatchUpdateSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"\" close=\"\">\r\n"
                + "update " + tableName.toLowerCase() + "\r\n<set>\r\n";
        String mybatisBatchWhereSqlPlus = "";
        String mybatisBatchUpdateSqlPlus = "";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();

            mybatisBatchUpdateSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName.equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')," : "#{item." + property + "},") + "\r\n</if>\r\n";
        }
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisBatchWhereSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName.equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')" : "#{item." + property + "}") + "\r\n</if>\r\n";

        }
        mybatisBatchUpdateSqlPlus += "</set>\r\n<where>\r\n" + mybatisBatchWhereSqlPlus + "</where>\r\n</foreach>\r\n</update>";
        mybatisBatchUpdateSql += mybatisBatchUpdateSqlPlus;
        return mybatisBatchUpdateSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        String mybatisBatchUpdateSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                + "update " + tableName.toLowerCase() + "\r\n<set>\r\n";
        String mybatisBatchUpdateSqlPlus = "";
        String mybatisBatchWhereSqlPlus = "";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments() == null ? "" : list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisBatchWhereSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName.equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')" : "#{item." + property + "}") + "\r\n</if>\r\n";
            mybatisBatchUpdateSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName.equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},") + "\r\n</if>\r\n";
        }
        mybatisBatchUpdateSqlPlus += "</set>\r\n<where>\r\n" + mybatisBatchWhereSqlPlus + "</where>\r\n</foreach>\r\n</update>";
        mybatisBatchUpdateSql += mybatisBatchUpdateSqlPlus;
        return mybatisBatchUpdateSql;
    }

}
