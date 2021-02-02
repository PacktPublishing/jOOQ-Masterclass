package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleBCustomer;
import com.classicmodels.pojo.SimpleBCustomerDetail;
import com.classicmodels.pojo.SimpleUCustomer;
import com.classicmodels.pojo.SimpleUCustomerDetail;
import java.math.BigDecimal;
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
import org.jooq.Record6;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchOneToOne() {

        // approach 1 (use the proper aliases)
        // unidirectional one-to-one (SimpleUCustomer has SimpleUCustomerDetail)
        List<SimpleUCustomer> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("detail.addressLineFirst"),
                CUSTOMERDETAIL.STATE.as("detail.state"), CUSTOMERDETAIL.CITY.as("detail.city"))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchInto(SimpleUCustomer.class);
        System.out.println("\nExample 1:");
        result1.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        
        // unidirectional one-to-one (SimpleBCustomer has SimpleBCustomerDetail)
        List<SimpleBCustomer> result21 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("detail.addressLineFirst"),
                CUSTOMERDETAIL.STATE.as("detail.state"), CUSTOMERDETAIL.CITY.as("detail.city"))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchInto(SimpleBCustomer.class);        
        
        // unidirectional one-to-one (SimpleBCustomerDetail has SimpleBCustomer)
        List<SimpleBCustomerDetail> result22 = ctx.select(
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY,
                CUSTOMER.CUSTOMER_NAME.as("customer.customerName"), 
                CUSTOMER.PHONE.as("customer.phone"), CUSTOMER.CREDIT_LIMIT.as("customer.creditLimit"))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchInto(SimpleBCustomerDetail.class);
        System.out.println("\nExample 2:");
        result21.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        result22.forEach(e -> {System.out.println(e + " => " + e.getCustomer()); });                        
                
        // approach 2 (use RecordMapper)
        // unidirectional one-to-one (SimpleUCustomer has SimpleUCustomerDetail)
        List<SimpleUCustomer> result31 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetch((Record6<String, String, BigDecimal, String, String, String> record) -> new SimpleUCustomer(
                        record.getValue(CUSTOMER.CUSTOMER_NAME),
                        record.getValue(CUSTOMER.PHONE),
                        record.getValue(CUSTOMER.CREDIT_LIMIT).floatValue(),
                        new SimpleUCustomerDetail(
                                record.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                record.getValue(CUSTOMERDETAIL.STATE),
                                record.getValue(CUSTOMERDETAIL.CITY))));
        System.out.println("\nExample 3.1:");
        result31.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        
        // bidirectional one-to-one (SimpleBCustomer <-> SimpleBCustomerDetail)
        List<SimpleBCustomer> result32 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetch((Record6<String, String, BigDecimal, String, String, String> record) -> {
                    
                    SimpleBCustomer customer = new SimpleBCustomer(
                        record.getValue(CUSTOMER.CUSTOMER_NAME),
                        record.getValue(CUSTOMER.PHONE),
                        record.getValue(CUSTOMER.CREDIT_LIMIT).floatValue());
                    
                    SimpleBCustomerDetail detail =new SimpleBCustomerDetail(
                        record.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        record.getValue(CUSTOMERDETAIL.STATE),
                        record.getValue(CUSTOMERDETAIL.CITY));
                    
                    customer.setDetail(detail);
                    detail.setCustomer(customer);
                       
                    return customer;
                });
        System.out.println("\nExample 3.2:");
        result32.forEach(e -> {System.out.println(e + " => " + e.getDetail() + " => " + e.getDetail().getCustomer()); });                        
               
        // approach 3 (use map(RecordMapper))
        // unidirectional one-to-one (SimpleUCustomer has SimpleUCustomerDetail)
        List<SimpleUCustomer> result41 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetch()
                .map(rs -> new SimpleUCustomer(
                rs.getValue(CUSTOMER.CUSTOMER_NAME),
                rs.getValue(CUSTOMER.PHONE),
                rs.getValue(CUSTOMER.CREDIT_LIMIT).floatValue(),
                new SimpleUCustomerDetail(
                        rs.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        rs.getValue(CUSTOMERDETAIL.STATE),
                        rs.getValue(CUSTOMERDETAIL.CITY))));
        System.out.println("\nExample 4.1:");
        result41.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        
        // bidirectional one-to-one (SimpleBCustomer <-> SimpleBCustomerDetail)
        List<SimpleBCustomer> result42 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetch()
                .map(rs -> {                    
                    SimpleBCustomer customer = new SimpleBCustomer(
                        rs.getValue(CUSTOMER.CUSTOMER_NAME),
                        rs.getValue(CUSTOMER.PHONE),
                        rs.getValue(CUSTOMER.CREDIT_LIMIT).floatValue());
                    
                    SimpleBCustomerDetail detail =new SimpleBCustomerDetail(
                        rs.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        rs.getValue(CUSTOMERDETAIL.STATE),
                        rs.getValue(CUSTOMERDETAIL.CITY));
                    
                    customer.setDetail(detail);
                    detail.setCustomer(customer);
                       
                    return customer;
                });
        System.out.println("\nExample 4.2:");
        result42.forEach(e -> {System.out.println(e + " => " + e.getDetail() + " => " + e.getDetail().getCustomer()); });                        

        // approach 4 (shortcut of approach 3)
        // unidirectional one-to-one (SimpleUCustomer has SimpleUCustomerDetail)
        List<SimpleUCustomer> result51 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetch(rs -> new SimpleUCustomer(
                rs.getValue(CUSTOMER.CUSTOMER_NAME),
                rs.getValue(CUSTOMER.PHONE),
                rs.getValue(CUSTOMER.CREDIT_LIMIT).floatValue(),
                new SimpleUCustomerDetail(
                        rs.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        rs.getValue(CUSTOMERDETAIL.STATE),
                        rs.getValue(CUSTOMERDETAIL.CITY))));
        System.out.println("\nExample 5.1:");
        result51.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        
        // bidirectional one-to-one (SimpleBCustomer <-> SimpleBCustomerDetail)
        List<SimpleBCustomer> result52 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetch(rs -> {                    
                    SimpleBCustomer customer = new SimpleBCustomer(
                        rs.getValue(CUSTOMER.CUSTOMER_NAME),
                        rs.getValue(CUSTOMER.PHONE),
                        rs.getValue(CUSTOMER.CREDIT_LIMIT).floatValue());
                    
                    SimpleBCustomerDetail detail =new SimpleBCustomerDetail(
                        rs.getValue(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                        rs.getValue(CUSTOMERDETAIL.STATE),
                        rs.getValue(CUSTOMERDETAIL.CITY));
                    
                    customer.setDetail(detail);
                    detail.setCustomer(customer);
                       
                    return customer;
                });
        System.out.println("\nExample 5.2:");
        result52.forEach(e -> {System.out.println(e + " => " + e.getDetail() + " => " + e.getDetail().getCustomer()); });                        
 
        // approach 5 (stream Map<Record, Record>)
        // unidirectional one-to-one (SimpleUCustomer has SimpleUCustomerDetail)
        Map<Record, Record> map61 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchMap(new Field[]{CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT},
                new Field[]{CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY});

        List<SimpleUCustomer> result61 = map61.entrySet()
                .stream()
                .map((e) -> {
                    SimpleUCustomer customer = e.getKey().into(SimpleUCustomer.class);
                    customer.setDetail(e.getValue().into(SimpleUCustomerDetail.class));

                    return customer;
                }).collect(Collectors.toList());
        System.out.println("\nExample 6.1:");
        result61.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        
        // bidirectional one-to-one (SimpleBCustomer <-> SimpleBCustomerDetail)
        Map<Record, Record> map62 = ctx.select( // is the same as map61
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchMap(new Field[]{CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT},
                new Field[]{CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY});

        List<SimpleBCustomer> result62 = map62.entrySet()
                .stream()
                .map((e) -> {
                    SimpleBCustomer customer = e.getKey().into(SimpleBCustomer.class);
                    SimpleBCustomerDetail detail = e.getValue().into(SimpleBCustomerDetail.class);
                    
                    customer.setDetail(detail);
                    detail.setCustomer(customer);

                    return customer;
                }).collect(Collectors.toList());
        System.out.println("\nExample 6.2:");
        result62.forEach(e -> {System.out.println(e + " => " + e.getDetail() + " => " + e.getDetail().getCustomer()); });                                                
        
        // approach 6 (map from ResultSet)
        // unidirectional one-to-one (SimpleUCustomer has SimpleUCustomerDetail)
        ResultSet rs71 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchResultSet();

        List<SimpleUCustomer> result71 = new ArrayList<>();
        try (rs71) {
            while (rs71.next()) {
                result71.add(new SimpleUCustomer(
                        rs71.getString("customer_name"), rs71.getString("phone"), rs71.getFloat("credit_limit"),
                        new SimpleUCustomerDetail(rs71.getString("address_line_first"),
                                rs71.getString("state"), rs71.getString("city")))
                );
            }
        } catch (SQLException ex) {
            // handle exception
        }
        System.out.println("\nExample 7.1:");
        result71.forEach(e -> {System.out.println(e + " => " + e.getDetail()); });                        
        
        // bidirectional one-to-one (SimpleBCustomer <-> SimpleBCustomerDetail)
        ResultSet rs72 = ctx.select( // the same as rs71
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .limit(3)
                .fetchResultSet();

        List<SimpleBCustomer> result72 = new ArrayList<>();
        try (rs72) {
            while (rs72.next()) {
                SimpleBCustomer customer = new SimpleBCustomer(
                        rs72.getString("customer_name"), rs72.getString("phone"), rs72.getFloat("credit_limit"));
                SimpleBCustomerDetail detail = new SimpleBCustomerDetail(rs72.getString("address_line_first"),
                                rs72.getString("state"), rs72.getString("city"));
                
                customer.setDetail(detail);
                detail.setCustomer(customer);
                
                result72.add(customer);
            }
        } catch (SQLException ex) {
            // handle exception
        }
        System.out.println("\nExample 7.2:");
        result72.forEach(e -> {System.out.println(e + " => " + e.getDetail() + " => " + e.getDetail().getCustomer()); });                                                
    }
}
