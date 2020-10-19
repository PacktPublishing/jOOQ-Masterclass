package com.classicmodels.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.transform.Transformers;
import org.jooq.Param;

public final class Queries {

    private void Queries() {
        throw new AssertionError("Cannot be instantiated");
    }

    /* Helper methods copied/inspired from/by jOOQ documentation */
    // Executes a native query and fetch ordinary, untyped Object[] representations
    protected static List<Object[]> nativeQueryToListOfObj(EntityManager em, org.jooq.Query query) {

        // Get the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL());

        // Get the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        return result.getResultList();
    }

    // If you're using custom data types or bindings then call this method
    /*
    protected static List<Object[]> nativeQueryToListOfObj(EntityManager em, org.jooq.Query query) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL());

        // Extract the bind values from the jOOQ query
        int i = 1;
        for (Param<?> param : query.getParams().values()) {
            if (!param.isInline()) {
                result.setParameter(i++, convertToDatabaseType(param));
            }
        }
    
        return result.getResultList();
    } 
    */
    
    // Executes a native query and fetch DTO with no constructor
    protected static <P> List<P> nativeQueryToPojo(
            EntityManager em, org.jooq.Query query, Class<P> type) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL());

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // in Hibernate 6 you should use the new Transformers
        result.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        Transformers.aliasToBean(type));

        // JPA returns the right type
        return result.getResultList();
    }

    // If you're using custom data types or bindings then call this method
    /*
    protected static <P> List<P> nativeQueryToPojo(
            EntityManager em, org.jooq.Query query, Class<P> type) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL());

        // Extract the bind values from the jOOQ query
        int i = 1;
        for (Param<?> param : query.getParams().values()) {
            if (!param.isInline()) {
                result.setParameter(i++, convertToDatabaseType(param));
            }
        }

        // in Hibernate 6 you should use the new Transformers
        result.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        Transformers.aliasToBean(type));

        // JPA returns the right type
        return result.getResultList();
    }
    */

    // Executes a native query and fetch DTO with constructor
    protected static <P> List<P> nativeQueryToPojoCntr(
            EntityManager em, org.jooq.Query query, String mapping) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // JPA returns the right type
        return result.getResultList();
    }

    // If you're using custom data types or bindings then call this method
    /*
    protected static <P> List<P> nativeQueryToPojoCntr(
            EntityManager em, org.jooq.Query query, String mapping) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        int i = 1;
        for (Param<?> param : query.getParams().values()) {
            if (!param.isInline()) {
                result.setParameter(i++, convertToDatabaseType(param));
            }
        }

        // JPA returns the right type
        return result.getResultList();
    }
    */
    
    // Executes a native query and fetch entities
    protected static <E> List<E> nativeQueryToEntity(
            EntityManager em, org.jooq.Query query, Class<E> type) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), type);

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // JPA returns the right type
        return result.getResultList();
    }
    
    // If you're using custom data types or bindings then call this method
    /*
    protected static <E> List<E> nativeQueryToEntity(
            EntityManager em, org.jooq.Query query, Class<E> type) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), type);

        // Extract the bind values from the jOOQ query
        int i = 1;
        for (Param<?> param : query.getParams().values()) {
            if (!param.isInline()) {
                result.setParameter(i++, convertToDatabaseType(param));
            }
        }

        // JPA returns the right type
        return result.getResultList();
    }
    */
    
    // Executes a native query and fetch entities via a single JPA EntityResult
    protected static <E> List<E> nativeQueryToEntityResult(
            EntityManager em, org.jooq.Query query, String mapping) {
        
        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }
        
        // JPA returns the right type
        return result.getResultList();
    }
    
    // If you're using custom data types or bindings then call this method
    /*
    protected static <E> List<E> nativeQueryToEntityResult(
            EntityManager em, org.jooq.Query query, String mapping) {
        
        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        int i = 1;
        for (Param<?> param : query.getParams().values()) {
            if (!param.isInline()) {
                result.setParameter(i++, convertToDatabaseType(param));
            }
        }
        
        // JPA returns the right type
        return result.getResultList();
    }
    */
    
    // Executes a native query and fetch entities via multiple JPA EntityResult
    protected static List<Object[]> nativeQueryToMultipleEntityResult(
            EntityManager em, org.jooq.Query query, String mapping) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // JPA returns the right type
        return result.getResultList();
    }
    
    // If you're using custom data types or bindings then call this method
    /*
    protected static List<Object[]> nativeQueryToMultipleEntityResult(
            EntityManager em, org.jooq.Query query, String mapping) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        int i = 1;
        for (Param<?> param : query.getParams().values()) {
            if (!param.isInline()) {
                result.setParameter(i++, convertToDatabaseType(param));
            }
        }

        // JPA returns the right type
        return result.getResultList();
    }
    */

    private static <T> Object convertToDatabaseType(Param<T> param) {
        return param.getBinding().converter().to(param.getValue());
    }
}
