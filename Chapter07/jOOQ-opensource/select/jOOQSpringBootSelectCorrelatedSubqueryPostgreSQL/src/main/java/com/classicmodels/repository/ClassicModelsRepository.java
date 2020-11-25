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
      "p1"."product_id", 
      "p1"."product_name", 
      "p1"."product_line", 
      "p1"."product_vendor", 
      "p1"."buy_price" 
    from 
      "public"."product" as "p1" 
    where 
      "p1"."buy_price" in (
        select 
          max("p2"."buy_price") 
        from 
          "public"."product" as "p2" 
        where 
          "p2"."product_line" = "p1"."product_line" 
        group by 
          "p2"."product_line"
      ) 
    order by 
      "p1"."product_line", 
      "p1"."buy_price"
     */
    public void findProductMaxBuyPriceByProductionLine() {

        Product p1 = PRODUCT.as("p1");
        Product p2 = PRODUCT.as("p2");

        // Select<Record1<BigDecimal>>
        var maxBuyPrice = select(max(p2.BUY_PRICE))
                .from(p2)
                .where(p2.PRODUCT_LINE.eq(p1.PRODUCT_LINE))
                .groupBy(p2.PRODUCT_LINE);

        System.out.println("EXAMPLE 1\n"
                + ctx.select(p1.PRODUCT_ID, p1.PRODUCT_NAME,
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
      "public"."employee"."employee_number", 
      "public"."employee"."first_name", 
      "public"."employee"."job_title", 
      (
        select 
          sum("public"."sale"."sale") 
        from 
          "public"."sale" 
        where 
          "public"."employee"."employee_number" = "public"."sale"."employee_number"
      ) as "sumSales" 
    from 
      "public"."employee" 
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
        System.out.println("EXAMPLE 2\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
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
      "public"."customerdetail"."city", 
      "public"."customerdetail"."country", 
      (
        select 
          (
            "public"."customer"."contact_first_name" || ? || "public"."customer"."contact_last_name"
          ) 
        from 
          "public"."customer" 
        where 
          "public"."customer"."customer_number" = "public"."customerdetail"."customer_number"
      ) as "fullName" 
    from 
      "public"."customerdetail"
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
        System.out.println("EXAMPLE 3\n"
                + ctx.select(
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
      "public"."office"."city", 
      "public"."office"."address_line_first", 
      (
        select 
          count(*) 
        from 
          "public"."employee" 
        where 
          "public"."employee"."office_code" = "public"."office"."office_code"
      ) as "employeesNr" 
    from 
      "public"."office"
     */
    public void findOfficeAndNoOfEmployee() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
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
          "s1"."sale", 
          "s1"."fiscal_year", 
          "s1"."employee_number" 
        from 
          "public"."sale" as "s1" 
        where 
          "s1"."sale" = (
            select 
              max("s2"."sale") 
            from 
              "public"."sale" as "s2" 
            where 
              (
                "s2"."employee_number" = "s1"."employee_number" 
                and "s2"."fiscal_year" = "s1"."fiscal_year"
              )
          ) 
        order by 
          "s1"."fiscal_year"
         */
        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");

        System.out.println("EXAMPLE 5\n"
                + ctx.select(s1.SALE_, s1.FISCAL_YEAR, s1.EMPLOYEE_NUMBER)
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
          "public"."sale"."fiscal_year", 
          "public"."sale"."employee_number", 
          max("public"."sale"."sale") 
        from 
          "public"."sale" 
        group by 
          "public"."sale"."fiscal_year", 
          "public"."sale"."employee_number" 
        order by 
          "public"."sale"."fiscal_year"
         */
        System.out.println("EXAMPLE 5 (via groupBy)\n"
                + ctx.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
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
          "public"."employee"."first_name", 
          "public"."employee"."last_name", 
          "public"."employee"."salary" 
        from 
          "public"."employee" 
        where 
          (
            select 
              avg("public"."sale"."sale") 
            from 
              "public"."sale"
          ) < (
            select 
              sum("public"."sale"."sale") 
            from 
              "public"."sale" 
            where 
              "public"."employee"."employee_number" = "public"."sale"."employee_number"
          )
         */
        System.out.println("EXAMPLE 6.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(field(select(avg(SALE.SALE_)).from(SALE)).lt(
                                (select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER)))))
                        .fetch()
        );

        /*
        select 
          distinct "public"."employee"."first_name", 
          "public"."employee"."last_name", 
          "public"."employee"."salary" 
        from 
          "public"."employee" 
          join "public"."office" on (
            select 
              avg("public"."sale"."sale") 
            from 
              "public"."sale"
          ) < (
            select 
              sum("public"."sale"."sale") 
            from 
              "public"."sale" 
            where 
              "public"."employee"."employee_number" = "public"."sale"."employee_number"
          )
         */
        System.out.println("EXAMPLE 6.2\n"
                + ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
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
      "public"."office"."city", 
      "public"."office"."address_line_first", 
      (
        select 
          max("public"."employee"."salary") 
        from 
          "public"."employee" 
        where 
          "public"."employee"."office_code" = "public"."office"."office_code"
      ) as "maxSalary", 
      (
        select 
          avg("public"."employee"."salary") 
        from 
          "public"."employee"
      ) as "avgSalary" 
    from 
      "public"."office"
     */
    public void findOfficeAndEmployeeMaxAndAvgSalary() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
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
      "public"."customer"."customer_number", 
      "public"."customer"."contact_first_name", 
      "public"."customer"."contact_last_name" 
    from 
      "public"."customer" 
    where 
      exists (
        select 
          count(*) 
        from 
          "public"."order" 
        where 
          "public"."customer"."customer_number" = "public"."order"."customer_number" 
        group by 
          "public"."order"."customer_number" 
        having 
          count(*) > ?
      ) 
    order by 
      "public"."customer"."contact_first_name", 
      "public"."customer"."contact_last_name"
     */
    public void findCustomerWithMoreThan10Sales() {

        System.out.println("EXAMPLE 8\n"
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

    // EXAMPLE 9
    /*
    select 
      "public"."orderdetail"."order_id", 
      "public"."orderdetail"."product_id", 
      "public"."orderdetail"."quantity_ordered", 
      "public"."orderdetail"."price_each", 
      "public"."orderdetail"."order_line_number" 
    from 
      "public"."orderdetail" 
    where 
      not exists (
        select 
          "public"."product"."product_id" 
        from 
          "public"."product" 
        where 
          (
            "public"."product"."product_id" = "public"."orderdetail"."product_id" 
            and "public"."product"."quantity_in_stock" > "public"."orderdetail"."quantity_ordered"
          )
      ) 
    group by 
      "public"."orderdetail"."product_id", 
      "public"."orderdetail"."order_id" 
    order by 
      "public"."orderdetail"."quantity_ordered"
     */
    public void findOrderdetailWithQuantityInStockLtQuantityOrdered() {

        System.out.println("EXAMPLE 9\n"
                + ctx.selectFrom(ORDERDETAIL)
                        .whereNotExists(select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)
                                        .and(PRODUCT.QUANTITY_IN_STOCK.coerce(Integer.class)
                                                .gt(ORDERDETAIL.QUANTITY_ORDERED))))
                        .groupBy(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.ORDER_ID)
                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                        .fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      "public"."product"."product_name", 
      "public"."product"."buy_price" 
    from 
      "public"."product" 
    where 
      "public"."product"."product_id" = any (
        select 
          "public"."orderdetail"."product_id" 
        from 
          "public"."orderdetail" 
        where 
          (
            "public"."product"."product_id" = "public"."orderdetail"."product_id" 
            and "public"."orderdetail"."quantity_ordered" > ?
          )
      )
     */
    public void findProductQuantityOrderedGt70() {

        System.out.println("EXAMPLE 10\n"
                + ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(any(
                                select(ORDERDETAIL.PRODUCT_ID).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)
                                                .and(ORDERDETAIL.QUANTITY_ORDERED.gt(70))))
                        ))
                        .fetch()
        );
    }

    // EXAMPLE 11
    /*
    select 
      "public"."product"."product_id", 
      "public"."product"."product_name" 
    from 
      "public"."product" 
    where 
      "public"."product"."msrp" > all (
        select 
          "public"."orderdetail"."price_each" 
        from 
          "public"."orderdetail" 
        where 
          "public"."product"."product_id" = "public"."orderdetail"."product_id"
      )
     */
    public void findProductWithMsrpGtSellPrice() {

        System.out.println("EXAMPLE 11\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.MSRP.gt(all(
                                select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
    }

    // EXAMPLE 12
    /*
    select 
      "public"."product"."product_id", 
      "public"."product"."product_name", 
      "public"."product"."buy_price" 
    from 
      "public"."product" 
    where 
      (
        select 
          avg("public"."product"."buy_price") 
        from 
          "public"."product"
      ) > any (
        select 
          "public"."orderdetail"."price_each" 
        from 
          "public"."orderdetail" 
        where 
          "public"."product"."product_id" = "public"."orderdetail"."product_id"
      )
     */
    public void findProductWithAvgBuyPriceGtAnyPriceEach() {

        System.out.println("EXAMPLE 12\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT)).gt(any(
                                select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
    }

    // EXAMPLE 13
    /*
    select 
      "public"."product"."product_id", 
      "public"."product"."product_name", 
      "public"."product"."buy_price" 
    from 
      "public"."product" 
    where 
      (
        select 
          avg("public"."product"."buy_price") 
        from 
          "public"."product"
      ) > all (
        select 
          "public"."orderdetail"."price_each" 
        from 
          "public"."orderdetail" 
        where 
          "public"."product"."product_id" = "public"."orderdetail"."product_id"
      )
     */
    public void findProductWithAvgBuyPriceGtAllPriceEach() {

        System.out.println("EXAMPLE 13\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT))
                                .gt(all(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
    }

    // EXAMPLE 14
    /*
    select 
      "public"."payment"."invoice_amount", 
      "public"."payment"."payment_date", 
      "public"."payment"."caching_date", 
      case when "public"."payment"."caching_date" is null then (
        select 
          "public"."customer"."credit_limit" 
        from 
          "public"."customer" 
        where 
          "public"."payment"."customer_number" = "public"."customer"."customer_number"
      ) else ? end as "credit_limit" 
    from 
      "public"."payment" 
    order by 
      "public"."payment"."caching_date"
     */
    public void findUnprocessedPayments() {

        System.out.println("EXAMPLE 14\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE,
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

    // EXAMPLE 15    
    /*
    select 
      "s"."sale_id", 
      "s"."employee_number" 
    from 
      "public"."sale" as "s" 
    where 
      "s"."fiscal_year" = ? 
    group by 
      "s"."employee_number"
    having 
      sum("s"."sale") > (
        select 
          sum("public"."sale"."sale") 
        from 
          "public"."sale" 
        where 
          (
            "public"."sale"."fiscal_year" = ? 
            and "s"."employee_number" = "public"."sale"."employee_number"
          ) 
        group by 
          "public"."sale"."employee_number"
      )
     */
    public void findEmployeeNumberWithMoreSalesIn2005Than2003() {

        Sale sale = SALE.as("s");

        System.out.println("EXAMPLE 15\n"
                + ctx.select(sale.EMPLOYEE_NUMBER)
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

    // EXAMPLE 16 
    /*
    update 
      "public"."customer" 
    set 
      "credit_limit" = (
        select 
          sum(
            "public"."payment"."invoice_amount"
          ) 
        from 
          "public"."payment" 
        where 
          "public"."payment"."customer_number" = "public"."customer"."customer_number"
      )
     */
    @Transactional
    public void updateCustomerCreditLimit() {

        System.out.println("EXAMPLE 16 (affected rows): "
                + +ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT,
                                select(sum(PAYMENT.INVOICE_AMOUNT)).from(PAYMENT)
                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .execute()
        );
    }

    // EXAMPLE 17
    /*
    delete from 
      "public"."payment" 
    where 
      "public"."payment"."customer_number" in (
        select 
          "public"."customer"."customer_number" 
        from 
          "public"."customer" 
        where 
          (
            "public"."payment"."customer_number" = "public"."customer"."customer_number" 
            and "public"."customer"."credit_limit" > ?
          )
      )
     */
    @Transactional
    public void deletePaymentOfCustomerCreditLimitGt150000() {

        System.out.println("EXAMPLE 17 (affected rows): "
                + +ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.in(select(CUSTOMER.CUSTOMER_NUMBER)
                                .from(CUSTOMER).where(PAYMENT.CUSTOMER_NUMBER
                                .eq(CUSTOMER.CUSTOMER_NUMBER)
                                .and(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.valueOf(150000))))))
                        .execute()
        );
    }

    // EXAMPLE 18
    /*
    insert into "public"."order" (
      "order_date", "required_date", "shipped_date", 
      "status", "customer_number"
    ) 
    select 
      distinct "public"."payment"."payment_date", 
      "public"."payment"."payment_date", 
      "public"."payment"."caching_date", 
      ?, 
      "public"."payment"."customer_number" 
    from 
      "public"."payment", 
      "public"."order" 
    where 
      "public"."payment"."payment_date" <> "public"."order"."order_date" 
    order by 
      "public"."payment"."payment_date"
     */
    @Transactional
    public void insertPaymentInOrder() {

        System.out.println("EXAMPLE 18 (affected rows): "
                + ctx.insertInto(ORDER, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
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