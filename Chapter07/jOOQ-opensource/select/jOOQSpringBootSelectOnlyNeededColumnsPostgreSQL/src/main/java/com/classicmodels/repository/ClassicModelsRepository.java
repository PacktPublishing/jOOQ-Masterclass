package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import static org.jooq.impl.DSL.asterisk;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
 
    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    // if all you need is a sub-set of columns then 
    // avoid these approach since they fetches too much data (all columns)
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
          "public"."order"
        where
          "public"."order"."order_id" = ?
        */
        System.out.println("EXAMPLE 1.4\n" +
                ctx.select(asterisk())
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
      "public"."employee"."first_name",
      "public"."employee"."last_name",
      "public"."sale".*
    from
      "public"."employee"
    join "public"."sale" on "public"."employee"."employee_number" = "public"."sale"."employee_number"
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
      "public"."employee"."first_name",
      "public"."employee"."last_name",
      "public"."employee"."salary"
    from
      "public"."employee"
    limit ?
    */
    public void findEmployeeLimit() {

        System.out.println("EXAMPLE 6\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(10)
                        .fetch()
        );
    }
    
    // EXAMPLE 7
    /*
    select
      "public"."employee"."first_name",
      "public"."employee"."last_name",
      "public"."employee"."salary"
    from
      "public"."employee"
    limit
      ? offset ?
    */
    public void findEmployeeLimitOffset() {

        System.out.println("EXAMPLE 7\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(10)
                        .offset(5)
                        .fetch()
        );
    }
    
    // EXAMPLE 8
    /*
    select
      "public"."employee"."first_name",
      "public"."employee"."last_name",
      "public"."employee"."salary"
    from
      "public"."employee"
    limit
      ? offset ?
    */
    public void findEmployeeLimitAndOffset() {

        System.out.println("EXAMPLE 8\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(5, 10)                        
                        .fetch()
        );
    }
    
    // EXAMPLE 9
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
      "public"."payment"."customer_number",
      "public"."payment"."check_number",
      "public"."payment"."payment_date",
      "public"."payment"."invoice_amount",
      "public"."payment"."caching_date"
    from
      "public"."office",
      "public"."employee",
      "public"."customer",
      "public"."payment"
    limit ?
    */
    public void decomposeSelect() {
        
        SelectQuery select = ctx.select()
                .from(OFFICE, EMPLOYEE, CUSTOMER, PAYMENT).limit(100).getQuery();
                
        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);
        select.addSelect(EMPLOYEE.JOB_TITLE);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));
        select.addSelect(PAYMENT.fields());
        
        System.out.println("EXAMPLE 9\n" +
                select.fetch()
        );
    }
        
    // EXAMPLE 10
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
      "public"."payment"."customer_number",
      "public"."payment"."check_number",
      "public"."payment"."payment_date",
      "public"."payment"."invoice_amount",
      "public"."payment"."caching_date"
    from
      "public"."office",
      "public"."employee",
      "public"."customer",
      "public"."payment"
    limit ?
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
        
        System.out.println("EXAMPLE 10\n" +
                select.fetch()
        );
    }    
}