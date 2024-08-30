<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
        
        h1
        {
            color: #0071BC;
            font-weight: bold;
            text-align: center;
            margin-bottom: 30px;
        }

        .container
        {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        
        .button
        {
            text-decoration: none;      
            font-weight: bold;         
            color: white;           
            background-color: #0071BC;
            padding: 10px 20px;
            border-radius: 4px;
            margin: 20px 0;            
            font-size: 130%;         
            text-align: center;
            width: 100%;
            max-width: 200px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .button:hover 
        {
            background-color: #005fa3;
        }
    </style>
</head>
<body>
    <div class="container">
        
        <h1>Welcome, <%= request.getSession().getAttribute("username") %>.</h1> <!--displays personalized welcome message using username from session-->
        
        <a href="conversations" class="button">Conversations</a>
        <a href="index.html" class="button">Logout</a>
        
    </div>
</body>
</html>
