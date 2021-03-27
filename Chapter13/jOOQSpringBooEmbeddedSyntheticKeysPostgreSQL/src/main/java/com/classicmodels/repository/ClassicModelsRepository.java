package com.classicmodels.repository;

import static jooq.generated.tables.CustomerMaster.CUSTOMER_MASTER;
import static jooq.generated.tables.OfficeMaster.OFFICE_MASTER;
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
    
    @Transactional
    public void joinCustomerAndOfficeaMasterViews() {
             
        var result1 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, 
                CUSTOMER_MASTER.CREDIT_LIMIT, CUSTOMER_MASTER.CUSTOMER_OFFICE_MASTER_FK)
                .from(CUSTOMER_MASTER)
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 1:\n" + result1);

        var result2 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, CUSTOMER_MASTER.CREDIT_LIMIT, 
                OFFICE_MASTER.OFFICE_CODE, OFFICE_MASTER.PHONE)
                .from(CUSTOMER_MASTER)
                .innerJoin(OFFICE_MASTER)
                .on(OFFICE_MASTER.OFFICE_MASTER_PK.eq(CUSTOMER_MASTER.CUSTOMER_OFFICE_MASTER_FK))
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 2:\n" + result2);
    }
}
