package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
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

    public void multisetEquality() {

        // multisets are equal        
        ctx.selectCount()
                .where(multiset(select(val("a"), val("b"), val("c")))
                        .eq(multiset(select(val("a"), val("b"), val("c")))))
                .fetch();

        // multisets are not equal
        ctx.selectCount()
                .where(multiset(select(val("a"), val("b"), val("c")))
                        .eq(multiset(select(val("a"), val("c"), val("b")))))
                .fetch();

        // multisets are equal
        ctx.selectCount()
                .where(multiset(select(val("a")).union(select(val("b")).union(select(val("c")))))
                        .eq(multiset(select(val("a")).union(select(val("b")).union(select(val("c")))))))
                .fetch();

        // multisets are not equal
        ctx.selectCount()
                .where(multiset(select(val("a")).union(select(val("b")).union(select(val("c")))))
                        .eq(multiset(select(val("a")).union(select(val("b"))))))
                .fetch();

        ctx.select(count().as("equal_count"))
                .from(EMPLOYEE)
                .where(multiset(selectDistinct(SALE.FISCAL_YEAR)
                        .from(SALE).where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                        .eq(multiset(select(val(2003)).union(select(val(2004)).union(select(val(2007)))))))
                .fetch();
    }
}
