package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeDto;
import java.util.List;
import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    private final DSLContext create;

    public QueryRepositoryImpl(DSLContext create) {
        this.create = create;
    }

    @Override
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear() {

        List<Object[]> result
                = create.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        sum(SALE.SALE_).over(partitionBy(SALE.FISCAL_YEAR)).as("TOTAL_SALES"))
                        .from(SALE)
                        .join(EMPLOYEE).using(SALE.EMPLOYEE_NUMBER)
                        .fetchInto(Object[].class);

        return result;
    }

    @Override
    public List<EmployeeDto> findEmployeesAndLeastSalary() {

        List<EmployeeDto> result = create.select(EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME,
                EMPLOYEE.SALARY,
                firstValue(EMPLOYEE.FIRST_NAME)
                        .over().orderBy(EMPLOYEE.SALARY).as("leastSalary"))
                .from(EMPLOYEE)
                .fetchInto(EmployeeDto.class);

        return result;
    }

    @Override
    public String findEmployeesFirstNamesAsCsv() {
        
        String result = create.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)                
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.SALARY.desc())
                .fetch()
                .formatCSV();

        return result;
    }

}
