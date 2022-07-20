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
          [classicmodels].[dbo].[order].[order_id],
          [classicmodels].[dbo].[order].[order_date],
          [classicmodels].[dbo].[order].[required_date],
          [classicmodels].[dbo].[order].[shipped_date],
          [classicmodels].[dbo].[order].[status],
          [classicmodels].[dbo].[order].[comments],
          [classicmodels].[dbo].[order].[customer_number]
        from [classicmodels].[dbo].[order]
        where [classicmodels].[dbo].[order].[order_id] = ?        
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
        from [classicmodels].[dbo].[order]
        where [classicmodels].[dbo].[order].[order_id] = ?        
         */
        System.out.println("EXAMPLE 1.4\n"
                + ctx.select(asterisk())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          [classicmodels].[dbo].[order].[order_id], 
          [classicmodels].[dbo].[order].[order_date], 
          [classicmodels].[dbo].[order].[required_date], 
          [classicmodels].[dbo].[order].[shipped_date], 
          [classicmodels].[dbo].[order].[status], 
          [classicmodels].[dbo].[order].[comments], 
          [classicmodels].[dbo].[order].[customer_number], 
          [classicmodels].[dbo].[orderdetail].[quantity_ordered] 
        from 
          [classicmodels].[dbo].[order] 
          join [classicmodels].[dbo].[orderdetail] 
            on [classicmodels].[dbo].[order].[order_id] = [classicmodels].[dbo].[orderdetail].[order_id] 
        where 
          [classicmodels].[dbo].[order].[order_id] = ?        
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
          [alias_94178018].* 
        from 
          (
            [classicmodels].[dbo].[order] 
            join [classicmodels].[dbo].[customer] [alias_94178018] 
             on [classicmodels].[dbo].[order].[customer_number] = [alias_94178018].[customer_number]
          ) 
        where 
          [classicmodels].[dbo].[order].[order_id] = ?        
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
      [classicmodels].[dbo].[order].[order_id],
      [classicmodels].[dbo].[order].[order_date],
      [classicmodels].[dbo].[order].[required_date],
      [classicmodels].[dbo].[order].[shipped_date],
      [classicmodels].[dbo].[order].[customer_number]
    from [classicmodels].[dbo].[order]
    where [classicmodels].[dbo].[order].[order_id] = ?    
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
      [classicmodels].[dbo].[order].[order_id],
      [classicmodels].[dbo].[order].[order_date],
      [classicmodels].[dbo].[order].[required_date],
      [classicmodels].[dbo].[order].[shipped_date],
      [classicmodels].[dbo].[order].[customer_number]
    from [classicmodels].[dbo].[order]
    where [classicmodels].[dbo].[order].[order_id] = ?    
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
      [classicmodels].[dbo].[employee].[first_name],
      [classicmodels].[dbo].[employee].[last_name],
      [classicmodels].[dbo].[sale].*
    from [classicmodels].[dbo].[employee]
    join [classicmodels].[dbo].[sale]
      on [classicmodels].[dbo].[employee].[employee_number] 
           = [classicmodels].[dbo].[sale].[employee_number]    
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
      [classicmodels].[dbo].[employee].[last_name],
      [classicmodels].[dbo].[employee].[first_name],
      [classicmodels].[dbo].[employee].[extension],
      [classicmodels].[dbo].[employee].[email],
      [classicmodels].[dbo].[employee].[salary],
      [classicmodels].[dbo].[employee].[reports_to],
      [classicmodels].[dbo].[employee].[job_title],
      [classicmodels].[dbo].[sale].*
    from [classicmodels].[dbo].[employee]
    join [classicmodels].[dbo].[sale]
      on [classicmodels].[dbo].[employee].[employee_number] 
           = [classicmodels].[dbo].[sale].[employee_number]    
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
      [classicmodels].[dbo].[office].[city] [location], 
      [classicmodels].[dbo].[office].[office_code], 
      [classicmodels].[dbo].[office].[phone], 
      [classicmodels].[dbo].[office].[address_line_first], 
      [classicmodels].[dbo].[office].[address_line_second], 
      [classicmodels].[dbo].[office].[state], 
      [classicmodels].[dbo].[office].[country], 
      [classicmodels].[dbo].[office].[postal_code], 
      [classicmodels].[dbo].[office].[territory] 
    from 
      [classicmodels].[dbo].[office]    
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
      case when [classicmodels].[dbo].[office].[city] is not null then 
         [classicmodels].[dbo].[office].[city] else ? end, 
      [classicmodels].[dbo].[office].[office_code], 
      [classicmodels].[dbo].[office].[phone], 
      [classicmodels].[dbo].[office].[address_line_first], 
      [classicmodels].[dbo].[office].[address_line_second], 
      [classicmodels].[dbo].[office].[state], 
      [classicmodels].[dbo].[office].[country], 
      [classicmodels].[dbo].[office].[postal_code], 
      [classicmodels].[dbo].[office].[territory] 
    from 
      [classicmodels].[dbo].[office]    
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
      case when [classicmodels].[dbo].[sale].[sale] > ? then 1 when not (
        [classicmodels].[dbo].[sale].[sale] > ?
      ) then 0 end [saleGt5000], 
      [classicmodels].[dbo].[sale].[sale_id], 
      [classicmodels].[dbo].[sale].[fiscal_year], 
      [classicmodels].[dbo].[sale].[employee_number] 
    from 
      [classicmodels].[dbo].[sale]    
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
        [classicmodels].[dbo].[sale].[sale] * ?
      ) [saleMul025], 
      [classicmodels].[dbo].[sale].[sale_id], 
      [classicmodels].[dbo].[sale].[fiscal_year], 
      [classicmodels].[dbo].[sale].[employee_number] 
    from 
      [classicmodels].[dbo].[sale]    
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
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[country], 
      [classicmodels].[dbo].[office].[office_code] 
    from 
      [classicmodels].[dbo].[office] 
    where 
      [classicmodels].[dbo].[office].[city] in (?, ?, ?) 
    order by 
      case [classicmodels].[dbo].[office].[city] when ? then 0 when ? then 1 when ? then 2 end asc    
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
      [classicmodels].[dbo].[employee].[employee_number], 
      [classicmodels].[dbo].[employee].[last_name], 
      [classicmodels].[dbo].[employee].[first_name], 
      [classicmodels].[dbo].[employee].[extension], 
      [classicmodels].[dbo].[employee].[email], 
      [classicmodels].[dbo].[employee].[office_code], 
      [classicmodels].[dbo].[employee].[salary], 
      [classicmodels].[dbo].[employee].[reports_to], 
      [classicmodels].[dbo].[employee].[job_title] 
    from 
      [classicmodels].[dbo].[employee] 
    where 
      [classicmodels].[dbo].[employee].[salary] in (
        select 
          [classicmodels].[dbo].[employee].[salary] 
        from 
          [classicmodels].[dbo].[employee] 
        where 
          [classicmodels].[dbo].[employee].[salary] < ?
      ) 
    order by 
      [classicmodels].[dbo].[employee].[salary]    
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
    select top 10
      [classicmodels].[dbo].[employee].[first_name],
      [classicmodels].[dbo].[employee].[last_name],
      [classicmodels].[dbo].[employee].[salary]
    from [classicmodels].[dbo].[employee]    
    // order by (select 0) -> without explicit ORDER BY
    order by [classicmodels].[dbo].[employee].[salary]
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
          [classicmodels].[dbo].[employee].[first_name],
          [classicmodels].[dbo].[employee].[last_name],
          [classicmodels].[dbo].[employee].[salary]
        from [classicmodels].[dbo].[employee]
        // order by (select 0) -> without explicit ORDER BY
        order by [classicmodels].[dbo].[employee].[salary]
        offset ? rows fetch next ? rows only
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
          top (
            select 
              min(
                [classicmodels].[dbo].[orderdetail].[quantity_ordered]
              ) 
            from 
              [classicmodels].[dbo].[orderdetail]
          ) [classicmodels].[dbo].[orderdetail].[order_id], 
          [classicmodels].[dbo].[orderdetail].[product_id], 
          [classicmodels].[dbo].[orderdetail].[quantity_ordered] 
        from 
          [classicmodels].[dbo].[orderdetail] 
        order by 
          [classicmodels].[dbo].[orderdetail].[quantity_ordered]        
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
      [classicmodels].[dbo].[employee].[first_name],
      [classicmodels].[dbo].[employee].[last_name],
      [classicmodels].[dbo].[employee].[salary]
    from [classicmodels].[dbo].[employee]
    // order by (select 0) -> without explicit ORDER BY   
    order by [classicmodels].[dbo].[employee].[salary]
    offset ? rows fetch next ? rows only
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
          [classicmodels].[dbo].[productline].[product_line], 
          [classicmodels].[dbo].[productline].[code], 
          [classicmodels].[dbo].[product].[product_name], 
          [classicmodels].[dbo].[product].[quantity_in_stock], 
          [classicmodels].[dbo].[product].[product_id] 
        from 
          [classicmodels].[dbo].[productline] 
          join [classicmodels].[dbo].[product] on [classicmodels].[dbo].[product].[product_id] = (
            select 
              top 1 [classicmodels].[dbo].[product].[product_id] 
            from 
              [classicmodels].[dbo].[product] 
            where 
              [classicmodels].[dbo].[productline].[product_line] = [classicmodels].[dbo].[product].[product_line] 
            order by 
              [classicmodels].[dbo].[product].[product_id]
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
    select top 100
      [classicmodels].[dbo].[office].[city],
      [classicmodels].[dbo].[office].[country],
      [classicmodels].[dbo].[employee].[job_title],
      [classicmodels].[dbo].[customer].[customer_number],
      [classicmodels].[dbo].[customer].[customer_name],
      [classicmodels].[dbo].[customer].[phone],
      [classicmodels].[dbo].[customer].[sales_rep_employee_number],
      [classicmodels].[dbo].[customer].[credit_limit],
      [classicmodels].[dbo].[payment].[customer_number],
      [classicmodels].[dbo].[payment].[check_number],
      [classicmodels].[dbo].[payment].[payment_date],
      [classicmodels].[dbo].[payment].[invoice_amount],
      [classicmodels].[dbo].[payment].[caching_date]
    from [classicmodels].[dbo].[office],
         [classicmodels].[dbo].[employee],
         [classicmodels].[dbo].[customer],
         [classicmodels].[dbo].[payment]
    order by (select 0)
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
    select top 100
      [classicmodels].[dbo].[office].[city],
      [classicmodels].[dbo].[office].[country],
      [classicmodels].[dbo].[employee].[job_title],
      [classicmodels].[dbo].[customer].[customer_number],
      [classicmodels].[dbo].[customer].[customer_name],
      [classicmodels].[dbo].[customer].[phone],
      [classicmodels].[dbo].[customer].[sales_rep_employee_number],
      [classicmodels].[dbo].[customer].[credit_limit],
      [classicmodels].[dbo].[payment].[customer_number],
      [classicmodels].[dbo].[payment].[check_number],
      [classicmodels].[dbo].[payment].[payment_date],
      [classicmodels].[dbo].[payment].[invoice_amount],
      [classicmodels].[dbo].[payment].[caching_date]
    from [classicmodels].[dbo].[office],
         [classicmodels].[dbo].[employee],
         [classicmodels].[dbo].[customer],
         [classicmodels].[dbo].[payment]
    order by (select 0)
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