package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.Product;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
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
      [p1].[product_id], 
      [p1].[product_name], 
      [p1].[product_line], 
      [p1].[product_vendor], 
      [p1].[buy_price] 
    from 
      [classicmodels].[dbo].[product] [p1] 
    where 
      [p1].[buy_price] in (
        select 
          max([p2].[buy_price]) 
        from 
          [classicmodels].[dbo].[product] [p2] 
        where 
          [p2].[product_line] = [p1].[product_line] 
        group by 
          [p2].[product_line]
      ) 
    order by 
      [p1].[product_line], 
      [p1].[buy_price]    
    */
    public void findProductMaxBuyPriceByProductionLine() {

        Product p1 = PRODUCT.as("p1");
        Product p2 = PRODUCT.as("p2");

        // Select<Record1<BigDecimal>>
        var maxBuyPrice = select(max(p2.BUY_PRICE))
                .from(p2)
                .where(p2.PRODUCT_LINE.eq(p1.PRODUCT_LINE))
                .groupBy(p2.PRODUCT_LINE);

        System.out.println("EXAMPLE 1\n" +
                ctx.select(p1.PRODUCT_ID, p1.PRODUCT_NAME,
                        p1.PRODUCT_LINE, p1.PRODUCT_VENDOR, p1.BUY_PRICE)
                        .from(p1)
                        .where(p1.BUY_PRICE.in(maxBuyPrice))
                        .orderBy(p1.PRODUCT_LINE, p1.BUY_PRICE)
                        .fetch()
        );

        // same query in one piece of fluent code
        /*
        System.out.println("EXAMPLE 1\n" +
                ctx.select(p1.PRODUCT_ID, p1.PRODUCT_NAME,
                        p1.PRODUCT_LINE, p1.PRODUCT_VENDOR, p1.BUY_PRICE)
                        .from(p1)
                        .where(p1.BUY_PRICE.in(select(max(p2.BUY_PRICE))
                                .from(p2)
                                .where(p2.PRODUCT_LINE.eq(p1.PRODUCT_LINE))
                                .groupBy(p2.PRODUCT_LINE)))
                        .orderBy(p1.PRODUCT_LINE, p1.BUY_PRICE)
                        .fetch()
        );
        */
    }

    // EXAMPLE 2
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

        // Field<?>
        var sumSales = select(sum(SALE.SALE_))
                .from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .asField("sumSales");

        // or, using DSL.Field(Select)
        /*
        var sumSales = field(select(sum(SALE.SALE_))
                .from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                .as("sumSales");
        */
        
        System.out.println("EXAMPLE 2\n" +
                ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, sumSales)
                        .from(EMPLOYEE)
                        .orderBy(sumSales.asc())
                        .fetch()
        );

        // same query in one piece of fluent code
        /*
        System.out.println("EXAMPLE 2\n" +
                ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, sumSales)
                        .from(EMPLOYEE)
                        .orderBy(select(sum(SALE.SALE_))
                                .from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                                .asField("sumSales").asc())
                        .fetch()
        );
        */
    }

    // EXAMPLE 3
    /*
    select 
      [classicmodels].[dbo].[customerdetail].[city], 
      [classicmodels].[dbo].[customerdetail].[country], 
      (
        select 
          (
            [classicmodels].[dbo].[customer].[contact_first_name] + ? 
              + [classicmodels].[dbo].[customer].[contact_last_name]
          ) 
        from 
          [classicmodels].[dbo].[customer] 
        where 
          [classicmodels].[dbo].[customer].[customer_number] 
              = [classicmodels].[dbo].[customerdetail].[customer_number]
      ) [fullName] 
    from 
      [classicmodels].[dbo].[customerdetail]    
    */
    public void findCustomerFullNameCityCountry() {

        // Field<?>
        var fullName = select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .asField("fullName");

        // or, using DSL.Field(Select)
        /*
        var fullName = field(select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))).as("fullName");
         */
        
        System.out.println("EXAMPLE 3\n" +
                ctx.select(
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, fullName)
                        .from(CUSTOMERDETAIL)
                        .fetch()
        );

          // same query in one piece of fluent code
        /*
        System.out.println("EXAMPLE 3\n" +
                ctx.select(
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY,
                        select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                                .asField("fullName"))
                        .from(CUSTOMERDETAIL)
                        .fetch()
        );
        */
    }

    // EXAMPLE 4
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

        System.out.println("EXAMPLE 4\n" +
                ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
                        (selectCount().from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE
                                        .eq(OFFICE.OFFICE_CODE))).asField("employeesNr"))
                        .from(OFFICE)
                        .fetch()
        );
    }
    
    // EXAMPLE 5
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

        System.out.println("EXAMPLE 5\n" +
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
        System.out.println("EXAMPLE 5 (via groupBy)\n" +
                ctx.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                        .orderBy(SALE.FISCAL_YEAR)
                        .fetch()
        );
    }

    // EXAMPLE 6
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
        System.out.println("EXAMPLE 6.1\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(field(select(avg(SALE.SALE_)).from(SALE)).lt(
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
        System.out.println("EXAMPLE 6.2\n" +
                ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .join(OFFICE)
                        .on(field(select(avg(SALE.SALE_)).from(SALE))
                                .lt(select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER))))
                        .fetch()
        );
    }
    
    // EXAMPLE 7
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

        System.out.println("EXAMPLE 7\n" +
                ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
                        (select(max(EMPLOYEE.SALARY)).from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE
                                        .eq(OFFICE.OFFICE_CODE))).asField("maxSalary"),
                        (select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE)).asField("avgSalary"))
                        .from(OFFICE)
                        .fetch()
        );
    }
    
    // EXAMPLE 8
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

        System.out.println("EXAMPLE 8\n" +
                ctx.select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                        .from(CUSTOMER)
                        .whereExists(selectCount().from(ORDER)
                                .where(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER))
                                .groupBy(ORDER.CUSTOMER_NUMBER)
                                .having((count().gt(10))))
                        .orderBy(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                        .fetch()
        );
    }

    // EXAMPLE 9
    /*
    
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
      `classicmodels`.`product`.`product_id`,
      `classicmodels`.`product`.`product_name`
    from
      `classicmodels`.`product`
    where
      `classicmodels`.`product`.`msrp` > all 
        (
          select
            `classicmodels`.`orderdetail`.`price_each`
          from
            `classicmodels`.`orderdetail`
          where
            `classicmodels`.`product`.`product_id` = `classicmodels`.`orderdetail`.`product_id`
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
      `classicmodels`.`product`.`product_id`,
      `classicmodels`.`product`.`product_name`,
      `classicmodels`.`product`.`buy_price`
    from
      `classicmodels`.`product`
    where
      (
        select
          avg(`classicmodels`.`product`.`buy_price`)
        from
          `classicmodels`.`product`
      ) > any (
        select
          `classicmodels`.`orderdetail`.`price_each`
        from
          `classicmodels`.`orderdetail`
        where
          `classicmodels`.`product`.`product_id` = `classicmodels`.`orderdetail`.`product_id`
      )
    */
    public void findProductWithAvgBuyPriceGtAnyPriceEach() {

        System.out.println("EXAMPLE 11\n" +
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT)).gt(any(
                                select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
                );
    }

    // EXAMPLE 12
    /*
    select
      `classicmodels`.`product`.`product_id`,
      `classicmodels`.`product`.`product_name`,
      `classicmodels`.`product`.`buy_price`
    from
      `classicmodels`.`product`
    where
      (
        select
          avg(`classicmodels`.`product`.`buy_price`)
        from
          `classicmodels`.`product`
      ) > all (
        select
          `classicmodels`.`orderdetail`.`price_each`
        from
          `classicmodels`.`orderdetail`
        where
          `classicmodels`.`product`.`product_id` = `classicmodels`.`orderdetail`.`product_id`
      )
    */
    public void findProductWithAvgBuyPriceGtAllPriceEach() {

        System.out.println("EXAMPLE 12\n" +
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT))
                                .gt(all(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
    }

    // EXAMPLE 13
    /*
    select
      `classicmodels`.`payment`.`invoice_amount`,
      `classicmodels`.`payment`.`payment_date`,
      `classicmodels`.`payment`.`caching_date`,
      case
        when `classicmodels`.`payment`.`caching_date` is null then (
          select
            `classicmodels`.`customer`.`credit_limit`
          from
            `classicmodels`.`customer`
          where
            `classicmodels`.`payment`.`customer_number` = `classicmodels`.`customer`.`customer_number`
        )
        else ?
      end as `credit_limit`
    from
      `classicmodels`.`payment`
    order by
      `classicmodels`.`payment`.`caching_date`
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
      `s`.`employee_number`
    from
      `classicmodels`.`sale` as `s`
    where
      `s`.`fiscal_year` = ?
    group by
      `s`.`employee_number`
    having
      sum(`s`.`sale`) > (
        select
          sum(`classicmodels`.`sale`.`sale`)
        from
          `classicmodels`.`sale`
        where
          (
          `classicmodels`.`sale`.`fiscal_year` = ?
              and `s`.`employee_number` = `classicmodels`.`sale`.`employee_number`
          )
        group by
          `classicmodels`.`sale`.`employee_number`
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
      `classicmodels`.`customer`
    set
      `classicmodels`.`customer`.`credit_limit` = (
        select
          sum(`classicmodels`.`payment`.`invoice_amount`)
        from
          `classicmodels`.`payment`
        where
          `classicmodels`.`payment`.`customer_number` = `classicmodels`.`customer`.`customer_number`
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
      `classicmodels`.`payment`
    where
      `classicmodels`.`payment`.`customer_number` in (
        select
          `classicmodels`.`customer`.`customer_number`
        from
          `classicmodels`.`customer`
        where
         (
           `classicmodels`.`payment`.`customer_number` = `classicmodels`.`customer`.`customer_number`
              and `classicmodels`.`customer`.`credit_limit` > ?
         )
      )
    */
    @Transactional
    public void deletePaymentOfCustomerCreditLimitGt150000() {

        System.out.println("EXAMPLE 16 (affected rows): " +
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.in(select(CUSTOMER.CUSTOMER_NUMBER)
                                .from(CUSTOMER).where(PAYMENT.CUSTOMER_NUMBER
                                .eq(CUSTOMER.CUSTOMER_NUMBER)
                                .and(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.valueOf(150000))))))
                        .execute()
        );
    }
    
    // EXAMPLE 17
    /*
    insert into `classicmodels`.`order` (
      `order_date`,
      `required_date`,
      `shipped_date`,
      `status`,
      `customer_number`
    )
      select distinct `classicmodels`.`payment`.`payment_date`,
        `classicmodels`.`payment`.`payment_date`,
        `classicmodels`.`payment`.`caching_date`,
        ?,
        `classicmodels`.`payment`.`customer_number`
      from
        `classicmodels`.`payment`,
        `classicmodels`.`order`
      where
        `classicmodels`.`payment`.`payment_date` <> `classicmodels`.`order`.`order_date`
      order by
        `classicmodels`.`payment`.`payment_date`
    */
    @Transactional
    public void insertPaymentInOrder() {
        
        System.out.println("EXAMPLE 17 (affected rows): " +
                ctx.insertInto(ORDER, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE, 
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)                
                        .select(selectDistinct(PAYMENT.PAYMENT_DATE.coerce(LocalDate.class), PAYMENT.PAYMENT_DATE.coerce(LocalDate.class),
                                PAYMENT.CACHING_DATE.coerce(LocalDate.class), val("Shipped"), PAYMENT.CUSTOMER_NUMBER).from(PAYMENT, ORDER)                        
                                .where(PAYMENT.PAYMENT_DATE.coerce(LocalDate.class).ne(ORDER.ORDER_DATE))
                                .orderBy(PAYMENT.PAYMENT_DATE))
                                //.onDuplicateKeyIgnore()
                        .execute()
        );
    }
}