package com.classicmodels.repository;

import static jooq.generated.Routines.getProduct;
import jooq.generated.routines.GetEmpsInOffice;
import jooq.generated.routines.GetProduct;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.Table;
import org.jooq.Record;
import static org.jooq.impl.DSL.call;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executeStoredProcedureSelect() {

        // EXECUTION 1
        GetProduct gp = new GetProduct();
        gp.setPid(1L);

        gp.execute(ctx.configuration());

        Result<Record> result1 = gp.getResults().get(0);
        System.out.println("Result: \n" + result1);

        // EXECUTION 2
        getProduct(ctx.configuration(), 1L); // returns void       

        // EXECUTION 3
        // Table<?> t = table(gp.getResults().get(0));
        Table<ProductRecord> t = table(gp.getResults().get(0).into(PRODUCT));
        ctx.selectFrom(t).fetch();
    }

    public void executeStoredProcedureMultipleSelect() {

        // EXECUTION 1
        GetEmpsInOffice geio = new GetEmpsInOffice();
        geio.setInOfficeCode("1");

        geio.execute(ctx.configuration());

        Results results = geio.getResults();    

        for (Result<Record> result : results) {
            System.out.println("Result set:\n");
            for (Record record : result) {
                System.out.println(record);
            }
        }
    }

    public void executeStoredProcedureViaCallStatement() {

        // CALL statement in an anonymous block
        ctx.begin(call(name("refresh_top3_product"))
                .args(val("Trains")))
                .execute();

        // CALL statement directly
        ctx.call(name("refresh_top3_product"))
                .args(val("Trains"))
                .execute();
    }
}
