package com.classicmodels.repository;

import static jooq.generated.Routines.getCustomer;
import jooq.generated.routines.GetCustomer;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import jooq.generated.tables.records.CustomerRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.Field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
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

        Result<Record> result1 = customers.getReturnValue();

        System.out.println("Result (1):\n" + result1);
        System.out.println("Name of first customer (1):\n" + result1.getValue(0, "CUSTOMER_NAME"));                
        
        // or, via Routines.getCustomer(Configuration c, Number cl)
        Result<Record> result2 = getCustomer(ctx.configuration(), 120000);
        
        System.out.println("Result (2):\n" + result2);
        System.out.println("Name of first customer (2):\n" + result2.getValue(0, "CUSTOMER_NAME"));                

        // Result<Record> to Table
        // Table<?> t = table(result1);
        Table<CustomerRecord> t = table(result1.into(CUSTOMER));

        ctx.select(t.field(name("CUSTOMER_NAME"))).from(t).fetch();

        ctx.select(CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.POSTAL_CODE,
                t.field(name("CUSTOMER_NAME")))
                .from(t)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(t.field(name("CUSTOMER_NUMBER"), Long.class)))
                .fetch();
        
        // use the result as a field
        Field<Result<Record>> fieldResult = getCustomer(120000);
        System.out.println("Field result: " + fieldResult);
        
        ctx.select(getCustomer(120000)).fetch();
        ctx.select(getCustomer(field(
                select(avg(CUSTOMER.CREDIT_LIMIT)).from(CUSTOMER)))).fetch();                        
    }

}
