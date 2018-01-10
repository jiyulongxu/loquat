
package com.loquat.autocreatesql.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 *
 * @author 219397
 */
public class PropertiesUtil {
    private static final Properties p = new Properties();
    
    private PropertiesUtil() {
    }

    ;
    
    /**
     * 
     * @param obj
     * @param file
     * @throws IllegalArgumentException
     * @throws java.io.IOException
     * @throws IllegalAccessException 
     */
    public static void store(Object obj,File file) throws IllegalArgumentException, IOException, IllegalAccessException  {
        Field[] fs = obj.getClass().getDeclaredFields();
        if (fs != null) {
            for (Field f : fs) {
            	f.setAccessible(true);
            	Object o = "";
            	if(f.get(obj)!=null)o = f.get(obj);
                p.setProperty(f.getName(), (String) (o));
            }
        }else{
            return;
        }
        //ifFileNotExists_Create(fileStr);
        try (Writer w = new FileWriter(file)) {
            p.store(w, null);
        }
    }
    /**
     * 
     * @param obj
     * @param file
     * @return 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.NoSuchFieldException 
     * @throws java.lang.IllegalAccessException 
     */
    public static Object load(Object obj,File file ) throws FileNotFoundException, IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException  {
        try (Reader r = new FileReader(file)) {
            p.load(r);
        }
        Field[] fs = obj.getClass().getDeclaredFields();
        Class c = obj.getClass();
        if (fs != null) {
            for (Field f : fs) {
            	f.setAccessible(true);
            	Field ff = c.getDeclaredField(f.getName());
            	ff.setAccessible(true);
                ff.set(obj, p.getProperty(f.getName()));
            }
        }else{
            return null;
        }
        return obj;
    }
    
//    public static boolean ifFileNotExists_Create(String fileStr) throws IOException{
//        File file = new File(fileStr);
//        if (!file.exists()) {
//            file.createNewFile();
//            return false;
//        };
//        return true;
//    }
//    public static boolean ifFolderNotExists_Create(String fileStr) throws IOException{
//        File file = new File(fileStr);
//        if (!file.isDirectory()) {
//            file.mkdirs();
//            return false;
//        };
//        return true;
//    }
    

}