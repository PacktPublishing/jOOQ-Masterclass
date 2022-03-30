package com.classicmodels.interceptor;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {

        Query query = DSL.using(SQLDialect.POSTGRES)
                .parser()
                .parseQuery(sql);

        if (query != null) {
            return query.getSQL();
        }

        return null;
    }
}