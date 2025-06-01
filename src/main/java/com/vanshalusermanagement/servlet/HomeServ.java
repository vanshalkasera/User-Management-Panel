package com.vanshalusermanagement.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




public class HomeServ extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/usermanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "vans708124";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get form values
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Prepare SQL query
            String query = "INSERT INTO users (name, email, mobile, address, dob, gender) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, mobile);
            ps.setString(4, address);
            ps.setString(5, dob);
            ps.setString(6, gender);

            // Execute update
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                out.println("<h3>User added successfully!</h3>");
                RequestDispatcher rd=request.getRequestDispatcher("index.html");
                rd.include(request, response);
            } else {
                out.println("<h3>Failed to add user.</h3>");
                RequestDispatcher rd=request.getRequestDispatcher("index.html");
                rd.include(request, response);
            }

            // Close resources
            ps.close();
            conn.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
