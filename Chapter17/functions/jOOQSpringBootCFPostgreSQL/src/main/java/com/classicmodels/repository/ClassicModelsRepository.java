package com.classicmodels.repository;

import static jooq.generated.Routines.getCustomer;
import static jooq.generated.Routines.getOfficesMultiple;
import jooq.generated.routines.GetCustomer;
import jooq.generated.tables.records.GetOfficesMultipleRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import static org.jooq.impl.DSL.table;
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

    public void executeSingleCursorFunction() {

        // EXECUTION 1
        GetCustomer customers = new GetCustomer();
        customers.setCl(50000);

        customers.execute(ctx.configuration());
        System.out.println(customers.getReturnValue());

        // EXECUTION 2
        getCustomer(50000);

        // EXECUTION 3
        ctx.select().from(unnest(getCustomer(50000))).fetch();
        ctx.select().from(table(getCustomer(50000))).fetch();
    }

    public void executeMultipleCursorFunction() {

        Result<GetOfficesMultipleRecord> results = getOfficesMultiple(ctx.configuration());

        for (GetOfficesMultipleRecord result : results) {
            Result<Record> records = result.getGetOfficesMultiple();
            System.out.println("-------------------------");
            for (Record r : records) {
                System.out.println(r.get("city") + ", " + r.get("country"));
            }
        }
    }
}
