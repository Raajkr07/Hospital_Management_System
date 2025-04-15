import java.io.*;
import java.sql.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/HospitalManagement")
public class HospitalManagement extends HttpServlet {

    // Database connection method
    private Connection connect() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        // Database URL and credentials
        String url = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Sharad@123";  // Ensure this is the correct password for your MySQL database
        return DriverManager.getConnection(url, user, password);
    }

    // Handle POST requests (Adding, Deleting patients and doctors)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = connect()) {
            if (action != null) {
                switch (action) {
                    case "addPatient" -> {
                        String name = request.getParameter("name");
                        int age = Integer.parseInt(request.getParameter("age"));
                        String gender = request.getParameter("gender");
                        String disease = request.getParameter("disease");

                        // Prepare the SQL query to insert the patient data into the 'patients' table
                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO patients (name, age, gender, disease) VALUES (?, ?, ?, ?)");
                        stmt.setString(1, name);
                        stmt.setInt(2, age);
                        stmt.setString(3, gender);
                        stmt.setString(4, disease);
                        stmt.executeUpdate();

                        // Respond with success message in JSON format
                        respondJson(response, "{\"message\":\"Patient added successfully!\"}");
                    }

                    case "addDoctor" -> {
                        String name = request.getParameter("name");
                        int age = Integer.parseInt(request.getParameter("age"));
                        String gender = request.getParameter("gender");
                        String specialty = request.getParameter("specialty");

                        // Prepare the SQL query to insert the doctor data into the 'doctors' table
                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO doctors (name, age, gender, specialty) VALUES (?, ?, ?, ?)");
                        stmt.setString(1, name);
                        stmt.setInt(2, age);
                        stmt.setString(3, gender);
                        stmt.setString(4, specialty);
                        stmt.executeUpdate();

                        // Respond with success message in JSON format
                        respondJson(response, "{\"message\":\"Doctor added successfully!\"}");
                    }

                    case "deletePatient" -> {
                        String name = request.getParameter("name");
                        if (name == null || name.isEmpty()) {
                            respondJson(response, "{\"error\":\"Patient name is required\"}");
                            return;
                        }

                        // Prepare the SQL query to delete the patient
                        PreparedStatement stmt = conn.prepareStatement("DELETE FROM patients WHERE name = ?");
                        stmt.setString(1, name);
                        int rows = stmt.executeUpdate();

                        // Respond with success message
                        if (rows > 0) {
                            respondJson(response, "{\"message\":\"" + rows + " patient(s) deleted.\"}");
                        } else {
                            respondJson(response, "{\"message\":\"No patient found with the name " + name + "\"}");
                        }
                    }

                    case "deleteDoctor" -> {
                        String name = request.getParameter("name");
                        if (name == null || name.isEmpty()) {
                            respondJson(response, "{\"error\":\"Doctor name is required\"}");
                            return;
                        }

                        // Prepare the SQL query to delete the doctor
                        PreparedStatement stmt = conn.prepareStatement("DELETE FROM doctors WHERE name = ?");
                        stmt.setString(1, name);
                        int rows = stmt.executeUpdate();

                        // Respond with success message
                        if (rows > 0) {
                            respondJson(response, "{\"message\":\"" + rows + " doctor(s) deleted.\"}");
                        } else {
                            respondJson(response, "{\"message\":\"No doctor found with the name " + name + "\"}");
                        }
                    }

                    default -> {
                    }
                }
            }

        } catch (SQLException e) {
            respondJson(response, "{\"error\":\"Database Error: " + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    // Handle GET requests (View patients and doctors data)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("view".equals(action)) {
            try (Connection conn = connect()) {
                List<Map<String, Object>> patients = new ArrayList<>();
                List<Map<String, Object>> doctors = new ArrayList<>();

                // Fetch all patients from the 'patients' table
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM patients");
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", rs.getString("name"));
                    row.put("age", rs.getInt("age"));
                    row.put("gender", rs.getString("gender"));
                    row.put("disease", rs.getString("disease"));
                    patients.add(row);
                }

                // Fetch all doctors from the 'doctors' table
                rs = conn.createStatement().executeQuery("SELECT * FROM doctors");
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", rs.getString("name"));
                    row.put("age", rs.getInt("age"));
                    row.put("gender", rs.getString("gender"));
                    row.put("specialty", rs.getString("specialty"));
                    doctors.add(row);
                }

                // Prepare JSON response with patients and doctors data
                StringBuilder json = new StringBuilder();
                json.append("{\"patients\":[");

                for (int i = 0; i < patients.size(); i++) {
                    Map<String, Object> p = patients.get(i);
                    json.append("{\"name\":\"").append(p.get("name")).append("\",")
                        .append("\"age\":").append(p.get("age")).append(",")
                        .append("\"gender\":\"").append(p.get("gender")).append("\",")
                        .append("\"disease\":\"").append(p.get("disease")).append("\"}");

                    if (i < patients.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("],");

                json.append("\"doctors\":[");

                for (int i = 0; i < doctors.size(); i++) {
                    Map<String, Object> d = doctors.get(i);
                    json.append("{\"name\":\"").append(d.get("name")).append("\",")
                        .append("\"age\":").append(d.get("age")).append(",")
                        .append("\"gender\":\"").append(d.get("gender")).append("\",")
                        .append("\"specialty\":\"").append(d.get("specialty")).append("\"}");

                    if (i < doctors.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]}");

                // Respond with the patient and doctor data in JSON format
                respondJson(response, json.toString());
            } catch (SQLException e) {
                respondJson(response, "{\"error\":\"Database Error: " + e.getMessage().replace("\"", "'") + "\"}");
            }
        }
    }

    // Method to send JSON response
    private void respondJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
    }
}
