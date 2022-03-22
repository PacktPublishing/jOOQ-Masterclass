package com.classicmodels.repository;

import java.util.Arrays;
import static jooq.generated.Routines.departmentTopicArr;
import static jooq.generated.Routines.employeeOfficeArr;
import jooq.generated.routines.EmployeeOfficeArr;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.sum;
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

    public void executeArrayFunction() {

        // EXECUTION 1
        EmployeeOfficeArr eoa = new EmployeeOfficeArr();
        eoa.set__1("1");

        eoa.execute(ctx.configuration());

        System.out.println("Execution 1: " + Arrays.toString(eoa.getReturnValue()));

        ctx.select().from(unnest(departmentTopicArr(2L)).as("t")).fetch();
        ctx.fetch(unnest(departmentTopicArr(2L)).as("t"));
        
        // EXECUTION 2
       ctx.select(field(name("t", "en")), sum(SALE.SALE_))
               .from(SALE)
               .rightJoin(unnest(employeeOfficeArr("1")).as("t", "en"))
               .on(field(name("t", "en")).eq(SALE.EMPLOYEE_NUMBER))
               .groupBy(field(name("t", "en")))
               .fetch();        
    }

}
