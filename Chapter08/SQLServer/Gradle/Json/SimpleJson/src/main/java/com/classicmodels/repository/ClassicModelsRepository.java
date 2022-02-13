package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONEntry;
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonTable;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.DATE;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NVARCHAR;
import static org.jooq.impl.SQLDataType.VARCHAR;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchSimpleJson() {

        // simple example of using jsonObject()
        Result<Record1<JSON>> result11 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT)).as("json_result"))
                .from(CUSTOMER)                
                .fetch();
        System.out.println("Example 1.1.1.a:\n" + result11);
        System.out.println("Example 1.1.1.b:\n" + result11.get(0).value1().data()); 
        System.out.println("Example 1.1.1.c:\n" + result11.formatJSON()); // or, formatJSON(JSONFormat.DEFAULT_FOR_RESULTS)
        System.out.println("Example 1.1.1.d:\n" + result11.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS));
        
        List<String> result12 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT)).as("json_result"))
                .from(CUSTOMER)       
                .orderBy(CUSTOMER.CUSTOMER_NAME).limit(3)
                .fetchInto(String.class);
        System.out.println("Example 1.1.2:\n" + result12);
        
        Result<Record1<JSON>> result13 = ctx.select(jsonObject(
                jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME),
                jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT)).as("json_result"))
                .from(CUSTOMER)                
                .fetch();
        System.out.println("Example 1.1.3.a:\n" + result13);
        System.out.println("Example 1.1.3.b:\n" + result13.formatJSON());
        
        List<String> result14 = ctx.select(jsonObject(
                jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME),
                jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT)).as("json_result"))
                .from(CUSTOMER)                
                .fetchInto(String.class);
        System.out.println("Example 1.1.4:\n" + result14);

        // simple example of using jsonEntry()
        JSONEntry customerNameEntry = jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME);
        JSONEntry creditLimitEntry = jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT);

        Result<Record1<JSON>> result6 = ctx.select(jsonObject(
                customerNameEntry, creditLimitEntry).as("json_result"))
                .from(CUSTOMER)
                .where(creditLimitEntry.value().gt(100000))
                .fetch();
        System.out.println("Example 1.2:\n" + result6);
    }

    // if you don't want to use coerce() then consider a converter as in the previous chapter 
    public void fetchJsonValue() {

        Result<Record1<JSON>> result21 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.email").as("email"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.1.1:\n" + result21);
        
        List<String> result22 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.email").as("email"))
                .from(MANAGER)
                .fetchInto(String.class);
        System.out.println("Example 2.1.2:\n" + result22);

        Result<Record1<JSON>> result2 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.address.city").as("city"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.2:\n" + result2);

        Result<Record1<JSON>> result3 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.phoneNumber[0].number[0]").as("firstHomePhone"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.3:\n" + result3);

        Result<Record1<JSON>> result4 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.email").as("email"))
                .from(MANAGER)
                .where(jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.projects[0].role").cast(String.class)
                        .contains("Principal Manager"))
                .fetch();
        System.out.println("Example 2.4:\n" + result4);

        Result<Record1<JSON>> result5 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.email").as("email"))
                .from(MANAGER)
                .where(jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.projects[0].role")
                        .like("%Principal Manager%"))
                .fetch();
        System.out.println("Example 2.5:\n" + result5);               
    }

    public void fetchForJson() {

        Result<Record1<JSON>> result1 = ctx.select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().path()
                .fetch();
        System.out.println("Example 3.1:\n" + result1.formatJSON());

        Result<Record1<JSON>> result2 = ctx.select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                CUSTOMER.CONTACT_LAST_NAME).as("name"), CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().path()
                .fetch();
        System.out.println("Example 3.2:\n" + result2.formatJSON());

        Result<Record1<JSON>> result3 = ctx.select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                CUSTOMER.CONTACT_LAST_NAME).as("name"), CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().path().root()
                .fetch();
        System.out.println("Example 3.3:\n" + result3.formatJSON());

        Result<Record1<JSON>> result4 = ctx.select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                CUSTOMER.CONTACT_LAST_NAME).as("name"), CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().path().root("customers")
                .fetch();
        System.out.println("Example 3.4:\n" + result4.formatJSON());

        // auto() -> format the output automatically based on the structure of the SELECT statement
        Result<Record1<JSON>> result5 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE)
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .forJSON().auto().root("customers")
                .fetch();
        System.out.println("Example 3.5:\n" + result5.formatJSON());
        
        // auto() -> format the output automatically based on the structure of the SELECT statement
        Result<Record1<JSON>> result6 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE)
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .forJSON().auto().withoutArrayWrapper()
                .fetch();
        System.out.println("Example 3.6:\n" + result6.formatJSON());

        // path() -> default usage
        Result<Record1<JSON>> result7 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE)
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .forJSON().path().root("customers")
                .fetch();
        System.out.println("Example 3.7:\n" + result7.formatJSON());

        // path() -> format nested results by using dot-separated column names or by using nested queries
        Result<Record1<JSON>> result8 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT.as("Payment.Amount"),
                PAYMENT.CACHING_DATE.as("Payment.CachingDate"))
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))               
                .forJSON().path().root("customers")
                .fetch();
        System.out.println("Example 3.8:\n" + result8.formatJSON());
        
        // JSON as a String        
        String result9 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT.as("Payment.Amount"),
                PAYMENT.CACHING_DATE.as("Payment.CachingDate"))
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))                
                .forJSON().path().root("customers")
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);
        System.out.println("Example 3.9:\n" + result9);
        
        // ordering, limiting and extracting JSON as a String        
        String result10 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT.as("Payment.Amount"),
                PAYMENT.CACHING_DATE.as("Payment.CachingDate"))
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .forJSON().path().root("customers")
                .fetchSingleInto(String.class);
        System.out.println("Example 3.10:\n" + result10);                
    }

    public void fetchJsonTable() {

        Result<Record> result1 = ctx.select()
                .from(jsonTable(
                        org.jooq.JSON.valueOf(
                                "[{\"name\":\"John\", \"contact\":{\"email\":\"john@yahoo.com\", \"phone\":\"0892 219 312\"}}]"),
                        "$")
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("email", VARCHAR).path("$.contact.email")
                        .column("phone", VARCHAR).path("$.contact.phone")
                        .as("t"))
                .fetch();
        System.out.println("Example 4.1:\n" + result1);

        Result<Record> result2 = ctx.select(table("t").asterisk())
                .from(MANAGER).crossApply(
                        jsonTable(MANAGER.MANAGER_DETAIL.coerce(JSON.class), val("$"))
                        .column("id").forOrdinality()
                        .column("firstName", VARCHAR(10)) // add the size to avoid VARCHAR(max)
                        .column("lastName", VARCHAR)
                        .column("gender", VARCHAR)
                        .column("dob", DATE)
                        .column("age", INTEGER)
                        .column("streetAddress", VARCHAR).path("$.address.streetAddress")
                        .column("city", VARCHAR).path("$.address.city")
                        .column("state", VARCHAR).path("$.address.state")
                        .column("zipOrPostal", VARCHAR).path("$.address.zipOrPostal")
                        .column("phoneNumber", NVARCHAR) // null 
                        .column("summary", VARCHAR) // null
                        .column("computerSkills", NVARCHAR)
                        .column("shareholder", VARCHAR) // null, if doesn't exists
                        .column("projects", NVARCHAR) // null 
                        .as("t"))
                .fetch();
        System.out.println("Example 4.2:\n" + result2);    
        
        Result<Record> result3 = ctx.select(table("t").asterisk())
                .from(MANAGER).crossApply(jsonTable(
                        MANAGER.MANAGER_DETAIL.coerce(JSON.class), val("$.projects"))
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("start", DATE)
                        .column("end", DATE) 
                        .column("type", VARCHAR)
                        .column("role", VARCHAR)
                        .column("details", VARCHAR)
                        .as("t"))
                .fetch();
        System.out.println("Example 4.3:\n" + result3);
        
        // filter result
        Result<Record> result4 = ctx.select(table("t").asterisk())
                .from(MANAGER).crossApply(jsonTable(MANAGER.MANAGER_DETAIL.coerce(JSON.class), val("$.projects"))
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("start", DATE)
                        .column("end", DATE) 
                        .column("type", VARCHAR)
                        .column("role", VARCHAR)
                        .column("details", VARCHAR)
                        .as("t"))
                .where(field("type").eq("development"))
                .fetch();
        System.out.println("Example 4.4:\n" + result4);
        
        // back to JSON        
        Result<Record1<JSON>> result5 = ctx.select(jsonObject("projects", 
                jsonObject(key("name").value(field("name")),
                        key("start").value(field("start")),
                        key("end").value(field(name("end"))),   
                        key("type").value(field("type")), 
                        key("role").value(field("role")),
                        key("details").value(field("details"))
                )))
                .from(MANAGER).crossApply(jsonTable(MANAGER.MANAGER_DETAIL.coerce(JSON.class), val("$.projects"))
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("start", DATE)
                        .column("end", DATE) 
                        .column("type", VARCHAR)
                        .column("role", VARCHAR)
                        .column("details", VARCHAR)
                        .as("t"))
                .fetch();
        System.out.println("Example 4.5:\n" + result5.formatJSON());
        
        // aggregate
        Result<Record2<String, Integer>> result6 = ctx.select(
                field("type", String.class), count(field("type")))
                .from(MANAGER).crossApply(jsonTable(MANAGER.MANAGER_DETAIL.coerce(JSON.class), val("$.projects"))
                        .column("type", VARCHAR)
                        .as("t"))
                .groupBy(field(name("type")))
                .fetch();
        System.out.println("Example 4.6:\n" + result6);
        
        // order and limit result
        Result<Record> result7 = ctx.select(table("t").asterisk())
                .from(MANAGER).crossApply(jsonTable(MANAGER.MANAGER_DETAIL.coerce(JSON.class), val("$.projects"))
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("start", DATE)
                        .column("end", DATE)
                        .column("type", VARCHAR)
                        .column("role", VARCHAR)
                        .column("details", VARCHAR)
                        .as("t"))
                .orderBy(field("start"))
                .limit((2))
                .fetch();
        System.out.println("Example 4.7:\n" + result7);
    }
}