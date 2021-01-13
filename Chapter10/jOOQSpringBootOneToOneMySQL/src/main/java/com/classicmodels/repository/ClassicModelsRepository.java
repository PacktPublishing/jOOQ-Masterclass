package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.pojo.SimpleCustomerDetail;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.jooq.Record;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchOneToOne() {

        // approach 1 (use the proper aliases)
        List<SimpleCustomer> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("details.addressLineFirst"),
                CUSTOMERDETAIL.STATE.as("details.state"), CUSTOMERDETAIL.CITY.as("details.city"))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchInto(SimpleCustomer.class);
        System.out.println("Example 1\n" + result1);

        // approach 2 (use map())
        List<SimpleCustomer> result2 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetch()
                .map(rs -> new SimpleCustomer(
                rs.getValue(CUSTOMER.CUSTOMER_NAME),
                rs.getValue(CUSTOMER.PHONE),
                rs.getValue(CUSTOMER.CREDIT_LIMIT).floatValue(),
                new SimpleCustomerDetail(
                        rs.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        rs.getValue(CUSTOMERDETAIL.STATE),
                        rs.getValue(CUSTOMERDETAIL.CITY))));
        System.out.println("Example 2\n" + result2);

        // approach 3 (shortcut of approach 2)
        List<SimpleCustomer> result3 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetch(rs -> new SimpleCustomer(
                rs.getValue(CUSTOMER.CUSTOMER_NAME),
                rs.getValue(CUSTOMER.PHONE),
                rs.getValue(CUSTOMER.CREDIT_LIMIT).floatValue(),
                new SimpleCustomerDetail(
                        rs.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        rs.getValue(CUSTOMERDETAIL.STATE),
                        rs.getValue(CUSTOMERDETAIL.CITY))));
        System.out.println("Example 3\n" + result3);

        // approach 4 (stream Map<Record, Record>)
        Map<Record, Record> map = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchMap(new Field[]{CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT},
                new Field[]{CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY});

        List<SimpleCustomer> result4 = map.entrySet()
                .stream()
                .map((e) -> {
                    SimpleCustomer customer = e.getKey().into(SimpleCustomer.class);
                    customer.setDetails(e.getValue().into(SimpleCustomerDetail.class));

                    return customer;
                }).collect(Collectors.toList());
        System.out.println("Example 4\n" + result4);
        
        // approach 5 (map from ResultSet)
        ResultSet rs = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchResultSet();

        List<SimpleCustomer> result5 = new ArrayList<>();
        try {            
            while (rs.next()) {
                result5.add(new SimpleCustomer(
                        rs.getString("customer_name"), rs.getString("phone"), rs.getFloat("credit_limit"),
                        new SimpleCustomerDetail(rs.getString("address_line_first"),
                                rs.getString("state"), rs.getString("city")))
                        
                );
            }
        } catch (SQLException ex) {
            // handle exception
        }
        System.out.println("Example 5\n" + result5);

    }
}