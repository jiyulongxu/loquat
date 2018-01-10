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
public class MybatisMergeSqlHandler extends SqlHandler {

    public MybatisMergeSqlHandler(String table, List<SqlEntity> list) {
        super(table, list);
    }
    
    
//    <insert id="replaceNews">
//		replace into tt_news
//		<trim prefix="(" suffix=")" suffixOverrides=",">
//			<if test="newsId!=null and newsId!=''">
//				 news_id,
//			</if>
//				update_by,
//				create_by,
//			<if test="delFlag!=null and delFlag!=''">
//				del_flag,
//			</if>
//				reader,
//				is_assist,
//				create_date,
//			
//				read_flag,
//				notice_id,
//				update_date
//
//		</trim>
//		select 
//			<if test="newsId!=null and newsId!=''">
//				#{newsId},
//			</if>
//			#{updateBy},
//			#{createBy},
//			<if test="delFlag!=null and delFlag!=''">
//				#{delFlag},
//			</if>
//			#{reader},
//			#{isAssist},
//			<if test="newsId!=null and newsId!=''">
//				create_date,
//			</if>
//			<if test="newsId==null or newsId==''">
//				now(),
//			</if>
//			if(isnull(#{readFlag}),1,#{readFlag}),
//			#{noticeId},
//			now()
//		<if test="newsId!=null and newsId!=''">
//			from tt_news where news_id = #{newsId}
//		</if>
//		
//	</insert>
    
    @Override
    public String createSqlByMysql(String tableName, List<SqlEntity> list) {
        tableName = tableName.toLowerCase();
        String  replaceSql = "<insert id=\"\">\r\nreplace into "+tableName+"\r\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\r\n";
	 for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            replaceSql += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
            
         }	
	replaceSql +="</trim>\r\nselect\r\n";	
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            replaceSql += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" 
                    + (columnTypeName .equals("DATE") ? "date_format(#{" + property + "},'%Y-%m-%d')," : "#{" + property + "},") + "\r\n</if>\r\n";
            
        }
        replaceSql+="<if test=\"?!=null and ?!=''\"> \r\n from "+tableName+" where ? = #{?}\r\n</if> \r\n</insert>";
        
        return replaceSql;
    }

    @Override
    public String createSqlByOracle(String tableName, List<SqlEntity> list) {
        String mybatisInsertSqlPlus = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">c";
        String mybatisUpdateSqlPlus = "";
        String mybatisMergeSql = "<update id=\"\" >\r\nmerge into " + tableName.toLowerCase() + " a using (select #{?} col from dual) b \r\n"
                + "on (a.?  = b.col) when matched then update \r\n"
                + "<set>\r\n";
        for (int i = 0; i < list.size(); i++) {
            String columnName = list.get(i).getColumn().toLowerCase();
            String columnTypeName = list.get(i).getColumnType();
            String comments = list.get(i).getComments()==null?"":list.get(i).getComments();
            String property = list.get(i).getProperty();
            String propertyType = list.get(i).getPropertyType();
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + ",\r\n</if>\r\n";
            mybatisUpdateSqlPlus += "<if test=\"" + property + "!=null and " + property + "!=''\">\r\n" + columnName.toLowerCase() + "="
                    + (columnTypeName .equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')," : "#{" + property + "},") + "\r\n</if>\r\n";
            
        }
        mybatisInsertSqlPlus += "</trim>\r\n<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\r\n";
        mybatisMergeSql += mybatisUpdateSqlPlus + "</set>\r\nwhen not matched then \r\ninsert\r\n";
        for (int j = 0; j < list.size(); j++) {
            String comments = list.get(j).getComments();
            String property = list.get(j).getProperty();
            String propertyType = list.get(j).getPropertyType();
            String columnName = list.get(j).getColumn().toLowerCase();
            String columnTypeName = list.get(j).getColumnType();
            mybatisInsertSqlPlus += "<if test=\"" + property + "!=null and "
                    + property + "!=''\">\r\n "
                    + (columnTypeName .equals("DATE") ? "to_date(#{" + property + "},'yyyy-mm-dd')," : "#{" + property + "},")
                    + "\r\n</if>\r\n";
        }
        
        mybatisMergeSql += mybatisInsertSqlPlus + "</trim>\r\n</update>\r\n";
        return mybatisMergeSql;
    }

}
