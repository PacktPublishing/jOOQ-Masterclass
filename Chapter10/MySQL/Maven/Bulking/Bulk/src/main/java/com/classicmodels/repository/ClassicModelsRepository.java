package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.enums.SaleRate;
import jooq.generated.enums.SaleVat;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.Order;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
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

    @Transactional
    public void bulkInserts() {

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.insertInto(ORDER)
                        .columns(ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.COMMENTS, 
                                ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L, BigDecimal.valueOf(322.59))
                        .values(LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L, BigDecimal.valueOf(455.33))
                        .values(LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L, BigDecimal.valueOf(190.99))
                        .onDuplicateKeyIgnore() // or, use onDuplicateKeyUpdate().set(...)
                        .execute()
        );

        Order o = ORDER;
        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.insertInto(o)
                        .columns(o.ORDER_DATE, o.REQUIRED_DATE, o.SHIPPED_DATE,
                                o.STATUS, o.COMMENTS, o.CUSTOMER_NUMBER, o.AMOUNT)
                        .select(
                                select(val(LocalDate.of(2010, 10, 10)), val(LocalDate.of(2010, 11, 1)),
                                        val(LocalDate.of(2010, 11, 5)), val("Shipped"), val(""), val(103L), val(BigDecimal.valueOf(230.99)))
                                        .whereNotExists(
                                                selectFrom(o)
                                                        .where(val(LocalDate.of(2010, 10, 10)).between(o.ORDER_DATE).and(o.SHIPPED_DATE)
                                                                .or(val(LocalDate.of(2010, 11, 5)).between(o.ORDER_DATE).and(o.SHIPPED_DATE)))
                                                        .and(val(103L).eq(o.CUSTOMER_NUMBER))
                                        )
                        )
                        .onDuplicateKeyIgnore() // or, use onDuplicateKeyUpdate().set(...)
                        .execute()
        );
        
        List<SaleRecord> listOfRecord
                = List.of(
                        new SaleRecord(null, 2003, 3443.22, 1370L,
                                null, SaleRate.SILVER, SaleVat.MAX, 3, 14.55, null),
                        new SaleRecord(null, 2005, 1221.12, 1504L,
                                null, SaleRate.SILVER, SaleVat.MAX, 5, 22.11, "UP"),
                        new SaleRecord(null, 2005, 1221.12, 1504L,
                                (byte) 1, SaleRate.SILVER, SaleVat.MAX, 7, 65.59, null));
        
        System.out.println("EXAMPLE 1.3 (affected rows): "
                + ctx.insertInto(SALE, SALE.fields())
                        .valuesOfRecords(listOfRecord)
                        .execute()
        );
        
        var listOfRows
                = List.of(row(2003, 3443.22, 1370L,
                        SaleRate.SILVER, SaleVat.MAX, 3, 14.55),
                        row(2005, 1221.12, 1504L,
                                SaleRate.SILVER, SaleVat.MAX, 5, 22.11),
                        row(2005, 1221.12, 1504L,
                                SaleRate.SILVER, SaleVat.MAX, 7, 65.59));

        System.out.println("EXAMPLE 1.4 (affected rows): "
                + ctx.insertInto(SALE,
                        SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.RATE, SALE.VAT,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .valuesOfRows(listOfRows)
                        .execute()
        );
    }

    @Transactional
    public void bulkUpdates() {
        
        ctx.update(SALE)
                .set(SALE.SALE_,
                        case_()
                                .when(SALE.EMPLOYEE_NUMBER.eq(1370L), SALE.SALE_.plus(100))
                                .when(SALE.EMPLOYEE_NUMBER.eq(1504L), SALE.SALE_.plus(500))
                                .when(SALE.EMPLOYEE_NUMBER.eq(1166L), SALE.SALE_.plus(1000)))
                .where(SALE.EMPLOYEE_NUMBER.in(1370L, 1504L, 1166L))
                .execute();

        ctx.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(EMPLOYEE.SALARY.mul(25).div(100)))
                .execute();
    }
    
    @Transactional
    public void bulkDeletes() {
        
        ctx.deleteFrom(SALE).execute();
    }    
}
