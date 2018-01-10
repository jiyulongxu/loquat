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
public class ResultMapFeildsHandler extends SqlHandler{

    public ResultMapFeildsHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }
    
    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        String resultMapFeilds = "<resultMap id=\"\" type=\"\">\r\n";
         for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            
            resultMapFeilds += "<result property=\"" + property + "\" column=\"" + columnName.toUpperCase() + "\"/>\r\n";
         }
         resultMapFeilds += "</resultMap>";
         return resultMapFeilds;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        return  createSqlByMysql(tableName , list) ;
    }
    
}
