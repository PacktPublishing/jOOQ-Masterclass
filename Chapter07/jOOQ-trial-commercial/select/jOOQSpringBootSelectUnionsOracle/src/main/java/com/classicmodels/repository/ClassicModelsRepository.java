package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    select 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME" 
    from 
      "SYSTEM"."EMPLOYEE" 
    union 
    select 
      "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME", 
      "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME" 
    from 
      "SYSTEM"."CUSTOMER"    
    */
    public void unionEmployeeAndCustomerNames() {

        System.out.println("EXAMPLE 1\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .union(select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select 
      (
        "SYSTEM"."EMPLOYEE"."FIRST_NAME" || ? || "SYSTEM"."EMPLOYEE"."LAST_NAME"
      ) "full_name" 
    from 
      "SYSTEM"."EMPLOYEE" 
    union 
    select 
      (
        "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME" || ? || "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME"
      ) 
    from 
      "SYSTEM"."CUSTOMER"    
    */
    public void unionEmployeeAndCustomerNamesConcatColumns() {

        System.out.println("EXAMPLE 2\n" +
                ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      (
        "SYSTEM"."EMPLOYEE"."FIRST_NAME" || ? || "SYSTEM"."EMPLOYEE"."LAST_NAME"
      ) "full_name", 
      ? "contactType" 
    from 
      "SYSTEM"."EMPLOYEE" 
    union 
    select 
      (
        "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME" || ? || "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME"
      ), 
      ? "contactType" 
    from 
      "SYSTEM"."CUSTOMER"    
    */
    public void unionEmployeeAndCustomerNamesDifferentiate() {

        System.out.println("EXAMPLE 3\n" +
                ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, val(" "),
                                EMPLOYEE.LAST_NAME).as("full_name"),
                        val("Employee").as("contactType"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                                        CUSTOMER.CONTACT_LAST_NAME),
                                val("Customer").as("contactType"))
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      (
        "SYSTEM"."EMPLOYEE"."FIRST_NAME" || ? || "SYSTEM"."EMPLOYEE"."LAST_NAME"
      ) "full_name" 
    from 
      "SYSTEM"."EMPLOYEE" 
    union 
    select 
      (
        "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME" || ? || "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME"
      ) 
    from 
      "SYSTEM"."CUSTOMER" 
    order by 
      "full_name"    
    */
    public void unionEmployeeAndCustomerNamesOrderBy() {

        System.out.println("EXAMPLE 4\n" +
                ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as(name("full_name")))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .orderBy(field(name("full_name")))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    (
      select 
        "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER", 
        "SYSTEM"."EMPLOYEE"."LAST_NAME", 
        "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
        "SYSTEM"."EMPLOYEE"."EXTENSION", 
        "SYSTEM"."EMPLOYEE"."EMAIL", 
        "SYSTEM"."EMPLOYEE"."OFFICE_CODE", 
        "SYSTEM"."EMPLOYEE"."SALARY", 
        "SYSTEM"."EMPLOYEE"."REPORTS_TO", 
        "SYSTEM"."EMPLOYEE"."JOB_TITLE" 
      from 
        "SYSTEM"."EMPLOYEE" 
      order by 
        "SYSTEM"."EMPLOYEE"."SALARY" asc fetch next ? rows only
    ) 
    union 
      (
        select 
          "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER", 
          "SYSTEM"."EMPLOYEE"."LAST_NAME", 
          "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
          "SYSTEM"."EMPLOYEE"."EXTENSION", 
          "SYSTEM"."EMPLOYEE"."EMAIL", 
          "SYSTEM"."EMPLOYEE"."OFFICE_CODE", 
          "SYSTEM"."EMPLOYEE"."SALARY", 
          "SYSTEM"."EMPLOYEE"."REPORTS_TO", 
          "SYSTEM"."EMPLOYEE"."JOB_TITLE" 
        from 
          "SYSTEM"."EMPLOYEE" 
        order by 
          "SYSTEM"."EMPLOYEE"."SALARY" desc fetch next ? rows only
      ) 
    order by 
      1    
    */
    public void unionEmployeeSmallestAndHighestSalary() {

        System.out.println("EXAMPLE 5\n" +
                ctx.selectFrom(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY.asc()).limit(1)
                        .union(
                                selectFrom(EMPLOYEE)
                                        .orderBy(EMPLOYEE.SALARY.desc()).limit(1))
                        .orderBy(1)
                        .fetch()
        );
    }

    // EXAMPLE 6
    /*
    select 
      "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."COUNTRY" 
    from 
      "SYSTEM"."OFFICE" 
    union all 
    select 
      "SYSTEM"."CUSTOMERDETAIL"."CITY", 
      "SYSTEM"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "SYSTEM"."CUSTOMERDETAIL" 
    order by 
      "CITY", 
      "COUNTRY"   
    */    
    public void unionAllOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 6\n" +
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .unionAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }

    // EXAMPLE 7
    /*
    select 
      "R1"."PRODUCT_ID", 
      "R1"."ORDER_ID", 
      min("R1"."PRICE_EACH") "min_price", 
      count(
        case when not (
          exists (
            select 
              "R2"."ORDER_ID", 
              "R2"."PRODUCT_ID", 
              "R2"."QUANTITY_ORDERED", 
              "R2"."PRICE_EACH", 
              "R2"."ORDER_LINE_NUMBER" 
            from 
              "SYSTEM"."ORDERDETAIL" "R2" 
            where 
              (
                "R2"."PRODUCT_ID" = "R1"."PRODUCT_ID" 
                and "R2"."PRICE_EACH" < "R1"."PRICE_EACH"
              )
          )
        ) then ? end
      ) "worst_price", 
      max("R1"."PRICE_EACH") "max_price", 
      count(
        case when not (
          exists (
            select 
              "R3"."ORDER_ID", 
              "R3"."PRODUCT_ID", 
              "R3"."QUANTITY_ORDERED", 
              "R3"."PRICE_EACH", 
              "R3"."ORDER_LINE_NUMBER" 
            from 
              "SYSTEM"."ORDERDETAIL" "R3" 
            where 
              (
                "R3"."PRODUCT_ID" = "R1"."PRODUCT_ID" 
                and "R3"."PRICE_EACH" > "R1"."PRICE_EACH"
              )
          )
        ) then ? end
      ) "best_price" 
    from 
      "SYSTEM"."ORDERDETAIL" "R1" 
    group by 
      "R1"."PRODUCT_ID", 
      "R1"."ORDER_ID"    
    */
    public void findMinMaxWorstBestPrice() {
        
        Orderdetail R1 = ORDERDETAIL.as("R1");
        Orderdetail R2 = ORDERDETAIL.as("R2");
        Orderdetail R3 = ORDERDETAIL.as("R3");
        
        System.out.println("EXAMPLE 7\n" +
                ctx.select(R1.PRODUCT_ID, R1.ORDER_ID, min(R1.PRICE_EACH).as("min_price"),
                        count(case_()
                                .when(notExists(select().from(R2)
                                        .where(R2.PRODUCT_ID.eq(R1.PRODUCT_ID)
                                                .and(R2.PRICE_EACH.lt(R1.PRICE_EACH)))), 1)                      
                        ).as("worst_price"), max(R1.PRICE_EACH).as("max_price"),
                        count(case_()
                                .when(notExists(select().from(R3)
                                        .where(R3.PRODUCT_ID.eq(R1.PRODUCT_ID)
                                                .and(R3.PRICE_EACH.gt(R1.PRICE_EACH)))), 1)                      
                        ).as("best_price"))
                        .from(R1)
                        .groupBy(R1.PRODUCT_ID, R1.ORDER_ID)
                        .fetch()
        );                 
    }   
    
    // EXAMPLE 8
    /*
    select "alias_79896943"."ORDER_ID",
           "alias_79896943"."PRICE_EACH",
           "alias_79896943"."QUANTITY_ORDERED"
    from (
            (select "SYSTEM"."ORDERDETAIL"."ORDER_ID",
                    "SYSTEM"."ORDERDETAIL"."PRICE_EACH",
                    "SYSTEM"."ORDERDETAIL"."QUANTITY_ORDERED"
             from "SYSTEM"."ORDERDETAIL"
             where "SYSTEM"."ORDERDETAIL"."QUANTITY_ORDERED" <= ?
             order by "SYSTEM"."ORDERDETAIL"."PRICE_EACH" FETCH NEXT ? ROWS ONLY)
          union
            (select "SYSTEM"."ORDERDETAIL"."ORDER_ID",
                    "SYSTEM"."ORDERDETAIL"."PRICE_EACH",
                    "SYSTEM"."ORDERDETAIL"."QUANTITY_ORDERED"
             from "SYSTEM"."ORDERDETAIL"
             where "SYSTEM"."ORDERDETAIL"."QUANTITY_ORDERED" >= ?
             order by "SYSTEM"."ORDERDETAIL"."PRICE_EACH" FETCH NEXT ? ROWS ONLY)) "alias_79896943"    
    */
    public void findTop5OrdersHavingQuantityOrderedLe20AndGe60OrderedByPrice() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select().from(
                        select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.QUANTITY_ORDERED.le(20L))
                                .orderBy(ORDERDETAIL.PRICE_EACH).limit(5)
                                .union(
                                        select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                                                .from(ORDERDETAIL)
                                                .where(ORDERDETAIL.QUANTITY_ORDERED.ge(60L))
                                                .orderBy(ORDERDETAIL.PRICE_EACH).limit(5)
                                )
                )
                        .fetch()
        );
    }
}