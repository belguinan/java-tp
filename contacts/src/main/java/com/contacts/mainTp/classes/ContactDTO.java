package com.contacts.mainTp.classes;

public final class ContactDTO {

    /**
     * Contact email
     */
    private String email;

    /**
     * Contact name
     */
    private String name;
    
    /**
     * @param email
     * @param name
     */
    public ContactDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }

    /**
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return String
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
}
