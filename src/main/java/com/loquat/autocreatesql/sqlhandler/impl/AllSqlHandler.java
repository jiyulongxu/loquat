/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.sqlhandler.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.loquat.autocreatesql.entity.SqlEntity;
import com.loquat.autocreatesql.sqlhandler.SqlHandler;

/**
 *
 * @author Administrator
 */
public class AllSqlHandler extends SqlHandler {

    public AllSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }

    @Override
    public String createSqlByMysql(String table, List<SqlEntity> list) {
        try {
            SqlHandler sqlHandler = new SelectSqlHandler(table, list);
            String str = sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new InsertSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new UpdateSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
           
            sqlHandler = new BeanFiledsHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new ResultMapFeildsHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisInsertSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchInsertSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchInsertSql2Handler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisUpdateSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchUpdateSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatiSelectSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            return str;
        } catch (SQLException ex) {
            Logger.getLogger(AllSqlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String createSqlByOracle(String table, List<SqlEntity> list) {
        try {
            SqlHandler sqlHandler = new SelectSqlHandler(table, list);
            String str = sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new InsertSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new UpdateSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new BeanFiledsHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new ResultMapFeildsHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisInsertSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchInsertSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchInsertSql2Handler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisUpdateSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchUpdateSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisMergeSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatisBatchMergeSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            sqlHandler = new MybatiSelectSqlHandler(table, list);
            str += sqlHandler.generate()+"\r\n\r\n";
            return str;
        } catch (SQLException ex) {
            Logger.getLogger(AllSqlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
