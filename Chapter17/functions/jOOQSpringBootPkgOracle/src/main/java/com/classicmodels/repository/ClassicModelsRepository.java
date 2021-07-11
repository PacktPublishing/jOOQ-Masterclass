package com.classicmodels.repository;

import static jooq.generated.packages.DepartmentPkg.getBgt;
import static jooq.generated.packages.DepartmentPkg.getMaxCash;
import jooq.generated.packages.department_pkg.GetBgt;
import jooq.generated.packages.department_pkg.GetMaxCash;
import jooq.generated.packages.department_pkg.udt.records.BgtArrRecord;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
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

    public void executeCursorFunction() {

        // EXECUTION 1
        GetMaxCash gmc = new GetMaxCash();
        gmc.execute(ctx.configuration());

        GetBgt bg = new GetBgt();
        bg.setPProfit(50000.0);
        bg.execute(ctx.configuration());

        // EXECUTION 2
        Double maxCash = getMaxCash(ctx.configuration());
        System.out.println("Max cash: " + maxCash);

        BgtArrRecord bar = getBgt(ctx.configuration(), 50000.0);
        System.out.println("Bgt: " + bar);

        // EXECUTION 3
        ctx.select().from(unnest(getBgt(ctx.configuration(), 50000.0)));
        ctx.select().from(table(getBgt(ctx.configuration(), 50000.0)));

        ctx.select(count().as("c"))
                .from(unnest(getBgt(ctx.configuration(), 50000.0)))
                .fetch();

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
                        .and(DEPARTMENT.LOCAL_BUDGET.in(getBgt(ctx.configuration(), 50000.0)))) // or, DEPARTMENT_PKG.getBgt(ctx.configuration(), 50000.0))
                .fetch();

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET)
                .from(OFFICE)
                .join(DEPARTMENT)
                .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)
                        .and(DEPARTMENT.LOCAL_BUDGET.in(getBgt(ctx.configuration(),
                                getMaxCash(ctx.configuration())))))
                .fetch();
    }
}