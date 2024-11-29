package com.contacts.jpa.repositories;

import jakarta.persistence.Table;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseRepository<T> implements AutoCloseable {

    /**
     * EntityManagerFactory must be created once
     */
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    /**
     * EntityManager must be created per request
     */
    private EntityManager entityManager;
    
    public DatabaseRepository() {
        this.entityManager = entityManagerFactory.createEntityManager();
    }
    
    /**
     * @param model
     * @throws Exception
     */
    public void create(T model) throws Exception
    {
        try {
            this.entityManager.getTransaction().begin();
            this.entityManager.persist(model);
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param model
     */
    public void delete(T model)
    {
        try {
            this.entityManager.getTransaction().begin();
            this.entityManager.remove(this.entityManager.merge(model));
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param model
     * @return
     */
    public List<T> get(Class<T> model) {        
        return entityManager.createQuery("Select a from " + model.getSimpleName() + " a", model).getResultList();
    }

    /**
     * @param model
     */
    public void truncate(Class<T> model)
    {
        try {
            this.entityManager.getTransaction().begin();
            String query = "Truncate " + model.getAnnotation(Table.class).name();
            this.entityManager.createNativeQuery(query).executeUpdate();
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void close() {
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.clear();
        }
    }
}
