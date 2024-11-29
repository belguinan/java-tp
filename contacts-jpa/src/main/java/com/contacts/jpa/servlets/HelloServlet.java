package com.contacts.jpa.servlets;

import java.io.IOException;
import java.util.List;

import com.contacts.jpa.beans.User;
import com.contacts.jpa.classes.InvalidArgumentException;
import com.contacts.jpa.classes.UserFactory;
import com.contacts.jpa.repositories.DatabaseRepository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * EntityManagerFactory must be created once as a singleton
     */
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    
    @Override
    @SuppressWarnings("rawtypes")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try (DatabaseRepository<User> repository = (new DatabaseRepository<User>(entityManagerFactory))) {
            List users = (List<User>) repository.get(User.class);
            request.setAttribute("users", users);
        }
                
        request.getRequestDispatcher("views/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException 
    {
        // If the delete form was submitted
        if (
            request.getParameter("_method") != null && 
            request.getParameter("_method").equals("DELETE") 
        ) {
            this.doDelete(request, response);
            return;
        }
        
        try (DatabaseRepository<User> repository = (new DatabaseRepository<User>(entityManagerFactory))) {
            // Create new user
            User user = UserFactory.fromRequest(request);

            // Insert user to db
            repository.create(user);
            
        } catch (InvalidArgumentException e) {
            // Set the error message as a request param
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            // Handle insert query failed error
            request.setAttribute("error", e.getMessage());
        }

        if (request.getAttribute("error") != null) {
            // Redirect and keep the current request params.
            this.doGet(request, response);
        }
        
        // Redirect to the index page
        response.sendRedirect(request.getContextPath());
    }

    @Override
    protected void doDelete(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException 
    {
        try (DatabaseRepository<User> repository = (new DatabaseRepository<User>(entityManagerFactory))) {
            this.truncateOrDelete(repository, request);
        }

        response.sendRedirect(request.getContextPath());
    }

    /**
     * @param repository
     * @param request
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void truncateOrDelete(DatabaseRepository repository, HttpServletRequest request) 
    {
        if (request.getParameter("_id") == null) {
            repository.truncate(User.class);
            return;
        }

        try {
            User user = UserFactory.fromRequest(request);
            repository.delete(user);
        } catch (InvalidArgumentException e) {}
    }
}
