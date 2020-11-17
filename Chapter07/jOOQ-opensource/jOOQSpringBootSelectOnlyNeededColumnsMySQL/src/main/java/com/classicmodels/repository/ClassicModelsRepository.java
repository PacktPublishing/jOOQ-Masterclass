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
          `classicmodels`.`order`.`order_id`,
          `classicmodels`.`order`.`order_date`,
          `classicmodels`.`order`.`required_date`,
          `classicmodels`.`order`.`shipped_date`,
          `classicmodels`.`order`.`status`,
          `classicmodels`.`order`.`comments`,
          `classicmodels`.`order`.`customer_number`
        from
          `classicmodels`.`order`
        where
          `classicmodels`.`order`.`order_id` = ?
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
          `classicmodels`.`order`
        where
          `classicmodels`.`order`.`order_id` = ?
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
      `classicmodels`.`order`.`order_id`,
      `classicmodels`.`order`.`order_date`,
      `classicmodels`.`order`.`required_date`,
      `classicmodels`.`order`.`shipped_date`,
      `classicmodels`.`order`.`customer_number`
    from
      `classicmodels`.`order`
    where
      `classicmodels`.`order`.`order_id` = ?
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
      `classicmodels`.`order`.`order_id`,
      `classicmodels`.`order`.`order_date`,
      `classicmodels`.`order`.`required_date`,
      `classicmodels`.`order`.`shipped_date`,
      `classicmodels`.`order`.`customer_number`
    from
      `classicmodels`.`order`
    where
      `classicmodels`.`order`.`order_id` = ?
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
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`sale`.*
    from
      `classicmodels`.`employee`
    join `classicmodels`.`sale` on `classicmodels`.`employee`.`employee_number` 
       = `classicmodels`.`sale`.`employee_number`
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
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`extension`,
      `classicmodels`.`employee`.`email`,
      `classicmodels`.`employee`.`salary`,
      `classicmodels`.`employee`.`reports_to`,
      `classicmodels`.`employee`.`job_title`,
      `classicmodels`.`sale`.*
    from
      `classicmodels`.`employee`
    join `classicmodels`.`sale` on `classicmodels`.`employee`.`employee_number` 
       = `classicmodels`.`sale`.`employee_number`
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
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`salary`
    from
      `classicmodels`.`employee`
    limit 10
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
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`salary`
    from
      `classicmodels`.`employee`
    limit 10 
    offset 5
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
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`salary`
    from
      `classicmodels`.`employee`
    limit 10 
    offset 5
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
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`country`,
      `classicmodels`.`employee`.`job_title`,
      `classicmodels`.`customer`.`customer_number`,
      `classicmodels`.`customer`.`customer_name`,
      `classicmodels`.`customer`.`phone`,
      `classicmodels`.`customer`.`sales_rep_employee_number`,
      `classicmodels`.`customer`.`credit_limit`,
      `classicmodels`.`payment`.`customer_number`,
      `classicmodels`.`payment`.`check_number`,
      `classicmodels`.`payment`.`payment_date`,
      `classicmodels`.`payment`.`invoice_amount`,
      `classicmodels`.`payment`.`caching_date`
    from
      `classicmodels`.`office`,
      `classicmodels`.`employee`,
      `classicmodels`.`customer`,
      `classicmodels`.`payment`
    limit
      ?
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
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`country`,
      `classicmodels`.`employee`.`job_title`,
      `classicmodels`.`customer`.`customer_number`,
      `classicmodels`.`customer`.`customer_name`,
      `classicmodels`.`customer`.`phone`,
      `classicmodels`.`customer`.`sales_rep_employee_number`,
      `classicmodels`.`customer`.`credit_limit`,
      `classicmodels`.`payment`.`customer_number`,
      `classicmodels`.`payment`.`check_number`,
      `classicmodels`.`payment`.`payment_date`,
      `classicmodels`.`payment`.`invoice_amount`,
      `classicmodels`.`payment`.`caching_date`
    from
      `classicmodels`.`office`,
      `classicmodels`.`employee`,
      `classicmodels`.`customer`,
      `classicmodels`.`payment`
    limit
      ?
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