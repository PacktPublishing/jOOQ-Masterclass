package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }      

    // if you get, ORA-40478: output value too large (maximum: 4000)
    // then you should set MAX_STRING_SIZE to EXTENTED instead of STANDARD
    public void arrayToJson() {

        var result = ctx.select(DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME,
                field("COLUMN_VALUE").as("EVALUATION"))
                .from(DEPARTMENT, lateral(select(field("COLUMN_VALUE"))
                        .from(table(DEPARTMENT.TOPIC))))
                .forJSON().path()
                .fetch();

        System.out.println("Example (array)" + result.formatJSON());
    }

    public void oneToOneToJson() {

        Result<Record1<JSON>> result11 = ctx.select(
                jsonObject(
                        // or, jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME)
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                // or, jsonObject(CUSTOMERDETAIL.CITY,
                                //        CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                                jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                        key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.1.1 (one-to-one):\n" + result11.formatJSON());

        // as a JSON String
        String result12 = ctx.select(
                jsonArrayAgg(
                        jsonObject(
                                // or, jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME)
                                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                                key("phone").value(CUSTOMER.PHONE),
                                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                                key("details").value(select(
                                        jsonObject(CUSTOMERDETAIL.CITY,
                                                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE))
                                        .from(CUSTOMERDETAIL)
                                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                        .orderBy(CUSTOMER.CREDIT_LIMIT))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);

        System.out.println("Example 1.1.2 (one-to-one):\n" + result12);

        // same thing as above via JOIN
        Result<Record1<JSON>> result2 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("phone").value(CUSTOMER.PHONE),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                key("details").value(
                        jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                key("state").value(CUSTOMERDETAIL.STATE)))))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.2 (one-to-one):\n" + result2.formatJSON());

        Result<Record1<JSON>> result31 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 1.3.1 (one-to-one):\n" + result31.formatJSON());

        String result32 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(10)
                .forJSON().path()
                .fetchSingleInto(String.class);

        System.out.println("Example 1.3.2 (one-to-one):\n" + result32);

        Result<Record1<JSON>> result4 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 1.4 (one-to-one):\n" + result4.formatJSON());
    }

    public void oneToOneToJsonLimit() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                        key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(2)
                .fetch();

        System.out.println("Example 2.1 (one-to-one):\n" + result1.formatJSON());

        Result<Record1<JSON>> result2 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("phone").value(CUSTOMER.PHONE),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                key("details").value(
                        jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                key("state").value(CUSTOMERDETAIL.STATE)))))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(2)
                .fetch();

        System.out.println("Example 2.2 (one-to-one and limit):\n" + result2.formatJSON());

        // limit your result before aggregating via subquery
        Result<Record1<JSON>> result3 = ctx.select(jsonObject(
                key("customerName").value(field(name("customerName"))),
                key("phone").value(field(name("phone"))),
                key("creditLimit").value(field(name("creditLimit"))),
                key("details").value(
                        jsonObject(key("city").value(field(name("city"))),
                                key("addressLineFirst").value(field(name("addressFirstLine"))),
                                key("state").value(field(name("state")))))))
                .from(select(CUSTOMER.CUSTOMER_NAME.as("customerName"),
                        CUSTOMER.PHONE.as("phone"), CUSTOMER.CREDIT_LIMIT.as("creditLimit"),
                        CUSTOMERDETAIL.CITY.as("city"),
                        CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressFirstLine"),
                        CUSTOMERDETAIL.STATE.as("state"))
                        .from(CUSTOMER)
                        .join(CUSTOMERDETAIL)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                        .orderBy(CUSTOMER.CUSTOMER_NAME)
                        .limit(2).asTable("t"))
                .fetch();

        System.out.println("Example 2.3 (one-to-one and limit):\n" + result3.formatJSON());

        Result<Record1<JSON>> result4 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(3)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 2.4 (one-to-one):\n" + result4.formatJSON());
    }

    public void oneToManyToJson() {

        Result<Record1<JSON>> result31 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(select(jsonArrayAgg(
                                // or, jsonObject(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK))
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))                
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 3.1.1 (one-to-many):\n" + result31.formatJSON());

        // as a JSON String
        String result32 = ctx.select(
                jsonArrayAgg(
                        jsonObject(
                                key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                                key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                                key("products").value(select(jsonArrayAgg(
                                        // or, jsonObject(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK))
                                        jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                                key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                                key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                        .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                        .from(PRODUCT)
                                        .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))
                        .orderBy(PRODUCTLINE.PRODUCT_LINE))
                .from(PRODUCTLINE)
                .fetchSingleInto(String.class);

        System.out.println("Example 3.1.2 (one-to-many):\n" + result32);

        // same thing as above via JOIN
        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 3.2 (one-to-many):\n" + result2.formatJSON());

        Result<Record1<JSON>> result3 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE,
                PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME.as("product.product_name"),
                PRODUCT.PRODUCT_VENDOR.as("product.product_vendor"),
                PRODUCT.QUANTITY_IN_STOCK.as("product.quantity_in_stock"))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 3.3 (one-to-many):\n" + result3.formatJSON());

        Result<Record1<JSON>> result4 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 3.4 (one-to-many):\n" + result4.formatJSON());

        var result51 = ctx.select( // Result<Record3<String, String, Object>>
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(5) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                // .forJSON().path() // add this if you need a return of type Result<Record1<JSON>>  
                .fetch();

        System.out.println("Example 3.5.1 (one-to-many):\n" + result51.formatJSON());
        
        Result<Record1<JSON>> result52 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(5) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                .forJSON().path()
                .fetch();

        System.out.println("Example 3.5.2 (one-to-many):\n" + result52.formatJSON());

        String result53 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                //.limit(2) // limit product lines
                .forJSON().path()
                .fetchSingleInto(String.class);

        System.out.println("Example 3.5.3 (one-to-many):\n" + result53);
        
        String result54 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                //.limit(2) // limit product lines
                .forJSON().path()
                .fetchSingle()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 3.5.4 (one-to-many):\n" + result54);
    }

    public void oneToManyToJsonLimit() {

        // limit 'one' in one-to-many
        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(2)
                .fetch();

        System.out.println("Example 4.1 (one-to-many and limit):\n" + result1.formatJSON());

        // limit 'many' in one-to-many
        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(field(name("productName"))),
                                        key("productVendor").value(field(name("productVendor"))),
                                        key("quantityInStock").value(field(name("quantityInStock")))))
                                .orderBy(field(name("quantityInStock"))))))
                .from(PRODUCTLINE,
                        lateral(select(PRODUCT.PRODUCT_NAME.as("productName"),
                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                                .limit(2)).asTable("t"))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 4.2 (one-to-many and limit):\n" + result2.formatJSON());

        // limit 'one' and 'many' in one-to-many
        Result<Record1<JSON>> result3 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(field(name("productName"))),
                                        key("productVendor").value(field(name("productVendor"))),
                                        key("quantityInStock").value(field(name("quantityInStock")))))
                                .orderBy(field(name("quantityInStock"))))))
                .from(PRODUCTLINE,
                        lateral(select(PRODUCT.PRODUCT_NAME.as("productName"),
                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                                .limit(2)).asTable("t")) // limit 'many'
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3) // limit 'one'
                .fetch();

        System.out.println("Example 4.3 (one-to-many and limit):\n" + result3.formatJSON());

        var result4 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .limit(5) // limit 'many'
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .limit(2) // limit 'one'
                .fetch();

        System.out.println("Example 4.4 (one-to-many):\n" + result4.formatJSON());
    }

    public void manyToManyToJsonManagersOffices() {

        Result<Record1<JSON>> result51 = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID),
                        key("managerName").value(MANAGER.MANAGER_NAME),
                        key("offices").value(select(jsonArrayAgg(jsonObject(
                                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)))
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID)))))
                .from(MANAGER)
                .fetch();

        System.out.println("Example 5.1.1 (many-to-many):\n" + result51.formatJSON());

        // as a JSON String
        String result52 = ctx.select(
                jsonArrayAgg(
                        jsonObject(
                                key("managerId").value(MANAGER.MANAGER_ID),
                                key("managerName").value(MANAGER.MANAGER_NAME),
                                key("offices").value(select(jsonArrayAgg(jsonObject(
                                        OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)))
                                        .from(OFFICE)
                                        .join(OFFICE_HAS_MANAGER)
                                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                        .where(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID))))))
                .from(MANAGER)
                .fetchSingleInto(String.class);

        System.out.println("Example 5.1.2 (many-to-many):\n" + result52);

        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID),
                        key("managerName").value(MANAGER.MANAGER_NAME),
                        key("offices").value(jsonArrayAgg(
                                jsonObject(key("officeCode").value(field(name("officeCode"))),
                                        key("state").value(field(name("state"))),
                                        key("city").value(field(name("city")))))
                                .orderBy(field(name("officeCode"))))))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .groupBy(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("Example 5.2 (many-to-many):\n" + result2.formatJSON());

        Result<Record1<JSON>> result31 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE,
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetch();

        System.out.println("Example 5.3.1 (many-to-many):\n" + result31.formatJSON());

        String result321 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE,
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetchSingleInto(String.class);

        System.out.println("Example 5.3.2.1 (many-to-many):\n" + result321);
        
        String result322 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE,
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetchSingle()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 5.3.2.2 (many-to-many):\n" + result322);

        Result<Record1<JSON>> result4 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field(name("office_code")), field(name("city")), field(name("state")))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("office_code"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("offices"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .orderBy(MANAGER.MANAGER_ID)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 5.4 (many-to-many):\n" + result4.formatJSON());

        Result<Record1<JSON>> result5 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field(name("office_code")), field(name("city")), field(name("state")))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("office_code"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("offices"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .orderBy(MANAGER.MANAGER_ID)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 5.5 (many-to-many):\n" + result5.formatJSON());

        Result<Record1<JSON>> result6 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(field(name("office_code")), field(name("city")), field(name("state")))
                        .from(select(OFFICE.OFFICE_CODE.as("office_code"),
                                OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                                OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("offices"))
                        .where(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                        .orderBy(MANAGER.MANAGER_ID)
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .forJSON().path()
                .fetch();

        System.out.println("Example 5.6 (many-to-many):\n" + result6.formatJSON());
    }

    public void manyToManyToJsonOfficesManagers() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("state").value(OFFICE.STATE),
                        key("city").value(OFFICE.CITY),
                        key("managers").value(select(jsonArrayAgg(jsonObject(
                                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))))
                .from(OFFICE)
                .fetch();

        System.out.println("Example 6.1 (many-to-many):\n" + result1.formatJSON());

        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("state").value(OFFICE.STATE),
                        key("city").value(OFFICE.CITY),
                        key("managers").value(jsonArrayAgg(
                                jsonObject(key("managerId").value(field(name("managerId"))),
                                        key("managerName").value(field(name("managerName")))))
                                .orderBy(field(name("managerId"))))))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("managerId"),
                        MANAGER.MANAGER_NAME.as("managerName"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("t"))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .groupBy(OFFICE.OFFICE_CODE, OFFICE.STATE, OFFICE.CITY)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Example 6.2 (many-to-many):\n" + result2.formatJSON());

        Result<Record1<JSON>> result31 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .forJSON().path()
                .fetch();

        System.out.println("Example 6.3.1 (many-to-many):\n" + result31.formatJSON());

        String result32 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .forJSON().path()
                .fetchSingleInto(String.class);

        System.out.println("Example 6.3.2 (many-to-many):\n" + result32);

        Result<Record1<JSON>> result4 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                field(name("manager_id")), field(name("manager_name")))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("manager_id"),
                        MANAGER.MANAGER_NAME.as("manager_name"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .orderBy(OFFICE.OFFICE_CODE)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 6.4 (many-to-many):\n" + result4.formatJSON());

        Result<Record1<JSON>> result5 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                field(name("manager_id")), field(name("manager_name")))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("manager_id"),
                        MANAGER.MANAGER_NAME.as("manager_name"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .orderBy(OFFICE.OFFICE_CODE)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 6.5 (many-to-many):\n" + result5.formatJSON());

        Result<Record1<JSON>> result6 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                select(field(name("manager_id")), field(name("manager_name")))
                        .from(select(MANAGER.MANAGER_ID.as("manager_id"),
                                MANAGER.MANAGER_NAME.as("manager_name"),
                                OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                                .from(MANAGER).join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                        .where(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                        .orderBy(OFFICE.OFFICE_CODE)
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetch();

        System.out.println("Example 6.6 (many-to-many):\n" + result6.formatJSON());
    }

    public void manyToManyToJsonManagersOfficesLimit() {

        Result<Record1<JSON>> result = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .limit(2) // limit the number of offices                        
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .limit(3) // limit the number of managers
                .forJSON().path()
                .fetch();

        System.out.println("Example 7 (many-to-many):\n" + result.formatJSON());
    }
}
