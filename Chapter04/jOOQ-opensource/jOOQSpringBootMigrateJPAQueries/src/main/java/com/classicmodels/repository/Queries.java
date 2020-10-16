package com.classicmodels.repository;

import java.util.List;
import javax.persistence.EntityManager;

public class Queries {

    private void Queries() {
        throw new AssertionError("Cannot be instantiated");
    }

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
}
