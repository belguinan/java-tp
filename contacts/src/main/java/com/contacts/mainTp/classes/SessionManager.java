package com.contacts.mainTp.classes;

import java.util.HashMap;
import jakarta.servlet.http.HttpSession;

public class SessionManager {

    /**
     * That will hold our each service
     * with a unique session id
     */
    private HashMap<String, ContactService> services;
    
    /**
     * Create new empty HashMap
     */
    public SessionManager()
    {
        this.services = new HashMap<String, ContactService>();
    }

    /**
     * @param session
     * 
     * @return ContactService
     */
    public ContactService findOrCreate(HttpSession session)
    {
        String key = session.getId();

        if (! this.services.containsKey(key)) {
            this.services.put(key, new ContactService());
        }

        return this.services.get(key);
    }
}
