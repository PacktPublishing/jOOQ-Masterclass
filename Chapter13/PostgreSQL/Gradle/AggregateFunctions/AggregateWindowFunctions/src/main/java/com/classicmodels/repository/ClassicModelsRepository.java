package com.classicmodels.repository;

import java.time.LocalDateTime;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.varSamp;
import static org.jooq.impl.SQLDataType.NUMERIC;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // SUM(), COUNT()
    public void usingSumAndCount() {

        // example 1
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                sum(EMPLOYEE.SALARY).over()
                        .partitionBy(OFFICE.OFFICE_CODE))
                .from(EMPLOYEE)
                .join(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();

        // example 2 (another formula of median)
        Field<Integer> x = ctx.select(sum(PRODUCT.QUANTITY_IN_STOCK)
                .over().partitionBy(PRODUCT.QUANTITY_IN_STOCK)).asField("x");
        Field<Integer> y = (val(2).mul(rowNumber().over().orderBy(PRODUCT.QUANTITY_IN_STOCK))
                .minus(count().over())).as("y");
        Field<Integer> z = count().over().partitionBy(PRODUCT.QUANTITY_IN_STOCK).as("z");

        ctx.select(sum(x).divide(sum(z)))
                .from(select(x, y, z).from(PRODUCT))
                .where(y.between(0, 2))
                .fetch();

        // in PostgreSQL, you can use median() for this job
        ctx.select(median(PRODUCT.QUANTITY_IN_STOCK))
                .from(PRODUCT)
                .fetch();

        // example 3        
        ctx.select(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CACHING_DATE,
                BANK_TRANSACTION.TRANSFER_AMOUNT, BANK_TRANSACTION.STATUS,
                sum(BANK_TRANSACTION.TRANSFER_AMOUNT).over()
                        .partitionBy(BANK_TRANSACTION.CUSTOMER_NUMBER)
                        .orderBy(BANK_TRANSACTION.CACHING_DATE)
                        .rowsBetweenUnboundedPreceding().andCurrentRow().as("result"))
                .from(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.STATUS.eq("SUCCESS"))
                .fetch();

        // example 4 - emulate CUME_DIST() 
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK,
                round(cast(cumeDist().over().orderBy(PRODUCT.QUANTITY_IN_STOCK), NUMERIC), 2).as("cume_dist"),
                round(((cast(count().over().orderBy(PRODUCT.QUANTITY_IN_STOCK).rangeUnboundedPreceding(), Double.class)).divide(
                        count().over().orderBy(PRODUCT.QUANTITY_IN_STOCK).rangeBetweenUnboundedPreceding()
                                .andUnboundedFollowing())), 2).as("em_cume_dist"))
                .from(PRODUCT)
                .fetch();

        // example 5 - How many other employees have the same salary as me?        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY,
                count().over().partitionBy(EMPLOYEE.JOB_TITLE).orderBy(EMPLOYEE.SALARY)
                        .groupsBetweenCurrentRow().andCurrentRow()
                        .excludeCurrentRow().as("count_of_equal"))
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)
                .fetch();
  
        // example 6 - how many sales are better by 5000 or less
        ctx.select(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_,
                count().over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.SALE_)
                        .rangeBetweenCurrentRow().andFollowing(5000).excludeGroup().as("count_of_better"))
                .from(SALE)
                .orderBy(SALE.FISCAL_YEAR, SALE.SALE_)
                .fetch();
    }

    // MIN(), MAX()
    public void usingMinMax() {

        ctx.select(field(name("t2", "min_od")), max(field(name("t2", "sd")))).from(
                select(field(name("t1", "od")), field(name("t1", "sd")),
                        max(field(name("t1", "od"))).filterWhere(field(name("t1", "od")).gt(field(name("t1", "max_sd"))))
                                .over().orderBy(
                                        field(name("t1", "od")), field(name("t1", "sd"))).rowsUnboundedPreceding().as("min_od")).from(
                        select(ORDER.ORDER_DATE.as("od"), ORDER.SHIPPED_DATE.as("sd"),
                                max(ORDER.SHIPPED_DATE).over().orderBy(ORDER.ORDER_DATE, ORDER.SHIPPED_DATE)
                                        .rowsBetweenUnboundedPreceding().andPreceding(1).as("max_sd"))
                                .from(ORDER).asTable("t1")).asTable("t2"))
                .groupBy(field(name("t2", "min_od")))
                .fetch();
    }

    // AVG()
    public void usingAvg() {

        ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRODUCT_ID,
                ORDERDETAIL.ORDER_LINE_NUMBER, ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH,
                avg(ORDERDETAIL.PRICE_EACH).over()
                        .partitionBy(ORDERDETAIL.ORDER_ID).orderBy(ORDERDETAIL.PRICE_EACH)
                        .rowsPreceding(2).as("avg_prec_3_prices"))
                .from(ORDERDETAIL)
                .fetch();

        ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY,
                avg(EMPLOYEE.SALARY)
                        .over()
                        .partitionBy(EMPLOYEE.JOB_TITLE).orderBy(EMPLOYEE.SALARY)
                        .groupsBetweenFollowing(1).andUnboundedFollowing().as("avg_of_better"))
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)
                .fetch();

        ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY,
                avg(EMPLOYEE.SALARY)
                        .over()
                        .partitionBy(EMPLOYEE.JOB_TITLE).orderBy(EMPLOYEE.SALARY)
                        .groupsBetweenCurrentRow().andUnboundedFollowing().excludeGroup().as("avg_of_better"))
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)
                .fetch();
    }

    // Calculate Running Totals via SUM()
    public void calculateRunningTotals() {

        ctx.selectDistinct(BANK_TRANSACTION.CACHING_DATE, BANK_TRANSACTION.CARD_TYPE,
                sum(BANK_TRANSACTION.TRANSFER_AMOUNT).over()
                        .partitionBy(BANK_TRANSACTION.CARD_TYPE)
                        .orderBy(BANK_TRANSACTION.CACHING_DATE).as("transaction_running_total"))
                .from(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.CACHING_DATE
                        .between(LocalDateTime.of(2005, 3, 1, 0, 0, 0),
                                LocalDateTime.of(2005, 3, 31, 0, 0, 0)))
                .orderBy(BANK_TRANSACTION.CARD_TYPE)
                .fetch();
    }

    // Calculate Running Averages via SUM() and AVG()
    public void calculateRunningAverages() {

        ctx.select(BANK_TRANSACTION.CACHING_DATE, BANK_TRANSACTION.CARD_TYPE,
                sum(BANK_TRANSACTION.TRANSFER_AMOUNT).as("daily_sum"),
                avg(sum(BANK_TRANSACTION.TRANSFER_AMOUNT)).over()
                        .orderBy(BANK_TRANSACTION.CACHING_DATE)
                        .rowsBetweenPreceding(2).andCurrentRow().as("transaction_running_average"))
                .from(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.CACHING_DATE
                        .between(LocalDateTime.of(2005, 3, 1, 0, 0, 0),
                                LocalDateTime.of(2005, 3, 31, 0, 0, 0))
                        .and(BANK_TRANSACTION.CARD_TYPE.eq("VisaElectron")))
                .groupBy(BANK_TRANSACTION.CACHING_DATE, BANK_TRANSACTION.CARD_TYPE)
                .orderBy(BANK_TRANSACTION.CACHING_DATE)
                .fetch();
    }
    
    // VAR_SAMP()
    // cumulative variance of salary values in office 1 ordered by commission
    public void cumulativeVarianceOfSalaryInOffice1ByCommission() {
        
        ctx.select(EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY, 
                varSamp(EMPLOYEE.SALARY).over().orderBy(EMPLOYEE.COMMISSION).as("cv"))
                .from(EMPLOYEE)
                .where(EMPLOYEE.OFFICE_CODE.eq("1"))
                .orderBy(EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                .fetch();                       
    }
}
