package com.classicmodels.repository;

import static jooq.generated.tables.CustomerMaster.CUSTOMER_MASTER;
import static jooq.generated.tables.OfficeMaster.OFFICE_MASTER;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.row;
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

        /* joins without synthetic keys */
        var result0 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, 
                CUSTOMER_MASTER.CREDIT_LIMIT, CUSTOMER_MASTER.CITY.as("customer_city"), 
                OFFICE_MASTER.CITY.as("office_city"), OFFICE_MASTER.PHONE)
                .from(CUSTOMER_MASTER)
                .leftOuterJoin(OFFICE_MASTER)
                .on(row(CUSTOMER_MASTER.COUNTRY, CUSTOMER_MASTER.STATE, CUSTOMER_MASTER.CITY)
                        .eq(row(OFFICE_MASTER.COUNTRY, OFFICE_MASTER.STATE, OFFICE_MASTER.CITY)))
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 0:\n" + result0);

        /* joins using synthetic keys */
        var result1 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, 
                CUSTOMER_MASTER.CREDIT_LIMIT, CUSTOMER_MASTER.CITY.as("customer_city"),
                CUSTOMER_MASTER.officeMaster().CITY.as("office_city"), CUSTOMER_MASTER.officeMaster().PHONE)
                .from(CUSTOMER_MASTER)
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 1:\n" + result1);

        var result2 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, 
                CUSTOMER_MASTER.CREDIT_LIMIT, CUSTOMER_MASTER.CITY.as("customer_city"),
                OFFICE_MASTER.CITY.as("office_city"), OFFICE_MASTER.PHONE)
                .from(CUSTOMER_MASTER)
                .leftOuterJoin(OFFICE_MASTER)
                .onKey()
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 2:\n" + result2);

        var result3 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, 
                CUSTOMER_MASTER.CREDIT_LIMIT, CUSTOMER_MASTER.CITY.as("customer_city"),
                OFFICE_MASTER.CITY.as("office_city"), OFFICE_MASTER.PHONE)
                .from(CUSTOMER_MASTER)
                .innerJoin(OFFICE_MASTER)
                .onKey()
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 3:\n" + result3);                      
    }
}
