package com.classicmodels.repository;

import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    public void naturalJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(table(name("OFFICE")).naturalJoin(table(name("CUSTOMERDETAIL"))))
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void naturalLeftOuterJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 2.1\n"
                + ctx.select()
                        .from(table(name("OFFICE")).naturalLeftOuterJoin(table(name("CUSTOMERDETAIL"))))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 2.2\n"
                + ctx.select()
                        .from(table(name("OFFICE")).naturalLeftOuterJoin(table(name("CUSTOMERDETAIL"))))
                        .where(field(name("CITY")).isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void naturalRightOuterJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select()
                        .from(table(name("OFFICE")).naturalRightOuterJoin(table(name("CUSTOMERDETAIL"))))
                        .fetch()
        );
    }
    
    // EXAMPLE 4
    public void naturalFullOuterJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select()
                        .from(table(name("OFFICE")).naturalFullOuterJoin(table(name("CUSTOMERDETAIL"))))
                        .fetch()
        );
    }
    
    // EXAMPLE 5
    public void naturalJoinOrderCustomerPayment() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(table(name("ORDER"))
                                .naturalJoin(table(name("CUSTOMER"))
                                        .naturalJoin(table(name("PAYMENT")))))
                        .fetch()
        );
    }
}
