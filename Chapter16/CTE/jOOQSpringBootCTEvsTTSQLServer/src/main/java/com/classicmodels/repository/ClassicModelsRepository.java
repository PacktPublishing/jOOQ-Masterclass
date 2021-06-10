package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void cte() {
        ctx.with("max_employee_sale", "employee_number", "max_sale")
                .as(select(SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER))
                .select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("max_sale")))
                .from(EMPLOYEE)
                .innerJoin(name("max_employee_sale"))
                .on(field(name("max_employee_sale", "employee_number")).eq(EMPLOYEE.EMPLOYEE_NUMBER))
                .fetch();
    }

    public void temporaryTable() {

        ctx.dropTemporaryTableIfExists("##max_employee_sale").execute();
        ctx.createTable("##max_employee_sale")
                .column("employee_number", SQLDataType.BIGINT.nullable(false))
                .column("max_sale", SQLDataType.FLOAT.nullable(false))
                .constraints(
                        constraint("max_employee_sale_pk").primaryKey("employee_number")
                ).execute();

        ctx.insertInto(table(name("##max_employee_sale")),
                field(name("employee_number"), Long.class),
                field(name("max_sale"), Double.class))
                .select(select(SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER))
                .execute();

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("max_sale")))
                .from(EMPLOYEE)
                .innerJoin(name("##max_employee_sale"))
                .on(field(name("##max_employee_sale", "employee_number"))
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                .fetch();
    }
}
