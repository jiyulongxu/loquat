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
public class MybatisBatchMergeSqlHandler extends SqlHandler {

    public MybatisBatchMergeSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String mybatisBatchReplace = "<insert id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"\" close=\"\">\r\n"
                + "replace into " + tableName.toLowerCase() + "\r\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
         for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisBatchReplace += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
         }
         mybatisBatchReplace +="</trim>\r\nselect\r\n";	
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisBatchReplace += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" 
                    + (columnTypeName .equals("DATE") ? "date_format(#{item." + property + "},'%Y-%m-%d')," : "#{item." + property + "},") + "\r\n</if>\r\n"; 
        }
        mybatisBatchReplace+="<if test=\"item.?!=null and item.?!=''\"> \r\n from "+tableName.toLowerCase()+" where ? = #{item.?}\r\n</if> \r\n</foreach>\r\n</insert>";       
        return mybatisBatchReplace;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        String mybatisBatchUpdateSqlPlus = "";
        String mybatisBatchMergeSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                + "merge into " + tableName.toLowerCase() + " a using (select #{item.?} col from dual) b \r\n"
                + "on (a.?  = b.col) when matched then update \r\n"
                + "<set>\r\n";
        String mybatisBatchInsertSqlPlusPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisBatchUpdateSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},") + "\r\n</if>\r\n";
              mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";

        }
        mybatisBatchInsertSqlPlusPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + property + "!=null and item."
                    + property + "!=''\">\r\n "
                    + (columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},")
                    + "\r\n</if>\r\n";
        }
        mybatisBatchMergeSql += mybatisBatchUpdateSqlPlus + "</set>\r\nwhen not matched then \r\ninsert\r\n"
                + mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</update>\r\n";
        return mybatisBatchMergeSql;
    }

}
