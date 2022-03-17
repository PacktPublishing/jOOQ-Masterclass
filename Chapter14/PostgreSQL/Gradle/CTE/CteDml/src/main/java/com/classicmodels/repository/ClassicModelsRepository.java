package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.EmployeeStatus.EMPLOYEE_STATUS;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Record1;
import static org.jooq.impl.DSL.currentTimestamp;
import static org.jooq.impl.DSL.deleteFrom;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.DSL.localDate;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.update;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.with;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void cte1() {

        // EXAMPLE 1 (UPDATE)
        ctx.with("cte", "msrp")
                .as(update(PRODUCT).set(PRODUCT.MSRP, BigDecimal.ZERO).returningResult(PRODUCT.MSRP))
                .select(field(name("msrp"))).from(name("cte"))
                .fetch();

        // same as previously but using selectFrom()
        ctx.with("cte", "msrp")
                .as(update(PRODUCT).set(PRODUCT.MSRP, BigDecimal.ZERO).returningResult(PRODUCT.MSRP))
                .selectFrom(name("cte"))
                .fetch();

        // same as previously but using CommonTableExpression
        CommonTableExpression<Record1<BigDecimal>> cte1 = name("cte").fields("msrp").as(
                update(PRODUCT).set(PRODUCT.MSRP, BigDecimal.ZERO).returningResult(PRODUCT.MSRP));

        List<BigDecimal> msrps = ctx.fetchValues(with(cte1).selectFrom(cte1));
        System.out.println("MSRPs: " + msrps);

        // EXAMPLE 2 (INSERT)
        ctx.with("cte", "sale_id")
                .as(insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1250.55, 1504L, 1, 0.0)
                        .returningResult(SALE.SALE_ID))
                .select(field(name("sale_id"))).from(name("cte"))
                .fetch();

        // same as previously but using selectFrom()
        ctx.with("cte", "sale_id")
                .as(insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1250.55, 1504L, 1, 0.0)
                        .returningResult(SALE.SALE_ID))
                .selectFrom(name("cte"))
                .fetch();

        // same as previously but using CommonTableExpression
        CommonTableExpression<Record1<Long>> cte2 = name("cte").fields("sale_id").as(
                insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1250.55, 1504L, 1, 0.0)
                        .returningResult(SALE.SALE_ID));

        long saleId = ctx.fetchValue(with(cte2).selectFrom(cte2));
        System.out.println("SALE ID: " + saleId);

        // EXAMPLE 3 (DELETE)
        ctx.with("cte", "sale_id", "sale")
                .as(deleteFrom(SALE)
                        .where(SALE.SALE_ID.gt(1000000L))
                        .returningResult(SALE.SALE_ID, SALE.SALE_))
                .select(field(name("sale_id")), field(name("sale"))).from(name("cte"))
                .fetch();
    }

    public void cte2() {

        ctx.with("cte", "sale_id", "sale")
                .as(insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1250.55, 1504L, 1, 0.0)
                        .returningResult(SALE.SALE_ID, SALE.SALE_))
                .insertInto(TOKEN, TOKEN.SALE_ID, TOKEN.AMOUNT)
                .select(select(field(name("sale_id"), Long.class),
                        field(name("sale"), Double.class)).from(name("cte")))
                .execute();

        ctx.with("cte1", "employee_number", "commission")
                .as(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.COMMISSION)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.COMMISSION.gt(0)))
                .with("cte2", "sale_id", "sale")
                .as(insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .select(select(val(2008), field(name("commission"), Double.class),
                                field(name("employee_number"), Long.class), val(1), val(0.0))
                                .from(name("cte1")))
                        .returningResult(SALE.SALE_ID, SALE.SALE_))
                .insertInto(TOKEN, TOKEN.SALE_ID, TOKEN.AMOUNT)
                .select(select(field(name("sale_id"), Long.class),
                        field(name("sale"), Double.class)).from(name("cte2")))
                .execute();
    }

    public void cte3() {

        ctx.with("cte", "employee_number")
                .as(update(SALE).set(SALE.REVENUE_GROWTH, 0.0)
                        .where(SALE.EMPLOYEE_NUMBER.in(
                                select(EMPLOYEE.EMPLOYEE_NUMBER)
                                        .from(EMPLOYEE)
                                        .where(EMPLOYEE.COMMISSION.isNull())))
                        .returningResult(SALE.EMPLOYEE_NUMBER))
                .insertInto(EMPLOYEE_STATUS, EMPLOYEE_STATUS.EMPLOYEE_NUMBER,
                        EMPLOYEE_STATUS.STATUS, EMPLOYEE_STATUS.ACQUIRED_DATE)
                .select(select(field(name("employee_number"), Long.class), val("REGULAR"),
                        val(LocalDate.now())).from(name("cte")))
                .execute();
    }
}
