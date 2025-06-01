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

            out.println("<html><head><title>Update Users</title><style>");
            out.println("/* [CSS same as before – omitted for brevity] */");
            out.println("</style></head><body>");
            out.println("<div class='nav'><div class='nav-title'>User Management Panel</div><ul>"
                    + "<li><a href='index.html'>Add User</a></li>"
                    + "<li><a href='fetchdata'>Fetch User</a></li>"
                    + "<li><a href='deleteuser'>Delete User</a></li>"
                    + "</ul></div>");

            out.println("<form action='updateuser' method='post'>");
            out.println("<div class='table-container'><h2>User List - Update Users</h2>");
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
                    String value = rset.getString(i);
                    out.println("<td><input type='text' name='" + colName + "_" + id + "' value='" + value + "'></td>");
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

                // Build the UPDATE query
                StringBuilder updateQuery = new StringBuilder("UPDATE users SET ");
                for (int i = 2; i <= columnCount; i++) {
                    updateQuery.append(rsmd.getColumnName(i)).append(" = ?");
                    if (i < columnCount) updateQuery.append(", ");
                }
                updateQuery.append(" WHERE id = ?");

                PreparedStatement updateStmt = conn.prepareStatement(updateQuery.toString());

                // Set all column values from request
                for (int i = 2; i <= columnCount; i++) {
                    String colName = rsmd.getColumnName(i);
                    String param = request.getParameter(colName + "_" + userId);
                    updateStmt.setString(i - 1, param);
                }

                // Set ID at last parameter
                updateStmt.setInt(columnCount, userId);
                updateStmt.executeUpdate();
                updateStmt.close();
            }

            pstmt.close();
            conn.close();
            response.sendRedirect("updateuser"); // refresh updated list

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
