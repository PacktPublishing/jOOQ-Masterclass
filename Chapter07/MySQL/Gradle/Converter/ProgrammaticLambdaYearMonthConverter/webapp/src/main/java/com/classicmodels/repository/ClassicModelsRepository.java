package com.classicmodels.repository;

import java.time.YearMonth;
import java.util.List;
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
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, 24249)
                .onDuplicateKeyIgnore()
                .execute();

        // behind the scene, jOOQ call our converter to convert from YearMonth to Integer
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void findAtelierOneCustomer() {

        List<YearMonth> result = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch(CUSTOMER.FIRST_BUY_DATE);

        System.out.println("Result: " + result);
    }
}
