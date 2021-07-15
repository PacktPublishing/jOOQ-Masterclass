package com.classicmodels.repository;

import static jooq.generated.Routines.getProduct;
import jooq.generated.routines.GetEmpsInOffice;
import jooq.generated.routines.GetProduct;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.Table;
import org.jooq.Record;
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
    
    public void executeStoredProcedureSelect() {

        // EXECUTION 1
        GetProduct gp = new GetProduct();
        gp.setPid(1L);

        gp.execute(ctx.configuration());
        System.out.println("Result: \n" + gp.getResults().get(0)); // Result<Record>

        // EXECUTION 2
        getProduct(ctx.configuration(), 1L);

        // EXECUTION 3
        Table<?> t = table(gp.getResults().get(0));
        ctx.selectFrom(t).fetch();
    }

    public void executeStoredProcedureMultipleSelect() {

        // EXECUTION 1
        GetEmpsInOffice geio = new GetEmpsInOffice();
        geio.setInOfficeCode("1");

        geio.execute(ctx.configuration());

        Results results = geio.getResults();

        for (Result<?> result : results) {            
            System.out.println("Result set:\n");
            for (Record record : result) {
                System.out.println(record);
            }
        }                        
    }
}
