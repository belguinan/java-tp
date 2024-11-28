package com.jdbc;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private String dbUser = "root";
    private String dbPassword = "root";

    private Connection connection;
    private static Database instance;
    
    private Database() {
        this.connection = this.createConnection();
    }

    public Connection createConnection() {
        try {
            return (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", this.dbUser, this.dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        if (this.connection == null) {
            this.connection = this.createConnection();
        }
        
        return this.connection;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }
}