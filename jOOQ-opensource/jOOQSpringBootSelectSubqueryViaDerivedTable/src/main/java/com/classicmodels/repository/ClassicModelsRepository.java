package com.classicmodels.repository;

import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Table;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    public Object[][] numberOfSales() {

        Table<?> salesTable = create.select(SALE.EMPLOYEE_NUMBER.as("empnr"), count().as("sales"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("salesTable");

        return create.select(salesTable.fields())
                .from(salesTable)
                .orderBy(salesTable.field("sales").desc())
                .fetchArrays();
    }

    public Object[][] employeesAndNumberOfSales() {

        Table<?> salesTable = create.select(SALE.EMPLOYEE_NUMBER.as("empnr"), count().as("sales"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("salesTable");

        return create.select(salesTable.field("empnr"), salesTable.field("sales"),
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(salesTable)
                .innerJoin(EMPLOYEE).on(salesTable.field("empnr").cast(Long.class)
                .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                .orderBy(salesTable.field("sales").desc())
                .fetchArrays();
    }

    /*
    Example Query Using Derived Tables and Scalar Subquery
    select
      `e1`.`first_name`,
      `e1`.`last_name`,
      `e1`.`office_code`
    from
      `classicmodels`.`employee` as `e1`,
      (
        select
          avg(`e2`.`salary`) as `avgsal`,
          `e2`.`office_code`
        from
          `classicmodels`.`employee` as `e2`
        group by
          `e2`.`office_code`
      ) as `e3`
    where
     (
       `e1`.`office_code` = `e3`.`office_code`
       and `e1`.`salary` >= `e3`.`avgsal`
     )
     */
    public Object[][] employeesWithSalaryGeAvgPerOffice() {       

        Employee e1 = EMPLOYEE.as("e1");        
        Employee e2 = EMPLOYEE.as("e2");
        
        Table<?> e3 = select(avg(e2.SALARY).as("avgsal"), e2.OFFICE_CODE)
                        .from(e2)                        
                        .groupBy(e2.OFFICE_CODE)
                .asTable("e3");
        
        return create.select(e1.FIRST_NAME, e1.LAST_NAME, e1.OFFICE_CODE).from(e1, e3)
                .where(e1.OFFICE_CODE.eq(e3.field("office_code", String.class))
                .and(e1.SALARY.ge(e3.field("avgsal", Integer.class))))
                 .fetchArrays();
    }

}
