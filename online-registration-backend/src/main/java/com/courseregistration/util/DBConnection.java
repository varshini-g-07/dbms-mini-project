package com.courseregistration.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dbms-miniproject";
    private static final String USER = "root";
    private static final String PASSWORD = "faunandfloraas";

    public static Connection getConnection() throws SQLException {
        System.out.println("creating connection here...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
