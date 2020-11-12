package com.classicmodels.repository;

import java.math.BigDecimal;
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
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
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
      `p1`.`product_id`,
      `p1`.`product_name`,
      `p1`.`product_line`,
      `p1`.`product_vendor`,
      `p1`.`buy_price`
    from
      `classicmodels`.`product` as `p1`
    where
      `p1`.`buy_price` in (
         select
           max(`p2`.`buy_price`)
         from
           `classicmodels`.`product` as `p2`
         where
           `p2`.`product_line` = `p1`.`product_line`
         group by
           `p2`.`product_line`
      )
    order by
      `p1`.`product_line`,
      `p1`.`buy_price`
    */
    public void findProductMaxBuyPriceByProductionLine() {

        Product p1 = PRODUCT.as("p1");
        Product p2 = PRODUCT.as("p2");

        // Select<Record1<BigDecimal>>
        var maxBuyPrice = select(max(p2.BUY_PRICE))
                .from(p2)
                .where(p2.PRODUCT_LINE.eq(p1.PRODUCT_LINE))
                .groupBy(p2.PRODUCT_LINE);

        System.out.println(
                ctx.select(p1.PRODUCT_ID, p1.PRODUCT_NAME,
                        p1.PRODUCT_LINE, p1.PRODUCT_VENDOR, p1.BUY_PRICE)
                        .from(p1)
                        .where(p1.BUY_PRICE.in(maxBuyPrice))
                        .orderBy(p1.PRODUCT_LINE, p1.BUY_PRICE)
                        .fetch()
        );

        // same query in one piece of fluent code
        /*
        System.out.println(
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
      `classicmodels`.`employee`.`employee_number`,
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`job_title`,
      (
        select
          sum(`classicmodels`.`sale`.`sale`)
        from
          `classicmodels`.`sale`
        where
          `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
      ) as `sumSales`
    from
      `classicmodels`.`employee`
    order by
      `sumSales` asc
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
        
        System.out.println(
                ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, sumSales)
                        .from(EMPLOYEE)
                        .orderBy(sumSales.asc())
                        .fetch()
        );

        // same query in one piece of fluent code
        /*
        System.out.println(
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
      `classicmodels`.`customerdetail`.`city`,
      `classicmodels`.`customerdetail`.`country`,
        (
          select
            concat
            (
              `classicmodels`.`customer`.`contact_first_name`,
              ?,
              `classicmodels`.`customer`.`contact_last_name`
            )
          from
            `classicmodels`.`customer`
          where
            `classicmodels`.`customer`.`customer_number` = `classicmodels`.`customerdetail`.`customer_number`
        ) as `fullName`
    from
      `classicmodels`.`customerdetail`
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
        
        System.out.println(
                ctx.select(
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, fullName)
                        .from(CUSTOMERDETAIL)
                        .fetch()
        );

          // same query in one piece of fluent code
        /*
        System.out.println(
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
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`address_line_first`,
      (
        select
          count(*)
        from
          `classicmodels`.`employee`
        where
          `classicmodels`.`employee`.`office_code` = `classicmodels`.`office`.`office_code`
      ) as `employeesNr`
    from
      `classicmodels`.`office`
     */
    public void findOfficeAndNoOfEmployee() {

        System.out.println(
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
          `s1`.`sale`,
          `s1`.`fiscal_year`,
          `s1`.`employee_number`
        from
          `classicmodels`.`sale` as `s1`
        where
          `s1`.`sale` = (
            select
              max(`s2`.`sale`)
            from
              `classicmodels`.`sale` as `s2`
            where
              (
                `s2`.`employee_number` = `s1`.`employee_number`
                   and `s2`.`fiscal_year` = `s1`.`fiscal_year`
              )
          )
        order by
          `s1`.`fiscal_year`
         */
        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");

        System.out.println(
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
          `classicmodels`.`sale`.`fiscal_year`,
          `classicmodels`.`sale`.`employee_number`,
          max(`classicmodels`.`sale`.`sale`)
        from
          `classicmodels`.`sale`
        group by
          `classicmodels`.`sale`.`fiscal_year`,
          `classicmodels`.`sale`.`employee_number`
        order by
          `classicmodels`.`sale`.`fiscal_year`
         */
        System.out.println(
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
          `classicmodels`.`employee`.`first_name`,
          `classicmodels`.`employee`.`last_name`,
          `classicmodels`.`employee`.`salary`
        from
          `classicmodels`.`employee`
        where
          (
            select
              avg(`classicmodels`.`sale`.`sale`)
            from   
              `classicmodels`.`sale`
          ) < (
            select
              sum(`classicmodels`.`sale`.`sale`)
            from
              `classicmodels`.`sale`
            where
              `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
          )
         */
        System.out.println(
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(field(select(avg(SALE.SALE_)).from(SALE)).lt(
                                (select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER)))))
                        .fetch()
        );

        /*
        select distinct 
          `classicmodels`.`employee`.`first_name`,
          `classicmodels`.`employee`.`last_name`,
          `classicmodels`.`employee`.`salary`
        from
          `classicmodels`.`employee`
        join `classicmodels`.`office` on (
          select
            avg(`classicmodels`.`sale`.`sale`)
          from
            `classicmodels`.`sale`
        ) < (
          select
            sum(`classicmodels`.`sale`.`sale`)
          from
            `classicmodels`.`sale`
          where
            `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
        )
         */
        System.out.println(
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
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`address_line_first`,
      (
        select
          max(`classicmodels`.`employee`.`salary`)
        from
          `classicmodels`.`employee`
        where
          `classicmodels`.`employee`.`office_code` = `classicmodels`.`office`.`office_code`
      ) as `maxSalary`,
      (
        select
          avg(`classicmodels`.`employee`.`salary`)
        from
          `classicmodels`.`employee`
      ) as `avgSalary`
    from
      `classicmodels`.`office`
     */
    public void findOfficeAndEmployeeMaxAndAvgSalary() {

        System.out.println(
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
      `classicmodels`.`customer`.`customer_number`,
      `classicmodels`.`customer`.`contact_first_name`,
      `classicmodels`.`customer`.`contact_last_name`
    from
      `classicmodels`.`customer`
    where exists 
      (
        select
          count(*)
        from
          `classicmodels`.`order`
        where
          `classicmodels`.`customer`.`customer_number` = `classicmodels`.`order`.`customer_number`
        group by
          `classicmodels`.`order`.`customer_number`
        having
          count(*) > ?
      )
    order by
      `classicmodels`.`customer`.`contact_first_name`,
      `classicmodels`.`customer`.`contact_last_name`
    */
    public void findCustomerWithMoreThan10Sales() {

        System.out.println(
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
      `classicmodels`.`orderdetail`.`order_id`,
      `classicmodels`.`orderdetail`.`product_id`,
      `classicmodels`.`orderdetail`.`quantity_ordered`,
      `classicmodels`.`orderdetail`.`price_each`,
      `classicmodels`.`orderdetail`.`order_line_number`
    from
      `classicmodels`.`orderdetail`
    where not exists 
      (
        select
          `classicmodels`.`product`.`product_id`
        from
          `classicmodels`.`product`
        where
          (
            `classicmodels`.`product`.`product_id` = `classicmodels`.`orderdetail`.`product_id`
               and `classicmodels`.`product`.`quantity_in_stock` > `classicmodels`.`orderdetail`.`quantity_ordered`
          )
      )
    group by
      `classicmodels`.`orderdetail`.`product_id`
    order by
      `classicmodels`.`orderdetail`.`quantity_ordered`
    */
    public void findOrderdetailWithQuantityInStockLtQuantityOrdered() {

        System.out.println(
                ctx.selectFrom(ORDERDETAIL)
                        .whereNotExists(select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)
                                        .and(PRODUCT.QUANTITY_IN_STOCK.coerce(Integer.class)
                                                .gt(ORDERDETAIL.QUANTITY_ORDERED))))
                        .groupBy(ORDERDETAIL.PRODUCT_ID)
                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                        .fetch()
        );
    }

    // EXAMPLE 10
    /*
    select
      `classicmodels`.`product`.`product_name`,
      `classicmodels`.`product`.`buy_price`
    from
      `classicmodels`.`product`
    where
      `classicmodels`.`product`.`product_id` = any (
        select
          `classicmodels`.`orderdetail`.`product_id`
        from
          `classicmodels`.`orderdetail`
        where
          (
            `classicmodels`.`product`.`product_id` = `classicmodels`.`orderdetail`.`product_id`
               and `classicmodels`.`orderdetail`.`quantity_ordered` > ?
          )
    )
    */
    public void findProductQuantityOrderedGt70() {

        System.out.println(
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

    // EXAMPLE 11
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

        System.out.println(
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
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

        System.out.println(
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
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

        System.out.println(
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
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
      `s`.`sale_id`,
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
        
        System.out.println(
        ctx.select(sale.SALE_ID, sale.EMPLOYEE_NUMBER)
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
             
    @Transactional
    public void qq() {
        ctx.insertInto(SALE, SALE.SALE_ID, SALE.SALE_, SALE.FISCAL_YEAR)
                .select(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY
                        .cast(Double.class), val(2005)).from(EMPLOYEE, SALE)
                .where(SALE.SALE_ID.notEqual(EMPLOYEE.EMPLOYEE_NUMBER)))
                .onDuplicateKeyIgnore()
                .execute();
    }
     
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

        System.out.println("Affected rows:"
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT,
                                select(sum(PAYMENT.INVOICE_AMOUNT)).from(PAYMENT)
                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .execute()
        );
    }

    @Transactional
    public void del() {

        System.out.println("Affected rows:"
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.in(select(CUSTOMER.CUSTOMER_NUMBER)
                                .from(CUSTOMER).where(PAYMENT.CUSTOMER_NUMBER
                                .eq(CUSTOMER.CUSTOMER_NUMBER)
                                .and(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.valueOf(150000))))))
                        .execute()
        );
    }
}
