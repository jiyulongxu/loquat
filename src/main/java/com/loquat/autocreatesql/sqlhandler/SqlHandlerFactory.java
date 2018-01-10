/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.sqlhandler;

import java.util.List;

import com.loquat.autocreatesql.entity.SqlEntity;
import com.loquat.autocreatesql.sqlhandler.impl.AllSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.BeanFiledsHandler;
import com.loquat.autocreatesql.sqlhandler.impl.InsertSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatiSelectSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisBatchInsertSql2Handler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisBatchInsertSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisBatchMergeSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisBatchUpdateSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisInsertSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisMergeSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.MybatisUpdateSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.ResultMapFeildsHandler;
import com.loquat.autocreatesql.sqlhandler.impl.SelectSqlHandler;
import com.loquat.autocreatesql.sqlhandler.impl.UpdateSqlHandler;

/**
 *
 * @author Administrator
 */
public class SqlHandlerFactory {

    private SqlHandlerFactory() {
    }

    public static SqlHandler getSqlHandler(String table, List<SqlEntity> list, String key) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (key.equalsIgnoreCase("selectSql")) {
            return new SelectSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("insertSql")) {
            return new InsertSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("updateSql")) {
            return new UpdateSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("beanFileds")) {
            return new BeanFiledsHandler(table, list);
        } else if (key.equalsIgnoreCase("resultMapFeilds")) {
            return new ResultMapFeildsHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatisInsertSql")) {
            return new MybatisInsertSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatisBatchInsertSql")) {
            return new MybatisBatchInsertSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatisBatchInsertSql2")) {
            return new MybatisBatchInsertSql2Handler(table, list);
        } else if (key.equalsIgnoreCase("mybatisUpdateSql")) {
            return new MybatisUpdateSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatisBatchUpdateSql")) {
            return new MybatisBatchUpdateSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatisMergeSql")) {
            return new MybatisMergeSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatisBatchMergeSql")) {
            return new MybatisBatchMergeSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("mybatiSelectSql")) {
            return new MybatiSelectSqlHandler(table, list);
        } else if (key.equalsIgnoreCase("all")) {
            return new AllSqlHandler(table, list);
        }
        return null;
    }
}
