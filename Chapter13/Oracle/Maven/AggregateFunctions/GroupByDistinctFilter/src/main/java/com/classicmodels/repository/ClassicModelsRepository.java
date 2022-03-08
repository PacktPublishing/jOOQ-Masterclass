package com.classicmodels.repository;

import java.math.BigDecimal;
import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.avgDistinct;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.ceil;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.extract;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void officeHavingLessThen3Employees() {

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, nvl(groupConcat(EMPLOYEE.FIRST_NAME), "N/A").as("NAME"))
                .from(OFFICE)
                .leftJoin(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .groupBy(OFFICE.OFFICE_CODE, OFFICE.CITY)
                .having(count().lt(3))
                .fetch();
    }

    public void employeeHavingLargestNumberOfSales() {

        ctx.select(SALE.EMPLOYEE_NUMBER, count())
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .having(count().plus(1).gt(all(
                        selectDistinct(count())
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER))))
                .fetch();
    }

    public void orderdetailsQuantityOufOfRange2040() {

        ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRODUCT_ID,
                ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                .from(ORDERDETAIL)
                .where(ORDERDETAIL.ORDER_ID
                        .in(select(ORDERDETAIL.ORDER_ID).from(ORDERDETAIL)
                                .groupBy(ORDERDETAIL.ORDER_ID)
                                .having(count().eq(1).and(min(ORDERDETAIL.QUANTITY_ORDERED).lt(20))
                                        .or(count().gt(1).and(min(ORDERDETAIL.QUANTITY_ORDERED).gt(40))))
                        )).fetch();
    }

    public void avgOfSumOfSales() {

        ctx.select(field(name("t", "en")), avg(field(name("t", "ss"), Double.class)).as("sale_avg"))
                .from(ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, sum(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR)
                        .asTable("t", "en", "fy", "ss"))
                .groupBy(field(name("t", "en")))
                .fetch();
    }

    public void groupByExtractedMonth() {

        ctx.select(extract(PAYMENT.PAYMENT_DATE, DatePart.MONTH), sum(PAYMENT.INVOICE_AMOUNT))
                .from(PAYMENT)
                .groupBy(extract(PAYMENT.PAYMENT_DATE, DatePart.MONTH))
                .orderBy(extract(PAYMENT.PAYMENT_DATE, DatePart.MONTH))
                .fetch();
    }

    public void exactDivisionOrderdetailTop3Product() {

        ctx.select(ORDERDETAIL.ORDER_ID)
                .from(ORDERDETAIL)
                .leftOuterJoin(TOP3PRODUCT)
                .on(ORDERDETAIL.PRODUCT_ID.eq(TOP3PRODUCT.PRODUCT_ID))
                .groupBy(ORDERDETAIL.ORDER_ID)
                .having(count(ORDERDETAIL.ORDER_ID).eq(
                        select(count(TOP3PRODUCT.PRODUCT_ID)).from(TOP3PRODUCT))
                        .and(count(TOP3PRODUCT.PRODUCT_ID).eq(
                                select(count(TOP3PRODUCT.PRODUCT_ID)).from(TOP3PRODUCT))))
                .fetch();
    }

    // add FILTER
    /////////////
    public void employeeSalary() {

        ctx.select(EMPLOYEE.SALARY,
                (sum(case_().when(EMPLOYEE.JOB_TITLE.eq("Sales Rep"), 1).else_(0))).as("Sales Rep"),
                (sum(case_().when(EMPLOYEE.JOB_TITLE.ne("Sales Rep"), 1).else_(0))).as("Others"))
                .from(EMPLOYEE)
                .groupBy(EMPLOYEE.SALARY)
                .fetch();

        ctx.select(EMPLOYEE.SALARY,
                (count().filterWhere(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))).as("Sales Rep"),
                (count().filterWhere(EMPLOYEE.JOB_TITLE.ne("Sales Rep"))).as("Others"))
                .from(EMPLOYEE)
                .groupBy(EMPLOYEE.SALARY)
                .fetch();
    }

    public void avgSumProduct() {

        ctx.select(
                avg(PRODUCT.BUY_PRICE).filterWhere(
                        PRODUCT.BUY_PRICE.ge(BigDecimal.valueOf(50))).as("avg_buy_price"),
                sum(PRODUCT.MSRP).filterWhere(
                        PRODUCT.MSRP.ge(BigDecimal.valueOf(50))).as("sum_msrp"))
                .from(PRODUCT)
                .fetch();
    }

    public void pivotViaFilter() {

        // no pivot        
        ctx.select(SALE.FISCAL_YEAR, SALE.FISCAL_MONTH,
                sum(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR, SALE.FISCAL_MONTH)
                .fetch();

        // pivot via FILTER        
        ctx.select(SALE.FISCAL_YEAR,
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(1)).as("Jan_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(2)).as("Feb_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(3)).as("Mar_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(4)).as("Apr_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(5)).as("May_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(6)).as("Jun_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(7)).as("Jul_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(8)).as("Aug_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(9)).as("Sep_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(10)).as("Oct_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(11)).as("Nov_sales"),
                sum(SALE.SALE_).filterWhere(SALE.FISCAL_MONTH.eq(12)).as("Dec_sales"))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();
    }

    public void filterInAggWindowFunction() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                sum(EMPLOYEE.SALARY)
                        .filterWhere(EMPLOYEE.COMMISSION.isNull())
                        .over().partitionBy(OFFICE.OFFICE_CODE))
                .from(EMPLOYEE)
                .join(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();

        ctx.select().from(
                select(EMPLOYEE.OFFICE_CODE,
                        (count().filterWhere(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                                .over().partitionBy(EMPLOYEE.OFFICE_CODE)).as("SALES_REP"),
                        (count().filterWhere(EMPLOYEE.JOB_TITLE.ne("Sales Rep"))
                                .over().partitionBy(EMPLOYEE.OFFICE_CODE)).as("OTHERS"))
                        .from(EMPLOYEE).asTable("T"))
                .groupBy(field(name("T", "OFFICE_CODE")),
                        field(name("T", "SALES_REP")), field(name("T", "OTHERS")))
                .fetch();
    }

    public void filterInOrderedSetAggregateFunction() {

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY)
                        .filterWhere(EMPLOYEE.SALARY.gt(80000))
                        .as("listagg"))
                .from(EMPLOYEE)
                .fetch();
    }

    public void minMaxBuyPriceProduct() {

        Orderdetail p1 = ORDERDETAIL.as("P1");
        Orderdetail p2 = ORDERDETAIL.as("P2");
        Orderdetail p3 = ORDERDETAIL.as("P3");

        ctx.select(p1.PRODUCT_ID, p1.product().PRODUCT_NAME,
                min(p1.PRICE_EACH).as("min_price"),
                count().filterWhere(notExists(
                        select().from(p2).where(
                                p2.PRODUCT_ID.eq(p1.PRODUCT_ID)
                                        .and(p2.PRICE_EACH.lt(p1.PRICE_EACH))))).as("min_price_occ"),
                max(p1.PRICE_EACH).as("max_price"),
                count().filterWhere(notExists(
                        select().from(p3).where(
                                p3.PRODUCT_ID.eq(p1.PRODUCT_ID)
                                        .and(p3.PRICE_EACH.gt(p1.PRICE_EACH))))).as("max_price_occ"))
                .from(p1)
                .groupBy(p1.PRODUCT_ID, p1.product().PRODUCT_NAME)
                .fetch();
    }

    public void firstNSalaries() { // you cannot use ORDER BY and LIMIT

        Employee e1 = EMPLOYEE.as("e1");
        Employee e2 = EMPLOYEE.as("e2");

        ctx.select(e1.EMPLOYEE_NUMBER, e1.SALARY)
                .from(e1, e2)
                .groupBy(e1.EMPLOYEE_NUMBER, e1.SALARY)
                .having((count().filterWhere(e1.SALARY.lt(e2.SALARY))).plus(1).le(5))
                .fetch();
    }

    // add DISTINCT
    ///////////////
    public void medianViaGroupByAndAvgDistinct() {

        var P1 = PRODUCT.as("P1");
        var P2 = PRODUCT.as("P2");
        ctx.select(avgDistinct(PRODUCT.QUANTITY_IN_STOCK).as("MEDIAN"))
                .from(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.in(
                        select(min(PRODUCT.QUANTITY_IN_STOCK))
                                .from(PRODUCT)
                                .where(PRODUCT.QUANTITY_IN_STOCK.in(
                                        select(P1.QUANTITY_IN_STOCK).from(P1, P2)
                                                .where(P2.QUANTITY_IN_STOCK.ge(P1.QUANTITY_IN_STOCK))
                                                .groupBy(P1.QUANTITY_IN_STOCK)
                                                .having(count().le(select(ceil(count().div(2f))).from(PRODUCT)))
                                ))
                                .union(select(max(PRODUCT.QUANTITY_IN_STOCK))
                                        .from(PRODUCT)
                                        .where(PRODUCT.QUANTITY_IN_STOCK.in(
                                                select(P1.QUANTITY_IN_STOCK).from(P1, P2)
                                                        .where(P2.QUANTITY_IN_STOCK.le(P1.QUANTITY_IN_STOCK))
                                                        .groupBy(P1.QUANTITY_IN_STOCK)
                                                        .having(count().le(select(ceil(count().div(2f))).from(PRODUCT)))
                                        )))))
                .fetch();
    }

    public void countDistinctSalePerFiscalYear() {

        ctx.select(SALE.FISCAL_YEAR, count().as("cnt"),
                countDistinct(SALE.EMPLOYEE_NUMBER).as("cnt_dis"), sum(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();
        
        // select the employees having sales in 3 distinct years
        ctx.select(SALE.EMPLOYEE_NUMBER)
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .having(countDistinct(SALE.FISCAL_YEAR).gt(3))
                .fetch();
    }

    public void sumAvgOrder() {

        ctx.select(ORDERDETAIL.PRODUCT_ID,
                sumDistinct(ORDERDETAIL.PRICE_EACH), avgDistinct(ORDERDETAIL.QUANTITY_ORDERED))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.PRODUCT_ID)
                .fetch();
    }
}
