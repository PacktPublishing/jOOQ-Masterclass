package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*  The NTILE() is a window function useful for distributing the 
        number of rows in the specified N number of groups. */
    
    public void ntileSalary() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                ntile(5).over().orderBy(EMPLOYEE.SALARY.desc()).as("salary_group"))
                .from(EMPLOYEE)
                .fetch();
    }

    public void ntileSalaryPerOffice() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                ntile(2).over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY.desc()).as("salary_group"))
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();
    }
    
    public void ntilePrices() {
        
        ctx.select(min(field("price")), max(field("price")), count(), field("bucket"))
                .from(select(ORDERDETAIL.PRICE_EACH.as("price"), 
                        ntile(10).over().orderBy(ORDERDETAIL.PRICE_EACH).as("bucket"))
                        .from(ORDERDETAIL))
                .groupBy(field("bucket"))
                .fetch();
    }
    
    // Calculating Recency, Frequency and Monetary (RFM) indices
    public void rfm() {

        ctx.select(field("customer_number"),
                ntile(4).over().orderBy(field("last_order_date")).as("rfm_recency"),
                ntile(4).over().orderBy(field("count_order")).as("rfm_frequency"),
                ntile(4).over().orderBy(field("avg_amount")).as("rfm_monetary")).from(
                select(ORDER.CUSTOMER_NUMBER.as("customer_number"),
                        max(ORDER.ORDER_DATE).as("last_order_date"),
                        count().as("count_order"), avg(ORDER.AMOUNT).as("avg_amount"))
                        .from(ORDER)
                        .groupBy(ORDER.CUSTOMER_NUMBER))
                .fetch();

        // RFM combined based on the previous query        
        ctx.select(field("customer_number"),
                field("rfm_recency").mul(100).plus(field("rfm_frequency").mul(10))
                        .plus(field("rfm_monetary")).as("rfm_combined")).from(
                select(field("customer_number"),
                        ntile(4).over().orderBy(field("last_order_date")).as("rfm_recency"),
                        ntile(4).over().orderBy(field("count_order")).as("rfm_frequency"),
                        ntile(4).over().orderBy(field("avg_amount")).as("rfm_monetary")).from(
                        select(ORDER.CUSTOMER_NUMBER.as("customer_number"),
                                max(ORDER.ORDER_DATE).as("last_order_date"),
                                count().as("count_order"), avg(ORDER.AMOUNT).as("avg_amount"))
                                .from(ORDER)
                                .groupBy(ORDER.CUSTOMER_NUMBER)))
                .fetch();
    }
}
