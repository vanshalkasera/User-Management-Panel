package com.vanshalusermanagement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class DeleteUser extends HttpServlet {
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

            out.println("<html><head><meta charset='UTF-8'><title>Delete Data</title>");
            out.println("<style>"
                    + "body { margin: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"
                    + "background: linear-gradient(135deg, #ebcb74, #9fb0e6); display: flex;"
                    + "flex-direction: column; align-items: center; min-height: 100vh; }"
                    + ".nav { width: 100%; background: linear-gradient(to right, #182848, #4b6cb7); padding: 15px 0; box-shadow: 0 4px 10px rgba(0,0,0,0.2); }"
                    + ".nav-title { color: #fff; font-size: 26px; font-weight: bold; text-align: center; margin-bottom: 10px; }"
                    + ".nav ul { list-style-type: none; display: flex; justify-content: center; margin: 0; padding: 0; }"
                    + ".nav ul li { margin: 0 20px; }"
                    + ".nav ul li a { color: #ffd700; text-decoration: none; font-size: 18px; font-weight: 600; transition: color 0.3s, text-shadow 0.3s; }"
                    + ".nav ul li a:hover { color: #fff; text-shadow: 0 0 8px rgba(255,255,255,0.6); }"

                    + ".table-container { margin-top: 40px; background: white; padding: 30px; border-radius: 16px;"
                    + "box-shadow: 0 12px 24px rgba(0, 0, 0, 0.2); width: 90%; max-width: 800px; overflow-x: auto; }"
                    + "table { width: 100%; border-collapse: collapse; }"
                    + "th, td { padding: 14px 20px; text-align: center; border: 1px solid #ccc; }"
                    + "th { background: linear-gradient(to right, #4b6cb7, #182848); color: white; }"
                    + "tr:nth-child(even) { background-color: #f2f2f2; }"
                    + "input[type='submit'] { padding: 12px 24px; background: linear-gradient(to right, #182848, #4b6cb7);"
                    + "color: #ffd700; font-size: 18px; font-weight: 600; border: none; border-radius: 8px; cursor: pointer;"
                    + "transition: color 0.3s, transform 0.3s, box-shadow 0.3s; margin-top: 20px; }"
                    + "input[type='submit']:hover { color: #ffffff; transform: scale(1.05);"
                    + "text-shadow: 0 0 8px rgba(255,255,255,0.6);"
                    + "box-shadow: 0 0 10px rgba(75, 108, 183, 0.5); }"
                    + "</style></head>");

            out.println("<body>");
            out.println("<div class='nav'>"
                    + "<div class='nav-title'>User Management Panel</div>"
                    + "<ul>"
                    + "<li><a href='index.html'>Add User</a></li>"
                    + "<li><a href='updateuser'>Update User</a></li>"
                    + "<li><a href='deleteuser'>Delete User</a></li>"
                    + "</ul></div>");

            out.println("<form action='deleteuser' method='post'>");
            out.println("<div class='table-container'>");
            out.println("<h2>User List - Delete Users</h2>");
            out.println("<table><tr><th>Select</th>");

            for (int i = 1; i <= columnCount; i++) {
                out.println("<th>" + rsmd.getColumnName(i) + "</th>");
            }
            out.println("</tr>");

            while (rset.next()) {
                String userId = rset.getString("id");
                out.println("<tr>");
                out.println("<td><input type='checkbox' name='userIds' value='" + userId + "'></td>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<td>" + rset.getString(i) + "</td>");
                }
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<input type='submit' value='Delete Selected Users'>");
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

        String[] selectedUsers = request.getParameterValues("userIds");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (selectedUsers == null || selectedUsers.length == 0) {
            out.println("<h3>No users selected for deletion.</h3>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            if (selectedUsers.length == 1) {
                String query = "DELETE FROM users WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(selectedUsers[0]));
                pstmt.executeUpdate();
                pstmt.close();
            } else {
                StringBuilder query = new StringBuilder("DELETE FROM users WHERE id IN (");
                for (int i = 0; i < selectedUsers.length; i++) {
                    query.append("?");
                    if (i < selectedUsers.length - 1) query.append(",");
                }
                query.append(")");
                PreparedStatement pstmt = conn.prepareStatement(query.toString());
                for (int i = 0; i < selectedUsers.length; i++) {
                    pstmt.setInt(i + 1, Integer.parseInt(selectedUsers[i]));
                }
                pstmt.executeUpdate();
                pstmt.close();
            }

            conn.close();
            response.sendRedirect("deleteuser");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
