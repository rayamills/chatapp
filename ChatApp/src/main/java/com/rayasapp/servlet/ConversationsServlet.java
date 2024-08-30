package com.rayasapp.servlet;

import java.util.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/conversations") 
public class ConversationsServlet extends HttpServlet 
{  
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
        String user = (String) request.getSession().getAttribute("username");//gets username from session
        System.out.println(user);
        List<String> users = new ArrayList<>();//stores list of users except current user

        try (Connection con = DatabaseConnection.getConnection()) //establishes database connection
        {
            String SQL = "SELECT username FROM users WHERE username != ?"; //query to get all users except current user

            try (PreparedStatement statement = con.prepareStatement(SQL))
            {
                statement.setString(1, user);//sets current username in SQL statement
                try (ResultSet rs = statement.executeQuery()) //executes query and retrieves results
                {
                    while (rs.next()) //iterates through result set
                    {
                        users.add(rs.getString("username")); //adds each username to users list
                    }//while
                }//try
            }//try 
            catch (SQLException e) 
            {
                e.printStackTrace(); 
            }//prep

        }//try 
        catch (SQLException e) 
        {
            response.sendRedirect("index.html");
            e.printStackTrace();
        }//conn

        if (users.isEmpty()) 
        {
            request.setAttribute("message", "No other users are found!"); //sets message attribute if no users found
        }

        request.setAttribute("users", users); //sets users list as request attribute
        request.getRequestDispatcher("conversations.jsp").forward(request, response); //forwards request to conversations.jsp
    }
}
