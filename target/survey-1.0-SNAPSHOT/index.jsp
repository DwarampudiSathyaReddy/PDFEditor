<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PDF to Text Converter</title>
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
        #welcome-message {
            text-align: center;
            max-width: 600px;
            margin: 2rem auto;
            font-size: 1.2rem;
            color: #e0e0e0;
        }
        button {
            background: linear-gradient(45deg, #ff007a, #c400c4);
            color: #ffffff;
            padding: 0.8rem 2rem;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1rem;
            font-weight: 500;
            transition: transform 0.2s ease, box-shadow 0.3s ease;
            margin: 0.5rem;
            display: inline-block;
        }
        button:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 16px rgba(255, 0, 122, 0.6);
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
            #welcome-message {
                font-size: 1rem;
            }
            button {
                padding: 0.7rem 1.5rem;
                font-size: 1rem;
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
            button {
                display: block;
                width: 100%;
                margin: 0.5rem 0;
            }
        }
    </style>
</head>
<body>
    <%@ include file="nav.jsp" %>
    <h1>PDF to Text Converter</h1>
    <p id="welcome-message">Welcome to the PDF to Text Converter! Convert, edit, and download your PDF files with customizable options.</p>
    <div style="text-align: center;">
        <button onclick="window.location.href='<%= request.getContextPath() %>/upload.jsp'">Upload a PDF</button>
        <button onclick="window.location.href='<%= request.getContextPath() %>/history.jsp'">View Conversion History</button>
        <button onclick="window.location.href='<%= request.getContextPath() %>/preview.jsp'">Preview a PDF</button>
    </div>
</body>
</html>