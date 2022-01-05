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
      "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE", 
      (
        select 
          sum("CLASSICMODELS"."SALE"."SALE") 
        from 
          "CLASSICMODELS"."SALE" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
      ) "sumSales" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    order by 
      "sumSales" asc    
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
    select
       "CLASSICMODELS"."CUSTOMERDETAIL"."CITY",
       "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY",
       (
          select
    ("CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME" || ' ' || "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME") 
          from
             "CLASSICMODELS"."CUSTOMER" 
          where
             "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" = "CLASSICMODELS"."CUSTOMERDETAIL"."CUSTOMER_NUMBER"
       )
       "fullName" 
    from
       "CLASSICMODELS"."CUSTOMERDETAIL"    
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
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_FIRST", 
      (
        select 
          count(*) 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE" = "CLASSICMODELS"."OFFICE"."OFFICE_CODE"
      ) "employeesNr" 
    from 
      "CLASSICMODELS"."OFFICE"   
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
          "s1"."SALE", 
          "s1"."FISCAL_YEAR", 
          "s1"."EMPLOYEE_NUMBER" 
        from 
          "CLASSICMODELS"."SALE" "s1" 
        where 
          "s1"."SALE" = (
            select 
              max("s2"."SALE") 
            from 
              "CLASSICMODELS"."SALE" "s2" 
            where 
              (
                "s2"."EMPLOYEE_NUMBER" = "s1"."EMPLOYEE_NUMBER" 
                and "s2"."FISCAL_YEAR" = "s1"."FISCAL_YEAR"
              )
          ) 
        order by 
          "s1"."FISCAL_YEAR"       
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
          "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER", 
          max("CLASSICMODELS"."SALE"."SALE") 
        from 
          "CLASSICMODELS"."SALE" 
        group by 
          "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" 
        order by 
          "CLASSICMODELS"."SALE"."FISCAL_YEAR"        
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
          "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        where 
          (
            select 
              avg("CLASSICMODELS"."SALE"."SALE") 
            from 
              "CLASSICMODELS"."SALE"
          ) < (
            select 
              sum("CLASSICMODELS"."SALE"."SALE") 
            from 
              "CLASSICMODELS"."SALE" 
            where 
              "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
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
          distinct "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
          join "CLASSICMODELS"."OFFICE" on (
            select 
              avg("CLASSICMODELS"."SALE"."SALE") 
            from 
              "CLASSICMODELS"."SALE"
          ) < (
            select 
              sum("CLASSICMODELS"."SALE"."SALE") 
            from 
              "CLASSICMODELS"."SALE" 
            where 
              "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
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
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_FIRST", 
      (
        select 
          max("CLASSICMODELS"."EMPLOYEE"."SALARY") 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE" = "CLASSICMODELS"."OFFICE"."OFFICE_CODE"
      ) "maxSalary", 
      (
        select 
          avg("CLASSICMODELS"."EMPLOYEE"."SALARY") 
        from 
          "CLASSICMODELS"."EMPLOYEE"
      ) "avgSalary" 
    from 
      "CLASSICMODELS"."OFFICE"    
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
      "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER", 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME", 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME" 
    from 
      "CLASSICMODELS"."CUSTOMER" 
    where 
      exists (
        select 
          count(*) 
        from 
          "CLASSICMODELS"."ORDER" 
        where 
          "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" = "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
        group by 
          "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
        having 
          count(*) > ?
      ) 
    order by 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME", 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME"    
    */
    public void findCustomerWithMoreThan10Sales() {

        System.out.println("EXAMPLE 7\n" +
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
    
    // EXAMPLE 8
    /*
    select
       "CLASSICMODELS"."ORDERDETAIL"."ORDERDETAIL_ID",
       "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID",
       "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID",
       "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED",
       "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH",
       "CLASSICMODELS"."ORDERDETAIL"."ORDER_LINE_NUMBER" 
    from
       "CLASSICMODELS"."ORDERDETAIL" 
    where
       not (exists 
       (
          select
             "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" 
          from
             "CLASSICMODELS"."PRODUCT" 
          where
             (
                "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID" 
                and "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK" > "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED"
             )
       )
    ) 
    group by
       "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID",
       "CLASSICMODELS"."ORDERDETAIL"."ORDERDETAIL_ID",
       "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID",
       "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED",
       "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH",
       "CLASSICMODELS"."ORDERDETAIL"."ORDER_LINE_NUMBER" 
    order by
       "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED"    
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = any (
        select 
          "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID" 
        from 
          "CLASSICMODELS"."ORDERDETAIL" 
        where 
          (
            "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID" 
            and "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" > ?
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      "CLASSICMODELS"."PRODUCT"."MSRP" > all (
        select 
          "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" 
        from 
          "CLASSICMODELS"."ORDERDETAIL" 
        where 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID"
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      (
        select 
          avg("CLASSICMODELS"."PRODUCT"."BUY_PRICE") 
        from 
          "CLASSICMODELS"."PRODUCT"
      ) > any (
        select 
          "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" 
        from 
          "CLASSICMODELS"."ORDERDETAIL" 
        where 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID"
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      (
        select 
          avg("CLASSICMODELS"."PRODUCT"."BUY_PRICE") 
        from 
          "CLASSICMODELS"."PRODUCT"
      ) > all (
        select 
          "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" 
        from 
          "CLASSICMODELS"."ORDERDETAIL" 
        where 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID"
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
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT", 
      "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE", 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE", 
      case when "CLASSICMODELS"."PAYMENT"."CACHING_DATE" is null then (
        select 
          "CLASSICMODELS"."CUSTOMER"."CREDIT_LIMIT" 
        from 
          "CLASSICMODELS"."CUSTOMER" 
        where 
          "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER"
      ) else ? end "credit_limit" 
    from 
      "CLASSICMODELS"."PAYMENT" 
    order by 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE"   
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
      "s"."EMPLOYEE_NUMBER" 
    from 
      "CLASSICMODELS"."SALE" "s" 
    where 
      "s"."FISCAL_YEAR" = ? 
    group by 
      "s"."EMPLOYEE_NUMBER" 
    having 
      sum("s"."SALE") > (
        select 
          sum("CLASSICMODELS"."SALE"."SALE") 
        from 
          "CLASSICMODELS"."SALE" 
        where 
          (
            "CLASSICMODELS"."SALE"."FISCAL_YEAR" = ? 
            and "s"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
          ) 
        group by 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
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
      "CLASSICMODELS"."CUSTOMER" 
    set 
      "CLASSICMODELS"."CUSTOMER"."CREDIT_LIMIT" = (
        select 
          sum(
            "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT"
          ) 
        from 
          "CLASSICMODELS"."PAYMENT" 
        where 
          "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER"
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
       "CLASSICMODELS"."SALE" 
    where
       "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" in 
       (
          select
             "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" 
          from
             "CLASSICMODELS"."EMPLOYEE" 
          where
             (
                "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" 
                and "CLASSICMODELS"."EMPLOYEE"."SALARY" >= ? 
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
       "CLASSICMODELS"."BANK_TRANSACTION" ("BANK_NAME", "BANK_IBAN", "TRANSFER_AMOUNT", "CACHING_DATE", "CUSTOMER_NUMBER", "CHECK_NUMBER", "CARD_TYPE", "STATUS") 
       select distinct
          'N/A',
          'N/A',
          "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT",
          nvl("CLASSICMODELS"."PAYMENT"."CACHING_DATE", "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE"),
          "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER",
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER",
          ?,
          ? 
       from
          "CLASSICMODELS"."PAYMENT" 
          left outer join
             "CLASSICMODELS"."BANK_TRANSACTION" 
             on ("CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER") = 
             (
    ("CLASSICMODELS"."BANK_TRANSACTION"."CUSTOMER_NUMBER", "CLASSICMODELS"."BANK_TRANSACTION"."CHECK_NUMBER")
             )
       where
          (
             "CLASSICMODELS"."BANK_TRANSACTION"."CUSTOMER_NUMBER" is null 
             and "CLASSICMODELS"."BANK_TRANSACTION"."CHECK_NUMBER" is null
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