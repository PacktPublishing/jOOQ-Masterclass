package com.classicmodels.repository;

import jooq.generated.routines.GetEmpsInOfficeJooq;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.jooq.Parameter;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Results;
import static org.jooq.impl.DSL.begin;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.SQLDataType.VARCHAR;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void createProcedure() {
              
        ctx.dropProcedureIfExists("get_emps_in_office_jooq")
                .execute();

        // or, use ctx.createOrReplaceProcedure() instead of dropping via dropProcedureIfExists()
        ctx.createProcedure("get_emps_in_office_jooq")                
                
                .execute();
    }
    
     public void callProcedure() {

         // calling the previously created procedure via the generated code
        GetEmpsInOfficeJooq proc = new GetEmpsInOfficeJooq();
        
        proc.execute(ctx.configuration());

        Results results = proc.getResults();

        for (Result<?> result : results) {            
            System.out.println("Result set:\n");
            for (Record record : result) {
                System.out.println(record);
            }
        }                              
     }
}
