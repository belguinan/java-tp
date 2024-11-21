package com.contacts.mainTp.servlets;

import java.io.IOException;

import com.contacts.mainTp.classes.ContactDTO;
import com.contacts.mainTp.classes.EmailVO;
import com.contacts.mainTp.classes.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/contacts", "/contacts/"})
public class ContactsServlet extends HttpServlet {

    private SessionManager sessionManager;

    public ContactsServlet() {
        super();
        this.sessionManager = new SessionManager();
    }
    
    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        

        // inject contacts into the view
        request.setAttribute("contacts", this.sessionManager.findOrCreate(request.getSession()));
        
        // forward that request to the view
        request.getRequestDispatcher("views/contacts.jsp").forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        // if the delete form was submitted
        if (
            request.getParameter("_method") != null && 
            request.getParameter("_method").equals("DELETE") 
        ) {
            this.doDelete(request, response);
        }
        
        try {
            // validate the email address
            EmailVO email = new EmailVO((String) request.getParameter("email"));

            // encapsulate the data..
            ContactDTO contact = new ContactDTO(email.getValue(), request.getParameter("name"));

            // push to the container
            this.sessionManager.findOrCreate(request.getSession()).add(contact);   
        } catch (IOException e) {
            // set the error message as a request param
            request.setAttribute("error", e.getMessage());

            // redirect and keep the current request params.
            this.doGet(request, response);
        }

        // redirect to the index page
        response.sendRedirect(request.getContextPath() + "/contacts");
    }

    @Override
    protected void doDelete(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if (request.getParameter("_index") == null) {
            this.sessionManager.findOrCreate(request.getSession()).flush();   
        }

        if (request.getParameter("_index") != null) {
            int index = Integer.parseInt(request.getParameter("_index"));
            this.sessionManager.findOrCreate(request.getSession()).delete(index);
        }

        this.doGet(request, response);
    }
}
