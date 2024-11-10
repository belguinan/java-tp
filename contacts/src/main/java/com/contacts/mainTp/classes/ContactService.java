package com.contacts.mainTp.classes;

import java.util.ArrayList;

public class ContactService {

    /**
     * Contacts array list
     */
    private ArrayList<ContactDTO> list;
    
    /**
     * Class Contructor
     */
    public ContactService() 
    {
        this.list = new ArrayList<ContactDTO>();
    }

    /**
     * Get all pushed contacts
     * 
     * @return ArrayList<ContactDTO>
     */
    public ArrayList<ContactDTO> all() 
    {
        return this.list;
    }

    /**
     * Add new contact to the current list
     * 
     * @param contact
     */
    public void add(ContactDTO contact)
    {
        this.list.add(contact);
    }

    /**
     * @return int
     */
    public int size()
    {
        return this.list.size();
    }
}
