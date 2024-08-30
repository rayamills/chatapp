package com.rayasapp.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection 
{
    //create database connection
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/chatapp"; //database URL for connection
    private static final String jdbcUsername = "root"; //database username
    private static final String jdbcPassword = ""; //database password

    public static Connection getConnection() throws SQLException 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver"); //loads MySQL JDBC driver
        } 
        catch (ClassNotFoundException e) 
        {
            throw new SQLException("MySQL JDBC Driver not found");
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword); //returns database connection
    }
}
