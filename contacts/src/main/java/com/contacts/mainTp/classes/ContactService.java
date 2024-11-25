package com.contacts.mainTp.classes;

import java.util.ArrayList;

public class ContactService {

    /**
     * Contacts array list
     */
    private ArrayList<Contact> list;
    
    /**
     * Class Contructor
     */
    public ContactService()
    {
        this.list = new ArrayList<Contact>();
    }

    /**
     * Get all pushed contacts
     * 
     * @return ArrayList<Contact>
     */
    public ArrayList<Contact> all() 
    {
        return this.list;
    }

    /**
     * Add new contact to the current list
     * 
     * @param contact
     */
    public void add(Contact contact)
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

    /**
     * Delete item by index
     * 
     * @param index
     */
    public void delete(int index)
    {
        this.list.remove(index);
    }

    /**
     * return void
     */
    public void flush()
    {
        this.list.clear();
    }
}
