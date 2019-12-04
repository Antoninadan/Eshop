package com.mainacad.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

class ConnectionToDB { // need package access
    private static final Logger LOG = Logger.getLogger(ConnectionToDB.class.getName());
    private static final String URL = "jdbc:postgresql://localhost:5433/e_shop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgresql";

    static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL,USER, PASSWORD);
        } catch (SQLException e) {
            LOG.severe("Connection to db denied");
        }
        return connection;
    }
}
