package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import javax.sql.DataSource;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.jooq.ResultQuery;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DataSource ds) {
        this.ctx = ctx;
    }

    // DSLContext fetching    
    public void fetchPlainSQL() {

        // Fetch via plain SQL
        System.out.println("Example 1.1\n"
                + ctx.fetch("SELECT customer_name FROM customer") // Result<Record>
        // + ctx.fetch("SELECT customer_name FROM customer").into(String.class) // List<String>
        );

        // Fetch from ResultQuery
        System.out.println("Example 1.2\n"
                + ctx.resultQuery("SELECT customer_name FROM customer").fetch() // Result<Record>
        // + ctx.resultQuery("SELECT customer_name FROM customer").fetchInto(String.class) // List<String>
        );

        ResultQuery<Record> resultQuery = ctx.resultQuery("SELECT customer_name FROM customer");

        System.out.println("Example 1.3\n"
                + resultQuery.fetch() // Result<Record>
        // + resultQuery.fetch().into(String.class) // List<String>
        );

        System.out.println("Example 1.4\n"
                + ctx.fetch(resultQuery) // Result<Record>
        // + ctx.fetch(resultQuery).into(String.class) // List<String>
        );

        System.out.println("Example 1.5\n"
                + ctx.fetch(CUSTOMER,
                        CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.ZERO),
                        CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.in(1370L, 1504L, 1611L))
        );
    }

    @Transactional(readOnly = true)
    public void fetchFewFields() {

        // Prefer
        System.out.println("Example 2.1\n"
                + ctx.select(CUSTOMER.CUSTOMER_NAME)
                        .from(CUSTOMER).fetch(CUSTOMER.CUSTOMER_NAME) // List<String>
        // or, like this
        // + ctx.fetchValues(CUSTOMER.CUSTOMER_NAME) // List<String>
        );

        // Avoid
        System.out.println("Example 2.2\n"
                + ctx.select().from(CUSTOMER)
                        .fetch(CUSTOMER.CUSTOMER_NAME) // List<String>                
        // .fetch(field(""), String.class)
        // .fetch("", String.class)
        // .fetch(0)
        );

        // The above case applies to multiple fields as well        
        // Prefer
        System.out.println("Example 2.3\n"
                + ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT).from(CUSTOMER)
                        .fetch()
                        .into(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT) // Result<Record2<String, BigDecimal>>                
        );

        // Avoid
        System.out.println("Example 2.4\n"
                + ctx.select().from(CUSTOMER)
                        .fetch()
                        .into(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT) // Result<Record2<String, BigDecimal>>
        // .into(field("customer_name", String.class), field("credit_limit", BigDecimal.class))                                        
        );
    }

    public void fetchAndConvert() {

        List<YearMonth> result = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.FIRST_BUY_DATE.isNotNull())
                .fetch(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);

        System.out.println("Example 3\n" + result);
    }
}
