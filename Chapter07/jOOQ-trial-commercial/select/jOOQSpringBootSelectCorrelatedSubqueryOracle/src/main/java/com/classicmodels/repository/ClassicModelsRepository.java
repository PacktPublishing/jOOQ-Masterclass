package com.classicmodels.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
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
      "p1"."PRODUCT_ID", 
      "p1"."PRODUCT_NAME", 
      "p1"."PRODUCT_LINE", 
      "p1"."PRODUCT_VENDOR", 
      "p1"."BUY_PRICE" 
    from 
      "SYSTEM"."PRODUCT" "p1" 
    where 
      "p1"."BUY_PRICE" in (
        select 
          max("p2"."BUY_PRICE") 
        from 
          "SYSTEM"."PRODUCT" "p2" 
        where 
          "p2"."PRODUCT_LINE" = "p1"."PRODUCT_LINE" 
        group by 
          "p2"."PRODUCT_LINE"
      ) 
    order by 
      "p1"."PRODUCT_LINE", 
      "p1"."BUY_PRICE"    
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
      "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER", 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."JOB_TITLE", 
      (
        select 
          sum("SYSTEM"."SALE"."SALE") 
        from 
          "SYSTEM"."SALE" 
        where 
          "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
      ) "sumSales" 
    from 
      "SYSTEM"."EMPLOYEE" 
    order by 
      "sumSales" asc    
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
    }

    // EXAMPLE 3
    /*
    select 
      "SYSTEM"."CUSTOMERDETAIL"."CITY", 
      "SYSTEM"."CUSTOMERDETAIL"."COUNTRY", 
      (
        select 
          (
            "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME" || ? || "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME"
          ) 
        from 
          "SYSTEM"."CUSTOMER" 
        where 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" = "SYSTEM"."CUSTOMERDETAIL"."CUSTOMER_NUMBER"
      ) "fullName" 
    from 
      "SYSTEM"."CUSTOMERDETAIL"    
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
      "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
      (
        select 
          count(*) 
        from 
          "SYSTEM"."EMPLOYEE" 
        where 
          "SYSTEM"."EMPLOYEE"."OFFICE_CODE" = "SYSTEM"."OFFICE"."OFFICE_CODE"
      ) "employeesNr" 
    from 
      "SYSTEM"."OFFICE"   
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
          "s1"."SALE", 
          "s1"."FISCAL_YEAR", 
          "s1"."EMPLOYEE_NUMBER" 
        from 
          "SYSTEM"."SALE" "s1" 
        where 
          "s1"."SALE" = (
            select 
              max("s2"."SALE") 
            from 
              "SYSTEM"."SALE" "s2" 
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
          "SYSTEM"."SALE"."FISCAL_YEAR", 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER", 
          max("SYSTEM"."SALE"."SALE") 
        from 
          "SYSTEM"."SALE" 
        group by 
          "SYSTEM"."SALE"."FISCAL_YEAR", 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER" 
        order by 
          "SYSTEM"."SALE"."FISCAL_YEAR"        
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
          "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
          "SYSTEM"."EMPLOYEE"."LAST_NAME", 
          "SYSTEM"."EMPLOYEE"."SALARY" 
        from 
          "SYSTEM"."EMPLOYEE" 
        where 
          (
            select 
              avg("SYSTEM"."SALE"."SALE") 
            from 
              "SYSTEM"."SALE"
          ) < (
            select 
              sum("SYSTEM"."SALE"."SALE") 
            from 
              "SYSTEM"."SALE" 
            where 
              "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
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
          distinct "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
          "SYSTEM"."EMPLOYEE"."LAST_NAME", 
          "SYSTEM"."EMPLOYEE"."SALARY" 
        from 
          "SYSTEM"."EMPLOYEE" 
          join "SYSTEM"."OFFICE" on (
            select 
              avg("SYSTEM"."SALE"."SALE") 
            from 
              "SYSTEM"."SALE"
          ) < (
            select 
              sum("SYSTEM"."SALE"."SALE") 
            from 
              "SYSTEM"."SALE" 
            where 
              "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
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
      "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
      (
        select 
          max("SYSTEM"."EMPLOYEE"."SALARY") 
        from 
          "SYSTEM"."EMPLOYEE" 
        where 
          "SYSTEM"."EMPLOYEE"."OFFICE_CODE" = "SYSTEM"."OFFICE"."OFFICE_CODE"
      ) "maxSalary", 
      (
        select 
          avg("SYSTEM"."EMPLOYEE"."SALARY") 
        from 
          "SYSTEM"."EMPLOYEE"
      ) "avgSalary" 
    from 
      "SYSTEM"."OFFICE"    
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
      "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER", 
      "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME", 
      "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME" 
    from 
      "SYSTEM"."CUSTOMER" 
    where 
      exists (
        select 
          count(*) 
        from 
          "SYSTEM"."ORDER" 
        where 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" = "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
        group by 
          "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
        having 
          count(*) > ?
      ) 
    order by 
      "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME", 
      "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME"    
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
    select 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."BUY_PRICE" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      "SYSTEM"."PRODUCT"."PRODUCT_ID" = any (
        select 
          "SYSTEM"."ORDERDETAIL"."PRODUCT_ID" 
        from 
          "SYSTEM"."ORDERDETAIL" 
        where 
          (
            "SYSTEM"."PRODUCT"."PRODUCT_ID" = "SYSTEM"."ORDERDETAIL"."PRODUCT_ID" 
            and "SYSTEM"."ORDERDETAIL"."QUANTITY_ORDERED" > ?
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
                                                .and(ORDERDETAIL.QUANTITY_ORDERED.gt(70L))))
                        ))
                        .fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      "SYSTEM"."PRODUCT"."MSRP" > all (
        select 
          "SYSTEM"."ORDERDETAIL"."PRICE_EACH" 
        from 
          "SYSTEM"."ORDERDETAIL" 
        where 
          "SYSTEM"."PRODUCT"."PRODUCT_ID" = "SYSTEM"."ORDERDETAIL"."PRODUCT_ID"
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
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."BUY_PRICE" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      (
        select 
          avg("SYSTEM"."PRODUCT"."BUY_PRICE") 
        from 
          "SYSTEM"."PRODUCT"
      ) > any (
        select 
          "SYSTEM"."ORDERDETAIL"."PRICE_EACH" 
        from 
          "SYSTEM"."ORDERDETAIL" 
        where 
          "SYSTEM"."PRODUCT"."PRODUCT_ID" = "SYSTEM"."ORDERDETAIL"."PRODUCT_ID"
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
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."BUY_PRICE" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      (
        select 
          avg("SYSTEM"."PRODUCT"."BUY_PRICE") 
        from 
          "SYSTEM"."PRODUCT"
      ) > all (
        select 
          "SYSTEM"."ORDERDETAIL"."PRICE_EACH" 
        from 
          "SYSTEM"."ORDERDETAIL" 
        where 
          "SYSTEM"."PRODUCT"."PRODUCT_ID" = "SYSTEM"."ORDERDETAIL"."PRODUCT_ID"
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
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT", 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE", 
      "SYSTEM"."PAYMENT"."CACHING_DATE", 
      case when "SYSTEM"."PAYMENT"."CACHING_DATE" is null then (
        select 
          "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" 
        from 
          "SYSTEM"."CUSTOMER" 
        where 
          "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" = "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER"
      ) else ? end "credit_limit" 
    from 
      "SYSTEM"."PAYMENT" 
    order by 
      "SYSTEM"."PAYMENT"."CACHING_DATE"   
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
      "SYSTEM"."SALE" "s" 
    where 
      "s"."FISCAL_YEAR" = ? 
    group by 
      "s"."EMPLOYEE_NUMBER" 
    having 
      sum("s"."SALE") > (
        select 
          sum("SYSTEM"."SALE"."SALE") 
        from 
          "SYSTEM"."SALE" 
        where 
          (
            "SYSTEM"."SALE"."FISCAL_YEAR" = ? 
            and "s"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
          ) 
        group by 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
      )    
    */
    public void findEmployeeNumberWithMoreSalesIn2005Than2003() {
        
        Sale sale = SALE.as("s");
        
        System.out.println("EXAMPLE 14\n" +
        ctx.select(sale.EMPLOYEE_NUMBER)
                .from(sale)
                .where(sale.FISCAL_YEAR.eq(BigInteger.valueOf(2005)))
                .groupBy(sale.EMPLOYEE_NUMBER)
                .having(sum(sale.SALE_).gt(
                        select(sum(SALE.SALE_)).from(SALE)
                                .where(SALE.FISCAL_YEAR.eq(BigInteger.valueOf(2003))
                                        .and(sale.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                                .groupBy(SALE.EMPLOYEE_NUMBER)))
                .fetch()
                );
    }
                 
    // EXAMPLE 15
    /*
    update 
      "SYSTEM"."CUSTOMER" 
    set 
      "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" = (
        select 
          sum(
            "SYSTEM"."PAYMENT"."INVOICE_AMOUNT"
          ) 
        from 
          "SYSTEM"."PAYMENT" 
        where 
          "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" = "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER"
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
      "SYSTEM"."PAYMENT" 
    where 
      "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" in (
        select 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" 
        from 
          "SYSTEM"."CUSTOMER" 
        where 
          (
            "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" = "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" 
            and "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" > ?
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
    insert into "SYSTEM"."ORDER" (
      "ORDER_DATE", "REQUIRED_DATE", "SHIPPED_DATE", 
      "STATUS", "CUSTOMER_NUMBER"
    ) 
    select 
      distinct "SYSTEM"."PAYMENT"."PAYMENT_DATE", 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE", 
      "SYSTEM"."PAYMENT"."CACHING_DATE", 
      ?, 
      "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" 
    from 
      "SYSTEM"."PAYMENT", 
      "SYSTEM"."ORDER" 
    where 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE" <> "SYSTEM"."ORDER"."ORDER_DATE" 
    order by 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE"    
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