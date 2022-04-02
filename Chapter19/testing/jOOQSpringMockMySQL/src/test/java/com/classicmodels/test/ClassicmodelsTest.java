package com.classicmodels.test;

import com.classicmodels.test.mock.provider.ClassicmodelsMockProvider;
import java.time.LocalDate;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClassicmodelsTest {

    public static DSLContext ctx;

    @BeforeAll
    public static void setup() {
        
        // Initialise your data provider
        MockDataProvider provider = new ClassicmodelsMockProvider();
        MockConnection connection = new MockConnection(provider);

        // Pass the mock connection to a jOOQ DSLContext
        ClassicmodelsTest.ctx = DSL.using(connection, SQLDialect.MYSQL);

        // Optionally, you may want to disable jOOQ logging
        ClassicmodelsTest.ctx.configuration().settings()
                .withExecuteLogging(Boolean.FALSE);
    }

    @Test
    public void givenSelectProductWhenFetchByIdThenResultOneRecord() {

        Result<Record2<Long, String>> result
                = ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(1L))
                        .fetch();

        assertThat(result, hasSize(equalTo(1)));
        assertThat(result.getValue(0, PRODUCT.PRODUCT_ID), is(equalTo(1L)));
        assertThat(result.getValue(0, PRODUCT.PRODUCT_NAME), is(equalTo("2002 Suzuki XREO")));
    }

    @Test
    public void givenSelectProductWhenFetchLimit3ThenResult3Record() {

        Result<Record3<Long, String, Integer>> result
                = ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.QUANTITY_IN_STOCK.gt(5000))
                        .limit(3)
                        .fetch();

        assertThat(result, hasSize(equalTo(3)));
        assertThat(result.getValue(0, PRODUCT.QUANTITY_IN_STOCK), is(greaterThan(5000)));
        assertThat(result.getValue(1, PRODUCT.QUANTITY_IN_STOCK), is(greaterThan(5000)));
        assertThat(result.getValue(2, PRODUCT.QUANTITY_IN_STOCK), is(greaterThan(5000)));
    }

    @Test
    public void givenUpdateSelectsWhenFetchManyThenTwoResultsOneRecordEach() {

        Results results = ctx.resultQuery(
                "update employee set employee.job_title='Sales Manager (NA)' where employee.employee_number={0};"
                + "select employee.job_title from employee where employee.employee_number={0};"
                + "select office.city from office where office.office_code={1}", 1370L, "1"
        ).fetchMany();

        assertThat(results, hasSize(equalTo(2)));
        assertThat(results.get(0).getValue(0, "job_title"), is(equalTo("Sales Manager (NA)")));
        assertThat(results.get(1).getValue(0, "city"), is(equalTo("San Francisco")));
    }

    @Test
    public void givenSelectWithBindingsWhenCorrectThenOneResultOneRecordEach() {

        Result<Record2<Long, String>> result1
                = ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.STATUS)
                        .from(ORDER)
                        .where(ORDER.REQUIRED_DATE.between(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1))
                                .and(ORDER.ORDER_ID.eq(10105L)))
                        .fetch();

        Result<Record2<Long, String>> result2
                = ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.STATUS)
                        .from(ORDER)
                        .where(ORDER.REQUIRED_DATE.between(LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1))
                                .and(ORDER.ORDER_ID.eq(10105L)))
                        .fetch();

        assertThat(result2, hasSize(equalTo(1)));
        assertThat(result2.getValue(0, "customer_number"), is(equalTo(145L)));
        assertThat(result2.getValue(0, "status"), is(equalTo("In Process")));
    }

    @Test
    public void givenSelectWithBindingsWhenWrongThenException() {

        Throwable ex = assertThrows(org.jooq.exception.DataAccessException.class, () -> {
            ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.STATUS)
                    .from(ORDER)
                    .where(ORDER.REQUIRED_DATE.between(LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(2))
                            .and(ORDER.ORDER_ID.eq(10105L)))
                    .fetch();
        });

        assertThat(ex.getCause().getMessage(), startsWith("Statement has improper bind values"));
    }

    @Test
    public void givenUpdateWithBindingsWhenCorrectThenAffected() {

        int affected = ctx.update(SALE).set(SALE.FISCAL_YEAR, 2004).where(SALE.FISCAL_YEAR.gt(2003))
                .execute();

        assertThat(affected, is(equalTo(250)));
    }

    @Test
    public void givenBatchWhenCorrectThenAffected() {

        int[] affected = ctx.batch(
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(1L, 2005, 1370L, 1282.64, 1, 15.55),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(10L, 2004, 1370L, 3938.24, 2, 22.33),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(40L, 2006, 1370L, 3923.24, 1, 12.55),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(100L, 2004, 1370L, 4676.14, 3, 10.2),
                ctx.delete(SALE).where(SALE.SALE_.lt(500.0))
        ).execute();

        assertThat(affected.length, is(equalTo(5)));
        assertThat(affected[0], is(equalTo(1)));
        assertThat(affected[1], is(equalTo(1)));
        assertThat(affected[2], is(equalTo(1)));
        assertThat(affected[3], is(equalTo(1)));
        assertThat(affected[4], is(equalTo(10)));
    }
}