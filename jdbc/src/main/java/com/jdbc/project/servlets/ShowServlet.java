package com.jdbc.project.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;

import com.jdbc.Database;
import com.jdbc.project.beans.Contact;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/show")
public class ShowServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            ArrayList contacts = new ArrayList<Contact>();
            var stm = Database.getInstance().getConnection().prepareStatement("Select * from users order by id desc");
            ResultSet result = stm.executeQuery();
            while(result.next()) {
                contacts.add(new Contact(
                    result.getString("name"),
                    result.getString("email")
                ));
            }

            String message = (String) request.getSession().getAttribute("message");
            
            if (message != null) {
                request.setAttribute("message", message);
                request.getSession().removeAttribute("message");
            }
            
            request.setAttribute("contacts", contacts);
            request.setAttribute("contacts", contacts);
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }
        
        // show resources here!
        request.getRequestDispatcher("views/show.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Good!");
    }

}
