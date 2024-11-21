package com.contacts.mainTp.classes;

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
