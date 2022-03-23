package com.classicmodels.repository;

import static jooq.generated.packages.DepartmentPkg.getBgt;
import static jooq.generated.packages.DepartmentPkg.getMaxCash;
import jooq.generated.packages.department_pkg.GetBgt;
import jooq.generated.packages.department_pkg.GetMaxCash;
import jooq.generated.packages.department_pkg.udt.records.BgtRecord;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
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

    public void executeCursorFunction() {

        // EXECUTION 1
        GetMaxCash gmc = new GetMaxCash();
        gmc.execute(ctx.configuration());
        double resultGmc1 = gmc.getReturnValue();
        System.out.println("Result (get_max_cash): " + resultGmc1);

        GetBgt bgt = new GetBgt();
        bgt.setPProfit(50000.0);
        bgt.execute(ctx.configuration());
        BgtRecord resultBgt1 = bgt.getReturnValue();
        System.out.println("Result (get_bgt, first value): " 
                + resultBgt1.get(0)); // get the first element from array

        // EXECUTION 2
        double resultGmc2 = getMaxCash(ctx.configuration());
        System.out.println("Max cash: " + resultGmc2);

        BgtRecord resultBgt2 = getBgt(ctx.configuration(), 50000.0);
        System.out.println("Bgt: " + resultBgt2);

        // EXECUTION 3        
        ctx.select(getMaxCash()).fetch();
        double mc = ctx.fetchValue(getMaxCash());
        
        // EXECUTION 4
        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET)
                .from(OFFICE)
                .join(DEPARTMENT)
                .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)
                        .and(DEPARTMENT.LOCAL_BUDGET.gt(getMaxCash()))) // or, DEPARTMENT_PKG.getMaxCash()
                .fetch();

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET)
                .from(OFFICE)
                .join(DEPARTMENT)
                .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)
                        .and(DEPARTMENT.LOCAL_BUDGET.eq(getBgt(ctx.configuration(), 50000.0).getLocalBudget()))) // or, DEPARTMENT_PKG.getBgt(ctx.configuration(), 50000.0))
                .fetch();

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET)
                .from(OFFICE)
                .join(DEPARTMENT)
                .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)
                        .and(DEPARTMENT.LOCAL_BUDGET.in(getBgt(ctx.configuration(),
                                getMaxCash(ctx.configuration())).getLocalBudget())))
                .fetch();
    }
}