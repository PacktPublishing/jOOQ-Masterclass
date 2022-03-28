package com.classicmodels.utils;

import java.util.Collections;
import javax.persistence.Query;
import org.hibernate.query.internal.AbstractProducedQuery;

public final class DaoUtils {

    private DaoUtils() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static String sql(Query query) {               

        AbstractProducedQuery abstractProducedQuery = query.unwrap(AbstractProducedQuery.class);

        String[] sqlStrings = abstractProducedQuery
                .getProducer()
                .getFactory()
                .getQueryPlanCache() // Deprecated. (since 5.2) it will be replaced with the new QueryEngine concept introduced in 6.0 Access to the caches of HQL/JPQL and native query plans.
                .getHQLQueryPlan(abstractProducedQuery.getQueryString(), false, Collections.emptyMap())
                .getSqlStrings();

        return sqlStrings.length > 0 ? sqlStrings[0] : null;
    }
}
