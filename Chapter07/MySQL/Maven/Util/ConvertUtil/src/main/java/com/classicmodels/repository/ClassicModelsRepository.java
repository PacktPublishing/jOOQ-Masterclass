package com.classicmodels.repository;

import com.classicmodels.pojo.Offtake;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.row;
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

    public void fetchSale() {

        Record1<Integer> resultInt = ctx.select(field("fiscal_year", Integer.class))
                .from(table("sale"))
                .fetchAny();

        System.out.println("Fiscal year: " + resultInt);

        Record3<Object, Object, Object> result = ctx.select(
                field("fiscal_year"), field("sale"), field("employee_number"))
                .from(table("sale"))
                .where(row(field("fiscal_year"), field("sale"), field("employee_number")).isNotNull())
                .fetchAny();

        Integer fiscalYear = result.get("fiscal_year", Integer.class);
        Double sale = result.get("sale", Double.class);
        Long employeeNumber = result.get("employee_number", Long.class);

        System.out.println("Fiscal year: " + fiscalYear);
        System.out.println("Sale: " + sale);
        System.out.println("Employee number: " + employeeNumber);

        // POJO mapping
        Offtake offtake = ctx.select(
                field("fiscal_year"), field("sale"), field("employee_number"))
                .from(table("sale"))
                .where(row(field("fiscal_year"), field("sale"), field("employee_number")).isNotNull())
                .fetchAnyInto(Offtake.class);

        System.out.println("Offtake: " + offtake);
    }
}
