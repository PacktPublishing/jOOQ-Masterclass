package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }    

    // EXAMPLE 1
    /*
    select 
      [classicmodels].[dbo].[employee].[employee_number], 
      [classicmodels].[dbo].[employee].[first_name], 
      [classicmodels].[dbo].[employee].[job_title], 
      (
        select 
          sum(
            [classicmodels].[dbo].[sale].[sale]
          ) 
        from 
          [classicmodels].[dbo].[sale] 
        where 
          [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number]
      ) [sumSales] 
    from 
      [classicmodels].[dbo].[employee] 
    order by 
      [sumSales] asc    
    */
    public void findEmployeesBySumSales() {

        // using type-safe DSL.Field(Select)                
        Field<BigDecimal> sumSales = field(select(sum(SALE.SALE_))
                .from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                .as("sumSales");
        
        // or, using the non type-safe asField()
        /*
        Field<?> sumSales = select(sum(SALE.SALE_))
                .from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .asField("sumSales");      
        */
                 
        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, sumSales)
                        .from(EMPLOYEE)
                        .orderBy(sumSales.asc())
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select [classicmodels].[dbo].[customerdetail].[city],
           [classicmodels].[dbo].[customerdetail].[country],
           (select ( [classicmodels].[dbo].[customer].[contact_first_name]
                     + ' '
                     + [classicmodels].[dbo].[customer].[contact_last_name] )
            from   [classicmodels].[dbo].[customer]
            where  [classicmodels].[dbo].[customer].[customer_number] =
                   [classicmodels].[dbo].[customerdetail].[customer_number])
           [fullName]
    from   [classicmodels].[dbo].[customerdetail]     
    */
    public void findCustomerFullNameCityCountry() {

        // using type-safe DSL.Field(Select)                        
        Field<String> fullName = field(select(concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))).as("fullName");         
        
        // or, using the non type-safe asField()
        /*
        Field<?> fullName = select(concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .asField("fullName");
        */
        
        System.out.println("EXAMPLE 2\n"
                + ctx.select(
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, fullName)
                        .from(CUSTOMERDETAIL)
                        .fetch()
        );

        // same query in one piece of fluent code
        /*
        System.out.println("EXAMPLE 2\n" +
                ctx.select(
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY,
                        field(select(concat(CUSTOMER.CONTACT_FIRST_NAME, inline(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER)))
                                .as("fullName"))
                        .from(CUSTOMERDETAIL)
                        .fetch()
        );
        */
    }

    // EXAMPLE 3
    /*
    select 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[address_line_first], 
      (
        select 
          count(*) 
        from 
          [classicmodels].[dbo].[employee] 
        where 
          [classicmodels].[dbo].[employee].[office_code] = [classicmodels].[dbo].[office].[office_code]
      ) [employeesNr] 
    from 
      [classicmodels].[dbo].[office]    
     */
    public void findOfficeAndNoOfEmployee() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
                        (field(selectCount().from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE
                                        .eq(OFFICE.OFFICE_CODE)))).as("employeesNr"))
                        .from(OFFICE)
                        .fetch()
        );
    }
    
    // EXAMPLE 4
    public void findMaxSalePerFiscalYearAndEmployee() {

        /*
        select 
          [s1].[sale], 
          [s1].[fiscal_year], 
          [s1].[employee_number] 
        from 
          [classicmodels].[dbo].[sale] [s1] 
        where 
          [s1].[sale] = (
            select 
              max([s2].[sale]) 
            from 
              [classicmodels].[dbo].[sale] [s2] 
            where 
              (
                [s2].[employee_number] = [s1].[employee_number] 
                and [s2].[fiscal_year] = [s1].[fiscal_year]
              )
          ) 
        order by 
          [s1].[fiscal_year]    
         */
        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");

        System.out.println("EXAMPLE 4\n" +
                ctx.select(s1.SALE_, s1.FISCAL_YEAR, s1.EMPLOYEE_NUMBER)
                        .from(s1)
                        .where(s1.SALE_.eq(select(max(s2.SALE_))
                                .from(s2)
                                .where(s2.EMPLOYEE_NUMBER.eq(s1.EMPLOYEE_NUMBER)
                                        .and(s2.FISCAL_YEAR.eq(s1.FISCAL_YEAR)))))
                        .orderBy(s1.FISCAL_YEAR)
                        .fetch()
        );

        // of course, it is simpler to rely on groupBy and not on a nested select 
        /*
        select 
          [classicmodels].[dbo].[sale].[fiscal_year], 
          [classicmodels].[dbo].[sale].[employee_number], 
          max(
            [classicmodels].[dbo].[sale].[sale]
          ) 
        from 
          [classicmodels].[dbo].[sale] 
        group by 
          [classicmodels].[dbo].[sale].[fiscal_year], 
          [classicmodels].[dbo].[sale].[employee_number] 
        order by 
          [classicmodels].[dbo].[sale].[fiscal_year]       
         */
        System.out.println("EXAMPLE 4 (via groupBy)\n" +
                ctx.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                        .orderBy(SALE.FISCAL_YEAR)
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void findEmployeeWithAvgSaleLtSumSales() {

        /*
        select 
          [classicmodels].[dbo].[employee].[first_name], 
          [classicmodels].[dbo].[employee].[last_name], 
          [classicmodels].[dbo].[employee].[salary] 
        from 
          [classicmodels].[dbo].[employee] 
        where 
          (
            select 
              avg(
                [classicmodels].[dbo].[sale].[sale]
              ) 
            from 
              [classicmodels].[dbo].[sale]
          ) < (
            select 
              sum(
                [classicmodels].[dbo].[sale].[sale]
              ) 
            from 
              [classicmodels].[dbo].[sale] 
            where 
              [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number]
          )        
         */
        System.out.println("EXAMPLE 5.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(select(avg(SALE.SALE_)).from(SALE).lt(
                                (select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER)))))
                        .fetch()
        );

        /*
        select 
          distinct [classicmodels].[dbo].[employee].[first_name], 
          [classicmodels].[dbo].[employee].[last_name], 
          [classicmodels].[dbo].[employee].[salary] 
        from 
          [classicmodels].[dbo].[employee] 
          join [classicmodels].[dbo].[office] on (
            select 
              avg(
                [classicmodels].[dbo].[sale].[sale]
              ) 
            from 
              [classicmodels].[dbo].[sale]
          ) < (
            select 
              sum(
                [classicmodels].[dbo].[sale].[sale]
              ) 
            from 
              [classicmodels].[dbo].[sale] 
            where 
              [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number]
          )        
         */
        System.out.println("EXAMPLE 5.2\n"
                + ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .join(OFFICE)
                        .on(select(avg(SALE.SALE_)).from(SALE)
                                .lt(select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER))))
                        .fetch()
        );
    }
    
    // EXAMPLE 6
    /*
    select 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[address_line_first], 
      (
        select 
          max(
            [classicmodels].[dbo].[employee].[salary]
          ) 
        from 
          [classicmodels].[dbo].[employee] 
        where 
          [classicmodels].[dbo].[employee].[office_code] = [classicmodels].[dbo].[office].[office_code]
      ) [maxSalary], 
      (
        select 
          avg(
            [classicmodels].[dbo].[employee].[salary]
          ) 
        from 
          [classicmodels].[dbo].[employee]
      ) [avgSalary] 
    from 
      [classicmodels].[dbo].[office]    
     */
    public void findOfficeAndEmployeeMaxAndAvgSalary() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
                        (field(select(max(EMPLOYEE.SALARY)).from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE
                                        .eq(OFFICE.OFFICE_CODE)))).as("maxSalary"),
                        (field(select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE))).as("avgSalary"))
                        .from(OFFICE)
                        .fetch()
        );
    }
    
    // EXAMPLE 7
    /*
    select 
      [classicmodels].[dbo].[customer].[customer_number], 
      [classicmodels].[dbo].[customer].[contact_first_name], 
      [classicmodels].[dbo].[customer].[contact_last_name] 
    from 
      [classicmodels].[dbo].[customer] 
    where 
      exists (
        select 
          count(*) 
        from 
          [classicmodels].[dbo].[order] 
        where 
          [classicmodels].[dbo].[customer].[customer_number] = [classicmodels].[dbo].[order].[customer_number] 
        group by 
          [classicmodels].[dbo].[order].[customer_number] 
        having 
          count(*) > ?
      ) 
    order by 
      [classicmodels].[dbo].[customer].[contact_first_name], 
      [classicmodels].[dbo].[customer].[contact_last_name]    
    */
    public void findCustomerWithMoreThan10Sales() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                        .from(CUSTOMER)
                        .whereExists(selectCount().from(ORDER)
                                .where(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER))
                                .groupBy(ORDER.CUSTOMER_NUMBER)
                                .having((count().gt(10))))
                        .orderBy(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                        .fetch()
        );
    }
    
    // EXAMPLE 8
    /*
    select
       [classicmodels].[dbo].[orderdetail].[orderdetail_id],
       [classicmodels].[dbo].[orderdetail].[order_id],
       [classicmodels].[dbo].[orderdetail].[product_id],
       [classicmodels].[dbo].[orderdetail].[quantity_ordered],
       [classicmodels].[dbo].[orderdetail].[price_each],
       [classicmodels].[dbo].[orderdetail].[order_line_number] 
    from
       [classicmodels].[dbo].[orderdetail] 
    where
       not (exists 
       (
          select
             [classicmodels].[dbo].[product].[product_id] 
          from
             [classicmodels].[dbo].[product] 
          where
             (
                [classicmodels].[dbo].[product].[product_id] = [classicmodels].[dbo].[orderdetail].[product_id] 
                and [classicmodels].[dbo].[product].[quantity_in_stock] > [classicmodels].[dbo].[orderdetail].[quantity_ordered]
             )
       )
    ) 
    group by
       [classicmodels].[dbo].[orderdetail].[product_id],
       [classicmodels].[dbo].[orderdetail].[orderdetail_id],
       [classicmodels].[dbo].[orderdetail].[order_id],
       [classicmodels].[dbo].[orderdetail].[quantity_ordered],
       [classicmodels].[dbo].[orderdetail].[price_each],
       [classicmodels].[dbo].[orderdetail].[order_line_number] 
    order by
       [classicmodels].[dbo].[orderdetail].[quantity_ordered]    
    */
    public void findOrderdetailWithQuantityInStockGtQuantityOrdered() {

        System.out.println("EXAMPLE 8\n"
                + ctx.selectFrom(ORDERDETAIL)
                        .whereNotExists(select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)
                                        .and(PRODUCT.QUANTITY_IN_STOCK.gt(ORDERDETAIL.QUANTITY_ORDERED))))
                        .groupBy(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.ORDERDETAIL_ID, ORDERDETAIL.ORDER_ID,
                                ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.ORDER_LINE_NUMBER)
                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                        .fetch()
        );
    }

    // EXAMPLE 9
    /*
    select 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[buy_price] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      [classicmodels].[dbo].[product].[product_id] = any (
        select 
          [classicmodels].[dbo].[orderdetail].[product_id] 
        from 
          [classicmodels].[dbo].[orderdetail] 
        where 
          (
            [classicmodels].[dbo].[product].[product_id] = [classicmodels].[dbo].[orderdetail].[product_id] 
            and [classicmodels].[dbo].[orderdetail].[quantity_ordered] > ?
          )
      )    
    */
    public void findProductQuantityOrderedGt70() {

        System.out.println("EXAMPLE 9\n" +
                ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(any(
                                select(ORDERDETAIL.PRODUCT_ID).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)
                                                .and(ORDERDETAIL.QUANTITY_ORDERED.gt(70))))
                        ))
                        .fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      [classicmodels].[dbo].[product].[msrp] > all (
        select 
          [classicmodels].[dbo].[orderdetail].[price_each] 
        from 
          [classicmodels].[dbo].[orderdetail] 
        where 
          [classicmodels].[dbo].[product].[product_id] = [classicmodels].[dbo].[orderdetail].[product_id]
      )    
    */
    public void findProductWithMsrpGtSellPrice() {

        System.out.println("EXAMPLE 10\n" +
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.MSRP.gt(all(
                                select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
    }

    // EXAMPLE 11
    /*
    select 
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[buy_price] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      (
        select 
          avg(
            [classicmodels].[dbo].[product].[buy_price]
          ) 
        from 
          [classicmodels].[dbo].[product]
      ) > any (
        select 
          [classicmodels].[dbo].[orderdetail].[price_each] 
        from 
          [classicmodels].[dbo].[orderdetail] 
        where 
          [classicmodels].[dbo].[product].[product_id] = [classicmodels].[dbo].[orderdetail].[product_id]
      )
    */
    public void findProductWithAvgBuyPriceGtAnyPriceEach() {

        System.out.println("EXAMPLE 11\n" +
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT).gt(any(
                                select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
                );
    }

    // EXAMPLE 12
    /*
    select 
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[buy_price] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      (
        select 
          avg(
            [classicmodels].[dbo].[product].[buy_price]
          ) 
        from 
          [classicmodels].[dbo].[product]
      ) > all (
        select 
          [classicmodels].[dbo].[orderdetail].[price_each] 
        from 
          [classicmodels].[dbo].[orderdetail] 
        where 
          [classicmodels].[dbo].[product].[product_id] = [classicmodels].[dbo].[orderdetail].[product_id]
      )    
    */
    public void findProductWithAvgBuyPriceGtAllPriceEach() {

        System.out.println("EXAMPLE 12\n" +
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT)
                                .gt(all(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
    }

    // EXAMPLE 13
    /*
    select 
      [classicmodels].[dbo].[payment].[invoice_amount], 
      [classicmodels].[dbo].[payment].[payment_date], 
      [classicmodels].[dbo].[payment].[caching_date], 
      case when [classicmodels].[dbo].[payment].[caching_date] is null then (
        select 
          [classicmodels].[dbo].[customer].[credit_limit] 
        from 
          [classicmodels].[dbo].[customer] 
        where 
          [classicmodels].[dbo].[payment].[customer_number] = [classicmodels].[dbo].[customer].[customer_number]
      ) else ? end [credit_limit] 
    from 
      [classicmodels].[dbo].[payment] 
    order by 
      [classicmodels].[dbo].[payment].[caching_date]    
    */    
    public void findUnprocessedPayments() {

        System.out.println("EXAMPLE 13\n" +
                ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE,
                        case_()
                                .when(PAYMENT.CACHING_DATE.isNull(),
                                        select(CUSTOMER.CREDIT_LIMIT)
                                                .from(CUSTOMER)
                                                .where(PAYMENT.CUSTOMER_NUMBER
                                                        .eq(CUSTOMER.CUSTOMER_NUMBER)))
                                .else_(BigDecimal.valueOf(0.0)).as("credit_limit"))
                        .from(PAYMENT)
                        .orderBy(PAYMENT.CACHING_DATE)
                        .fetch()
        );
    }       
    
    // EXAMPLE 14
    /*
    select 
      [s].[employee_number] 
    from 
      [classicmodels].[dbo].[sale] [s] 
    where 
      [s].[fiscal_year] = ? 
    group by 
      [s].[employee_number] 
    having 
      sum([s].[sale]) > (
        select 
          sum(
            [classicmodels].[dbo].[sale].[sale]
          ) 
        from 
          [classicmodels].[dbo].[sale] 
        where 
          (
            [classicmodels].[dbo].[sale].[fiscal_year] = ? 
            and [s].[employee_number] = [classicmodels].[dbo].[sale].[employee_number]
          ) 
        group by 
          [classicmodels].[dbo].[sale].[employee_number]
      )    
    */
    public void findEmployeeNumberWithMoreSalesIn2005Than2003() {
        
        Sale sale = SALE.as("s");
        
        System.out.println("EXAMPLE 14\n" +
        ctx.select(sale.EMPLOYEE_NUMBER)
                .from(sale)
                .where(sale.FISCAL_YEAR.eq(2005))
                .groupBy(sale.EMPLOYEE_NUMBER)
                .having(sum(sale.SALE_).gt(
                        select(sum(SALE.SALE_)).from(SALE)
                                .where(SALE.FISCAL_YEAR.eq(2003)
                                        .and(sale.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                                .groupBy(SALE.EMPLOYEE_NUMBER)))
                .fetch()
                );
    }
                 
    // EXAMPLE 15
    /*
    update 
      [classicmodels].[dbo].[customer] 
    set 
      [classicmodels].[dbo].[customer].[credit_limit] = (
        select 
          sum(
            [classicmodels].[dbo].[payment].[invoice_amount]
          ) 
        from 
          [classicmodels].[dbo].[payment] 
        where 
          [classicmodels].[dbo].[payment].[customer_number] = [classicmodels].[dbo].[customer].[customer_number]
      )   
     */
    @Transactional
    public void updateCustomerCreditLimit() {

        System.out.println("EXAMPLE 15 (affected rows): " +
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT,
                                select(sum(PAYMENT.INVOICE_AMOUNT)).from(PAYMENT)
                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .execute()
        );
    }

    // EXAMPLE 16
    /*
    delete from
       [classicmodels].[dbo].[sale] 
    where
       [classicmodels].[dbo].[sale].[employee_number] in 
       (
          select
             [classicmodels].[dbo].[employee].[employee_number] 
          from
             [classicmodels].[dbo].[employee] 
          where
             (
                [classicmodels].[dbo].[sale].[employee_number] = [classicmodels].[dbo].[employee].[employee_number] 
                and [classicmodels].[dbo].[employee].[salary] >= ? 
             )
       )      
    */
    @Transactional
    public void deleteSaleOfEmployeeSalaryGt20000() {

        System.out.println("EXAMPLE 16 (affected rows): "
                + +ctx.deleteFrom(SALE)
                        .where(SALE.EMPLOYEE_NUMBER.in(select(EMPLOYEE.EMPLOYEE_NUMBER)
                                .from(EMPLOYEE).where(SALE.EMPLOYEE_NUMBER
                                .eq(EMPLOYEE.EMPLOYEE_NUMBER)
                                .and(EMPLOYEE.SALARY.ge(20000)))))
                        .execute()
        );
    }
    
    // EXAMPLE 17
    /*
    insert into
       [classicmodels].[dbo].[bank_transaction] ([bank_name], [bank_iban], [transfer_amount], [caching_date], [customer_number], [check_number], [card_type], [status]) 
       select distinct
          'N/A',
          'N/A',
          [classicmodels].[dbo].[payment].[invoice_amount],
          coalesce([classicmodels].[dbo].[payment].[caching_date], [classicmodels].[dbo].[payment].[payment_date]),
          [classicmodels].[dbo].[payment].[customer_number],
          [classicmodels].[dbo].[payment].[check_number],
          ?,
          ? 
       from
          [classicmodels].[dbo].[payment] 
          left outer join
             [classicmodels].[dbo].[bank_transaction] 
             on ([classicmodels].[dbo].[payment].[customer_number] = [classicmodels].[dbo].[bank_transaction].[customer_number] 
             and [classicmodels].[dbo].[payment].[check_number] = [classicmodels].[dbo].[bank_transaction].[check_number]) 
       where
          (
             [classicmodels].[dbo].[bank_transaction].[customer_number] is null 
             and [classicmodels].[dbo].[bank_transaction].[check_number] is null
          )    
    */
    @Transactional
    public void insertPaymentInBankTransaction() {
        System.out.println("EXAMPLE 17 (affected rows): "
                + ctx.insertInto(BANK_TRANSACTION, BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.BANK_IBAN,
                        BANK_TRANSACTION.TRANSFER_AMOUNT, BANK_TRANSACTION.CACHING_DATE, BANK_TRANSACTION.CUSTOMER_NUMBER, 
                        BANK_TRANSACTION.CHECK_NUMBER, BANK_TRANSACTION.CARD_TYPE, BANK_TRANSACTION.STATUS)
                        .select(selectDistinct(inline("N/A"), inline("N/A"), PAYMENT.INVOICE_AMOUNT, 
                                nvl(PAYMENT.CACHING_DATE, PAYMENT.PAYMENT_DATE), 
                                PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, val("MasterCard"), val("SUCCESS"))
                                .from(PAYMENT)
                                .leftOuterJoin(BANK_TRANSACTION)
                                .on(row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                                        .eq(row(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER)))
                                .where(row(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER).isNull()))
                        .execute()
        );
    }        
}