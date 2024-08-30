package com.rayasapp.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register") 
public class RegistrationServlet extends HttpServlet 
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chatapp"; //database URL for connection
    private static final String DB_USER = "root"; //database username
    private static final String DB_PASSWORD = ""; //database password

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        //get username and password from request
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try 
        {
            //explicitly load mysql JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            //connect to database
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
            {
                //insert statement
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, username); //sets username in SQL query
                    statement.setString(2, password); //sets password in SQL query

                    int rowsInserted = statement.executeUpdate(); //executes update and returns number of rows inserted
                    
                    if (rowsInserted > 0) 
                    {
                        response.sendRedirect("login.jsp?message=Registration successful, please log in."); //redirects to login page with success message
                    } 
                    else 
                    {
                        response.sendRedirect("register.jsp?error=Registration failed, please try again."); //redirects to register page with error message
                    }//If/Else
                }//Try
            }//Try
        }//try
        
        catch (ClassNotFoundException e) 
        {
            throw new ServletException("MySQL JDBC Driver not found", e); //throws ServletException if JDBC driver is not found
        }//catch
        catch (SQLException e) 
        {
            throw new ServletException("Database connection error", e); //throws ServletException if there is a database error
        }//catch
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        response.sendRedirect("register.jsp"); //redirects to register page for GET requests
    }//throw
}//class
