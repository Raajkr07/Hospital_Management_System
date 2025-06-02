<%-- 
    Document   : viewData
    Created on : 9 Apr 2025, 12:42:49 am
    Author     : raj kumar
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>📋 Hospital Data View</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f8ff;
            padding: 20px;
        }

        h1, h2 {
            text-align: center;
            color: #4682b4;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0px 4px 8px rgba(0,0,0,0.1);
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #4682b4;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .back-btn {
            display: block;
            width: 200px;
            margin: 30px auto;
            padding: 10px;
            text-align: center;
            background-color: #4682b4;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .back-btn:hover {
            background-color: #4169e1;
        }
    </style>
</head>
<body>

    <h1>🏥 Hospital Management - Data View</h1>

    <h2>🧍 Patient List</h2>
    <table>
        <tr>
            <th>Name</th>
            <th>Age</th>
            <th>Gender</th>
            <th>Disease</th>
        </tr>
        <%
            List<Map<String, Object>> patients = (List<Map<String, Object>>) request.getAttribute("patients");
            if (patients != null && !patients.isEmpty()) {
                for (Map<String, Object> patient : patients) {
        %>
        <tr>
            <td><%= patient.get("name") %></td>
            <td><%= patient.get("age") %></td>
            <td><%= patient.get("gender") %></td>
            <td><%= patient.get("disease") %></td>
        </tr>
        <%      }
            } else { %>
        <tr>
            <td colspan="4">No patient records found.</td>
        </tr>
        <% } %>
    </table>

    <h2>👩‍⚕ Doctor List</h2>
    <table>
        <tr>
            <th>Name</th>
            <th>Age</th>
            <th>Gender</th>
            <th>Specialty</th>
        </tr>
        <%
            List<Map<String, Object>> doctors = (List<Map<String, Object>>) request.getAttribute("doctors");
            if (doctors != null && !doctors.isEmpty()) {
                for (Map<String, Object> doctor : doctors) {
        %>
        <tr>
            <td><%= doctor.get("name") %></td>
            <td><%= doctor.get("age") %></td>
            <td><%= doctor.get("gender") %></td>
            <td><%= doctor.get("specialty") %></td>
        </tr>
        <%      }
            } else { %>
        <tr>
            <td colspan="4">No doctor records found.</td>
        </tr>
        <% } %>
    </table>

    <a class="back-btn" href="HospitalManagement">← Back to Home</a>
</body>
</html>
