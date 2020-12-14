package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
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
                        .from(OFFICE.naturalJoin(CUSTOMERDETAIL))
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void naturalLeftOuterJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(OFFICE.naturalLeftOuterJoin(CUSTOMERDETAIL))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void naturalRightOuterJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select()
                        .from(OFFICE.naturalRightOuterJoin(CUSTOMERDETAIL))
                        .fetch()
        );
    }
    
    // EXAMPLE 4
    public void naturalFullOuterJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select()
                        .from(OFFICE.naturalFullOuterJoin(CUSTOMERDETAIL))                        
                        .fetch()
        );
    }
}
