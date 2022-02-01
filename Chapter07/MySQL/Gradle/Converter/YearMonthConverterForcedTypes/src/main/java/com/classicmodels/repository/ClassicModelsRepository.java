package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
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

        // non-type-safe,
        // converter is not used (we insert '202010' directly)
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, 24249)
                .onDuplicateKeyIgnore()
                .execute();

        // non-type-safe,
        // converter is used thanks to <forcedTypes/>
        // convert from 'YearMonth' to 'int'
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();

        // type-safe,
        // converter is used thanks to <forcedTypes/>
        // convert from 'YearMonth' to 'int'
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void findAtelierOneCustomer() {

        // in the next three examples, converter is used thanks to <forcedTypes/>
        // convert from 'int' to 'YearMonth'
        
        YearMonth ym = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchAny(CUSTOMER.FIRST_BUY_DATE);
        System.out.println("YM: " + ym);

        YearMonth[] ymArr = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchArray(CUSTOMER.FIRST_BUY_DATE);
        System.out.println("ymArr: " + Arrays.toString(ymArr));

        List<YearMonth> ymList = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch(CUSTOMER.FIRST_BUY_DATE);
        System.out.println("ymList: " + ymList);
    }
}