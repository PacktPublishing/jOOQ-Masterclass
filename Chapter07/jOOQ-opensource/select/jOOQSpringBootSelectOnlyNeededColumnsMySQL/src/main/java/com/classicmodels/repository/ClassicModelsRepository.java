package com.classicmodels.repository;

import java.util.Map;
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
          `classicmodels`.`order`
        where
          `classicmodels`.`order`.`order_id` = ?
         */
        System.out.println("EXAMPLE 1.4\n"
                + ctx.select(asterisk())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );

        /*
        select 
          `classicmodels`.`order`.`order_id`, 
          `classicmodels`.`order`.`order_date`, 
          `classicmodels`.`order`.`required_date`, 
          `classicmodels`.`order`.`shipped_date`, 
          `classicmodels`.`order`.`status`, 
          `classicmodels`.`order`.`comments`, 
          `classicmodels`.`order`.`customer_number`, 
          `classicmodels`.`orderdetail`.`quantity_ordered` 
        from 
          `classicmodels`.`order` 
          join `classicmodels`.`orderdetail` on `classicmodels`.`order`.`order_id` 
             = `classicmodels`.`orderdetail`.`order_id` 
        where 
          `classicmodels`.`order`.`order_id` = ?        
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
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`sale`.*
    from
      `classicmodels`.`employee`
    join `classicmodels`.`sale` on `classicmodels`.`employee`.`employee_number` 
       = `classicmodels`.`sale`.`employee_number`
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
      `classicmodels`.`office`.`city` as `location`, 
      `classicmodels`.`office`.`office_code`, 
      `classicmodels`.`office`.`phone`, 
      `classicmodels`.`office`.`address_line_first`, 
      `classicmodels`.`office`.`address_line_second`, 
      `classicmodels`.`office`.`state`, 
      `classicmodels`.`office`.`country`, 
      `classicmodels`.`office`.`postal_code`, 
      `classicmodels`.`office`.`territory` 
    from 
      `classicmodels`.`office`    
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
      ifnull(
        `classicmodels`.`office`.`city`, 
        ?
      ), 
      `classicmodels`.`office`.`office_code`, 
      `classicmodels`.`office`.`phone`, 
      `classicmodels`.`office`.`address_line_first`, 
      `classicmodels`.`office`.`address_line_second`, 
      `classicmodels`.`office`.`state`, 
      `classicmodels`.`office`.`country`, 
      `classicmodels`.`office`.`postal_code`, 
      `classicmodels`.`office`.`territory` 
    from 
      `classicmodels`.`office`    
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
      (
        `classicmodels`.`sale`.`sale` > ?
      ) as `saleGt5000`, 
      `classicmodels`.`sale`.`sale_id`, 
      `classicmodels`.`sale`.`fiscal_year`, 
      `classicmodels`.`sale`.`employee_number` 
    from 
      `classicmodels`.`sale`    
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
      (
        `classicmodels`.`sale`.`sale` * ?
      ) as `saleMul025`, 
      `classicmodels`.`sale`.`sale_id`, 
      `classicmodels`.`sale`.`fiscal_year`, 
      `classicmodels`.`sale`.`employee_number` 
    from 
      `classicmodels`.`sale`    
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
      `classicmodels`.`office`.`city`, 
      `classicmodels`.`office`.`country`, 
      `classicmodels`.`office`.`office_code` 
    from 
      `classicmodels`.`office` 
    where 
      `classicmodels`.`office`.`city` in (?, ?, ?) 
    order by 
      case `classicmodels`.`office`.`city` when ? then 0 when ? then 1 when ? then 2 end asc    
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
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`salary`
    from
      `classicmodels`.`employee`
    limit 10
     */
    public void findEmployeeLimit() {

        System.out.println("EXAMPLE 11\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(10)
                        .fetch()
        );
    }

    // EXAMPLE 12
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

        System.out.println("EXAMPLE 12\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(10)
                        .offset(5)
                        .fetch()
        );
    }

    // EXAMPLE 13
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

        System.out.println("EXAMPLE 13\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .limit(5, 10)
                        .fetch()
        );
    }

    // EXAMPLE 14
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

        System.out.println("EXAMPLE 14\n"
                + select.fetch()
        );
    }

    // EXAMPLE 15
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

        System.out.println("EXAMPLE 15\n"
                + select.fetch()
        );
    }
}