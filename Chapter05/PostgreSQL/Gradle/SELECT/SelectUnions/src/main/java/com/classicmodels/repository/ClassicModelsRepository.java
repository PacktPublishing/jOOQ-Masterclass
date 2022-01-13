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
      "public"."employee"."first_name", 
      "public"."employee"."last_name" 
    from 
      "public"."employee" 
    union 
    select 
      "public"."customer"."contact_first_name", 
      "public"."customer"."contact_last_name" 
    from 
      "public"."customer"
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
        "public"."employee"."first_name" || ' ' || "public"."employee"."last_name"
      ) as "full_name" 
    from 
      "public"."employee" 
    union 
    select 
      (
        "public"."customer"."contact_first_name" || ' ' || "public"."customer"."contact_last_name"
      ) 
    from 
      "public"."customer"
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
        "public"."employee"."first_name" || ' ' || "public"."employee"."last_name"
      ) as "full_name", 
      'Employee' as "contactType" 
    from 
      "public"."employee" 
    union 
    select 
      (
        "public"."customer"."contact_first_name" || ' ' || "public"."customer"."contact_last_name"
      ), 
      'Customer' as "contactType" 
    from 
      "public"."customer"    
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
        "public"."employee"."first_name" || ' ' || "public"."employee"."last_name"
      ) as "full_name" 
    from 
      "public"."employee" 
    union 
    select 
      (
        "public"."customer"."contact_first_name" || ' ' || "public"."customer"."contact_last_name"
      ) 
    from 
      "public"."customer" 
    order by 
      full_name    
     */
    public void unionEmployeeAndCustomerNamesOrderBy() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .orderBy(field("full_name"))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    (
      select 
        "public"."employee"."employee_number", 
        "public"."employee"."last_name", 
        "public"."employee"."first_name", 
        "public"."employee"."extension", 
        "public"."employee"."email", 
        "public"."employee"."office_code", 
        "public"."employee"."salary", 
        "public"."employee"."reports_to", 
        "public"."employee"."job_title" 
      from 
        "public"."employee" 
      order by 
        "public"."employee"."salary" asc 
      limit 
        ?
    ) 
    union 
      (
        select 
          "public"."employee"."employee_number", 
          "public"."employee"."last_name", 
          "public"."employee"."first_name", 
          "public"."employee"."extension", 
          "public"."employee"."email", 
          "public"."employee"."office_code", 
          "public"."employee"."salary", 
          "public"."employee"."reports_to", 
          "public"."employee"."job_title" 
        from 
          "public"."employee" 
        order by 
          "public"."employee"."salary" desc 
        limit 
          ?
      ) 
    order by 
      1
     */
    public void unionEmployeeSmallestAndHighestSalary() {

        System.out.println("EXAMPLE 5\n"
                + ctx.selectFrom(EMPLOYEE)
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
      "public"."office"."city", 
      "public"."office"."country" 
    from 
      "public"."office" 
    union all 
    select 
      "public"."customerdetail"."city", 
      "public"."customerdetail"."country" 
    from 
      "public"."customerdetail" 
    order by 
      "city", 
      "country"
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
    "R1"."product_id", 
    "R1"."order_id", 
    min("R1"."price_each") as "min_price", 
    count(*) filter (
      where 
        not (
          exists (
            select 
              "R2"."orderdetail_id", 
              "R2"."order_id", 
              "R2"."product_id", 
              "R2"."quantity_ordered", 
              "R2"."price_each", 
              "R2"."order_line_number" 
            from 
              "public"."orderdetail" as "R2" 
            where 
              (
                "R2"."product_id" = "R1"."product_id" 
                and "R2"."price_each" < "R1"."price_each"
              )
          )
        )
    ) as "worst_price", 
    max("R1"."price_each") as "max_price", 
    count(*) filter (
      where 
        not (
          exists (
            select 
              "R3"."orderdetail_id", 
              "R3"."order_id", 
              "R3"."product_id", 
              "R3"."quantity_ordered", 
              "R3"."price_each", 
              "R3"."order_line_number" 
            from 
              "public"."orderdetail" as "R3" 
            where 
              (
                "R3"."product_id" = "R1"."product_id" 
                and "R3"."price_each" > "R1"."price_each"
              )
          )
        )
    ) as "best_price" 
  from 
    "public"."orderdetail" as "R1" 
  group by 
    "R1"."product_id", 
    "R1"."order_id"  
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
      "t"."order_id", 
      "t"."price_each", 
      "t"."quantity_ordered" 
    from 
      (
        (
          select 
            "public"."orderdetail"."order_id", 
            "public"."orderdetail"."price_each", 
            "public"."orderdetail"."quantity_ordered" 
          from 
            "public"."orderdetail" 
          where 
            "public"."orderdetail"."quantity_ordered" <= ? 
          order by 
            "public"."orderdetail"."price_each" fetch next ? rows only
        ) 
        union 
          (
            select 
              "public"."orderdetail"."order_id", 
              "public"."orderdetail"."price_each", 
              "public"."orderdetail"."quantity_ordered" 
            from 
              "public"."orderdetail" 
            where 
              "public"."orderdetail"."quantity_ordered" >= ? 
            order by 
              "public"."orderdetail"."price_each" fetch next ? rows only
          )
      ) as "t"    
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
    create table product_stock AS
    select "public"."product"."product_name"
    from "public"."product"
    where "public"."product"."quantity_in_stock" < ?
    union
    select "public"."product"."product_name"
    from "public"."product"
    where "public"."product"."quantity_in_stock" >= ?    
     */
    public void findProductStockLt500Gt9500() {

        ctx.dropTableIfExists("product_stock").execute();

        ctx.select(PRODUCT.PRODUCT_NAME)
                .into(table("product_stock"))
                .from(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.lt(500))
                .union(
                        select(PRODUCT.PRODUCT_NAME)
                                .from(PRODUCT)
                                .where(PRODUCT.QUANTITY_IN_STOCK.ge(9500))
                ).fetch();

        System.out.println("EXAMPLE 9\n"
                + ctx.selectFrom(table("product_stock")).fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      "public"."customer"."customer_number", 
      count(*) as "silver", 
      0 as "gold", 
      0 as "platinum" 
    from 
      "public"."customer" 
      join "public"."payment" on "public"."customer"."customer_number" = "public"."payment"."customer_number" 
    group by 
      "public"."customer"."customer_number" 
    having 
      count(*) < ? 
    union 
    select 
      "public"."customer"."customer_number", 
      0, 
      count(*), 
      0 
    from 
      "public"."customer" 
      join "public"."payment" on "public"."customer"."customer_number" = "public"."payment"."customer_number" 
    group by 
      "public"."customer"."customer_number" 
    having 
      count(*) = ? 
    union 
    select 
      "public"."customer"."customer_number", 
      0, 
      0, 
      count(*) 
    from 
      "public"."customer" 
      join "public"."payment" on "public"."customer"."customer_number" = "public"."payment"."customer_number" 
    group by 
      "public"."customer"."customer_number" 
    having 
      count(*) > ?    
     */
    public void findSilverGoldPlatinumCustomers() {
        System.out.println("EXAMPLE 10\n"
                + ctx.select(CUSTOMER.CUSTOMER_NUMBER, count().as("silver"), inline(0).as("gold"), inline(0).as("platinum"))
                        .from(CUSTOMER)
                        .join(PAYMENT)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                        .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                        .having(count().lt(2))
                        .union(
                                select(CUSTOMER.CUSTOMER_NUMBER, inline(0), count(), inline(0))
                                        .from(CUSTOMER)
                                        .join(PAYMENT)
                                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                                        .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                                        .having(count().eq(2))
                        ).union(
                                select(CUSTOMER.CUSTOMER_NUMBER, inline(0), inline(0), count())
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
