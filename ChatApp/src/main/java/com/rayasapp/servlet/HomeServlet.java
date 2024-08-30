package com.rayasapp.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet 
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException 
    {
        HttpSession session = request.getSession(false); //retrieves current session without creating new one
        if (session != null && session.getAttribute("username") != null) //checks if session exists and has 'username' attribute
        {
            request.getRequestDispatcher("home.jsp").forward(request, response); //forwards request to 'home.jsp' if user is logged in
        } 
        else 
        {
            response.sendRedirect("login.jsp"); //redirects to 'login.jsp' if no user is logged in
        }
    }//get

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException 
    {
        HttpSession session = request.getSession(false); //retrieves current session without creating new one
        if (session != null && session.getAttribute("username") != null) //checks if session exists and has 'username' attribute
        {
            response.sendRedirect("login.jsp"); //redirects to 'login.jsp' if user is already logged in
        }
    }//post
}
