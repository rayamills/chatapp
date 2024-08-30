<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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

        #chat-window 
        {
            border: 1px solid #ddd;
            width: 100%;
            max-width: 400px;
            height: 300px;
            overflow-y: scroll;
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
            text-align: left;   
            box-sizing: border-box; 
        }

        .message 
        {
            margin: 5px 0;
            padding: 5px;
            border-radius: 4px;
        }

        .sender 
        {
            font-weight: bold;
        }

        .timestamp 
        {
            font-size: small;
            color: gray;
        }

        #message
        {
            width: 70%;
            padding: 10px;
            border: 1px solid #0071BC;
            border-radius: 4px;
            box-sizing: border-box;
            margin-right: 10px;
        }

        button 
        {
            padding: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            background-color: #0071BC;
            color: white;
            font-weight: bold;
            transition: background-color 0.3s;
            width: 25%; 
        }

        button:hover 
        {
            background-color: #005fa3;
        }

        .input-container 
        {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 20px;
            max-width: 400px;
        }

        .input-container button 
        {
            width: 30%;
        }
    </style>
    <script>
        let username = "<%= request.getSession().getAttribute("username") %>"; //get username from session
        let recipient = "<%= request.getParameter("recipient") %>"; //get recipient from request parameter
        let socket = new WebSocket("ws://localhost:8080/ChatApp/chat?username=" + username + "&recipient=" + recipient); //establish WebSocket connection to server

        socket.onopen = function() 
        {
            console.log("WebSocket connection established."); //log successful WebSocket connection
        };

        socket.onmessage = function(event) 
        {
            let chatWindow = document.getElementById("chat-window"); //get chat window element
            let message = document.createElement("div"); //create new div element for message
            message.className = "message"; 
            message.textContent = event.data; //set text content to message data received from WebSocket
            chatWindow.appendChild(message); //add new message to chat window
            chatWindow.scrollTop = chatWindow.scrollHeight; //scroll to bottom of chat window
        };

        socket.onerror = function(error) 
        {
            console.error("WebSocket Error: " + error.message); //log WebSocket error
        };

        socket.onclose = function() 
        {
            console.log("WebSocket connection closed."); //log when WebSocket connection is closed
        };

        function sendMessage() 
        {
            let messageInput = document.getElementById("message"); //get input field for message
            let message = username + ":" + recipient + ":" + messageInput.value; //format message with username and recipient
            if (socket.readyState === WebSocket.OPEN) 
            {
                socket.send(message); //send message through WebSocket
                messageInput.value = ""; //clear input field after sending message
            } 
            else 
            {
                console.error("WebSocket is not open. Cannot send message."); //log error if WebSocket is not open
            }
        }
    </script>
</head>
<body>
    <div>
        
        <div id="chat-window">
            <c:forEach var="msg" items="${messages}"> <!--iterate over messages and display each-->
                <div class="message">
                    <span class="sender">${msg.sender}</span>: <!--display sender of message-->
                    ${msg.message} <!--display content of message-->
                    <span class="timestamp">(${msg.timestamp})</span> <!--display timestamp of message-->
                </div>
            </c:forEach>
        </div>
        
        <div class="input-container">
            <input type="text" id="message" placeholder="Type your message here..." /> <!--input field for typing message-->
            <button onclick="sendMessage()">Send</button> <!--button to send message-->
        </div>
        
    </div>
</body>
</html>
