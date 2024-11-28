package com.jdbc.project.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.jdbc.Database;
import com.jdbc.project.beans.Contact;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        Contact contact = new Contact(name, email);
        
        // prepared or direct
        try {
            Connection connection = Database.getInstance().getConnection();

            var stm = connection.prepareStatement("INSERT INTO users (name, email) VALUES (?, ?)");
            stm.setString(1, contact.name());
            stm.setString(2, contact.email());

            if (stm.executeUpdate() == 0) {
                throw new SQLException("Failed to insert data!");
            }

            request.setAttribute("message", "Contact inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            this.doGet(request, response);
        }

        // which one should we use to keep the success message
        request.getRequestDispatcher(request.getContextPath() + "/show").forward(request, response);

        // 
        response.sendRedirect(request.getContextPath() + "/show");
    }
}
