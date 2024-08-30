package com.rayasapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet 
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
                //query to check if username & password match
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, username); //sets username in SQL query
                    statement.setString(2, password); //sets password in SQL query

                    //execute query
                    try (ResultSet resultSet = statement.executeQuery()) 
                    {

                        //check if user exists
                        if (resultSet.next()) 
                        {
                            //user authenticated
                            request.getSession().setAttribute("username", username); //stores username in session
                            response.sendRedirect("home.jsp"); //redirects to home page after successful login
                        } 
                        else 
                        {
                            //authentication fail -> redirect back to login page with error
                            response.sendRedirect("login.jsp?error=Invalid username or password"); 
                        }//if/else
                    }//try
                }//try
            }//try
        }//try 
        catch (ClassNotFoundException e) 
        {
            throw new ServletException("MySQL JDBC Driver not found", e); //throws ServletException if JDBC driver is not found
        }
        catch (SQLException e) 
        {
            throw new ServletException("Database connection error", e); //throws ServletException if there is a database error
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        //back to login page if request is get
        response.sendRedirect("login.jsp"); 
    }
}
