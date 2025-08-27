<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.example.survey.ConversionHistory" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Conversion History</title>
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
        table {
            width: 90%;
            max-width: 800px;
            margin: 2rem auto;
            border-collapse: collapse;
            background-color: #1c1c1c;
            box-shadow: 0 6px 16px rgba(255, 0, 122, 0.3);
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            padding: 1.2rem;
            text-align: left;
            border-bottom: 1px solid #333;
        }
        th {
            background: linear-gradient(45deg, #ff007a, #c400c4);
            font-weight: 600;
            color: #ffffff;
            font-size: 1.1rem;
        }
        td {
            color: #e0e0e0;
        }
        td a {
            color: #ff007a;
            text-decoration: none;
            margin-right: 1rem;
            font-weight: 500;
            transition: color 0.3s ease;
        }
        td a:hover {
            color: #c400c4;
            text-decoration: underline;
        }
        p {
            text-align: center;
            color: #e0e0e0;
            margin: 1rem 0;
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
            table {
                width: 95%;
                margin: 1rem auto;
            }
            th, td {
                padding: 0.8rem;
                font-size: 0.9rem;
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
    <h1>Conversion History</h1>
    <% List<ConversionHistory> history = (List<ConversionHistory>) request.getAttribute("history");
        if (history == null || history.isEmpty()) { %>
        <p>No history found</p>
    <% } else { %>
        <table>
            <tr>
                <th>File Name</th>
                <th>Formatting Option</th>
                <th>Timestamp</th>
                <th>Actions</th>
            </tr>
            <% for (ConversionHistory item : history) { %>
                <tr>
                    <td><%= item.getFilename() %></td>
                    <td><%= item.getFormattingOption() != null ? item.getFormattingOption() : "None" %></td>
                    <td><%= item.getTimestamp() %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/download?filename=<%= item.getFilename() %>">Download</a>
                        <a href="<%= request.getContextPath() %>/edit?filename=<%= item.getFilename() %>">Edit</a>
                    </td>
                </tr>
            <% } %>
        </table>
    <% } %>
    <p><a href="<%= request.getContextPath() %>/upload.jsp">Convert Another PDF</a></p>
    <p><a href="<%= request.getContextPath() %>/index.jsp">Back to Home</a></p>
</body>
</html>