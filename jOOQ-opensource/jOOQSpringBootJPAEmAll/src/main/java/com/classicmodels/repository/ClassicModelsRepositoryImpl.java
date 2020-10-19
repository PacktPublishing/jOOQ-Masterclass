package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;
import javax.persistence.EntityManager;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepositoryImpl implements ClassicModelsRepository {

    private final DSLContext ctx;
    private final EntityManager em;

    public ClassicModelsRepositoryImpl(DSLContext ctx, EntityManager em) {
        this.ctx = ctx;
        this.em = em;
    }

    @Override
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear() {

        org.jooq.Query query = ctx.select(EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                sum(SALE.SALE_)
                        .over(partitionBy(SALE.FISCAL_YEAR)).as("TOTAL_SALES"))
                .from(SALE)
                .join(EMPLOYEE).on(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER));

        return Queries.nativeQueryToListOfObj(em, query);
    }

    @Override
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary() {

        org.jooq.Query query = ctx.select(EMPLOYEE.FIRST_NAME.as("firstName"),
                EMPLOYEE.LAST_NAME.as("lastName"),
                EMPLOYEE.SALARY.as("salary"),
                firstValue(EMPLOYEE.FIRST_NAME)
                        .over().orderBy(EMPLOYEE.SALARY).as("leastSalary"))
                .from(EMPLOYEE);

        return Queries.nativeQueryToPojo(em, query, EmployeeNoCntr.class);
    }

    @Override
    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr() {

        org.jooq.Query query = ctx.select(EMPLOYEE.FIRST_NAME.as("firstName"),
                EMPLOYEE.LAST_NAME.as("lastName"),
                EMPLOYEE.SALARY.as("salary"),
                firstValue(EMPLOYEE.FIRST_NAME)
                        .over().orderBy(EMPLOYEE.SALARY).as("leastSalary"))
                .from(EMPLOYEE);

        return Queries.nativeQueryToPojoCntr(em, query, "EmployeeDtoMapping");
    }

    @Override
    public List<Employee> findEmployeeInCity(String city) {

        org.jooq.Query query
                = ctx.select()
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(
                                select(OFFICE.OFFICE_CODE)
                                        .from(OFFICE)
                                        .where(OFFICE.CITY.eq(city))));

        return Queries.nativeQueryToEntity(em, query, Employee.class);
    }
    
    @Override
    public List<Employee> findEmployeeBySalary(int salary) {
        
        org.jooq.Query query
                = ctx.select()
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.eq(salary));
        
        return Queries.nativeQueryToEntityResult(em, query, "EmployeeEntityMapping");
    }
    
    @Override
    public List<Object[]> findEmployeeAndOffices() {
      
        org.jooq.Query query
                = ctx.select()
                .from(EMPLOYEE)
                .join(OFFICE).on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE));        
        
        return Queries.nativeQueryToMultipleEntityResult(em, query, "EmployeeOfficeEntityMapping");
    }   
}