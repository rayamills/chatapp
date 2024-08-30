package com.rayasapp.servlet;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.util.Map;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.websocket.server.ServerEndpoint;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
public class ChatServlet 
{
    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>(); //stores active user sessions

    @OnOpen
    public void onOpen(Session session) 
    {
        String username = session.getRequestParameterMap().get("username").get(0); //gets username from session request parameters
        session.getUserProperties().put("username", username); //stores username in session user properties
        userSessions.put(username, session); //adds session to userSessions map
        System.out.println("User connected: " + username);

        //Get recipient from session
        String recipient = session.getRequestParameterMap().get("recipient") != null ? session.getRequestParameterMap().get("recipient").get(0) : null;

        if (recipient != null) 
        {
            //loads previous conversation between users
            try 
            {
                loadMessageHistory(session, username, recipient); //loads message history for user and recipient
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException 
    {
        //splits message into sender, recipient, message content
        String[] messageParts = message.split(":", 3);
        if (messageParts.length != 3) 
        {
            System.out.println("Invalid message format: " + message);
            return; //exits if message format is invalid
        }

        String sender = messageParts[0]; //extracts sender from message
        String recipient = messageParts[1]; //extracts recipient from message
        String messageText = messageParts[2]; //extracts message content

        System.out.println("Received message from " + sender + " to " + recipient + ": " + messageText);

        //saves message to database
        saveMessageToDatabase(sender, recipient, messageText);

        //formats message to send to client
        String formattedMessage = sender + ": " + messageText;

        //sends message to recipient
        Session recipientSession = userSessions.get(recipient); //retrieves recipient's session from userSessions map
        if (recipientSession != null && recipientSession.isOpen()) 
        {
            recipientSession.getBasicRemote().sendText(formattedMessage); //sends message to recipient if session is open
        }
        else 
        {
            System.out.println("Recipient session not found or not open for " + recipient);
        }

        //sends message back to sender to display
        session.getBasicRemote().sendText("You: " + messageText);
    }

    @OnClose
    public void onClose(Session session) 
    {
        String username = (String) session.getUserProperties().get("username"); //gets username from session user properties
        userSessions.remove(username); //removes session from userSessions map
        System.out.println("User disconnected: " + username);
    }

    private void loadMessageHistory(Session session, String sender, String recipient) throws IOException 
    {
        try (Connection con = DatabaseConnection.getConnection()) 
        {
            String SQL = "SELECT sender, message, timestamp FROM messages WHERE " +
                         "(sender = ? AND recipient = ?) OR (sender = ? AND recipient = ?) ORDER BY timestamp";

            try (PreparedStatement statement = con.prepareStatement(SQL)) 
            {
                statement.setString(1, sender); //sets sender as current user
                statement.setString(2, recipient); //sets recipient as other user
                statement.setString(3, recipient); //sets recipient as current user
                statement.setString(4, sender); //sets sender as other user

                try (ResultSet resultSet = statement.executeQuery()) 
                {
                    //sends each message to client
                    while (resultSet.next()) 
                    {
                        String msgSender = resultSet.getString("sender");
                        String msgText = resultSet.getString("message");
                        String msgTimestamp = resultSet.getString("timestamp");

                        String formattedMessage = msgSender + " (" + msgTimestamp + "): " + msgText; //formats message with timestamp
                        session.getBasicRemote().sendText(formattedMessage); //sends formatted message to client
                    }
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private void saveMessageToDatabase(String sender, String recipient, String message) 
    {
        try (Connection con = DatabaseConnection.getConnection()) 
        {
            //saves messages to database
            String SQL = "INSERT INTO messages (sender, recipient, message) VALUES (?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(SQL)) 
            {
                statement.setString(1, sender); //sets sender for SQL query
                statement.setString(2, recipient); //sets recipient for SQL query
                statement.setString(3, message); //sets message content for SQL query
                statement.executeUpdate(); //executes query to save message
                System.out.println("Message saved to database.");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
