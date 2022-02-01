package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void insertCustomer() {

        // converter is not used (we insert the Integer '202010' directly)
        ctx.insertInto(CUSTOMER)
                .values(null, 
                        "Atelier-" + UUID.randomUUID().toString(), // random customer_name 
                        "Markus", "Alop", "0892 339 423",
                        1370, 50000, 24249)
                .execute();

        // non type-safe,
        // behind the scene, jOOQ call our converter to convert from YearMonth to Integer
        ctx.insertInto(CUSTOMER)
                .values(null, 
                         "Atelier-" + UUID.randomUUID().toString(), // random customer_name
                        "Markus", "Alop", "0892 339 423",
                        1370, 50000, YearMonth.of(2020, 10))
                .execute();
        
        // type-safe,
        // behind the scene, jOOQ call our converter to convert from YearMonth to Integer
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                .values("Atelier-" + UUID.randomUUID().toString(), // random customer_name
                        "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .execute();
    }

    public void findAtelierOneCustomer() {

        List<YearMonth> result = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.like("Atelier%"))
                .fetch(CUSTOMER.FIRST_BUY_DATE);

        System.out.println("Result: " + result);
    }
}
