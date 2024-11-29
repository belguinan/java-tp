package com.contacts.jpa.repositories;

import jakarta.persistence.Table;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DatabaseRepository<T> implements AutoCloseable {

    /**
     * EntityManager must be created per request
     */
    private EntityManager entityManager;
    
    public DatabaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Select every single item in our table and 
     * Return it in a list without a pagination
     * 
     * @param model
     * @return
     */
    public List<T> get(Class<T> model) {
        return entityManager.createQuery("Select a from " + model.getSimpleName() + " a", model).getResultList();
    }
    
    /**
     * Persiste the model to our entity manager persistence 
     * Context then we store it in our current database
     * 
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
     * We must attach the model to the persistance 
     * context in order to call remove directly
     * 
     * @param model
     */
    public void delete(T model)
    {
        this.inTransaction(() -> {
            this.entityManager.remove(this.entityManager.merge(model));
        });
    }

    /**
     * This is stupid probably, we use native sql query, 
     * must look for a better way here!
     * 
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
     * First class function like implemented to prevent 
     * Code repetition probably there is a better way
     * 
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

    /**
     * AutoClosable implementation must use this method, we should 
     * Close the entity manager with try with resource management
     */
    @Override
    public void close() {
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
