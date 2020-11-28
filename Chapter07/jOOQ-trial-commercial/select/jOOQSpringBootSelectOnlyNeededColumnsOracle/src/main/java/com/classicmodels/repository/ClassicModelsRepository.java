package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.nvl;
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
          "SYSTEM"."ORDER"."ORDER_ID", 
          "SYSTEM"."ORDER"."ORDER_DATE", 
          "SYSTEM"."ORDER"."REQUIRED_DATE", 
          "SYSTEM"."ORDER"."SHIPPED_DATE", 
          "SYSTEM"."ORDER"."STATUS", 
          "SYSTEM"."ORDER"."COMMENTS", 
          "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
        from 
          "SYSTEM"."ORDER" 
        where 
          "SYSTEM"."ORDER"."ORDER_ID" = ?
        */
        System.out.println("EXAMPLE 1.1\n" +
                ctx.select()
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 1.2\n" +
                ctx.selectFrom(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );        

        System.out.println("EXAMPLE 1.3\n" +
                ctx.select(ORDER.fields())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          * 
        from 
          "SYSTEM"."ORDER" 
        where 
          "SYSTEM"."ORDER"."ORDER_ID" = ?        
        */
        System.out.println("EXAMPLE 1.4\n" +
                ctx.select(asterisk())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          "SYSTEM"."ORDER"."ORDER_ID", 
          "SYSTEM"."ORDER"."ORDER_DATE", 
          "SYSTEM"."ORDER"."REQUIRED_DATE", 
          "SYSTEM"."ORDER"."SHIPPED_DATE", 
          "SYSTEM"."ORDER"."STATUS", 
          "SYSTEM"."ORDER"."COMMENTS", 
          "SYSTEM"."ORDER"."CUSTOMER_NUMBER", 
          "SYSTEM"."ORDERDETAIL"."QUANTITY_ORDERED" 
        from 
          "SYSTEM"."ORDER" 
          join "SYSTEM"."ORDERDETAIL" on "SYSTEM"."ORDER"."ORDER_ID" = "SYSTEM"."ORDERDETAIL"."ORDER_ID" 
        where 
          "SYSTEM"."ORDER"."ORDER_ID" = ?        
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
    }

    // EXAMPLE 2    
    // list only the columns (fields) that are needed
    /*
    select 
      "SYSTEM"."ORDER"."ORDER_ID", 
      "SYSTEM"."ORDER"."ORDER_DATE", 
      "SYSTEM"."ORDER"."REQUIRED_DATE", 
      "SYSTEM"."ORDER"."SHIPPED_DATE", 
      "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
    from 
      "SYSTEM"."ORDER" 
    where 
      "SYSTEM"."ORDER"."ORDER_ID" = ?    
    */
    public void findOrderExplicitFields() {

        System.out.println("EXAMPLE 2\n" +
                ctx.select(ORDER.ORDER_ID, ORDER.ORDER_DATE,
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
      "SYSTEM"."ORDER"."ORDER_ID", 
      "SYSTEM"."ORDER"."ORDER_DATE", 
      "SYSTEM"."ORDER"."REQUIRED_DATE", 
      "SYSTEM"."ORDER"."SHIPPED_DATE", 
      "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
    from 
      "SYSTEM"."ORDER" 
    where 
      "SYSTEM"."ORDER"."ORDER_ID" = ?    
    */
    public void findOrderAsteriskExcept() {

        System.out.println("EXAMPLE 3\n" +
                ctx.select(asterisk().except(ORDER.COMMENTS, ORDER.STATUS))
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."SALE".* 
    from 
      "SYSTEM"."EMPLOYEE" 
      join "SYSTEM"."SALE" on "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"    
    */
    public void findOrderAndSale() {

        System.out.println("EXAMPLE 4\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.asterisk())
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."EXTENSION", 
      "SYSTEM"."EMPLOYEE"."EMAIL", 
      "SYSTEM"."EMPLOYEE"."SALARY", 
      "SYSTEM"."EMPLOYEE"."REPORTS_TO", 
      "SYSTEM"."EMPLOYEE"."JOB_TITLE", 
      "SYSTEM"."SALE".* 
    from 
      "SYSTEM"."EMPLOYEE" 
      join "SYSTEM"."SALE" on "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"   
    */
    public void findOrderExceptAndSale() {

        System.out.println("EXAMPLE 5\n" +
                ctx.select(EMPLOYEE.asterisk().except(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.OFFICE_CODE),
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
      "SYSTEM"."OFFICE"."CITY" "location", 
      "SYSTEM"."OFFICE"."OFFICE_CODE", 
      "SYSTEM"."OFFICE"."PHONE", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_SECOND", 
      "SYSTEM"."OFFICE"."STATE", 
      "SYSTEM"."OFFICE"."COUNTRY", 
      "SYSTEM"."OFFICE"."POSTAL_CODE", 
      "SYSTEM"."OFFICE"."TERRITORY" 
    from 
      "SYSTEM"."OFFICE"    
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
      nvl("SYSTEM"."OFFICE"."CITY", ?), 
      "SYSTEM"."OFFICE"."OFFICE_CODE", 
      "SYSTEM"."OFFICE"."PHONE", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_SECOND", 
      "SYSTEM"."OFFICE"."STATE", 
      "SYSTEM"."OFFICE"."COUNTRY", 
      "SYSTEM"."OFFICE"."POSTAL_CODE", 
      "SYSTEM"."OFFICE"."TERRITORY" 
    from 
      "SYSTEM"."OFFICE"        
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
      case when "SYSTEM"."SALE"."SALE" > ? then 1 when not ("SYSTEM"."SALE"."SALE" > ?) 
         then 0 end "saleGt5000", 
      "SYSTEM"."SALE"."SALE_ID", 
      "SYSTEM"."SALE"."FISCAL_YEAR", 
      "SYSTEM"."SALE"."EMPLOYEE_NUMBER" 
    from 
      "SYSTEM"."SALE"    
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
      ("SYSTEM"."SALE"."SALE" * ?) "saleMul025", 
      "SYSTEM"."SALE"."SALE_ID", 
      "SYSTEM"."SALE"."FISCAL_YEAR", 
      "SYSTEM"."SALE"."EMPLOYEE_NUMBER" 
    from 
      "SYSTEM"."SALE"    
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
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."SALARY" 
    from 
      "SYSTEM"."EMPLOYEE" fetch next ? rows only    
    */
    public void findEmployeeLimit() {

        System.out.println("EXAMPLE 10\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(10)
                        .fetch()
        );
    }
    
    // EXAMPLE 11
    /*
    select 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."SALARY" 
    from 
      "SYSTEM"."EMPLOYEE" offset ? rows fetch next ? rows only    
    */
    public void findEmployeeLimitOffset() {

        System.out.println("EXAMPLE 11\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(10)
                        .offset(5)
                        .fetch()
        );
    }
    
    // EXAMPLE 12
    /*
    select 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."SALARY" 
    from 
      "SYSTEM"."EMPLOYEE" offset ? rows fetch next ? rows only    
    */
    public void findEmployeeLimitAndOffset() {

        System.out.println("EXAMPLE 12\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(5, 10)                        
                        .fetch()
        );
    }
    
    // EXAMPLE 13
    /*
    select 
      "v0" "CITY", 
      "v1" "COUNTRY", 
      "v2" "JOB_TITLE", 
      "v3" "CUSTOMER_NUMBER", 
      "v4" "CUSTOMER_NAME", 
      "v5" "PHONE", 
      "v6" "SALES_REP_EMPLOYEE_NUMBER", 
      "v7" "CREDIT_LIMIT", 
      "v8" "CUSTOMER_NUMBER", 
      "v9" "CHECK_NUMBER", 
      "v10" "PAYMENT_DATE", 
      "v11" "INVOICE_AMOUNT", 
      "v12" "CACHING_DATE" 
    from 
      (
        select 
          "x"."v0", 
          "x"."v1", 
          "x"."v2", 
          "x"."v3", 
          "x"."v4", 
          "x"."v5", 
          "x"."v6", 
          "x"."v7", 
          "x"."v8", 
          "x"."v9", 
          "x"."v10", 
          "x"."v11", 
          "x"."v12", 
          rownum "rn" 
        from 
          (
            select 
              "SYSTEM"."OFFICE"."CITY" "v0", 
              "SYSTEM"."OFFICE"."COUNTRY" "v1", 
              "SYSTEM"."EMPLOYEE"."JOB_TITLE" "v2", 
              "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" "v3", 
              "SYSTEM"."CUSTOMER"."CUSTOMER_NAME" "v4", 
              "SYSTEM"."CUSTOMER"."PHONE" "v5", 
              "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" "v6", 
              "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" "v7", 
              "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" "v8", 
              "SYSTEM"."PAYMENT"."CHECK_NUMBER" "v9", 
              "SYSTEM"."PAYMENT"."PAYMENT_DATE" "v10", 
              "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" "v11", 
              "SYSTEM"."PAYMENT"."CACHING_DATE" "v12" 
            from 
              "SYSTEM"."OFFICE", 
              "SYSTEM"."EMPLOYEE", 
              "SYSTEM"."CUSTOMER", 
              "SYSTEM"."PAYMENT"
          ) "x" 
        where 
          rownum <= (0 + ?)
      ) 
    where 
      "rn" > 0    
    */
    public void decomposeSelect() {
        
        SelectQuery select = ctx.select()
                .from(OFFICE, EMPLOYEE, CUSTOMER, PAYMENT).limit(100).getQuery();
                
        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);
        select.addSelect(EMPLOYEE.JOB_TITLE);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));
        select.addSelect(PAYMENT.fields());
        
        System.out.println("EXAMPLE 13\n" +
                select.fetch()
        );
    }
        
    // EXAMPLE 14
    /*    
    select 
      "v0" "CITY", 
      "v1" "COUNTRY", 
      "v2" "JOB_TITLE", 
      "v3" "CUSTOMER_NUMBER", 
      "v4" "CUSTOMER_NAME", 
      "v5" "PHONE", 
      "v6" "SALES_REP_EMPLOYEE_NUMBER", 
      "v7" "CREDIT_LIMIT", 
      "v8" "CUSTOMER_NUMBER", 
      "v9" "CHECK_NUMBER", 
      "v10" "PAYMENT_DATE", 
      "v11" "INVOICE_AMOUNT", 
      "v12" "CACHING_DATE" 
    from 
      (
        select 
          "x"."v0", 
          "x"."v1", 
          "x"."v2", 
          "x"."v3", 
          "x"."v4", 
          "x"."v5", 
          "x"."v6", 
          "x"."v7", 
          "x"."v8", 
          "x"."v9", 
          "x"."v10", 
          "x"."v11", 
          "x"."v12", 
          rownum "rn" 
        from 
          (
            select 
              "SYSTEM"."OFFICE"."CITY" "v0", 
              "SYSTEM"."OFFICE"."COUNTRY" "v1", 
              "SYSTEM"."EMPLOYEE"."JOB_TITLE" "v2", 
              "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" "v3", 
              "SYSTEM"."CUSTOMER"."CUSTOMER_NAME" "v4", 
              "SYSTEM"."CUSTOMER"."PHONE" "v5", 
              "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" "v6", 
              "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" "v7", 
              "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" "v8", 
              "SYSTEM"."PAYMENT"."CHECK_NUMBER" "v9", 
              "SYSTEM"."PAYMENT"."PAYMENT_DATE" "v10", 
              "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" "v11", 
              "SYSTEM"."PAYMENT"."CACHING_DATE" "v12" 
            from 
              "SYSTEM"."OFFICE", 
              "SYSTEM"."EMPLOYEE", 
              "SYSTEM"."CUSTOMER", 
              "SYSTEM"."PAYMENT"
          ) "x" 
        where 
          rownum <= (0 + ?)
      ) 
    where 
      "rn" > 0    
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
        
        System.out.println("EXAMPLE 14\n" +
                select.fetch()
        );
    }    
}