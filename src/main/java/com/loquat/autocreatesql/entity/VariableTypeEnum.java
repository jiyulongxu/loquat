/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.entity;

/**
 *
 * @author User
 */
public enum VariableTypeEnum {
    STRING("VARCHAR","String"),
    STRING2("CHAR","String"),
    STRING3("TEXT","String"),
    INTEGER("INTEGER","Integer"),
    DOUBLE("DOUBLE","Double"),
    BYTE("TINYINT","Byte"),
    DATE("TIMESTAMP","Date"),
    DATE2("DATE","Date"),
    LONG("BIGINT","Long");
    private String javaVari;
    
    private String databaseVari;

    private VariableTypeEnum( String databaseVari,String javaVari) {
        this.databaseVari = databaseVari;
        this.javaVari = javaVari;
    }

    public String getJavaVari() {
        return javaVari;
    }

    public String getDatabaseVari() {
        return databaseVari;
    }
}
