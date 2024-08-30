<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <style>
        body 
        {
            font-family: Tahoma, sans-serif;
            display: flex;   
            justify-content: center;
            align-items: center;
            height: 100vh; 
            margin: 0;                  
            background-color: white;    
        }

        h2, h1 
        {
            color: #0071BC;
            text-align: center;
            font-weight: bold;
        }

        form 
        {
            margin-top: 20px;           
            text-align: center;           
        }

        input[type="text"],
        input[type="password"] 
        {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #0071BC;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] 
        {
            background-color: #0071BC;
            color: white;
            font-weight: bold;
            padding: 10px;
            border: none; 
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
            margin-top: 10px;
        }

        input[type="submit"]:hover 
        {
            background-color: #005fa3; 
        }

        .error-message 
        {
            color: red;                
            margin-top: 10px;          
            text-align: center;     
        }
    </style>
</head>
<body>
    <div>
        
        <h2>Register</h2> 
        
        <form action="register" method="post"> <!--submits registration form data to 'register' endpoint-->
            <input type="text" id="username" name="username" placeholder="Username" required> <!--input field for username, required-->
            <input type="password" id="password" name="password" placeholder="Password" required> <!--input field for password, required-->
            <input type="submit" value="Register"> 
        </form>
        
        <%
            //checks if error parameter is present in request
            String error = request.getParameter("error");
            if (error != null) 
            {
        %>
            <p class="error-message"><%= error %></p> 
        <%
            }
        %>
        
    </div>
</body>

</html>
