package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Record3;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.table;
import org.jooq.tools.Convert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void insertSale(Object o1, Object o2, Object o3) {

        Integer fiscalYear = Convert.convert(o1, Integer.class);
        Double sale = Convert.convert(o2, Double.class);
        Long employeeNumber = Convert.convert(o3, Long.class);

        ctx.insertInto(table("sale"), field("fiscal_year"), field("sale"), field("employee_number"))
                .values(fiscalYear, sale, employeeNumber)
                .execute();

        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(fiscalYear, sale, employeeNumber)
                .execute();
    }

    public void fetchSale() {

        Record3<Object, Object, Object> result = ctx.select(
                field("fiscal_year"), field("sale"), field("employee_number"))
                .from(table("sale"))
                .where(row(field("fiscal_year"), field("sale"), field("employee_number")).isNotNull())
                .fetchAny();

        Integer fiscalYear = result.get("fiscal_year", Integer.class);   // Convert.convert(result.get("fiscal_year"), Integer.class);
        Double sale = result.get("sale", Double.class);                  // Convert.convert(result.get("sale"), Double.class);
        Long employeeNumber = result.get("employee_number", Long.class); // Convert.convert(result.get("employee_number"), Long.class);
        
        System.out.println("Fiscal year: " + fiscalYear);
        System.out.println("Sale: " + sale);
        System.out.println("Employee number: " + employeeNumber);
    }
}