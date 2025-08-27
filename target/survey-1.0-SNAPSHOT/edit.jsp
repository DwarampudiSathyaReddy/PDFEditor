<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit PDF: <%= request.getAttribute("filename") %></title>
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
        form {
            max-width: 700px;
            margin: 2rem auto;
            background: linear-gradient(45deg, #1c1c1c, #2c1c2c);
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(255, 0, 122, 0.3);
        }
        label {
            display: block;
            margin: 0.5rem 0 0.3rem;
            font-weight: 500;
            color: #ffffff;
            font-size: 1.1rem;
        }
        textarea {
            width: 100%;
            padding: 0.8rem;
            margin-bottom: 1.2rem;
            border: 2px solid #ff007a;
            border-radius: 6px;
            background-color: #1c1c1c;
            color: #ffffff;
            font-size: 1rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            min-height: 200px;
            resize: vertical;
        }
        textarea:focus {
            border-color: #c400c4;
            outline: none;
            box-shadow: 0 0 10px rgba(255, 0, 122, 0.5);
        }
        select {
            width: 100%;
            padding: 0.8rem;
            margin-bottom: 1.2rem;
            border: 2px solid #ff007a;
            border-radius: 6px;
            background-color: #1c1c1c;
            color: #ffffff;
            font-size: 1rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        select:focus {
            border-color: #c400c4;
            outline: none;
            box-shadow: 0 0 10px rgba(255, 0, 122, 0.5);
        }
        input[type="submit"] {
            background: linear-gradient(45deg, #ff007a, #c400c4);
            color: #ffffff;
            padding: 0.8rem 2rem;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1rem;
            font-weight: 500;
            transition: transform 0.2s ease, box-shadow 0.3s ease;
        }
        input[type="submit"]:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 16px rgba(255, 0, 122, 0.6);
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
            form {
                width: 95%;
                margin: 1rem auto;
            }
            input[type="submit"] {
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
        }
    </style>
</head>
<body>
    <%@ include file="nav.jsp" %>
    <h1>Edit PDF: <%= request.getAttribute("filename") %></h1>
    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } else { %>
        <form action="<%= request.getContextPath() %>/saveEdit" method="post">
            <label for="content">Edit Content:</label>
            <textarea id="content" name="content" required><%= request.getAttribute("content") %></textarea>
            <input type="hidden" name="filename" value="<%= request.getAttribute("filename") %>">
            <label for="downloadFormat">Download Format:</label>
            <select id="downloadFormat" name="downloadFormat">
                <option value="txt">Text</option>
                <option value="word">Word</option>
            </select>
            <input type="submit" value="Save Edited File">
        </form>
        <p>Edited file: <%= request.getAttribute("filename") %></p>
        <p>
            <a href="<%= request.getContextPath() %>/download?filename=<%= request.getAttribute("filename") %>">Download</a>
            <a href="<%= request.getContextPath() %>/upload.jsp">Upload Another PDF</a>
            <a href="<%= request.getContextPath() %>/history.jsp">View History</a>
        </p>
    <% } %>
</body>
</html>