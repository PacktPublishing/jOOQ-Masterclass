package com.classicmodels.repository;

import jooq.generated.routines.GetCustomer;
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

    public void executeCursorFunction() {
        
        GetCustomer customers = new GetCustomer();                
        customers.setCl(50000);
        
        customers.execute(ctx.configuration());       
        System.out.println(customers.getReturnValue());                                   
    }

}
