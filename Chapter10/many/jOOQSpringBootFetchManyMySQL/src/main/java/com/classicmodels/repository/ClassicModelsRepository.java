package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Office;
import jooq.generated.tables.pojos.Employee;
import org.jooq.DSLContext;
import org.jooq.Results;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional(readOnly=true)
    public void fetchEmployeesAndOffices() {

        Results results = ctx.resultQuery("SELECT * FROM employee LIMIT 10; SELECT * FROM office LIMIT 5")
                .fetchMany();

        System.out.println("Results: " + results);
        System.out.println("Size: " + results.size()); // 2, means 2 result sets

        // fetch each result set to its POJO
        List<Employee> employees = results.get(0).into(Employee.class);
        List<Office> offices = results.get(1).into(Office.class);

        System.out.println("Employees: " + employees);
        System.out.println("Offices: " + offices);
    }

    @Transactional
    public void deleteAndCountSalesOfNonSalesRepHavingNoCustomers() {
       
        Results results = ctx.resultQuery(
                """
                DELETE FROM `sale` WHERE `sale`.`employee_number`={0} 
                AND NOT EXISTS (SELECT `employee_number` 
                    FROM `employee` WHERE `employee`.`employee_number`={0} 
                        AND `employee`.`job_title` = 'Sales Rep') 
                AND NOT EXISTS (SELECT `sales_rep_employee_number` 
                    FROM `customer` WHERE `customer`.`sales_rep_employee_number`={0});                
                SELECT ROW_COUNT() AS affected;
                """, val(1143)        
        ).fetchMany();

        System.out.println("Results: " + results);
    }
}