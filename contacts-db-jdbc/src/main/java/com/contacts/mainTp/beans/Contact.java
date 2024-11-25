package com.contacts.mainTp.beans;

public record Contact(
    Long id,
    String email,
    String name
) {
    public static Contact withoutId(String email, String name) {
        return new Contact(null, email, name);
    }
}