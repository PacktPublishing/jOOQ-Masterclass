package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Sale;
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
    public void fetchEmployeesAndSales() {

        Results results = ctx.resultQuery("SELECT TOP 10 * FROM employee; SELECT TOP 5 * FROM sale")
                .fetchMany();

        System.out.println("Results: " + results);
        System.out.println("Size: " + results.size()); // 2, means 2 result sets

        // map each result set to its POJO
        List<Employee> employees = results.get(0).into(Employee.class);
        List<Sale> sales = results.get(1).into(Sale.class);

        System.out.println("Employees: " + employees);
        System.out.println("Sales: " + sales);
    }

    @Transactional
    public void deleteAndCountSalesOfNonSalesRepHavingNoCustomers() {
       
        Results results = ctx.resultQuery(
                """
                DELETE FROM [sale] WHERE [sale].[employee_number]={0} 
                AND NOT EXISTS (SELECT [employee_number] 
                    FROM [employee] WHERE [employee].[employee_number]={0} 
                        AND [employee].[job_title] = 'Sales Rep') 
                AND NOT EXISTS (SELECT [sales_rep_employee_number] 
                    FROM [customer] WHERE [customer].[sales_rep_employee_number]={0});                
                SELECT @@ROWCOUNT AS affected;
                """, val(1143)        
        ).fetchMany();

        System.out.println("Results: " + results);
    }
}
