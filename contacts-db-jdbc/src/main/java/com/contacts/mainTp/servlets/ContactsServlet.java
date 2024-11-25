package com.contacts.mainTp.servlets;

import java.io.IOException;
import java.sql.SQLException;

import com.contacts.mainTp.beans.Contact;
import com.contacts.mainTp.classes.ContactRepository;
import com.contacts.mainTp.classes.Db;
import com.contacts.mainTp.attributes.EmailVO;
import com.contacts.mainTp.classes.InvalidArgumentException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/contacts", "/contacts/"})
public class ContactsServlet extends HttpServlet {
    
    @Override
    protected void doGet(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException 
    {
        // Get or create contacts container
        ContactRepository contacts = new ContactRepository(Db.getInstance());

        try {
            // Selecy every item from the db
            contacts.all();
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        // Inject contacts container into the view
        request.setAttribute("contacts", contacts);
        
        // Forward that request to the view
        request.getRequestDispatcher("views/contacts.jsp").forward(request, response);
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
        }
        
        try {
            // Validate the email address
            EmailVO email = new EmailVO((String) request.getParameter("email"));

            // Encapsulate the data..
            Contact contact = Contact.withoutId(email.getValue(), request.getParameter("name"));

            // Get or create contacts container
            ContactRepository contacts = new ContactRepository(Db.getInstance());

            // Insert the new contact
            contacts.add(contact); 
            
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
        response.sendRedirect(request.getContextPath() + "/contacts");
    }

    @Override
    protected void doDelete(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException 
    {
        // Get or create contacts container
        ContactRepository contacts = new ContactRepository(Db.getInstance());

        try {
            // Clear button was clicked
            if (request.getParameter("_id") == null) {
                contacts.truncate();
                throw new Exception("Database truncated!");
            }

            // Delete signle item button was clicked
            long id = (long) Long.parseLong(request.getParameter("_id"));

            // Delete single item
            contacts.deleteById(id);

        } catch (Exception e) {
            // Handle insert query failed error
        }

        // Return to our homepage
        response.sendRedirect(request.getContextPath() + "/contacts");
    }

    @Override
    public void destroy()    
    {
        try {
            Db.getInstance().getConnection().close();
        } catch (SQLException e) {}

        super.destroy();
    }
}
