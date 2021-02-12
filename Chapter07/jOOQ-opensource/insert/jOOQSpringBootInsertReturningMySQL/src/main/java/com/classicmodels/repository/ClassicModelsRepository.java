package com.classicmodels.repository;

import jooq.generated.enums.SaleRate;
import jooq.generated.enums.SaleVat;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.val;
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
      `sale_id`,`fiscal_year`,`sale`,`employee_number`)
    values
      (default, ?, ?, ?, default, ?, ?, default)
     */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE)
                .values(default_(), 2004, 2311.42, 1370L, 
                        default_(), SaleRate.SILVER, SaleVat.NONE, default_())
                .returningResult(SALE.SALE_ID)
                .fetchOne();

        System.out.println("EXAMPLE 1 (inserted id):\n" + insertedId); // as Long, insertedId.value1()
    }

// EXAMPLE 2
    /*
    insert into `classicmodels`.`sale` (
      `sale_id`,`fiscal_year`,`sale`,`employee_number`)
    values
     (default, ?, ?, ?, default, ?, ?, default), 
     (default, ?, ?, ?, default, ?, ?, default), 
     (default, ?, ?, ?, default, ?, ?, default
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE)
                .values(default_(), 2004, 2311.42, 1370L,
                        default_(), SaleRate.PLATINUM, SaleVat.NONE, default_())
                .values(default_(), 2003, 900.21, 1504L,
                        default_(), SaleRate.SILVER, SaleVat.NONE, default_())
                .values(default_(), 2005, 1232.2, 1166L,
                        default_(), SaleRate.GOLD, SaleVat.MIN, default_())
                .returningResult(SALE.SALE_ID)
                .fetch();

        System.out.println("EXAMPLE 2 (inserted ids):\n" + insertedIds);
    }

    // EXAMPLE 3
    /*
    insert into `classicmodels`.`customer` (
      `customer_number`,`customer_name`,`contact_last_name`,`contact_first_name`,`phone`,
      `sales_rep_employee_number`,`credit_limit`)
    values
      (default, ?, ?, ?, ?, ?, ?, ?)
    
    insert into `classicmodels`.`customerdetail` (
      `customer_number`,`address_line_first`,`address_line_second`,`city`,
      `state`,`postal_code`,`country`)
    values
      (?, ?, ?, ?, ?, ?, ?)
     */
    public void insertReturningOfCustomerInCustomerDetail() {

        // passing explicit "null" instead of default_() produces implementation specific behaviour
        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER)
                                .values(default_(), "Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321", null, null, null)
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                "No. 14 Avenue", null, "Los Angeles", null, null, "USA")
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
              ?, `classicmodels`.`employee`.`last_name`
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

        var inserted = ctx.insertInto(MANAGER)
                .values(default_(), select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1165L)),
                        default_(), default_())
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();
        
        System.out.println("EXAMPLE 4 (inserted ids):\n" + inserted);
    }
}