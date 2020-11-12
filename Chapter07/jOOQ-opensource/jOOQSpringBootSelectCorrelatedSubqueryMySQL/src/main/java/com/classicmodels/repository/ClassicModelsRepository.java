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

    public void findProductsMaxBuyPriceByProductionLine() {

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

    public void qq() {

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

    public void cv() {

        ctx.selectFrom(ORDER)
                .whereNotExists(select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                        .where(ORDER.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)
                                .and(ORDER.STATUS.eq("Shipped"))))
                .orderBy(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE)
                .fetch();
    }

    public void xx() {

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

    public void cvb() {

        System.out.println(
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.lt(all(
                                select(EMPLOYEE.employee().SALARY).from(EMPLOYEE)
                                        .where(EMPLOYEE.REPORTS_TO.ne(EMPLOYEE.EMPLOYEE_NUMBER))
                        )))
                        .fetch()
        );
    }

    // scalar subquery
    public void xxc() {

        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .where(field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT)).gt(any(
                        select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                .fetch();
    }

    public void qqs() {

        System.out.println(
                ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .where(field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT))
                                .gt(all(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID)))))
                        .fetch()
        );
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
