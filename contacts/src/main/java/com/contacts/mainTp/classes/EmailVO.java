package com.contacts.mainTp.classes;

import java.io.IOException;

public final class EmailVO {

    /**
     * Valid email address
     */
    private String email;

    /**
     * @param String email
     */
    public EmailVO(String email) throws IOException
    {   
        if (! this.isValid(email)) {
            throw new IOException("Invalid email address");
        }

        this.email = email;
    }

    /**
     * @param email
     * @return Boolean
     */
    public Boolean isValid(String email)
    {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * @return String
     */
    public String getValue()
    {
        return this.email;
    }
}
