/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loquat.autocreatesql.utils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Administrator
 */
public class JFrameUtil {

    private JFrameUtil() {
    }

    public static boolean isAnyBlank(String... strs) {
        return StringUtils.isAnyBlank(strs);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static void alert(String str) {
        if (!isBlank(str)) {
            JOptionPane.showMessageDialog(null, str);
        }
    }

    public static boolean createFolderAndFile(String file) throws IOException {
        String ff = StringUtils.substringBeforeLast(file, "/");
        if (ff.equals(file)) {
            ff = StringUtils.substringBeforeLast(file, "//");
        }
        if (ff.equals(file)) {
            ff = StringUtils.substringBeforeLast(file, "\\");
        }
        if (ff.equals(file)) {
            ff = StringUtils.substringBeforeLast(file, "\\\\");
        }
        String f = StringUtils.substringAfterLast(file, "/");
        if (f.equals(file)) {
            f = StringUtils.substringAfterLast(file, "//");
        }
        if (f.equals(file)) {
            f = StringUtils.substringAfterLast(file, "\\");
        }
        if (f.equals(file)) {
            f = StringUtils.substringAfterLast(file, "\\\\");
        }
        File fileff = new File(ff);
        File filef = new File(ff, f);
        if (!fileff.isDirectory()) {
            fileff.mkdirs();
            if (!filef.exists()) {
                filef.createNewFile();
                return true;
            }
        }
        return true;
    }

    public static void openExplorer(String str) throws IOException {
        if (!isBlank(str)) {
            Runtime.getRuntime().exec(
                    "cmd.exe /C  start explorer " + str);
        }
    }

    public static final char UNDERLINE = '_';

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel2(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            //String.valueOf(Character.toUpperCase(sb.charAt(position)));  
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "hang_ddd";
        str = underlineToCamel2(str);
        System.out.println(str);
                
    }
}
