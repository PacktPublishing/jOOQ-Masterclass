package com.classicmodels.repository;

import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Manager.MANAGER;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONEntry;
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record;
import org.jooq.Result;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonExists;
import static org.jooq.impl.DSL.jsonObjectAgg;
import static org.jooq.impl.DSL.jsonTable;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.DATE;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.JSON;
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

        // simple example of using jsonArray()        
        List<String> result21 = ctx.select(jsonArray(jsonObject(
                jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME),
                jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT))).as("json_result"))
                .from(CUSTOMER)
                .fetchInto(String.class);
        System.out.println("Example 1.2.1:\n" + result21);

        List<String> result22 = ctx.select(jsonArray(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                CUSTOMER.CONTACT_LAST_NAME), CUSTOMER.CREDIT_LIMIT).as("json_result"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetchInto(String.class);
        System.out.println("Example 1.2.2:\n" + result22);

        // simple example of using jsonArrayAgg()
        String result31 = ctx.select(jsonArrayAgg(jsonObject(
                jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME),
                jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT))).as("json_result"))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);
        System.out.println("Example 1.3.1:\n" + result31);

        String result32 = ctx.select(jsonArrayAgg(CUSTOMER.CUSTOMER_NAME).as("json_result"))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);
        System.out.println("Example 1.3.2:\n" + result32);

        String result33 = ctx.select(jsonArrayAgg(jsonObject(
                jsonEntry("customerName", field("customer_name")),
                jsonEntry("creditLimit", field("credit_limit"))))
                .orderBy(field("credit_limit")).as("json_result"))
                .from(select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)
                        .from(CUSTOMER).orderBy(CUSTOMER.CUSTOMER_NAME).limit(3))
                .fetchSingleInto(String.class);
        System.out.println("Example 1.3.3:\n" + result33);

        // simple example of using jsonObjectAgg()               
        String result41 = ctx.select(jsonObjectAgg(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT).as("json_result"))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);
        System.out.println("Example 1.4.1:\n" + result41);

        String result42 = ctx.select(jsonObjectAgg(
                field("customer_name", String.class), field("credit_limit")).as("json_result"))
                .from(select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)
                        .from(CUSTOMER).orderBy(CUSTOMER.CUSTOMER_NAME).limit(3))
                .fetchSingleInto(String.class);
        System.out.println("Example 1.4.2:\n" + result42);                       

        // simple example of using jsonExists()
        Result<Record2<Long, String>> result5 = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .from(MANAGER)
                .where(jsonExists(MANAGER.MANAGER_DETAIL, "$.shareholder"))
                .fetch();
        System.out.println("Example 1.5:\n" + result5);

        // simple example of using jsonEntry()
        JSONEntry customerNameEntry = jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME);
        JSONEntry creditLimitEntry = jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT);

        Result<Record1<JSON>> result6 = ctx.select(jsonObject(
                customerNameEntry, creditLimitEntry).as("json_result"))
                .from(CUSTOMER)
                .where(creditLimitEntry.value().gt(100000))
                .fetch();
        System.out.println("Example 1.6:\n" + result6);
    }

    public void fetchJsonValue() {

        Result<Record1<JSON>> result21 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.email").as("email"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.1.1:\n" + result21);

        List<String> result22 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.email").as("email"))
                .from(MANAGER)
                .fetchInto(String.class);
        System.out.println("Example 2.1.2:\n" + result22);

        String result23 = ctx.select(jsonArrayAgg(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.email")).as("email"))
                .from(MANAGER)
                .fetchSingleInto(String.class);
        System.out.println("Example 2.1.3:\n" + result23);

        Result<Record1<JSON>> result2 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.address.city").as("city"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.2:\n" + result2);

        Result<Record1<JSON>> result3 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[0]").as("homeMobilePhones"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.3:\n" + result3);

        Result<Record1<JSON>> result4 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[1].number[*]").as("allMobilePhones"))
                .from(MANAGER)
                .fetch();
        System.out.println("Example 2.4:\n" + result4);

        Result<Record1<JSON>> result5 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.email").as("email"))
                .from(MANAGER)
                .where(jsonValue(MANAGER.MANAGER_DETAIL, "$.projects[*].role").coerce(String.class)
                        .contains("Principal Manager"))
                .fetch();
        System.out.println("Example 2.5:\n" + result5);

        Result<Record1<JSON>> result6 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.email").as("email"))
                .from(MANAGER)
                .where(jsonValue(MANAGER.MANAGER_DETAIL, "$.projects[*].role")
                        .like("%Principal Manager%"))
                .fetch();
        System.out.println("Example 2.6:\n" + result6);

        Result<Record1<JSON>> result7 = ctx.select(
                jsonValue(MANAGER.MANAGER_DETAIL, "$.email").as("email"))
                .from(MANAGER)
                .where(jsonValue(MANAGER.MANAGER_DETAIL, "$.projects[0].start").coerce(LocalDate.class)
                        .gt(LocalDate.of(2015, 1, 1)).and(
                        jsonValue(MANAGER.MANAGER_DETAIL, "$.projects[0].end").coerce(LocalDate.class)
                                .lt(LocalDate.of(2020, 1, 1))
                ))
                .fetch();
        System.out.println("Example 2.7:\n" + result7);
    }

    public void fetchJsonTable() {

        Result<Record> result1 = ctx.select()
                .from(jsonTable(
                        org.jooq.JSON.valueOf(
                                "[{\"name\":\"John\", \"contact\":{\"email\":\"john@yahoo.com\", \"phone\":\"0892 219 312\"}}]"),
                        "$[*]")
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("email", VARCHAR).path("$.contact.email")
                        .column("phone", VARCHAR).path("$.contact.phone")
                        .as("t"))
                .fetch();
        System.out.println("Example 3.1:\n" + result1);

        Result<Record> result2 = ctx.select(table("t").asterisk())
                .from(MANAGER, jsonTable(MANAGER.MANAGER_DETAIL, val("$"))
                        .column("id").forOrdinality()
                        .column("firstName", VARCHAR) // it is better to specify a size as well, VARCHAR(20)
                        .column("lastName", VARCHAR)
                        .column("gender", VARCHAR)
                        .column("dob", DATE)
                        .column("age", INTEGER)
                        .column("streetAddress", VARCHAR).path("$.address.streetAddress")
                        .column("city", VARCHAR).path("$.address.city")
                        .column("state", VARCHAR).path("$.address.state")
                        .column("zipOrPostal", VARCHAR).path("$.address.zipOrPostal")
                        .column("phoneNumber", JSON)
                        .column("summary", VARCHAR)
                        .column("computerSkills", JSON)
                        .column("shareholder", VARCHAR) // null, if doesn't exists
                        .column("projects", JSON)
                        .as("t"))
                .fetch();
        System.out.println("Example 3.2:\n" + result2);

        Result<Record> result3 = ctx.select(table("t").asterisk())
                .from(MANAGER, jsonTable(MANAGER.MANAGER_DETAIL, val("$.projects[*]"))
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("start", DATE)
                        .column("end", DATE)
                        .column("type", VARCHAR)
                        .column("role", VARCHAR)
                        .column("details", VARCHAR)
                        .as("t"))
                .fetch();
        System.out.println("Example 3.3:\n" + result3);

        // filter result
        Result<Record> result4 = ctx.select(table("t").asterisk())
                .from(MANAGER, jsonTable(MANAGER.MANAGER_DETAIL, val("$.projects[*]"))
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
        System.out.println("Example 3.4:\n" + result4);

        // back to JSON        
        Result<Record1<JSON>> result5 = ctx.select(jsonObject("projects", jsonArrayAgg(
                jsonObject(key("name").value(field("name")),
                        key("start").value(field("start")),
                        key("end").value(field("end")),
                        key("type").value(field("type")),
                        key("role").value(field("role")),
                        key("details").value(field("details"))
                ))))
                .from(MANAGER, jsonTable(MANAGER.MANAGER_DETAIL, val("$.projects[*]"))
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("start", DATE)
                        .column("end", DATE)
                        .column("type", VARCHAR)
                        .column("role", VARCHAR)
                        .column("details", VARCHAR)
                        .as("t"))
                .fetch();
        System.out.println("Example 3.5:\n" + result5.formatJSON());

        // aggregate
        Result<Record2<String, Integer>> result6 = ctx.select(
                field("type", String.class), count(field("type")))
                .from(MANAGER, jsonTable(MANAGER.MANAGER_DETAIL, val("$.projects[*]"))
                        .column("type", VARCHAR)
                        .as("t"))
                .groupBy(field("type"))
                .fetch();
        System.out.println("Example 3.6:\n" + result6);

        // order and limit result
        Result<Record> result7 = ctx.select(table("t").asterisk())
                .from(MANAGER, jsonTable(MANAGER.MANAGER_DETAIL, val("$.projects[*]"))
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
        System.out.println("Example 3.7:\n" + result7);
    }
}
