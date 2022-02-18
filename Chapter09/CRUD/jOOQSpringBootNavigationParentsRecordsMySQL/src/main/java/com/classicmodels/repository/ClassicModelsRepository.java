package com.classicmodels.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jooq.generated.Keys;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OfficeRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void officesOfEmployees() {

        // a given list of employees
        List<EmployeeRecord> employees = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(75000))
                .fetch();

        // execute a SELECT for each employee and return duplicates
        List<OfficeRecord> officesOneByOne = new ArrayList<>();
        employees.forEach(employee -> {
            officesOneByOne.add(employee.fetchParent(Keys.EMPLOYEE_OFFICE_FK)); // or, Keys.EMPLOYEE_OFFICE_FK.fetchParent(employee)
        });
        System.out.println("Offices (collect one by one):\n" + officesOneByOne);

        // much better approach, single SELECT, no duplicates        
        List<OfficeRecord> officesAll = Keys.EMPLOYEE_OFFICE_FK.fetchParents(employees);

        System.out.println("Offices (all):\n" + officesAll);

        // or, get it as a Table
        Table<OfficeRecord> officeTable = Keys.EMPLOYEE_OFFICE_FK.parents(employees);

        var result = ctx.selectFrom(officeTable)
                .where(officeTable.field("city", String.class).eq("San Francisco"))
                .fetch();

        System.out.println("Result :\n" + result);
    }

    public void salesOfEmployee() {

        EmployeeRecord employee = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L))
                .fetchOne();

        List<SaleRecord> sales1 = employee.fetchChildren(Keys.SALE_EMPLOYEE_FK);
        List<SaleRecord> sales2 = Keys.SALE_EMPLOYEE_FK.fetchChildren(employee);

        Table<SaleRecord> salesTable1 = employee.children(Keys.SALE_EMPLOYEE_FK);
        Table<SaleRecord> salesTable2 = Keys.SALE_EMPLOYEE_FK.children(employee);

        System.out.println("Sales (records):\n" + sales1);
        System.out.println("Sales (records):\n" + sales2);
    }

    @Transactional
    public void deleteSales() {

        // approach 1 (less queries, but extra-loop)        
        List<SaleRecord> sales = ctx.fetch(SALE, SALE.SALE_.lt(2000d));
        List<EmployeeRecord> employees = Keys.SALE_EMPLOYEE_FK.fetchParents(sales);
        
        // if you need Table<EmployeeRecord> then use
        Table<EmployeeRecord> employeesTable = Keys.SALE_EMPLOYEE_FK.parents(sales);

        for (SaleRecord sale : sales) {
            for (EmployeeRecord employee : employees) {

                if (Objects.equals(sale.getEmployeeNumber(), employee.getEmployeeNumber())
                        && "Sales Rep".equals(employee.getJobTitle())) {
                    sale.delete();

                    break;
                }
            }
        }

        // approach 2 (more queries
        for (SaleRecord sale : ctx.fetch(SALE, SALE.SALE_.lt(2000d))) {

            if ("Sales Rep".equals(sale.fetchParent(
                    Keys.SALE_EMPLOYEE_FK).getJobTitle())) {
                sale.delete();
            }
        }
    }
}
