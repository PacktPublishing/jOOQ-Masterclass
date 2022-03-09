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
        
        ctx.select(min(field("PRICE")), max(field("PRICE")), count(), field("BUCKET"))
                .from(select(ORDERDETAIL.PRICE_EACH.as("PRICE"), 
                        ntile(10).over().orderBy(ORDERDETAIL.PRICE_EACH).as("BUCKET"))
                        .from(ORDERDETAIL))
                .groupBy(field("BUCKET"))
                .fetch();
    }
    
    // Calculating Recency, Frequency and Monetary (RFM) indices
    public void rfm() {

        ctx.select(field("CUSTOMER_NUMBER"),
                ntile(4).over().orderBy(field("LAST_ORDER_DATE")).as("RFM_RECENCY"),
                ntile(4).over().orderBy(field("COUNT_ORDER")).as("RFM_FTEQUENCY"),
                ntile(4).over().orderBy(field("AVG_AMOUNT")).as("RFM_MONETARY")).from(
                select(ORDER.CUSTOMER_NUMBER.as("CUSTOMER_NUMBER"),
                        max(ORDER.ORDER_DATE).as("LAST_ORDER_DATE"),
                        count().as("COUNT_ORDER"), avg(ORDER.AMOUNT).as("AVG_AMOUNT"))
                        .from(ORDER)
                        .groupBy(ORDER.CUSTOMER_NUMBER))
                .fetch();

        // RFM combined based on the previous query        
        ctx.select(field("CUSTOMER_NUMBER"),
                field("RFM_RECENCY").mul(100).plus(field("RFM_FTEQUENCY").mul(10))
                        .plus(field("RFM_MONETARY")).as("RFM_COMBINED")).from(
                select(field("CUSTOMER_NUMBER"),
                        ntile(4).over().orderBy(field("LAST_ORDER_DATE")).as("RFM_RECENCY"),
                        ntile(4).over().orderBy(field("COUNT_ORDER")).as("RFM_FTEQUENCY"),
                        ntile(4).over().orderBy(field("AVG_AMOUNT")).as("RFM_MONETARY")).from(
                        select(ORDER.CUSTOMER_NUMBER.as("CUSTOMER_NUMBER"),
                                max(ORDER.ORDER_DATE).as("LAST_ORDER_DATE"),
                                count().as("COUNT_ORDER"), avg(ORDER.AMOUNT).as("AVG_AMOUNT"))
                                .from(ORDER)
                                .groupBy(ORDER.CUSTOMER_NUMBER)))
                .fetch();
    }
}
