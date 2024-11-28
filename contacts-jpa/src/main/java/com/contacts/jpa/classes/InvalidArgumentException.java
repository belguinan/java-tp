package com.contacts.jpa.classes;

public class InvalidArgumentException extends Exception {
    /**
     * @param message
     */
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException() {
        super();
    }
}
