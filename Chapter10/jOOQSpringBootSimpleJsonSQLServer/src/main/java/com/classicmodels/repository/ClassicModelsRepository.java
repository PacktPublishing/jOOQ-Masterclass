package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONEntry;
import org.jooq.Record1;
import org.jooq.Record;
import org.jooq.Result;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonTable;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.key;
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
        Result<Record1<JSON>> result1 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT)).as("json_result"))
                .from(CUSTOMER)
                .limit(3)
                .fetch();
        System.out.println("Example 1.1:\n" + result1);

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

        Result<Record1<JSON>> result1 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL.coerce(JSON.class), "$.email").as("email"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.1:\n" + result1);

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
                .forJSON().path().root("customer")
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
                .forJSON().auto().root("customer")
                .fetch();
        System.out.println("Example 3.5:\n" + result5.formatJSON());

        // path() -> default usage
        Result<Record1<JSON>> result6 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE)
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .forJSON().path().root("customer")
                .fetch();
        System.out.println("Example 3.6:\n" + result6.formatJSON());

        // path() -> format nested results by using dot-separated column names or by using nested queries
        Result<Record1<JSON>> result7 = ctx.select(
                CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CREDIT_LIMIT,
                PAYMENT.INVOICE_AMOUNT.as("Payment.Amount"),
                PAYMENT.CACHING_DATE.as("Payment.CachingDate"))
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .forJSON().path().root("customer")
                .fetch();
        System.out.println("Example 3.7:\n" + result7.formatJSON());
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
                        .column("firstName", VARCHAR)
                        .column("lastName", VARCHAR)
                        .column("gender", VARCHAR)
                        .column("dob", DATE)
                        .column("age", INTEGER)
                        .column("streetAddress", VARCHAR).path("$.address.streetAddress")
                        .column("city", VARCHAR).path("$.address.city")
                        .column("state", VARCHAR).path("$.address.state")
                        .column("zipOrPostal", VARCHAR).path("$.address.zipOrPostal")
                        .column("phoneNumber", NVARCHAR).path("$.phoneNumber") // null (needs crossApply again)
                        .column("summary", VARCHAR).path("$.summary") // null (needs crossApply again)
                        .column("computerSkills", NVARCHAR).path("$.computerSkills")
                        .column("shareholder", VARCHAR).path("$.shareholder") // null, if doesn't exists
                        .column("projects", NVARCHAR).path("$.projects") // null (needs crossApply again)
                        .as("t"))
                .fetch();
        System.out.println("Example 4.2:\n" + result2);
    }
}