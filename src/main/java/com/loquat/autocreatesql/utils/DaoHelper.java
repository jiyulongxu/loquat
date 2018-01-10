package com.loquat.autocreatesql.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;

import com.loquat.autocreatesql.entity.SqlEntity;

/**
 *
 * @author Administrator
 */
public class DaoHelper extends HashMap{
    
    public boolean isRightSql(String str){
        return this.keySet().contains(str);
    }

    public ResultSetMetaData getResultSetMetaData(Connection connection,String tableName) throws SQLException {
        String sql = "select * from " + tableName/*+" limit 1"*/;
        try {
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            return rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return null;
    }
     public DatabaseMetaData getDatabaseMetaData(Connection connection) throws SQLException{
         DatabaseMetaData dbmd=connection.getMetaData();
         return dbmd;
     }
     public void autoCreatSqlForMysql(Connection connection,String tableName) {
         try {
            ResultSetMetaData data = this.getResultSetMetaData(connection,tableName);
            if (data == null) {
                return;
            }
            String selectSql = "select ";
            String insertSql = "insert into " + tableName.toLowerCase() + " ( ";
            String updateSql = "update " + tableName.toLowerCase() + " set ";
            String beanFileds = "\r\n";
            String resultMapFeilds = "\r\n<resultMap id=\"\" type=\"\">\r\n";
            String mybatisInsertSql = "\r\n<insert id=\"\" >\r\ninsert into " + tableName.toLowerCase() + "\r\n";
            String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
            String mybatisBatchInsertSql = "<insert id=\"\" parameterType=\"java.util.List\">\r\ninsert into " + tableName.toLowerCase() + "(\r\n";
            String mybatisBatchInsertSql2 = "<insert id=\"\" parameterType=\"java.util.List\">\r\n"
                    + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"\" close=\"\">\r\n"
                    + "insert into " + tableName.toLowerCase() + "\r\n" + mybatisInsertSqlPlus;
            String mybatisUpdateSql = "<update id=\"\">\r\nupdate " + tableName.toLowerCase() + "\r\n<set>\r\n";
            String mybatisUpdateSqlPlus = "";
            String mybatisWhereSqlPlus = "<where>\r\n";
            String mybatisBatchUpdateSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                    + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"\" close=\"\">\r\n"
                    + "update " + tableName.toLowerCase() + "\r\n<set>\r\n";
            String mybatisBatchUpdateSqlPlus = "";
            String mybatisBatchWhereSqlPlus = "";
            String mybatisBatchInsertSqlPlusPlus = "";
            String mybatiSelectSql = "<select id=\"\" parameterType=\"\">\r\n";
            for (int i = 1; i <= data.getColumnCount(); i++) {

//				获得指定列的列名 
                String columnName = data.getColumnName(i).toLowerCase();
//				获得指定列的数据类型名    
                String columnTypeName = data.getColumnTypeName(i);
                selectSql += (i == (data.getColumnCount()) ? columnName : columnName + ", ");
                insertSql += (i == (data.getColumnCount()) ? columnName + ")" : columnName + ", ");
                updateSql += (i == (data.getColumnCount()) ? columnName + "=?" : columnName + "=?, ");
                beanFileds += "private String " + columnName.toLowerCase() + ";   //" + columnTypeName + "\r\n";
                resultMapFeilds += "<result property=\"" + columnName.toLowerCase() + "\" column=\"" + columnName.toUpperCase() + "\"/>\r\n";
                mybatisInsertSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and " + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
                mybatisBatchInsertSql += (i == (data.getColumnCount()) ? columnName + ")" : columnName + ", ");
                mybatisUpdateSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and " + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "str_to_date(#{" + columnName.toLowerCase() + "},'%Y-%m-%d')," : "#{" + columnName.toLowerCase() + "},") + "\r\n</if>\r\n";
                mybatisWhereSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and " + columnName.toLowerCase() + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "str_to_date(#{" + columnName.toLowerCase() + "},'%Y-%m-%d')" : "#{" + columnName.toLowerCase() + "}") + "\r\n</if>\r\n";
                mybatisBatchUpdateSqlPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item." + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "str_to_date(#{item." + columnName.toLowerCase() + "},'%Y-%m-%d')," : "#{item." + columnName.toLowerCase() + "},") + "\r\n</if>\r\n";
                mybatisBatchWhereSqlPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item." + columnName.toLowerCase() + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "str_to_date(#{item." + columnName.toLowerCase() + "},'%Y-%m-%d')" : "#{item." + columnName.toLowerCase() + "}") + "\r\n</if>\r\n";
                mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item." + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";

            }
            insertSql += "values(";
            mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
            mybatisBatchInsertSqlPlusPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
            mybatisBatchInsertSql += "\r\n<foreach item=\"item\" index=\"index\" collection=\"list\" "
                    + "open=\"values\" separator=\",\" close=\"\">\r\n(\r\n";
            mybatisUpdateSqlPlus = mybatisUpdateSqlPlus + "</set>\r\n" + mybatisWhereSqlPlus + "</where>\r\n";
            mybatisUpdateSql += mybatisUpdateSqlPlus + "</update>\r\n";

            for (int j = 1; j <= data.getColumnCount(); j++) {
                String columnName = data.getColumnName(j).toLowerCase();
                String columnTypeName = data.getColumnTypeName(j);
                insertSql += (j == (data.getColumnCount()) ? "?)" : "?, ");
                mybatisInsertSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and "
                        + columnName.toLowerCase() + "!=''\">\r\n "
                        + (columnTypeName .equals("DATE") ? "str_to_date(#{" + columnName.toLowerCase() + "},'%Y-%m-%d')," : "#{" + columnName.toLowerCase() + "},")
                        + "\r\n</if>\r\n";
                mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item."
                        + columnName.toLowerCase() + "!=''\">\r\n "
                        + (columnTypeName .equals("DATE") ? "str_to_date(#{item." + columnName.toLowerCase() + "},'%Y-%m-%d')," : "#{item." + columnName.toLowerCase() + "},")
                        + "\r\n</if>\r\n";
                if (j == (data.getColumnCount())) {
                    mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "str_to_date(#{item." + columnName.toLowerCase() + "},'%Y-%m-%d')," : "#{item." + columnName.toLowerCase() + "}";
                } else {
                    mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "str_to_date(#{item." + columnName.toLowerCase() + "},'%Y-%m-%d')," : "#{item." + columnName.toLowerCase() + "},";
                }
            }
            mybatiSelectSql += selectSql + "\r\n";
            selectSql += " from " + tableName.toLowerCase() + " where 1=1 \r\n";
            updateSql += " where 1=1 \r\n";
            resultMapFeilds += "</resultMap>\r\n";
            mybatisInsertSql += mybatisInsertSqlPlus + "</trim>\r\n</insert>\r\n";
            mybatisBatchUpdateSqlPlus += "</set>\r\n<where>\r\n" + mybatisBatchWhereSqlPlus + "</where>\r\n</foreach>\r\n</update>\r\n";
            mybatisBatchInsertSql += "\r\n)\r\n</foreach>\r\n</insert>\r\n";
            mybatisBatchUpdateSql += mybatisBatchUpdateSqlPlus + "\r\n";
            mybatisBatchInsertSql2 += mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</insert>\r\n";
            mybatiSelectSql += mybatisWhereSqlPlus + "</where>\r\n</select>\r\n";
            String all = selectSql+insertSql + "\r\n"+updateSql + beanFileds+resultMapFeilds+mybatisInsertSql+ "\r\n"+mybatisBatchInsertSql+ "\r\n"
                 +mybatisBatchInsertSql2+ "\r\n"+mybatisUpdateSql+ "\r\n"+mybatisBatchUpdateSql+ "\r\n"+mybatiSelectSql;
            this.put("all",all);
            this.put("selectSql", selectSql);
            this.put("insertSql", insertSql + "\r\n");
            this.put("updateSql", updateSql + "\r\n");
            this.put("beanFileds", beanFileds);
            this.put("resultMapFeilds", resultMapFeilds);
            this.put("mybatisInsertSql", mybatisInsertSql);
            this.put("mybatisBatchInsertSql", mybatisBatchInsertSql);
            this.put("mybatisBatchInsertSql2", mybatisBatchInsertSql2);
            this.put("mybatisUpdateSql", mybatisUpdateSql);
            this.put("mybatisBatchUpdateSql", mybatisBatchUpdateSql);
            this.put("mybatiSelectSql", mybatiSelectSql);
            
            
          } catch (SQLException e) {
            e.printStackTrace();
        } 
         
     }
    

    public void autoCreatSql(Connection connection,String tableName) {
        try {
            ResultSetMetaData data = this.getResultSetMetaData(connection,tableName);
            if (data == null) {
                return;
            }
            String selectSql = "select ";
            String insertSql = "insert into " + tableName.toLowerCase() + " ( ";
            String updateSql = "update " + tableName.toLowerCase() + " set ";
            String beanFileds = "\r\n";
            String resultMapFeilds = "\r\n<resultMap id=\"\" type=\"\">\r\n";
            String mybatisInsertSql = "\r\n<insert id=\"\" >\r\ninsert into " + tableName.toLowerCase() + "\r\n";
            String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
            String mybatisBatchInsertSql = "<insert id=\"\" parameterType=\"java.util.List\">\r\ninsert into " + tableName.toLowerCase() + "(\r\n";
            String mybatisBatchInsertSql2 = "<insert id=\"\" parameterType=\"java.util.List\">\r\n"
                    + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                    + "insert into " + tableName.toLowerCase() + "\r\n" + mybatisInsertSqlPlus;
            String mybatisUpdateSql = "<update id=\"\">\r\nupdate " + tableName.toLowerCase() + "\r\n<set>\r\n";
            String mybatisUpdateSqlPlus = "";
            String mybatisWhereSqlPlus = "<where>\r\n";
            String mybatisBatchUpdateSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                    + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                    + "update " + tableName.toLowerCase() + "\r\n<set>\r\n";
            String mybatisBatchUpdateSqlPlus = "";
            String mybatisBatchWhereSqlPlus = "";
            String mybatisMergeSql = "<update id=\"\" >\r\nmerge into " + tableName.toLowerCase() + " a using (select #{?} col from dual) b \r\n"
                    + "on (a.?  = b.col) when matched then update \r\n"
                    + "<set>\r\n";
            String mybatisBatchMergeSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                    + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                    + "merge into " + tableName.toLowerCase() + " a using (select #{item.?} col from dual) b \r\n"
                    + "on (a.?  = b.col) when matched then update \r\n"
                    + "<set>\r\n";
            String mybatisBatchInsertSqlPlusPlus = "";
            String mybatiSelectSql = "<select id=\"\" parameterType=\"\">\r\n";
            for (int i = 1; i <= data.getColumnCount(); i++) {

//				获得指定列的列名 
                String columnName = data.getColumnName(i).toLowerCase();
//				获得指定列的数据类型名    
                String columnTypeName = data.getColumnTypeName(i);
                selectSql += (i == (data.getColumnCount()) ? columnName : columnName + ", ");
                insertSql += (i == (data.getColumnCount()) ? columnName + ")" : columnName + ", ");
                updateSql += (i == (data.getColumnCount()) ? columnName + "=?" : columnName + "=?, ");
                beanFileds += "private String " + columnName.toLowerCase() + ";   //" + columnTypeName + "\r\n";
                resultMapFeilds += "<result property=\"" + columnName.toLowerCase() + "\" column=\"" + columnName.toUpperCase() + "\"/>\r\n";
                mybatisInsertSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and " + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
                mybatisBatchInsertSql += (i == (data.getColumnCount()) ? columnName + ")" : columnName + ", ");
                mybatisUpdateSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and " + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "to_date(#{" + columnName.toLowerCase() + "},'yyyy-mm-dd')," : "#{" + columnName.toLowerCase() + "},") + "\r\n</if>\r\n";
                mybatisWhereSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and " + columnName.toLowerCase() + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "to_date(#{" + columnName.toLowerCase() + "},'yyyy-mm-dd')" : "#{" + columnName.toLowerCase() + "}") + "\r\n</if>\r\n";
                mybatisBatchUpdateSqlPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item." + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "to_date(#{item." + columnName.toLowerCase() + "},'yyyy-mm-dd')," : "#{item." + columnName.toLowerCase() + "},") + "\r\n</if>\r\n";
                mybatisBatchWhereSqlPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item." + columnName.toLowerCase() + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                        + (columnTypeName .equals("DATE") ? "to_date(#{item." + columnName.toLowerCase() + "},'yyyy-mm-dd')" : "#{item." + columnName.toLowerCase() + "}") + "\r\n</if>\r\n";
                mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item." + columnName.toLowerCase() + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";

            }
            insertSql += "values(";
            mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
            mybatisBatchInsertSqlPlusPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
            mybatisBatchInsertSql += "\r\n<foreach item=\"item\" index=\"index\" collection=\"list\" "
                    + "open=\"(\" separator=\"union all\" close=\")\">\r\nselect\r\n";

            mybatisMergeSql += mybatisUpdateSqlPlus + "</set>\r\nwhen not matched then \r\ninsert\r\n";
            mybatisUpdateSqlPlus = mybatisUpdateSqlPlus + "</set>\r\n" + mybatisWhereSqlPlus + "</where>\r\n";
            mybatisUpdateSql += mybatisUpdateSqlPlus + "</update>\r\n";

            for (int j = 1; j <= data.getColumnCount(); j++) {
                String columnName = data.getColumnName(j).toLowerCase();
                String columnTypeName = data.getColumnTypeName(j);
                insertSql += (j == (data.getColumnCount()) ? "?)" : "?, ");
                mybatisInsertSqlPlus += "<if test=\"" + columnName.toLowerCase() + "!=null and "
                        + columnName.toLowerCase() + "!=''\">\r\n "
                        + (columnTypeName .equals("DATE") ? "to_date(#{" + columnName.toLowerCase() + "},'yyyy-mm-dd')," : "#{" + columnName.toLowerCase() + "},")
                        + "\r\n</if>\r\n";
                mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + columnName.toLowerCase() + "!=null and item."
                        + columnName.toLowerCase() + "!=''\">\r\n "
                        + (columnTypeName .equals("DATE") ? "to_date(#{item." + columnName.toLowerCase() + "},'yyyy-mm-dd')," : "#{item." + columnName.toLowerCase() + "},")
                        + "\r\n</if>\r\n";
                if (j == (data.getColumnCount())) {
                    mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "to_date(#{item." + columnName.toLowerCase() + "},'yyyy-mm-dd')," : "#{item." + columnName.toLowerCase() + "}";
                } else {
                    mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "to_date(#{item." + columnName.toLowerCase() + "},'yyyy-mm-dd')," : "#{item." + columnName.toLowerCase() + "},";
                }
            }
            mybatiSelectSql += selectSql + "\r\n";
            selectSql += " from " + tableName.toLowerCase() + " where 1=1 \r\n";
            updateSql += " where 1=1 \r\n";
            resultMapFeilds += "</resultMap>\r\n";
            mybatisInsertSql += mybatisInsertSqlPlus + "</trim>\r\n</insert>\r\n";
            mybatisMergeSql += mybatisInsertSqlPlus + "</trim>\r\n</update>\r\n";
            mybatisBatchMergeSql += mybatisBatchUpdateSqlPlus + "</set>\r\nwhen not matched then \r\ninsert\r\n"
                    + mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</update>\r\n";

            mybatisBatchUpdateSqlPlus += "</set>\r\n<where>\r\n" + mybatisBatchWhereSqlPlus + "</where>\r\n</foreach>\r\n</update>\r\n";
            mybatisBatchInsertSql += "\r\nfrom dual\r\n</foreach>\r\n</insert>\r\n";
            mybatisBatchUpdateSql += mybatisBatchUpdateSqlPlus + "\r\n";
            mybatisBatchInsertSql2 += mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</insert>\r\n";
            mybatiSelectSql += mybatisWhereSqlPlus + "</where>\r\n</select>\r\n";
            String all = selectSql+insertSql + "\r\n"+updateSql + beanFileds+resultMapFeilds+mybatisInsertSql+ "\r\n"+mybatisBatchInsertSql+ "\r\n"
                 +mybatisBatchInsertSql2+ "\r\n"+mybatisMergeSql+ "\r\n"+mybatisUpdateSql+ "\r\n"+mybatisBatchUpdateSql+ "\r\n"+mybatisBatchMergeSql+ "\r\n"+mybatiSelectSql;
            this.put("all",all);
            this.put("selectSql", selectSql);
            this.put("insertSql", insertSql + "\r\n");
            this.put("updateSql", updateSql + "\r\n");
            this.put("beanFileds", beanFileds);
            this.put("resultMapFeilds", resultMapFeilds);
            this.put("mybatisInsertSql", mybatisInsertSql);
            this.put("mybatisBatchInsertSql", mybatisBatchInsertSql);
            this.put("mybatisBatchInsertSql2", mybatisBatchInsertSql2);
            this.put("mybatisMergeSql", mybatisMergeSql);
            this.put("mybatisUpdateSql", mybatisUpdateSql);
            this.put("mybatisBatchUpdateSql", mybatisBatchUpdateSql);
            this.put("mybatisBatchMergeSql", mybatisBatchMergeSql);
            this.put("mybatiSelectSql", mybatiSelectSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public void autoCreatSqlByProperty(String tableName,List<SqlEntity> list) {
        if (list.isEmpty()) {
            return;
        }
        String selectSql = "select ";
        String insertSql = "insert into " + tableName.toLowerCase() + " ( ";
        String updateSql = "update " + tableName.toLowerCase() + " set ";
        String beanFileds = "\r\n";
        String resultMapFeilds = "\r\n<resultMap id=\"\" type=\"\">\r\n";
        String mybatisInsertSql = "\r\n<insert id=\"\" >\r\ninsert into " + tableName.toLowerCase() + "\r\n";
        String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        String mybatisBatchInsertSql = "<insert id=\"\" parameterType=\"java.util.List\">\r\ninsert into " + tableName.toLowerCase() + "(\r\n";
        String mybatisBatchInsertSql2 = "<insert id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                + "insert into " + tableName.toLowerCase() + "\r\n" + mybatisInsertSqlPlus;
        String mybatisUpdateSql = "<update id=\"\">\r\nupdate " + tableName.toLowerCase() + "\r\n<set>\r\n";
        String mybatisUpdateSqlPlus = "";
        String mybatisWhereSqlPlus = "<where>\r\n";
        String mybatisBatchUpdateSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                + "update " + tableName.toLowerCase() + "\r\n<set>\r\n";
        String mybatisBatchUpdateSqlPlus = "";
        String mybatisBatchWhereSqlPlus = "";
        String mybatisMergeSql = "<update id=\"\" >\r\nmerge into " + tableName.toLowerCase() + " a using (select #{?} col from dual) b \r\n"
                + "on (a.?  = b.col) when matched then update \r\n"
                + "<set>\r\n";
        String mybatisBatchMergeSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"begin\" close=\";end;\">\r\n"
                + "merge into " + tableName.toLowerCase() + " a using (select #{item.?} col from dual) b \r\n"
                + "on (a.?  = b.col) when matched then update \r\n"
                + "<set>\r\n";
        String mybatisBatchInsertSqlPlusPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        String mybatiSelectSql = "<select id=\"\" parameterType=\"\">\r\n";
       /**
        * 
        */
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            selectSql += (i+1 == (list.size()) ? columnName : columnName + ", ");
            insertSql += (i+1 == (list.size()) ? columnName + ")" : columnName + ", ");
            updateSql += (i+1 == (list.size()) ? columnName + "=?" : columnName + "=?, ");
            beanFileds += "/**\r\n *"+comments+" \r\n */\r\nprivate "+propertyType+" " + property + ";\r\n";
            resultMapFeilds += "<result property=\"" + property + "\" column=\"" + columnName.toUpperCase() + "\"/>\r\n";
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
            mybatisBatchInsertSql += (i+1 == (list.size()) ? columnName + ")" : columnName + ", ");
            mybatisUpdateSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')," : "#{" + property + "},") + "\r\n</if>\r\n";
            mybatisWhereSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')" : "#{" + property + "}") + "\r\n</if>\r\n";
            mybatisBatchUpdateSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},") + "\r\n</if>\r\n";
            mybatisBatchWhereSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')" : "#{item." + property + "}") + "\r\n</if>\r\n";
            mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
            
        }
        insertSql += "values(";
        mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        mybatisBatchInsertSqlPlusPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        mybatisBatchInsertSql += "\r\n<foreach item=\"item\" index=\"index\" collection=\"list\" "
                + "open=\"(\" separator=\"union all\" close=\")\">\r\nselect\r\n";
        mybatisMergeSql += mybatisUpdateSqlPlus + "</set>\r\nwhen not matched then \r\ninsert\r\n";
        mybatisUpdateSqlPlus = mybatisUpdateSqlPlus + "</set>\r\n" + mybatisWhereSqlPlus + "</where>\r\n";
        mybatisUpdateSql += mybatisUpdateSqlPlus + "</update>\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            insertSql += (j+1 == (list.size()) ? "?)" : "?, ");
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and "
                    + property + "!=''\">\r\n "
                    + (columnTypeName .equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')," : "#{" + property + "},")
                    + "\r\n</if>\r\n";
            mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + property + "!=null and item."
                    + property + "!=''\">\r\n "
                    + (columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},")
                    + "\r\n</if>\r\n";
            if (j == (list.size()-1)) {
                mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "}";
            } else {
                mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "to_date(#{item." + property + "},'yyyy-mm-dd')," : "#{item." + property + "},";
            }
        }
        mybatiSelectSql += selectSql + "\r\n";
        selectSql += " from " + tableName.toLowerCase() + " where 1=1 \r\n";
        updateSql += " where 1=1 \r\n";
        resultMapFeilds += "</resultMap>\r\n";
        mybatisInsertSql += mybatisInsertSqlPlus + "</trim>\r\n</insert>\r\n";
        mybatisMergeSql += mybatisInsertSqlPlus + "</trim>\r\n</update>\r\n";
        mybatisBatchMergeSql += mybatisBatchUpdateSqlPlus + "</set>\r\nwhen not matched then \r\ninsert\r\n"
                + mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</update>\r\n";
        mybatisBatchUpdateSqlPlus += "</set>\r\n<where>\r\n" + mybatisBatchWhereSqlPlus + "</where>\r\n</foreach>\r\n</update>\r\n";
        mybatisBatchInsertSql += "\r\nfrom dual\r\n</foreach>\r\n</insert>\r\n";
        mybatisBatchUpdateSql += mybatisBatchUpdateSqlPlus + "\r\n";
        mybatisBatchInsertSql2 += mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</insert>\r\n";
        mybatiSelectSql += mybatisWhereSqlPlus + "</where>\r\n</select>\r\n";
        String all = selectSql+insertSql + "\r\n"+updateSql + beanFileds+resultMapFeilds+mybatisInsertSql+ "\r\n"+mybatisBatchInsertSql+ "\r\n"
            +mybatisBatchInsertSql2+ "\r\n"+mybatisMergeSql+ "\r\n"+mybatisUpdateSql+ "\r\n"+mybatisBatchUpdateSql+ "\r\n"+mybatisBatchMergeSql+ "\r\n"+mybatiSelectSql;
        this.put("all",all);
        this.put("selectSql", selectSql);
        this.put("insertSql", insertSql + "\r\n");
        this.put("updateSql", updateSql + "\r\n");
        this.put("beanFileds", beanFileds);
        this.put("resultMapFeilds", resultMapFeilds);
        this.put("mybatisInsertSql", mybatisInsertSql);
        this.put("mybatisBatchInsertSql", mybatisBatchInsertSql);
        this.put("mybatisBatchInsertSql2", mybatisBatchInsertSql2);
        this.put("mybatisMergeSql", mybatisMergeSql);
        this.put("mybatisUpdateSql", mybatisUpdateSql);
        this.put("mybatisBatchUpdateSql", mybatisBatchUpdateSql);
        this.put("mybatisBatchMergeSql", mybatisBatchMergeSql);
        this.put("mybatiSelectSql", mybatiSelectSql); 
    }

    public void autoCreatSqlByPropertyForMysql(String tableName,List<SqlEntity> list) {
        if (list.isEmpty()) {
            return;
        }
        String selectSql = "select ";
        String insertSql = "insert into " + tableName.toLowerCase() + " ( ";
        String updateSql = "update " + tableName.toLowerCase() + " set ";
        String beanFileds = "\r\n";
        String resultMapFeilds = "\r\n<resultMap id=\"\" type=\"\">\r\n";
        String mybatisInsertSql = "\r\n<insert id=\"\" >\r\ninsert into " + tableName.toLowerCase() + "\r\n";
        String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        String mybatisBatchInsertSql = "<insert id=\"\" parameterType=\"java.util.List\">\r\ninsert into " + tableName.toLowerCase() + "(\r\n";
        String mybatisBatchInsertSql2 = "<insert id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"\" close=\"\">\r\n"
                + "insert into " + tableName.toLowerCase() + "\r\n" + mybatisInsertSqlPlus;
        String mybatisUpdateSql = "<update id=\"\">\r\nupdate " + tableName.toLowerCase() + "\r\n<set>\r\n";
        String mybatisUpdateSqlPlus = "";
        String mybatisWhereSqlPlus = "<where>\r\n";
        String mybatisBatchUpdateSql = "<update id=\"\" parameterType=\"java.util.List\">\r\n"
                + "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\" open=\"\" close=\"\">\r\n"
                + "update " + tableName.toLowerCase() + "\r\n<set>\r\n";
        String mybatisBatchUpdateSqlPlus = "";
        String mybatisBatchWhereSqlPlus = "";
        String mybatisBatchInsertSqlPlusPlus = "";
        String mybatiSelectSql = "<select id=\"\" parameterType=\"\">\r\n";
       /**
        * 
        */
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            selectSql += (i+1 == (list.size()) ? columnName : columnName + ", ");
            insertSql += (i+1 == (list.size()) ? columnName + ")" : columnName + ", ");
            updateSql += (i+1 == (list.size()) ? columnName + "=?" : columnName + "=?, ");
            beanFileds += "/**\r\n *"+comments+" \r\n */\r\nprivate "+propertyType+" " + property + ";\r\n";
            resultMapFeilds += "<result property=\"" + property + "\" column=\"" + columnName.toUpperCase() + "\"/>\r\n";
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
            mybatisBatchInsertSql += (i+1 == (list.size()) ? columnName + ")" : columnName + ", ");
            mybatisUpdateSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "str_to_date(#{" + property + "}'%Y-%m-%d')," : "#{" + property + "},") + "\r\n</if>\r\n";
            mybatisWhereSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "str_to_date(#{" + property + "},'%Y-%m-%d')" : "#{" + property + "}") + "\r\n</if>\r\n";
            mybatisBatchUpdateSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')," : "#{item." + property + "},") + "\r\n</if>\r\n";
            mybatisBatchWhereSqlPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\nand " + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')" : "#{item." + property + "}") + "\r\n</if>\r\n";
            mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + property + "!=null and item." + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
            
        }
        insertSql += "values(";
        mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        mybatisBatchInsertSqlPlusPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        mybatisBatchInsertSql += "\r\n<foreach item=\"item\" index=\"index\" collection=\"list\" "
                + "open=\"values\" separator=\",\" close=\"\">\r\n(\r\n";
        mybatisUpdateSqlPlus = mybatisUpdateSqlPlus + "</set>\r\n" + mybatisWhereSqlPlus + "</where>\r\n";
        mybatisUpdateSql += mybatisUpdateSqlPlus + "</update>\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            insertSql += (j+1 == (list.size()) ? "?)" : "?, ");
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and "
                    + property + "!=''\">\r\n "
                    + (columnTypeName .equals("DATE") ? "str_to_date(#{" + property + "},'%Y-%m-%d')," : "#{" + property + "},")
                    + "\r\n</if>\r\n";
            mybatisBatchInsertSqlPlusPlus += "<if test=\"item." + property + "!=null and item."
                    + property + "!=''\">\r\n "
                    + (columnTypeName .equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')," : "#{item." + property + "},")
                    + "\r\n</if>\r\n";
            if (j == (list.size()-1)) {
                mybatisBatchInsertSql += columnTypeName .equals("DATE")  ? "str_to_date(#{item." + property + "},'%Y-%m-%d')" : "#{item." + property + "}";
            } else {
                mybatisBatchInsertSql += columnTypeName .equals("DATE") ? "str_to_date(#{item." + property + "},'%Y-%m-%d')," : "#{item." + property + "},";
            }
        }
        mybatiSelectSql += selectSql + "\r\n";
        selectSql += " from " + tableName.toLowerCase() + " where 1=1 \r\n";
        updateSql += " where 1=1 \r\n";
        resultMapFeilds += "</resultMap>\r\n";
        mybatisInsertSql += mybatisInsertSqlPlus + "</trim>\r\n</insert>\r\n";
       
        mybatisBatchUpdateSqlPlus += "</set>\r\n<where>\r\n" + mybatisBatchWhereSqlPlus + "</where>\r\n</foreach>\r\n</update>\r\n";
        mybatisBatchInsertSql += "\r\n)\r\n</foreach>\r\n</insert>\r\n";
        mybatisBatchUpdateSql += mybatisBatchUpdateSqlPlus + "\r\n";
        mybatisBatchInsertSql2 += mybatisBatchInsertSqlPlusPlus + "</trim>\r\n</foreach>\r\n</insert>\r\n";
        mybatiSelectSql += mybatisWhereSqlPlus + "</where>\r\n</select>\r\n";
        String all = selectSql+insertSql + "\r\n"+updateSql + beanFileds+resultMapFeilds+mybatisInsertSql+ "\r\n"+mybatisBatchInsertSql+ "\r\n"
            +mybatisBatchInsertSql2+ "\r\n"+mybatisUpdateSql+ "\r\n"+mybatisBatchUpdateSql+  "\r\n"+mybatiSelectSql;
        this.put("all",all);
        this.put("selectSql", selectSql);
        this.put("insertSql", insertSql + "\r\n");
        this.put("updateSql", updateSql + "\r\n");
        this.put("beanFileds", beanFileds);
        this.put("resultMapFeilds", resultMapFeilds);
        this.put("mybatisInsertSql", mybatisInsertSql);
        this.put("mybatisBatchInsertSql", mybatisBatchInsertSql);
        this.put("mybatisBatchInsertSql2", mybatisBatchInsertSql2);
        this.put("mybatisUpdateSql", mybatisUpdateSql);
        this.put("mybatisBatchUpdateSql", mybatisBatchUpdateSql);
        this.put("mybatiSelectSql", mybatiSelectSql); 
        
    }

   
}
