package com.vanshalusermanagement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class UpdateUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/usermanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "vans708124";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            String query = "SELECT * FROM users";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            ResultSetMetaData rsmd = rset.getMetaData();
            int columnCount = rsmd.getColumnCount();

            out.println("<html><head><meta charset='UTF-8'><title>Update Users</title>");
            out.println("<style>"
                    + "body { margin: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"
                    + "background: linear-gradient(135deg, #d5f0ee, #b8c1ec); display: flex; flex-direction: column;"
                    + "align-items: center; min-height: 100vh; }"
                    + ".nav { width: 100%; background: linear-gradient(to right, #0f2027, #2c5364); padding: 15px 0;"
                    + "box-shadow: 0 4px 10px rgba(0,0,0,0.3); }"
                    + ".nav-title { color: #fff; font-size: 26px; font-weight: bold; text-align: center; margin-bottom: 10px; }"
                    + ".nav ul { list-style-type: none; display: flex; justify-content: center; margin: 0; padding: 0; }"
                    + ".nav ul li { margin: 0 20px; }"
                    + ".nav ul li a { color: #ffeb3b; text-decoration: none; font-size: 18px; font-weight: 600; transition: color 0.3s, text-shadow 0.3s; }"
                    + ".nav ul li a:hover { color: #ffffff; text-shadow: 0 0 10px rgba(255,255,255,0.7); }"

                    + ".table-container { margin-top: 40px; background: white; padding: 30px; border-radius: 16px;"
                    + "box-shadow: 0 12px 24px rgba(0, 0, 0, 0.2); width: 90%; max-width: 900px; overflow-x: auto; }"
                    + "table { width: 100%; border-collapse: collapse; }"
                    + "th, td { padding: 14px 20px; text-align: center; border: 1px solid #ccc; }"
                    + "th { background: linear-gradient(to right, #2c5364, #0f2027); color: white; }"
                    + "tr:nth-child(even) { background-color: #f0f0f0; }"
                    + "input[type='text'], input[type='date'], select { width: 90%; padding: 8px; border-radius: 6px; border: 1px solid #aaa; }"

                    + "input[type='submit'] { padding: 12px 24px; background: linear-gradient(to right, #0f2027, #2c5364);"
                    + "color: #ffeb3b; font-size: 18px; font-weight: 600; border: none; border-radius: 8px; cursor: pointer;"
                    + "transition: color 0.3s, transform 0.3s, box-shadow 0.3s; margin-top: 20px; }"
                    + "input[type='submit']:hover { color: #ffffff; transform: scale(1.05);"
                    + "text-shadow: 0 0 8px rgba(255,255,255,0.6);"
                    + "box-shadow: 0 0 10px rgba(44, 83, 100, 0.5); }"
                    + "</style></head>");

            out.println("<body>");
            out.println("<div class='nav'>"
                    + "<div class='nav-title'>User Management Panel</div>"
                    + "<ul>"
                    + "<li><a href='index.html'>Add User</a></li>"
                    + "<li><a href='fetchdata'>Fetch User</a></li>"
                    + "<li><a href='deleteuser'>Delete User</a></li>"
                    + "</ul></div>");

            out.println("<form action='updateuser' method='post'>");
            out.println("<div class='table-container'>");
            out.println("<h2>User List - Update Users</h2>");
            out.println("<table><tr><th>ID</th>");

            for (int i = 2; i <= columnCount; i++) {
                out.println("<th>" + rsmd.getColumnName(i) + "</th>");
            }
            out.println("</tr>");

            while (rset.next()) {
                int id = rset.getInt("id");
                out.println("<tr>");
                out.println("<td>" + id + "<input type='hidden' name='id_" + id + "' value='" + id + "'></td>");

                for (int i = 2; i <= columnCount; i++) {
                    String colName = rsmd.getColumnName(i);
                    String colKey = colName.toLowerCase(); // to avoid case sensitivity
                    String value = rset.getString(i);

                    if (colKey.contains("date")) {
                        out.println("<td><input type='calender' name='" + colName + "_" + id + "' value='" + value + "'></td>");
                    } else if (colKey.equals("gender")) {
                        out.println("<td><select name='" + colName + "_" + id + "'>");
                        out.println("<option value='Male'" + ("Male".equalsIgnoreCase(value) ? " selected" : "") + ">Male</option>");
                        out.println("<option value='Female'" + ("Female".equalsIgnoreCase(value) ? " selected" : "") + ">Female</option>");
                        out.println("<option value='Other'" + ("Other".equalsIgnoreCase(value) ? " selected" : "") + ">Other</option>");
                        out.println("</select></td>");
                    } else {
                        out.println("<td><input type='text' name='" + colName + "_" + id + "' value='" + value + "'></td>");
                    }
                }

                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<input type='submit' value='Update All Users'>");
            out.println("</div></form></body></html>");

            rset.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String fetchQuery = "SELECT * FROM users";
            PreparedStatement pstmt = conn.prepareStatement(fetchQuery);
            ResultSet rset = pstmt.executeQuery();
            ResultSetMetaData rsmd = rset.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rset.next()) {
                int userId = rset.getInt("id");

                StringBuilder updateQuery = new StringBuilder("UPDATE users SET ");
                for (int i = 2; i <= columnCount; i++) {
                    updateQuery.append(rsmd.getColumnName(i)).append(" = ?");
                    if (i < columnCount) updateQuery.append(", ");
                }
                updateQuery.append(" WHERE id = ?");

                PreparedStatement updateStmt = conn.prepareStatement(updateQuery.toString());
                for (int i = 2; i <= columnCount; i++) {
                    String colName = rsmd.getColumnName(i);
                    String param = request.getParameter(colName + "_" + userId);
                    updateStmt.setString(i - 1, param);
                }

                updateStmt.setInt(columnCount, userId);
                updateStmt.executeUpdate();
                updateStmt.close();
            }

            pstmt.close();
            conn.close();
            response.sendRedirect("updateuser");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
