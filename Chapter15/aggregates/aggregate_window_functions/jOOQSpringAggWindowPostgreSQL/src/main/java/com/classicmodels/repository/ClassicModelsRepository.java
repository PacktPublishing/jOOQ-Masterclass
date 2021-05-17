package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
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
                round(((cast(count().over().orderBy(PRODUCT.QUANTITY_IN_STOCK).rangeUnboundedPreceding(), Double.class)).divide(
                        count().over().orderBy(PRODUCT.QUANTITY_IN_STOCK).rangeBetweenUnboundedPreceding()
                                .andUnboundedFollowing())), 2).as("cume_dist"))
                .from(PRODUCT)
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
                        .rowsPreceding(3).as("avg_last_3_prices"))
                .from(ORDERDETAIL)
                .fetch();
    }
}
