<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Poppins', Arial, sans-serif;
            background-color: #000000;
            color: #ffffff;
            line-height: 1.6;
            padding: 1rem;
        }
        nav {
            background: linear-gradient(45deg, #ff007a, #c400c4);
            padding: 1rem 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            margin-bottom: 2rem;
            text-align: center;
        }
        nav a {
            color: #ffffff;
            text-decoration: none;
            margin: 0 1.5rem;
            font-weight: 500;
            font-size: 1.1rem;
            transition: opacity 0.3s ease, transform 0.2s ease;
        }
        nav a:hover {
            opacity: 0.9;
            transform: translateY(-2px);
        }
        h1 {
            color: #ffffff;
            text-align: center;
            margin: 1.5rem 0;
            font-size: 2.2rem;
            background: linear-gradient(45deg, #ff007a, #ffffff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        p.error {
            color: #ff4d8a;
            text-align: center;
            font-weight: 500;
            background-color: #2c1c2c;
            padding: 0.8rem;
            border-radius: 6px;
            margin: 1rem auto;
            max-width: 600px;
        }
        a {
            color: #ff007a;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s ease;
        }
        a:hover {
            color: #c400c4;
            text-decoration: underline;
        }
        @media (max-width: 768px) {
            body {
                padding: 0.5rem;
            }
            nav {
                padding: 0.8rem 1rem;
            }
            nav a {
                margin: 0 0.8rem;
                font-size: 1rem;
            }
            h1 {
                font-size: 1.8rem;
            }
            p.error {
                width: 95%;
                margin: 1rem auto;
            }
        }
        @media (max-width: 480px) {
            h1 {
                font-size: 1.5rem;
            }
            nav a {
                display: inline-block;
                margin: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <%@ include file="nav.jsp" %>
    <h1>Error</h1>
    <p class="error"><%= request.getAttribute("error") %></p>
    <p><a href="<%= request.getContextPath() %>/index.jsp">Back to Home</a></p>
</body>
</html>