package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
    
    // GROUP_CONCAT()
    public void groupConcatEmployee() {

        ctx.select(groupConcat(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME))
                .orderBy(EMPLOYEE.SALARY.desc()).separator("; ").as("names_of_employees"))
                .from(EMPLOYEE)
                .fetch();
    }
}
