package com.contacts.mainTp.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.contacts.mainTp.beans.Contact;

public class ContactRepository {

    /**
     * Contacts array list
     */
    private ArrayList<Contact> list;

    /**
     * Database connection
     */
    private final Connection connection;
    
    /**
     * Class Contructor
     */
    public ContactRepository(Db database)
    {
        this.connection = database.getConnection();
        this.list = new ArrayList<Contact>();
    }

    /**
     * Get the loaded list
     * 
     * @return
     */
    public ArrayList<Contact> list() {
        return this.list;
    }
    
    /**
     * return void
     */
    public void flush()
    {
        this.list.clear();
    }
    
    /**
     * @return int
     */
    public int size()
    {
        return this.list.size();
    }
    
    /**
     * Get all pushed contacts
     * 
     * @return ArrayList<Contact>
     */
    public ArrayList<Contact> all() 
    {
        this.flush();
        
        try (
            Statement stm = (Statement) this.connection.createStatement();
            ResultSet resultSet = (ResultSet) stm.executeQuery("SELECT * FROM users ORDER BY id desc");
        ) {
            while (resultSet.next()) {
                Contact contact = new Contact(
                    resultSet.getLong("id"), 
                    resultSet.getString("email"),
                    resultSet.getString("name")
                );

                this.list.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.list;
    }

    /**
     * Add new contact to the current list
     * 
     * @param contact
     */
    public void add(Contact contact) throws Exception
    {
        try (
            PreparedStatement stm = (PreparedStatement) this.connection.prepareStatement("INSERT INTO users (email, name) VALUES (?, ?)", 0, 0, 0);
        ) {
            stm.setString(1, contact.name());
            stm.setString(2, contact.email());

            if (stm.executeUpdate() == 0) {
                throw new Exception("Failed to insert");
            }

            this.list.add(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete item by index
     * 
     * @param index
     */
    public void deleteById(long id) throws Exception
    {
        try (
            PreparedStatement stm = (PreparedStatement) this.connection.prepareStatement("DELETE FROM users WHERE id = ?");
        ) {
            stm.setLong(1, id);
            if (stm.executeUpdate() == 0) {
                throw new Exception("Failed to insert");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove all items from db
     */
    public void truncate()
    {
        try {
            PreparedStatement stm = (PreparedStatement) this.connection.prepareStatement("TRUNCATE users");
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
