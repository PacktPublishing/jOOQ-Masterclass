package com.classicmodels.repository;

import static jooq.generated.Routines.getCustomer;
import jooq.generated.routines.GetCustomer;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.unnest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executeCursorFunction() {
        
        // EXECUTION 1
        GetCustomer customers = new GetCustomer();                
        customers.setCl(50000);
        
        customers.execute(ctx.configuration());       
        System.out.println(customers.getReturnValue());    
        
        // EXECUTION 2
        ctx.select().from(unnest(getCustomer(50000))).fetch();
        
        // EXECUTION 3
        ctx.select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.COUNTRY, 
                field(name("t", "customer_number")))
                .from(CUSTOMERDETAIL)
                .innerJoin(unnest(getCustomer(50000)).as("t"))
                .on(field(name("t", "customer_number")).eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetch();
    }

}
