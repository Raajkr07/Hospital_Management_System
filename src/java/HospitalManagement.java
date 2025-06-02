import java.io.*;
import java.sql.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/HospitalManagement")
public class HospitalManagement extends HttpServlet {

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        String url = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Raj@2003";
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = connect()) {
            if (null != action) switch (action) {
                case "addPatient" -> {
                    String name = request.getParameter("name");
                    int age = Integer.parseInt(request.getParameter("age"));
                    String gender = request.getParameter("gender");
                    String disease = request.getParameter("disease");
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO patients (name, age, gender, disease) VALUES (?, ?, ?, ?)");
                    stmt.setString(1, name);
                    stmt.setInt(2, age);
                    stmt.setString(3, gender);
                    stmt.setString(4, disease);
                    stmt.executeUpdate();
                    respondJson(response, "{\"message\":\"Patient added successfully!\"}");
                    }
                case "addDoctor" -> {
                    String name = request.getParameter("name");
                    int age = Integer.parseInt(request.getParameter("age"));
                    String gender = request.getParameter("gender");
                    String specialty = request.getParameter("specialty");
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO doctors (name, age, gender, specialty) VALUES (?, ?, ?, ?)");
                    stmt.setString(1, name);
                    stmt.setInt(2, age);
                    stmt.setString(3, gender);
                    stmt.setString(4, specialty);
                    stmt.executeUpdate();
                    respondJson(response, "{\"message\":\"Doctor added successfully!\"}");
                    }
                case "deletePatient" -> {
                    String name = request.getParameter("name");
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM patients WHERE name = ?");
                    stmt.setString(1, name);
                    int rows = stmt.executeUpdate();
                    respondJson(response, "{\"message\":\"" + rows + " patient(s) deleted.\"}");
                    }
                case "deleteDoctor" -> {
                    String name = request.getParameter("name");
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM doctors WHERE name = ?");
                    stmt.setString(1, name);
                    int rows = stmt.executeUpdate();
                    respondJson(response, "{\"message\":\"" + rows + " doctor(s) deleted.\"}");
                    }
                default -> {
                }
            }

        } catch (SQLException e) {
            respondJson(response, "{\"error\":\"Database Error: " + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("view".equals(action)) {
            try (Connection conn = connect()) {
                List<Map<String, Object>> patients = new ArrayList<>();
                List<Map<String, Object>> doctors = new ArrayList<>();

                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM patients");
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", rs.getString("name"));
                    row.put("age", rs.getInt("age"));
                    row.put("gender", rs.getString("gender"));
                    row.put("disease", rs.getString("disease"));
                    patients.add(row);
                }

                rs = conn.createStatement().executeQuery("SELECT * FROM doctors");
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", rs.getString("name"));
                    row.put("age", rs.getInt("age"));
                    row.put("gender", rs.getString("gender"));
                    row.put("specialty", rs.getString("specialty"));
                    doctors.add(row);
                }

                StringBuilder json = new StringBuilder();
                json.append("{\"patients\":[");
                for (int i = 0; i < patients.size(); i++) {
                    Map<String, Object> p = patients.get(i);
                    json.append("{\"name\":\"").append(p.get("name")).append("\",")
                        .append("\"age\":").append(p.get("age")).append(",")
                        .append("\"gender\":\"").append(p.get("gender")).append("\",")
                        .append("\"disease\":\"").append(p.get("disease")).append("\"}");
                    if (i < patients.size() - 1) json.append(",");
                }
                json.append("],");

                json.append("\"doctors\":[");
                for (int i = 0; i < doctors.size(); i++) {
                    Map<String, Object> d = doctors.get(i);
                    json.append("{\"name\":\"").append(d.get("name")).append("\",")
                        .append("\"age\":").append(d.get("age")).append(",")
                        .append("\"gender\":\"").append(d.get("gender")).append("\",")
                        .append("\"specialty\":\"").append(d.get("specialty")).append("\"}");
                    if (i < doctors.size() - 1) json.append(",");
                }
                json.append("]}");

                respondJson(response, json.toString());
            } catch (SQLException e) {
                respondJson(response, "{\"error\":\"Database Error: " + e.getMessage().replace("\"", "'") + "\"}");
            }
        }
    }

    private void respondJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
    }
}
