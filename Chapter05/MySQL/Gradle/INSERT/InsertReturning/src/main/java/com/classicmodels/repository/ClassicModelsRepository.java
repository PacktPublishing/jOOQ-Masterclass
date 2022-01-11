package com.classicmodels.repository;

import java.util.UUID;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.inline;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    insert into `classicmodels`.`sale` (
      `fiscal_year`, `sale`, `employee_number`, 
      `revenue_growth`, `fiscal_month`
    ) 
    values 
      (?, ?, ?, ?, ?)    
     */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE,
                SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.REVENUE_GROWTH, SALE.FISCAL_MONTH)
                .values(2004, 2311.42, 1370L, 10.12, 1)
                .returningResult(SALE.SALE_ID) // or, returningResult() to return whole fields
                .fetchOne();

        System.out.println("EXAMPLE 1 (inserted id): \n" + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    insert into `classicmodels`.`sale` (
      `fiscal_year`, `sale`, `employee_number`, 
      `revenue_growth`, `fiscal_month`
    ) 
    values 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?)    
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE,
                SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.REVENUE_GROWTH, SALE.FISCAL_MONTH)
                .values(2004, 2311.42, 1370L, 12.50, 1)
                .values(2003, 900.21, 1504L, 23.99, 2)
                .values(2005, 1232.2, 1166L, 14.65, 3)
                .returningResult(SALE.SALE_ID) // or, returningResult() to return whole fields
                .fetch();

        System.out.println("EXAMPLE 2 (inserted ids): \n" + insertedIds);
    }

    // EXAMPLE 3
    /*
    insert into `classicmodels`.`customer` (
      `customer_number`, `customer_name`, 
      `contact_last_name`, `contact_first_name`, 
      `phone`, `sales_rep_employee_number`, 
      `credit_limit`, `first_buy_date`
    ) 
    values 
      (
        default, ?, ?, ?, ?, default, default, 
        default
      )

    insert into `classicmodels`.`customerdetail` (
      `customer_number`, `address_line_first`, 
      `address_line_second`, `city`, `state`, 
      `postal_code`, `country`
    ) 
    values 
      (
        ?, ?, default, ?, default, default, ?
      )
     */
    public void insertReturningOfCustomerInCustomerDetail() {

        // Note: passing explicit "null" instead of default_() produces implementation specific behaviour
        
        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER)
                                .values(default_(),
                                        UUID.randomUUID().toString(), // random customer_name
                                        "Kyle", "Doyle", "+ 44 321 321", 
                                        default_(), default_(), default_())
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                UUID.randomUUID().toString(), // random address_line_first
                                default_(), "Los Angeles", default_(), default_(), "USA")
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    insert into `classicmodels`.`manager` (
      `manager_id`, `manager_name`, `manager_detail`, 
      `manager_evaluation`
    ) 
    values 
      (
        default, 
        (
          select 
            concat(
              `classicmodels`.`employee`.`first_name`, 
              ' ', `classicmodels`.`employee`.`last_name`
            ) 
          from 
            `classicmodels`.`employee` 
          where 
            `classicmodels`.`employee`.`employee_number` = ?
        ), 
        default, 
        default
      )    
     */
    public void insertEmployeeInManagerReturningId() {

        // Result<Record1<Long>>
        var inserted = ctx.insertInto(MANAGER)
                .values(default_(), select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1165L)),
                        default_(), default_())
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 4 (inserted ids):\n" + inserted);
    }
}
