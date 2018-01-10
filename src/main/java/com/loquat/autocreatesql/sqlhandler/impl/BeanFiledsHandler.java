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
public class BeanFiledsHandler extends SqlHandler{

    public BeanFiledsHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String beanFileds = "";
         for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
           
            beanFileds += "/**\r\n *"+comments+" \r\n */\r\nprivate "+propertyType+" " + property + ";\r\n";
         }
         return beanFileds;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        return createSqlByMysql( tableName,  list) ;
    }
    
}
