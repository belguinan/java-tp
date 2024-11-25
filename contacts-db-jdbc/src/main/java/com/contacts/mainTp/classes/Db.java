package com.contacts.mainTp.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

    // Currrent db instance
    private static Db instance;

    // Database creds
    private String dbName = "java_test_db";
    private String dbUser = "root";
    private String dbPassword = "root";

    // Mysql connection
    private Connection conn;
    
    // Private constructor
    private Db() {
        try {
            this.conn = this.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Connection
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        return (Connection) DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/" + this.dbName, 
            this.dbUser, 
            this.dbPassword
        );
    }

    /**
     * @return Connection
     */
    public Connection getConnection() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                return this.conn;
            }

            this.conn = this.createConnection();
        } catch (SQLException e) {
            
        }

        return this.conn;
    }

    /**
     * @return self
     */
    public static Db getInstance() {
        if (instance == null) {
            instance = new Db();
        }

        return instance;
    }
}
