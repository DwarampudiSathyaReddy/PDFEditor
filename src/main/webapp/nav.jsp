<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
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
    @media (max-width: 768px) {
        nav {
            padding: 0.8rem 1rem;
        }
        nav a {
            margin: 0 0.8rem;
            font-size: 1rem;
        }
    }
    @media (max-width: 480px) {
        nav a {
            display: inline-block;
            margin: 0.5rem;
        }
    }
</style>
<nav>
    <a href="<%= request.getContextPath() %>/index.jsp">Home</a>
    <a href="<%= request.getContextPath() %>/upload.jsp">Upload PDF</a>
    <a href="<%= request.getContextPath() %>/history.jsp">History</a>
    <a href="<%= request.getContextPath() %>/preview.jsp">Preview PDF</a>
</nav>