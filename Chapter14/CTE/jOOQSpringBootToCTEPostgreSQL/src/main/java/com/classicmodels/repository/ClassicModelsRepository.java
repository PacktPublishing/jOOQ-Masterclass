package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void derivedTableToTemporaryTableToCTE1() {

        // derived tables        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                sum(SALE.SALE_), field(select(sum(SALE.SALE_)).from(SALE))
                .divide(field(select(countDistinct(SALE.EMPLOYEE_NUMBER)).from(SALE))).as("avg_sales"))
                .from(EMPLOYEE)
                .innerJoin(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .groupBy(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .having(sum(SALE.SALE_).gt(field(select(sum(SALE.SALE_)).from(SALE))
                        .divide(field(select(countDistinct(SALE.EMPLOYEE_NUMBER)).from(SALE)))))
                .fetch();

        // temporary tables
        ctx.dropTemporaryTableIfExists("t1").execute();
        ctx.dropTemporaryTableIfExists("t2").execute();
        ctx.dropTemporaryTableIfExists("t3").execute();

        ctx.createTemporaryTable("t1").as(
                select(sum(SALE.SALE_).as("sum_all_sales")).from(SALE)).execute();

        ctx.createTemporaryTable("t2").as(
                select(countDistinct(SALE.EMPLOYEE_NUMBER).as("nbr_employee")).from(SALE)).execute();

        ctx.createTemporaryTable("t3").as(
                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, sum(SALE.SALE_).as("employee_sale"))
                        .from(EMPLOYEE)
                        .innerJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .groupBy(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)).execute();

        ctx.select(field(name("first_name")), field(name("last_name")),
                field(name("employee_sale")), field(name("sum_all_sales"))
                .divide(field(name("nbr_employee"), Integer.class)).as("avg_sales"))
                .from(table(name("t1")), table(name("t2")), table(name("t3")))
                .where(field(name("employee_sale")).gt(
                        field(name("sum_all_sales")).divide(field(name("nbr_employee"), Integer.class))))
                .fetch();

        // CTE
        ctx.with("cte1", "sum_all_sales")
                .as(select(sum(SALE.SALE_)).from(SALE)) // or, asMaterialized
                .with("cte2", "nbr_employee")
                .as(select(countDistinct(SALE.EMPLOYEE_NUMBER)).from(SALE))
                .with("cte3", "first_name", "last_name", "employee_sale")
                .as(select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, sum(SALE.SALE_).as("employee_sale"))
                        .from(EMPLOYEE)
                        .innerJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .groupBy(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME))
                .select(field(name("first_name")), field(name("last_name")),
                        field(name("employee_sale")), field(name("sum_all_sales"))
                        .divide(field(name("nbr_employee"), Integer.class)).as("avg_sales"))
                .from(table(name("cte1")), table(name("cte2")), table(name("cte3")))
                .where(field(name("employee_sale")).gt(
                        field(name("sum_all_sales")).divide(field(name("nbr_employee"), Integer.class))))
                .fetch();
    }

    public void derivedTableToTemporaryTableToCTE2() {

        // derived table
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("max_sale")))
                .from(EMPLOYEE)
                .innerJoin(select(SALE.EMPLOYEE_NUMBER.as("employee_number"), max(SALE.SALE_).as("max_sale"))
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("max_employee_sale"))
                .on(field(name("max_employee_sale", "employee_number"))
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                .fetch();

        // temporary table
        ctx.dropTemporaryTableIfExists("max_employee_sale").execute();
        ctx.createTemporaryTable("max_employee_sale").as(
                select(SALE.EMPLOYEE_NUMBER, max(SALE.SALE_).as("max_sale"))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER))
                .execute();

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("max_sale")))
                .from(EMPLOYEE)
                .innerJoin(name("max_employee_sale"))
                .on(field(name("max_employee_sale", "employee_number"))
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                .fetch();

        // CTE
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
}
