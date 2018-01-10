package com.loquat.autocreatesql.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectionManager {

    private final String driver;
    private final String url;
    private final String user;// oracle��ݿ���û���
    private final String pwd;// oracle��
    private static ConnectionManager connectionManager;

    private ConnectionManager(final String driver, final String url, final String user, final String pwd) throws ClassNotFoundException, SQLException {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(
                url, user, pwd);
        this.setConnection(conn);
//        this.connectionHolder = new ThreadLocal<Connection>() {
//            @Override
//            protected Connection initialValue() {
//                try {
//                    Connection conn ;
//                    Class.forName(driver);
//                    conn = DriverManager.getConnection(
//                            url, user, pwd);
//                    return conn;
//                } catch (ClassNotFoundException | SQLException ex) {
//                    Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
//                    throw new RuntimeException(ex);
//                }
//            }
//        };
    }

    private  ThreadLocal<Connection> connectionHolder;

    public static Connection getConnection(String driver, String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        if (connectionManager == null||connectionManager.getConnectionHolder().get().isClosed()) {
            connectionManager = new ConnectionManager(driver, url, user, pwd);
        }
        return connectionManager.getConnectionHolder().get();
    }

    private void setConnection(Connection conn) {
        connectionHolder = new ThreadLocal<>();
        connectionHolder.set(conn);
    }

    private ThreadLocal<Connection> getConnectionHolder() {
        return connectionHolder;
    }

}
