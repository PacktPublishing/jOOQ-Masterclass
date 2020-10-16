package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeNoCntr;
import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepositoryImpl implements ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear() {

        List<Object[]> result
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        sum(SALE.SALE_).over(partitionBy(SALE.FISCAL_YEAR)).as("TOTAL_SALES"))
                        .from(SALE)
                        .join(EMPLOYEE).using(SALE.EMPLOYEE_NUMBER)
                        .fetchInto(Object[].class);

        return result;
    }

    @Override
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary() {

        List<EmployeeNoCntr> result = ctx.select(EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME,
                EMPLOYEE.SALARY,
                firstValue(EMPLOYEE.FIRST_NAME)
                        .over().orderBy(EMPLOYEE.SALARY).as("leastSalary"))
                .from(EMPLOYEE)
                .fetchInto(EmployeeNoCntr.class);

        return result;
    }

    @Override
    public String findEmployeesFirstNamesAsCsv() {
        
        String result = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)                
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.SALARY.desc())
                .fetch()
                .formatCSV();

        return result;
    }

}