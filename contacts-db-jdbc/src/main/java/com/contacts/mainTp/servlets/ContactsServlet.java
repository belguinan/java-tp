package com.contacts.mainTp.servlets;

import java.io.IOException;

import com.contacts.mainTp.classes.Contact;
import com.contacts.mainTp.classes.ContactService;
import com.contacts.mainTp.classes.EmailVO;
import com.contacts.mainTp.classes.InvalidArgumentException;
import com.contacts.mainTp.classes.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/contacts", "/contacts/"})
public class ContactsServlet extends HttpServlet {

    /**
     * Session manager, assign each session 
     * Id to an empty contacts container
     */
    private SessionManager sessionManager;

    /**
     * Creates the session manager
     */
    public ContactsServlet() {
        super();
        this.sessionManager = new SessionManager();
    }
    
    @Override
    protected void doGet(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException 
    {
        // Get or create contacts container
        ContactService contacts  = this.sessionManager.findOrCreate(request.getSession(), "contacts");
        
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
            Contact contact = new Contact(email.getValue(), request.getParameter("name"));

            // Get the container
            ContactService contacts = this.sessionManager.findOrCreate(request.getSession(), "contacts");

            // Push new created contact
            contacts.add(contact); 
            
        } catch (InvalidArgumentException e) {
            // Set the error message as a request param
            request.setAttribute("error", e.getMessage());

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
        ContactService contacts = this.sessionManager.findOrCreate(request.getSession(), "contacts");

        try {
            // Clear button was clicked
            if (request.getParameter("_index") == null) {
                throw new InvalidArgumentException();
            }

            // Delete signle item button was clicked
            int index = Integer.parseInt(request.getParameter("_index"));

            // Delete single item
            contacts.delete(index);
        } catch (InvalidArgumentException e) {
            // Purge all the items
            contacts.flush(); 
        }

        // Return to our homepage
        response.sendRedirect(request.getContextPath() + "/contacts");
    }
}
