package com.classicmodels.repository;

import java.util.Map;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.Comparator;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1    
    // if all you need is a sub-set of columns then 
    // avoid (or, at least pay attention to) these approaches 
    // since they (may) fetches too much data (all columns)
    public void findOrderAllFields() {

        /*
        select
          "public"."order"."order_id",
          "public"."order"."order_date",
          "public"."order"."required_date",
          "public"."order"."shipped_date",
          "public"."order"."status",
          "public"."order"."comments",
          "public"."order"."customer_number"
        from
          "public"."order"
        where
          "public"."order"."order_id" = ?
         */
        System.out.println("EXAMPLE 1.1\n"
                + ctx.select()
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );

        System.out.println("EXAMPLE 1.2\n"
                + ctx.selectFrom(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );

        System.out.println("EXAMPLE 1.3\n"
                + ctx.select(ORDER.fields())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );

        /*
        select
          *
        from
          "public"."order"
        where
          "public"."order"."order_id" = ?
         */
        System.out.println("EXAMPLE 1.4\n"
                + ctx.select(asterisk())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          "public"."order"."order_id", 
          "public"."order"."order_date", 
          "public"."order"."required_date", 
          "public"."order"."shipped_date", 
          "public"."order"."status", 
          "public"."order"."comments", 
          "public"."order"."customer_number", 
          "public"."orderdetail"."quantity_ordered" 
        from 
          "public"."order" 
          join "public"."orderdetail" on "public"."order"."order_id" = "public"."orderdetail"."order_id" 
        where 
          "public"."order"."order_id" = ?        
        */
        System.out.println("EXAMPLE 1.5\n"
                + ctx.select(ORDER.fields())
                        .select(ORDERDETAIL.QUANTITY_ORDERED)
                        .from(ORDER)
                        .innerJoin(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          "alias_94178018".* 
        from 
          (
            "public"."order" 
            left outer join "public"."customer" as "alias_94178018" 
             on "public"."order"."customer_number" = "alias_94178018"."customer_number"
          ) 
        where 
          "public"."order"."order_id" = 10101        
        */
        System.out.println("EXAMPLE 1.6\n"
                + ctx.select(ORDER.customer().asterisk())                        
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 2    
    // list only the columns (fields) that are needed
    /*
    select
      "public"."order"."order_id",
      "public"."order"."order_date",
      "public"."order"."required_date",
      "public"."order"."shipped_date",
      "public"."order"."customer_number"
    from
      "public"."order"
    where
      "public"."order"."order_id" = ?
     */
    public void findOrderExplicitFields() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(ORDER.ORDER_ID, ORDER.ORDER_DATE,
                        ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.CUSTOMER_NUMBER)
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 3
    // list the columns (fields) that should be skipped
    /*
    select
      "public"."order"."order_id",
      "public"."order"."order_date",
      "public"."order"."required_date",
      "public"."order"."shipped_date",
      "public"."order"."customer_number"
    from
      "public"."order"
    where
      "public"."order"."order_id" = ?
     */
    public void findOrderAsteriskExcept() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(asterisk().except(ORDER.COMMENTS, ORDER.STATUS))
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select
      "public"."employee"."first_name",
      "public"."employee"."last_name",
      "public"."sale".*
    from
      "public"."employee"
    join "public"."sale" on "public"."employee"."employee_number" = "public"."sale"."employee_number"
     */
    public void findOrderAndSale() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.asterisk())
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select
      "public"."employee"."last_name",
      "public"."employee"."first_name",
      "public"."employee"."extension",
      "public"."employee"."email",
      "public"."employee"."salary",
      "public"."employee"."reports_to",
      "public"."employee"."job_title",
      "public"."sale".*
    from
      "public"."employee"
    join "public"."sale" on "public"."employee"."employee_number" = "public"."sale"."employee_number"
     */
    public void findOrderExceptAndSale() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select(EMPLOYEE.asterisk().except(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.OFFICE_CODE),
                        SALE.asterisk())
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    // EXAMPLE 6
    /*
    select 
      "public"."office"."city" as "location", 
      "public"."office"."office_code", 
      "public"."office"."phone", 
      "public"."office"."address_line_first", 
      "public"."office"."address_line_second", 
      "public"."office"."state", 
      "public"."office"."country", 
      "public"."office"."postal_code", 
      "public"."office"."territory" 
    from 
      "public"."office"    
     */
    public void findOfficeUseAliasForCity() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(OFFICE.CITY.as("location"),
                        OFFICE.asterisk().except(OFFICE.CITY))
                        .from(OFFICE)
                        .fetch()
        );
    }

    // EXAMPLE 7
    /*
    select 
      coalesce("public"."office"."city", ?), 
      "public"."office"."office_code", 
      "public"."office"."phone", 
      "public"."office"."address_line_first", 
      "public"."office"."address_line_second", 
      "public"."office"."state", 
      "public"."office"."country", 
      "public"."office"."postal_code", 
      "public"."office"."territory" 
    from 
      "public"."office"    
     */
    public void findOfficeUseNvlForCity() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(nvl(OFFICE.CITY, "N/A"),
                        OFFICE.asterisk().except(OFFICE.CITY))
                        .from(OFFICE)
                        .fetch()
        );
    }

    // EXAMPLE 8
    /*
    select 
      ("public"."sale"."sale" > ?) as "saleGt5000", 
      "public"."sale"."sale_id", 
      "public"."sale"."fiscal_year", 
      "public"."sale"."employee_number" 
    from 
      "public"."sale"    
    */
    public void findSaleGt5000() {
        
        System.out.println("EXAMPLE 8\n"
                + ctx.select(field(SALE.SALE_.gt(5000.0)).as("saleGt5000"),                        
                        SALE.asterisk().except(SALE.SALE_))
                        .from(SALE)
                        .fetch()
        );
    }
    
    // EXAMPLE 9
    /*
    select 
      ("public"."sale"."sale" * ?) as "saleMul025", 
      "public"."sale"."sale_id", 
      "public"."sale"."fiscal_year", 
      "public"."sale"."employee_number" 
    from 
      "public"."sale"        
    */
    public void findSaleMul025() {
        
        System.out.println("EXAMPLE 9\n"
                + ctx.select(field(SALE.SALE_.mul(0.25)).as("saleMul025"),                        
                        SALE.asterisk().except(SALE.SALE_))
                        .from(SALE)
                        .fetch()
        );
    }
    
    // EXAMPLE 10
    /*
    select 
      "public"."office"."city", 
      "public"."office"."country", 
      "public"."office"."office_code" 
    from 
      "public"."office" 
    where 
      "public"."office"."city" in (?, ?, ?) 
    order by 
      case "public"."office"."city" when ? then 0 when ? then 1 when ? then 2 end asc    
    */
    // Consider reading: https://blog.jooq.org/2014/05/07/how-to-implement-sort-indirection-in-sql/
    public void findOfficeInCityByCertainSort() {

        String[] citiesArr = {"Paris", "Tokyo", "Boston"};
        System.out.println("EXAMPLE 10.1\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE)
                        .from(OFFICE)
                        .where(OFFICE.CITY.in(citiesArr))
                        .orderBy(OFFICE.CITY.sortAsc(citiesArr)) 
                        .fetch()
        );
        
        Map<String, Integer> citiesMap = Map.of("Paris", 1, "Tokyo", 3, "Boston", 2);
        System.out.println("EXAMPLE 10.2\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE)
                        .from(OFFICE)
                        .where(OFFICE.CITY.in(citiesMap.keySet()))
                        .orderBy(OFFICE.CITY.sort(citiesMap)) 
                        .fetch()
        );      
    }
    
    // EXAMPLE 11
    /*
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
    where 
      "public"."employee"."salary" in (
        select 
          "public"."employee"."salary" 
        from 
          "public"."employee" 
        where 
          "public"."employee"."salary" < ?
      ) 
    order by 
      "public"."employee"."salary"    
     */
    public void findEmployeeSalary60000(boolean isSaleRep) {

        System.out.println("EXAMPLE 11\n"
                + ctx.select()
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.compare(isSaleRep ? Comparator.IN : Comparator.NOT_IN,
                                select(EMPLOYEE.SALARY).from(EMPLOYEE).where(EMPLOYEE.SALARY.lt(60000))))
                        .orderBy(EMPLOYEE.SALARY)
                        .fetch()
        );
    }
    
    // EXAMPLE 12
    /*
    select
        "public"."employee"."first_name",
        "public"."employee"."last_name",
        "public"."employee"."salary" 
    from
        "public"."employee" 
    order by
        "public"."employee"."salary" fetch next ? rows only
     */
    public void findEmployeeLimit() {

        System.out.println("EXAMPLE 12\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY)
                        .limit(10)
                        .fetch()
        );
    }

    // EXAMPLE 13    
    public void findEmployeeLimitOffset() {

        /*
        select
            "public"."employee"."first_name",
            "public"."employee"."last_name",
            "public"."employee"."salary" 
        from
            "public"."employee" 
        order by
            "public"."employee"."salary" offset ? rows fetch next ? rows only
        */
        System.out.println("EXAMPLE 13.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY)                        
                        .limit(10)
                        .offset(5)
                        .fetch()
        );

        /*        
        select 
          "public"."orderdetail"."order_id", 
          "public"."orderdetail"."product_id", 
          "public"."orderdetail"."quantity_ordered" 
        from 
          "public"."orderdetail" 
        order by 
          "public"."orderdetail"."quantity_ordered" fetch next (
            select 
              min(
                "public"."orderdetail"."quantity_ordered"
              ) 
            from 
              "public"."orderdetail"
          ) rows only        
        */
        System.out.println("EXAMPLE 13.2\n"
                + ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED)
                        .from(ORDERDETAIL)
                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED)                        
                        .limit(field(select(min(ORDERDETAIL.QUANTITY_ORDERED)).from(ORDERDETAIL)))                        
                        .fetch()
        );
    }

    // EXAMPLE 14
    /*
    select
        "public"."employee"."first_name",
        "public"."employee"."last_name",
        "public"."employee"."salary" 
    from
        "public"."employee" 
    order by
        "public"."employee"."salary" offset ? rows fetch next ? rows only
     */
    public void findEmployeeLimitAndOffset() {

        System.out.println("EXAMPLE 14\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY)
                        .limit(5, 10)
                        .fetch()
        );
    }
    
    public void limit1InJoinedTable() {

        /*
    select
        "public"."productline"."product_line",
        "public"."productline"."code",
        "public"."product"."product_name",
        "public"."product"."quantity_in_stock",
        "public"."product"."product_id" 
    from
        "public"."productline" 
    join
        "public"."product" 
            on "public"."product"."product_id" = (
                select
                    "public"."product"."product_id" 
            from
                "public"."product" 
            where
                "public"."productline"."product_line" = "public"."product"."product_line" 
            order by
                "public"."product"."product_id" fetch next ? rows only
        )        
         */
        System.out.println("EXAMPLE 15\n"
                + ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE,
                        PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK, PRODUCT.PRODUCT_ID)
                        .from(PRODUCTLINE)
                        .join(PRODUCT)
                        .on(PRODUCT.PRODUCT_ID.eq(select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.PRODUCT_ID).limit(1)))
                        .fetch()
        );        
    }

    // EXAMPLE 16
    /*
    select
        "public"."office"."city",
        "public"."office"."country",
        "public"."employee"."job_title",
        "public"."customer"."customer_number",
        "public"."customer"."customer_name",
        "public"."customer"."phone",
        "public"."customer"."sales_rep_employee_number",
        "public"."customer"."credit_limit",
        "public"."customer"."first_buy_date",
        "public"."payment"."customer_number",
        "public"."payment"."check_number",
        "public"."payment"."payment_date",
        "public"."payment"."invoice_amount",
        "public"."payment"."caching_date",
        "public"."payment"."version",
        "public"."payment"."modified" 
    from
        "public"."office",
        "public"."employee",
        "public"."customer",
        "public"."payment" fetch next ? rows only    
     */
    public void decomposeSelect() {

        SelectQuery select = ctx.select()
                .from(OFFICE, EMPLOYEE, CUSTOMER, PAYMENT).limit(100).getQuery();

        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);
        select.addSelect(EMPLOYEE.JOB_TITLE);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));
        select.addSelect(PAYMENT.fields());

        System.out.println("EXAMPLE 16\n"
                + select.fetch()
        );
    }

    // EXAMPLE 17
    /*    
    select
        "public"."office"."city",
        "public"."office"."country",
        "public"."employee"."job_title",
        "public"."customer"."customer_number",
        "public"."customer"."customer_name",
        "public"."customer"."phone",
        "public"."customer"."sales_rep_employee_number",
        "public"."customer"."credit_limit",
        "public"."customer"."first_buy_date",
        "public"."payment"."customer_number",
        "public"."payment"."check_number",
        "public"."payment"."payment_date",
        "public"."payment"."invoice_amount",
        "public"."payment"."caching_date",
        "public"."payment"."version",
        "public"."payment"."modified" 
    from
        "public"."office",
        "public"."employee",
        "public"."customer",
        "public"."payment" fetch next ? rows only    
     */
    public void decomposeSelectAndFrom() {

        SelectQuery select = ctx.select().limit(100).getQuery();

        select.addFrom(OFFICE);
        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);

        select.addFrom(EMPLOYEE);
        select.addSelect(EMPLOYEE.JOB_TITLE);

        select.addFrom(CUSTOMER);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));

        select.addFrom(PAYMENT);
        select.addSelect(PAYMENT.fields());

        System.out.println("EXAMPLE 17\n"
                + select.fetch()
        );
    }
}