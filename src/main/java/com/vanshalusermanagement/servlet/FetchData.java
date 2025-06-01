package com.vanshalusermanagement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
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

            // Begin HTML
            out.println("<html><head><title>User Data</title>");
            out.println("<style>\r\n"
            		+ "    body {\r\n"
            		+ "      margin: 0;\r\n"
            		+ "      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\r\n"
            		+ "      background: linear-gradient(135deg, #ebcb74, #9fb0e6);\r\n"
            		+ "      display: flex;\r\n"
            		+ "      flex-direction: column;\r\n"
            		+ "      align-items: center;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    .nav {\r\n"
            		+ "      width: 100%;\r\n"
            		+ "      background: linear-gradient(to right, #182848, #4b6cb7);\r\n"
            		+ "      color: rgb(241, 173, 16);\r\n"
            		+ "      padding: 20px 0;\r\n"
            		+ "      text-align: center;\r\n"
            		+ "      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    .nav-title {\r\n"
            		+ "      font-size: 32px;\r\n"
            		+ "      font-weight: bold;\r\n"
            		+ "      margin-bottom: 10px;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    .nav ul {\r\n"
            		+ "      list-style-type: none;\r\n"
            		+ "      padding: 0;\r\n"
            		+ "      margin: 0;\r\n"
            		+ "      display: flex;\r\n"
            		+ "      justify-content: center;\r\n"
            		+ "      gap: 30px;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    .nav ul li a {\r\n"
            		+ "      color: #ffd700;\r\n"
            		+ "      text-decoration: none;\r\n"
            		+ "      font-size: 18px;\r\n"
            		+ "      font-weight: 600;\r\n"
            		+ "      transition: color 0.3s, transform 0.3s;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    .nav ul li a:hover {\r\n"
            		+ "      color: #ffffff;\r\n"
            		+ "      transform: scale(1.05);\r\n"
            		+ "      text-shadow: 0 0 8px rgba(255, 255, 255, 0.6);\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    .table-container {\r\n"
            		+ "      margin-top: 40px;\r\n"
            		+ "      background: white;\r\n"
            		+ "      padding: 30px;\r\n"
            		+ "      border-radius: 16px;\r\n"
            		+ "      box-shadow: 0 12px 24px rgba(0, 0, 0, 0.2);\r\n"
            		+ "      width: 90%;\r\n"
            		+ "      max-width: 800px;\r\n"
            		+ "      overflow-x: auto;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    table {\r\n"
            		+ "      width: 100%;\r\n"
            		+ "      border-collapse: collapse;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    th, td {\r\n"
            		+ "      padding: 14px 20px;\r\n"
            		+ "      text-align: center;\r\n"
            		+ "      border: 1px solid #ccc;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    th {\r\n"
            		+ "      background: linear-gradient(to right, #4b6cb7, #182848);\r\n"
            		+ "      color: white;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    tr:nth-child(even) {\r\n"
            		+ "      background-color: #f2f2f2;\r\n"
            		+ "    }\r\n"
            		+ "\r\n"
            		+ "    @media (max-width: 600px) {\r\n"
            		+ "      .nav ul {\r\n"
            		+ "        flex-direction: column;\r\n"
            		+ "        gap: 12px;\r\n"
            		+ "      }\r\n"
            		+ "\r\n"
            		+ "      table {\r\n"
            		+ "        font-size: 14px;\r\n"
            		+ "      }\r\n"
            		+ "    }\r\n"
            		+ "  </style>");
            out.println("</head><body>");
            out.println("<form action='Fetch Data' method='post'>");
            out.println("<div class='nav'>"
                    + "<div class='nav-title'>User Management Panel</div>"
                    + "<ul>"
                    + "<li><a href='index.html'>Add User</a></li>"
                    + "<li><a href='updateuser'>Update User</a></li>"
                    + "<li><a href='deleteuser'>Delete User</a></li>"
                    + "</ul></div>");

            out.println("<div class='table-container'>");
            out.println("<table ><thead><tr>");

            // Column Headers
            for (int i = 1; i <= columnCount; i++) {
                out.println("<th>" + rsmd.getColumnName(i) + "</th>");
            }

            out.println("</tr></thead><tbody>");

            // Data Rows
            while (rset.next()) {
                out.println("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<td>" + rset.getString(i) + "</td>");
                }
                out.println("</tr>");
            }

            out.println("</tbody></table></div></form></body></html>");

            rset.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
