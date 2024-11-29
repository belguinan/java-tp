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
     * @return
     */
    public List<T> get(Class<T> model) {        
        return entityManager.createQuery("Select a from " + model.getSimpleName() + " a", model).getResultList();
    }
    
    /**
     * @param model
     * @throws Exception
     */
    public void create(T model) throws Exception
    {
        this.inTransaction(() -> {
            this.entityManager.persist(model);
        });
    }

    
    /**
     * @param model
     */
    public void delete(T model)
    {
        this.inTransaction(() -> {
            this.entityManager.remove(this.entityManager.merge(model));
        });
    }

    /**
     * @param model
     */
    public void truncate(Class<T> model)
    {
        this.inTransaction(() -> {
            String query = "Truncate " + model.getAnnotation(Table.class).name();
            this.entityManager.createNativeQuery(query).executeUpdate();
        });
    }

    /**
     * @param callback
     */
    public void inTransaction(Runnable callback) {
        try {
            this.entityManager.getTransaction().begin();
            callback.run();
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (this.entityManager.getTransaction().isActive()) {
                this.entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void close() {
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
