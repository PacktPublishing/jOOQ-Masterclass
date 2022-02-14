package com.classicmodels.repository;

import com.classicmodels.pojo.NamePhone;
import com.classicmodels.pojo.PhoneCreditLimit;
import com.classicmodels.pojo.SalarySale;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Function2;
import static org.jooq.Functions.nullOnAnyNull;
import org.jooq.Record;
import org.jooq.Record2;
import static org.jooq.Records.intoArray;
import static org.jooq.Records.intoList;
import static org.jooq.Records.intoMap;
import static org.jooq.Records.mapping;
import org.jooq.ResultQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void collectPlainSQL() {

        List<Record> result1 = ctx.fetch("SELECT customer_name, phone FROM customer")
                .collect(toList());
        System.out.println("Example 1.1\n" + result1);

        List<String> result2 = ctx.fetch("SELECT customer_name FROM customer")
                .collect(intoList(r -> r.get(0, String.class)));
        System.out.println("Example 1.2\n" + result2);

        Map<Long, Record> result3 = ctx.fetch("SELECT customer_number, phone FROM customer")
                .collect(intoMap(r -> r.get(0, Long.class)));
        System.out.println("Example 1.3\n" + result3);

        Map<Long, String> result4 = ctx.fetch("SELECT customer_number, phone FROM customer")
                .collect(intoMap(r -> r.get(0, Long.class), r -> r.get(0, String.class)));
        System.out.println("Example 1.4\n" + result4);

        String[] result5 = ctx.fetch("SELECT customer_name FROM customer")
                .collect(intoArray(new String[0], r -> r.get(0, String.class)));
        System.out.println("Example 1.5\n" + Arrays.toString(result5));

        ResultQuery<Record> resultQuery1 = ctx.resultQuery("SELECT customer_name, phone FROM customer");
        ResultQuery<Record> resultQuery2 = ctx.resultQuery("SELECT customer_name FROM customer");
        ResultQuery<Record> resultQuery3 = ctx.resultQuery("SELECT customer_number, phone FROM customer");

        List<Record> result6 = resultQuery1.collect(toList());
        System.out.println("Example 1.6\n" + result6);

        List<String> result7 = resultQuery2.collect(intoList(r -> r.get(0, String.class)));
        System.out.println("Example 1.7\n" + result7);

        Map<Long, Record> result8 = resultQuery3.collect(intoMap(r -> r.get(0, Long.class)));
        System.out.println("Example 1.8\n" + result8);

        Map<Long, String> result9 = resultQuery3.collect(intoMap(r -> r.get(0, Long.class), r -> r.get(0, String.class)));
        System.out.println("Example 1.9\n" + result9);

        String[] result10 = resultQuery2.collect(intoArray(new String[0], r -> r.get(0, String.class)));
        System.out.println("Example 1.10\n" + Arrays.toString(result10));
    }

    public void collectJavaSchema() {

        List<Record2<String, String>> result1 = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE)
                .from(CUSTOMER)
                .collect(toList());
        System.out.println("Example 2.1\n" + result1);

        List<String> result2 = ctx.select(CUSTOMER.CUSTOMER_NAME)
                .from(CUSTOMER)
                .collect(intoList()); // or, intoList(r -> r.get(CUSTOMER.CUSTOMER_NAME))
        System.out.println("Example 2.2\n" + result2);

        Map<Long, String> result3 = ctx.select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.PHONE)
                .from(CUSTOMER)
                .collect(intoMap());
        System.out.println("Example 2.3\n" + result3);

        String[] result4 = ctx.select(CUSTOMER.CUSTOMER_NAME)
                .from(CUSTOMER)
                .collect(intoArray(new String[0]));
        System.out.println("Example 2.4\n" + Arrays.toString(result4));

        // Mapping (POJO)
        List<NamePhone> result5 = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE)
                .from(CUSTOMER)
                .fetch(mapping(NamePhone::new));
        System.out.println("Example 2.5\n" + result5);

        // jOOQ defines Function[N], N = {1 ... 22}
        Function2<String, String, NamePhone> function = (k, v) -> new NamePhone(k, v);
        List<NamePhone> result6 = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE)
                .from(CUSTOMER)
                .fetch(mapping(function));
        System.out.println("Example 2.6\n" + result6);

        // Mapping (Java 16 Record)
        List<PhoneCreditLimit> result7 = ctx.select(CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .fetch(mapping(PhoneCreditLimit::new));
        System.out.println("Example 2.7\n" + result7);

        // null safety when mapping nested rows from LEFT JOIN and so on (there is also nullOnAllNull())                
        List<SalarySale> result8 = ctx.select(EMPLOYEE.SALARY, SALE.SALE_)
                .from(EMPLOYEE)
                .leftJoin(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .fetch(mapping(SalarySale::new));
        System.out.println("Example 2.8: " + result8);

        List<SalarySale> result9 = ctx.select(EMPLOYEE.SALARY, SALE.SALE_)
                .from(EMPLOYEE)
                .leftJoin(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .fetch(mapping(nullOnAnyNull(SalarySale::new)));
        System.out.println("Example 2.9: " + result9);
    }
}
