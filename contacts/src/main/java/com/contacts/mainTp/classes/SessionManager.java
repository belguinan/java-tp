package com.contacts.mainTp.classes;

import jakarta.servlet.http.HttpSession;

public class SessionManager {
    
    /**
     * @return ContactService
     */
    public ContactService findOrCreate(HttpSession session, String key)
    {
        ContactService service = (ContactService) session.getAttribute(key);
        
        if (service == null) {
            session.setAttribute(key, new ContactService());
        }

        return (ContactService) session.getAttribute(key);
    }
}
