package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.EmployeeStatus.EMPLOYEE_STATUS;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.jooq.Parameter;
import static org.jooq.impl.DSL.begin;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void createProcedure1() {
        
        Parameter<Integer> budget = in("budget", INTEGER);

        // or, use ctx.dropProcedureIfExists() and ctx.createProcedure()
        // ctx.dropProcedureIfExists("get_office_gt_budget_jooq").execute();
        // ctx.createProcedure(...)

        ctx.createOrReplaceProcedure("get_office_gt_budget_jooq")
                .parameters(budget)
                .as(select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.STATE)
                        .from(OFFICE)
                        .where(OFFICE.INTERNAL_BUDGET.gt(budget)))
                .execute();
    }
    
    public void callProcedure1() {

        // calling the previously created procedure via the generated code
        /*
        GetOfficeGtBudgetJooq proc = new GetOfficeGtBudgetJooq();
        proc.setBudget(10000);
        // or, proc.set(in("budget", INTEGER), 10000);

        proc.execute(ctx.configuration());
        System.out.println("Result: \n" + proc.getResults().get(0));
        */
    }

    public void createProcedure2() {

        Parameter<Long> inEmployeeNumber = in("in_employee_number", BIGINT);
        
        ctx.createOrReplaceProcedure("get_emp_jooq")
                .parameters(inEmployeeNumber)
                .as(begin(
                        select(EMPLOYEE_STATUS.ACQUIRED_DATE, EMPLOYEE_STATUS.STATUS)
                                .from(EMPLOYEE_STATUS)
                                .where(EMPLOYEE_STATUS.EMPLOYEE_NUMBER.eq(inEmployeeNumber)),
                        select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                                .from(EMPLOYEE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(inEmployeeNumber))
                ))
                .execute();
    }   

    public void callProcedure2() {

        // calling the previously created procedure via the generated code
        /*
        GetEmpJooq proc = new GetEmpJooq();
        proc.setInEmployeeNumber(1504L);

        proc.execute(ctx.configuration());

        Results results = proc.getResults();

        for (Result<Record> result : results) {
            System.out.println("Result set:\n");
            for (Record record : result) {
                System.out.println(record);
            }
        }
        */
    }
    
}
