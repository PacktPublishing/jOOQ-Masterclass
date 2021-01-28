package com.classicmodels.repository;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void streamingWithAndWithoutLazy() {

        // non-lazy streaming
        ctx.selectFrom(SALE)
                .fetch() // jOOQ fetches the whole result set into memory and close the database connection
                .stream() // stream over the in-memory result set (no database connection is active)
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);

        // lazy streaming (pay attention to not accidentally forget the fetch() method)
        ctx.selectFrom(SALE)
                .stream() // stream over the result set (the database connection remains open)
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);
    }

    public void q() {

        ctx.selectFrom(SALE)
                .resultSetType(ResultSet.TYPE_FORWARD_ONLY)
                .resultSetConcurrency(ResultSet.CONCUR_READ_ONLY)
                .fetchSize(Integer.MIN_VALUE)                
                .stream()
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(x -> {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(x);
                });
    }
}
