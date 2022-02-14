package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import com.classicmodels.pojo.NamePhone;
import com.classicmodels.pojo.PhoneCreditLimit;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.jooq.ResultQuery;
import org.jooq.Record;
import static org.jooq.Records.intoList;
import static org.jooq.impl.DSL.currentTimestamp;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // DSLContext fetching    
    public void fetchPlainSQL() {

        // Fetch via plain SQL
        System.out.println("Example 1.1\n"
                + ctx.fetch("SELECT customer_name FROM customer") // Result<Record>
        // + ctx.fetch("SELECT customer_name FROM customer").into(String.class) // List<String>
        );

        // using Records utility
        List<Record> result1 = ctx.fetch("SELECT customer_name, phone FROM customer")
                .collect(toList());
        System.out.println("Example 1.1.1\n" + result1);

        List<String> result2 = ctx.fetch("SELECT customer_name FROM customer")
                .collect(intoList(r -> r.get(0, String.class)));
        System.out.println("Example 1.1.2\n" + result2);

        // Fetch from ResultQuery
        System.out.println("Example 1.2\n"
                + ctx.resultQuery("SELECT customer_name FROM customer").fetch() // Result<Record>
        // + ctx.resultQuery("SELECT customer_name FROM customer").fetchInto(String.class) // List<String>
        );

        // using Records utility
        List<String> result3 = ctx.resultQuery("SELECT customer_name FROM customer")
                .collect(intoList(r -> r.get(0, String.class)));
        System.out.println("Example 1.2.1\n" + result3);

        ResultQuery<Record> resultQuery = ctx.resultQuery("SELECT customer_name FROM customer");

        System.out.println("Example 1.3\n"
                + resultQuery.fetch() // Result<Record>
        // + resultQuery.fetch().into(String.class) // List<String>
        );

        System.out.println("Example 1.4\n"
                + ctx.fetch(resultQuery) // Result<Record>
        // + ctx.fetch(resultQuery).into(String.class) // List<String>
        );

        // using Records utility
        List<String> result4 = resultQuery.collect(intoList(r -> r.get(0, String.class)));
        System.out.println("Example 1.4.1\n" + result4);

        List<String> result5 = ctx.fetch(resultQuery).collect(intoList(r -> r.get(0, String.class)));
        System.out.println("Example 1.4.2\n" + result5);

        System.out.println("Example 1.5\n"
                + ctx.fetch(CUSTOMER,
                        CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.ZERO),
                        CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.in(1370L, 1504L, 1611L))
        );
    }

    public void fetchFewFields() {

        Timestamp ts = ctx.fetchValue(currentTimestamp());
        System.out.println("Example 2.1.1\n" + ts);

        // Prefer
        System.out.println("Example 2.1.2\n"
                + ctx.select(CUSTOMER.CUSTOMER_NAME)
                        .from(CUSTOMER).fetch(CUSTOMER.CUSTOMER_NAME) // List<String>                        

        // or, like this
        // + ctx.fetchValues(CUSTOMER.CUSTOMER_NAME) // List<String>

        // or, like this
        // + ctx.select(CUSTOMER.CUSTOMER_NAME)
        //                .from(CUSTOMER).fetchInto(String.class) // List<String>      

        // or, like this
        // + ctx.select(CUSTOMER.CUSTOMER_NAME)
        //      .from(CUSTOMER).fetch();          // Result<Record1<String>>
        );

        // Also prefer using Records utility               
        List<String> result = ctx.select(CUSTOMER.CUSTOMER_NAME)
                .from(CUSTOMER).collect(intoList()); 
        System.out.println("Example 2.1.3\n" + result);

        // Avoid
        System.out.println("Example 2.2\n"
                + ctx.select().from(CUSTOMER)
                        .fetch(CUSTOMER.CUSTOMER_NAME) // List<String>                
        // .fetch(field(""), String.class)
        // .fetch("", String.class)
        // .fetch(0)

        // also avoid
        //      + ctx.selectFrom(CUSTOMER)
        //              .fetch(CUSTOMER.CUSTOMER_NAME) // List<String>
        );

        // The above case applies to multiple fields as well        
        // Prefer
        System.out.println("Example 2.3\n"
                + ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT).from(CUSTOMER)
                        .fetch() // Result<Record2<String, BigDecimal>>                
        );

        // Avoid
        System.out.println("Example 2.4\n"
                + ctx.select().from(CUSTOMER)
                        .fetch()
                        .into(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT) // Result<Record2<String, BigDecimal>>
        // .into(field("customer_name", String.class), field("credit_limit", BigDecimal.class))                                        
        );
    }

    public void avoidExtraSelectsWithPojos() {

        // Avoid two SELECTs        
        List<NamePhone> result1 = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE)
                .from(CUSTOMER).fetchInto(NamePhone.class);
        System.out.println("Example 3.1\n" + result1);

        List<PhoneCreditLimit> result2 = ctx.select(CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER).fetchInto(PhoneCreditLimit.class);
        System.out.println("Example 3.2\n" + result2);

        // Prefer one SELECT and map the result as you want
        var result = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER).fetch(); // Result<Record3<String, String, BigDecimal>>

        List<NamePhone> fromResult1 = result.into(NamePhone.class);
        List<PhoneCreditLimit> fromResult2 = result.into(PhoneCreditLimit.class);

        System.out.println("Example 3.3\n" + fromResult1);
        System.out.println("Example 3.4\n" + fromResult2);
        
        // Or, via map()
        List<NamePhone> fromResult3 = result.map(r -> new NamePhone(r.value1(), r.value2()));
        List<PhoneCreditLimit> fromResult4 = result.map(r -> new PhoneCreditLimit(r.value2(), r.value3()));
        
        System.out.println("Example 3.5\n" + fromResult3);
        System.out.println("Example 3.6\n" + fromResult4);
    }

    public void avoidExtraSelectsWithArbitraryTypes() {

        // Avoid two SELECTs        
        String result1 = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE)
                .from(CUSTOMER).fetch().formatJSON();
        System.out.println("Example 4.1\n" + result1);

        Set<BigDecimal> result2 = ctx.select(CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER).fetchSet(CUSTOMER.CREDIT_LIMIT);
        System.out.println("Example 4.2\n" + result2);

        // Prefer one SELECT and map the result as you want
        var result = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER).fetch(); // Result<Record3<String, String, BigDecimal>>

        String fromResult1 = result.into(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE).formatJSON();
        Set<BigDecimal> fromResult2 = result.intoSet(CUSTOMER.CREDIT_LIMIT);

        System.out.println("Example 4.3\n" + fromResult1);
        System.out.println("Example 4.4\n" + fromResult2);
    }

    public void fetchAndConvert() {

        List<YearMonth> result = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.FIRST_BUY_DATE.isNotNull())
                .fetch(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);

        System.out.println("Example 5\n" + result);
    }
}
