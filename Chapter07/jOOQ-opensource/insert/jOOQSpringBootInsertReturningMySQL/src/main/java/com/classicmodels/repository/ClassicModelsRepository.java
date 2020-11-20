package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
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
    alues
      (?, ?, ?, ?)
     */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE)
                .values(null, 2004, 2311.42, 1370L)
                .returningResult(SALE.SALE_ID)
                .fetchOne();

        System.out.println("EXAMPLE 1 (inserted id): " + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    insert into `classicmodels`.`sale` (
      `sale_id`,`fiscal_year`,`sale`,`employee_number`)
    values
     (?, ?, ?, ?),(?, ?, ?, ?),(?, ?, ?, ?)
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE)
                .values(null, 2004, 2311.42, 1370L)
                .values(null, 2003, 900.21, 1504L)
                .values(null, 2005, 1232.2, 1166L)
                .returningResult(SALE.SALE_ID)
                .fetch();

        System.out.println("EXAMPLE 2 (inserted ids): " + insertedIds);
    }

    // EXAMPLE 3
    /*
    insert into `classicmodels`.`customer` (
      `customer_number`,`customer_name`,`contact_last_name`,`contact_first_name`,`phone`,
      `sales_rep_employee_number`,`credit_limit`)
    values
      (?, ?, ?, ?, ?, ?, ?)
    
    insert into `classicmodels`.`customerdetail` (
      `customer_number`,`address_line_first`,`address_line_second`,`city`,
      `state`,`postal_code`,`country`)
    values
      (?, ?, ?, ?, ?, ?, ?)
    */
    public void insertReturningOfCustomerInCustomerDetail() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER)
                                .values(null, "Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321", null, null)
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(), 
                                "No. 14 Avenue", null, "Los Angeles", null, null, "USA")
                        .execute()
        );
    }
}