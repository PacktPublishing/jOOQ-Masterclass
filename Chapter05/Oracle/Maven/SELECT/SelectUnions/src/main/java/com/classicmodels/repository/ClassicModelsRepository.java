package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
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
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    union 
    select 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME", 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME" 
    from 
      "CLASSICMODELS"."CUSTOMER"    
     */
    public void unionEmployeeAndCustomerNames() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
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
        "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME" || ' ' || "CLASSICMODELS"."EMPLOYEE"."LAST_NAME"
      ) "full_name" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    union 
    select 
      (
        "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME" || ' ' || "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME"
      ) 
    from 
      "CLASSICMODELS"."CUSTOMER"
     */
    public void unionEmployeeAndCustomerNamesConcatColumns() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      (
        "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME" || ' ' || "CLASSICMODELS"."EMPLOYEE"."LAST_NAME"
      ) "full_name", 
      'Employee' "contactType" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    union 
    select 
      (
        "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME" || ' ' || "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME"
      ), 
      'Customer' "contactType" 
    from 
      "CLASSICMODELS"."CUSTOMER"
     */
    public void unionEmployeeAndCustomerNamesDifferentiate() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, inline(" "),
                                EMPLOYEE.LAST_NAME).as("full_name"),
                        inline("Employee").as("contactType"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "),
                                        CUSTOMER.CONTACT_LAST_NAME),
                                inline("Customer").as("contactType"))
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      (
        "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME" || ' ' || "CLASSICMODELS"."EMPLOYEE"."LAST_NAME"
      ) "full_name" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    union 
    select 
      (
        "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME" || ' ' || "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME"
      ) 
    from 
      "CLASSICMODELS"."CUSTOMER" 
    order by 
      "full_name"    
     */
    public void unionEmployeeAndCustomerNamesOrderBy() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as(name("full_name")))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .orderBy(field(name("full_name")))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    (
      select 
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
        "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
        "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
        "CLASSICMODELS"."EMPLOYEE"."EXTENSION", 
        "CLASSICMODELS"."EMPLOYEE"."EMAIL", 
        "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE", 
        "CLASSICMODELS"."EMPLOYEE"."SALARY", 
        "CLASSICMODELS"."EMPLOYEE"."COMMISSION", 
        "CLASSICMODELS"."EMPLOYEE"."REPORTS_TO", 
        "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" 
      from 
        "CLASSICMODELS"."EMPLOYEE" 
      order by 
        "CLASSICMODELS"."EMPLOYEE"."SALARY" asc fetch next ? rows only
    ) 
    union 
      (
        select 
          "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
          "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."EXTENSION", 
          "CLASSICMODELS"."EMPLOYEE"."EMAIL", 
          "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE", 
          "CLASSICMODELS"."EMPLOYEE"."SALARY", 
          "CLASSICMODELS"."EMPLOYEE"."COMMISSION", 
          "CLASSICMODELS"."EMPLOYEE"."REPORTS_TO", 
          "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        order by 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" desc fetch next ? rows only
      ) 
    order by 
      1    
     */
    public void unionEmployeeSmallestAndHighestSalary() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select(EMPLOYEE.asterisk().except(EMPLOYEE.EMPLOYEE_OF_YEAR, EMPLOYEE.MONTHLY_BONUS))
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY.asc()).limit(1)
                        .union(
                                select(EMPLOYEE.asterisk().except(EMPLOYEE.EMPLOYEE_OF_YEAR, EMPLOYEE.MONTHLY_BONUS))
                                        .from(EMPLOYEE)
                                        .orderBy(EMPLOYEE.SALARY.desc()).limit(1))
                        .orderBy(1)
                        .fetch()
        );
    }

    // EXAMPLE 6
    /*
    select 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."COUNTRY" 
    from 
      "CLASSICMODELS"."OFFICE" 
    union all 
    select 
      "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "CLASSICMODELS"."CUSTOMERDETAIL" 
    order by 
      "CITY", 
      "COUNTRY"   
     */
    public void unionAllOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
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
              "R2"."ORDERDETAIL_ID", 
              "R2"."ORDER_ID", 
              "R2"."PRODUCT_ID", 
              "R2"."QUANTITY_ORDERED", 
              "R2"."PRICE_EACH", 
              "R2"."ORDER_LINE_NUMBER" 
            from 
              "CLASSICMODELS"."ORDERDETAIL" "R2" 
            where 
              (
                "R2"."PRODUCT_ID" = "R1"."PRODUCT_ID" 
                and "R2"."PRICE_EACH" < "R1"."PRICE_EACH"
              )
          )
        ) then 1 end
      ) "worst_price", 
      max("R1"."PRICE_EACH") "max_price", 
      count(
        case when not (
          exists (
            select 
              "R3"."ORDERDETAIL_ID", 
              "R3"."ORDER_ID", 
              "R3"."PRODUCT_ID", 
              "R3"."QUANTITY_ORDERED", 
              "R3"."PRICE_EACH", 
              "R3"."ORDER_LINE_NUMBER" 
            from 
              "CLASSICMODELS"."ORDERDETAIL" "R3" 
            where 
              (
                "R3"."PRODUCT_ID" = "R1"."PRODUCT_ID" 
                and "R3"."PRICE_EACH" > "R1"."PRICE_EACH"
              )
          )
        ) then 1 end
      ) "best_price" 
    from 
      "CLASSICMODELS"."ORDERDETAIL" "R1" 
    group by 
      "R1"."PRODUCT_ID", 
      "R1"."ORDER_ID"    
     */
    public void findMinMaxWorstBestPrice() {

        Orderdetail R1 = ORDERDETAIL.as("R1");
        Orderdetail R2 = ORDERDETAIL.as("R2");
        Orderdetail R3 = ORDERDETAIL.as("R3");

        System.out.println("EXAMPLE 7.1\n"
                + ctx.select(R1.PRODUCT_ID, R1.ORDER_ID, min(R1.PRICE_EACH).as("min_price"),
                        count().filterWhere(notExists(select().from(R2)
                                .where(R2.PRODUCT_ID.eq(R1.PRODUCT_ID)
                                        .and(R2.PRICE_EACH.lt(R1.PRICE_EACH)))))
                                .as("worst_price"), max(R1.PRICE_EACH).as("max_price"),
                        count().filterWhere(notExists(select().from(R3)
                                .where(R3.PRODUCT_ID.eq(R1.PRODUCT_ID)
                                        .and(R3.PRICE_EACH.gt(R1.PRICE_EACH)))))
                                .as("best_price"))
                        .from(R1)
                        .groupBy(R1.PRODUCT_ID, R1.ORDER_ID)
                        .fetch()
        );

        System.out.println("EXAMPLE 7.2\n"
                + ctx.select(R1.PRODUCT_ID, R1.ORDER_ID, min(R1.PRICE_EACH).as("min_price"),
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
    select 
      "t"."ORDER_ID", 
      "t"."PRICE_EACH", 
      "t"."QUANTITY_ORDERED" 
    from 
      (
        (
          select 
            "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID", 
            "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH", 
            "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" 
          from 
            "CLASSICMODELS"."ORDERDETAIL" 
          where 
            "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" <= ? 
          order by 
            "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" fetch next ? rows only
        ) 
        union 
          (
            select 
              "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID", 
              "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH", 
              "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" 
            from 
              "CLASSICMODELS"."ORDERDETAIL" 
            where 
              "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" >= ? 
            order by 
              "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" fetch next ? rows only
          )
      ) "t"    
     */
    public void findTop5OrdersHavingQuantityOrderedLe20AndGe60OrderedByPrice() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select().from(
                        select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.QUANTITY_ORDERED.le(20))
                                .orderBy(ORDERDETAIL.PRICE_EACH).limit(5)
                                .union(
                                        select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                                                .from(ORDERDETAIL)
                                                .where(ORDERDETAIL.QUANTITY_ORDERED.ge(60))
                                                .orderBy(ORDERDETAIL.PRICE_EACH).limit(5)
                                ).asTable("t"))
                        .fetch()
        );
    }

    // EXAMPLE 9
    /*
    create table "product_stock" AS
    select "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME"
    from "CLASSICMODELS"."PRODUCT"
    where "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK" < 500
    union
    select "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME"
    from "CLASSICMODELS"."PRODUCT"
    where "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK" >= 9500    
     */
    public void findProductStockLt500Gt9500() {

        ctx.dropTableIfExists(name("product_stock")).execute();

        ctx.select(PRODUCT.PRODUCT_NAME)
                .into(table(name("product_stock")))
                .from(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.lt(inline(500)))
                .union(
                        select(PRODUCT.PRODUCT_NAME)
                                .from(PRODUCT)
                                .where(PRODUCT.QUANTITY_IN_STOCK.ge(inline(9500)))
                ).fetch();

        System.out.println("EXAMPLE 9\n"
                + ctx.selectFrom(table(name("product_stock"))).fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      "t"."ORDER_ID", 
      "t"."PRICE_EACH", 
      "t"."QUANTITY_ORDERED" 
    from 
      (
        (
          select 
            "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID", 
            "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH", 
            "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" 
          from 
            "CLASSICMODELS"."ORDERDETAIL" 
          where 
            "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" <= ? 
          order by 
            "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" fetch next ? rows only
        ) 
        union 
          (
            select 
              "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID", 
              "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH", 
              "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" 
            from 
              "CLASSICMODELS"."ORDERDETAIL" 
            where 
              "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" >= ? 
            order by 
              "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" fetch next ? rows only
          )
      ) "t"      
     */
    public void findSilverGoldPlatinumCustomers() {
        System.out.println("EXAMPLE 10\n"
                + ctx.select(CUSTOMER.CUSTOMER_NUMBER, count().as("silver"), inline(0), inline(0))
                        .from(CUSTOMER)
                        .join(PAYMENT)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                        .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                        .having(count().lt(2))
                        .union(
                                select(CUSTOMER.CUSTOMER_NUMBER, inline(0), count().as("gold"), inline(0))
                                        .from(CUSTOMER)
                                        .join(PAYMENT)
                                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                                        .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                                        .having(count().eq(2))
                        ).union(
                                select(CUSTOMER.CUSTOMER_NUMBER, inline(0), inline(0), count().as("platinum"))
                                        .from(CUSTOMER)
                                        .join(PAYMENT)
                                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                                        .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                                        .having(count().gt(2))
                        )
                        .fetch()
        );
    }
}
