package com.contacts.jpa.classes;

import com.contacts.jpa.beans.User;

import jakarta.servlet.http.HttpServletRequest;

public class UserFactory {

    public static User fromRequest(HttpServletRequest request) throws InvalidArgumentException
    {
        // Get user name
        String name = (String) request.getParameter("name");
        
        // Validate the email address
        EmailVO email = new EmailVO((String) request.getParameter("email"));
        
        try {
            // Get user id
            long id = (long) Long.parseLong(request.getParameter("_id"));
            return new User(id, name, email.getValue());
        } catch (NumberFormatException e) {
            return new User(name, email.getValue());
        }
    }
}
