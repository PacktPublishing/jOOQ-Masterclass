package com.classicmodels.repository;

import jooq.generated.routines.GetCustomer;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import jooq.generated.tables.records.CustomerRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Table;
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

    public void executeCursorFunction() {

        GetCustomer customers = new GetCustomer();
        customers.setCl(120000);

        customers.execute(ctx.configuration());

        Result<Record> result = customers.getReturnValue();

        System.out.println("Result:\n" + result);
        System.out.println("Name of first customer:\n" + result.getValue(0, "CUSTOMER_NAME"));

        Table<CustomerRecord> t = table(result.into(CUSTOMER));

        ctx.select(t.field(name("CUSTOMER_NAME"))).from(t).fetch();

        ctx.select(CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.POSTAL_CODE,
                t.field(name("CUSTOMER_NAME")))
                .from(t)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(t.field(name("CUSTOMER_NUMBER"), Long.class)))
                .fetch();
    }

}
