<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Start New Chat</title>
    <style>
        body 
        {
            font-family: Tahoma, sans-serif;
            background-color: white;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        h1 
        {
            color: #0071BC;
            text-align: center;
            font-weight: bold;
        }

        ul 
        {
            list-style: none;
            padding: 0;
            margin: 0;
            text-align: center; 
        }

        ul li 
        {
            margin-bottom: 10px;
        }

        ul li a 
        {
            text-decoration: none;
            color: white;
            font-weight: bold;
            display: inline-block;
            padding: 10px;
            background-color: #0071BC;
            border-radius: 4px;
            transition: background-color 0.3s;
            width: 200px; 
            text-align: center;
        }

        ul li a:hover 
        {
            background-color: #005fa3; 
        }
    </style>
</head>
<body>
    <div>
        
        <h1>Your Chats</h1>
        
        <ul>
            <c:forEach var="user" items="${users}"> <!--loops through each user in 'users' list to generate chat links-->
                <li><a href="chat.jsp?recipient=${user}">${user}</a></li> <!--creates clickable link for each user to start chat-->
            </c:forEach>
        </ul> 
 
    </div>
</body>
</html>
